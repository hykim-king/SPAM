/**
 * 파일명: ReportController.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-06 <br>
 * 설 명: 신고 기능 컨트롤러 (관리자 / 사용자 기능 통합) <br>
 */
package com.pcwk.ehr.report.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pcwk.ehr.report.domain.ReportSearchDTO;
import com.pcwk.ehr.report.domain.ReportVO;
import com.pcwk.ehr.report.service.ReportService;
import com.pcwk.ehr.product.domain.ProductVO;
import com.pcwk.ehr.product.service.ProductService;
import com.pcwk.ehr.user.domain.UserVO;

@Controller
@RequestMapping("/report")
public class ReportController {

    private static final String SESSION_LOGIN_USER = "loginUser";
    private static final String ROLE_ADMIN = "02";

    @Autowired
    private ReportService reportService;

    @Autowired
    private ProductService productService;

    // ================ 관리자 ================

    // 1. 신고 목록 조회 (관리자용 전체 조회)
    @GetMapping("/admin_doRetrieve.do")
    public String doRetrieve(ReportSearchDTO search, Model model, HttpSession session) {
        // 1. 보안 유효성 검사 (관리자 권한 확인)
        UserVO loginUser = getLoginUser(session);
        if (!isAdmin(loginUser)) {
            return accessDeniedRedirect(loginUser);
        }

        // 2026-07-14 [수정] 조회 전 총 페이지를 계산하고 잘못된 페이지 번호를 안전하게 보정한다.
        int pNo = search.getPageNo() <= 0 ? 1 : search.getPageNo();
        int pSize = 10;
        int totalCount = reportService.totalCnt();
        int totalPage = totalCount == 0 ? 0 : (int) Math.ceil(totalCount / (double) pSize);
        if (totalPage > 0 && pNo > totalPage) {
            pNo = totalPage;
        }

        search.setPageNo(pNo);
        search.setPageSize(pSize);
        List<ReportVO> list = reportService.doRetrieve(search);

        int pageBlockSize = 5;
        int startPage = totalPage == 0 ? 0 : ((pNo - 1) / pageBlockSize) * pageBlockSize + 1;
        int endPage = totalPage == 0 ? 0 : Math.min(startPage + pageBlockSize - 1, totalPage);

        model.addAttribute("list", list);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("search", search);

        return "report/admin_report_list";
    }
    
    // 2. 신고 상세 조회 (관리자용)
    @GetMapping("/admin_report_detail.do")
    public String adminReportDetail(ReportVO report, Model model, HttpSession session) {
    	
    	// 1. 보안 유효성 검사 (관리자 권한 확인)
        UserVO loginUser = getLoginUser(session);
        if (!isAdmin(loginUser)) {
            return accessDeniedRedirect(loginUser);
        }

        // 2. 파라미터 검증 및 서비스 호출을 통한 단건 상세 데이터 조회
        // report 객체 내에 reportNo가 자동으로 매핑되어 들어옵니다.
        ReportVO outVO = reportService.doSelectOne(report);
        
        // 3. 조회된 데이터를 Model에 담아 JSP(화면)로 전달
        model.addAttribute("outVO", outVO);
        
        // 4. 관리자용 상세 페이지 JSP 경로 리턴
        return "report/admin_report_detail"; 
    }
    
    // 3. 관리자용 신고 처리 상태 변경 (접수/완료/반려)
    @PostMapping(value="/doUpdateStatus.do", produces="text/html; charset=UTF-8")
    @ResponseBody
    public String doUpdateStatus(ReportVO report, HttpSession session) {
        UserVO loginUser = getLoginUser(session);
        if (isAdmin(loginUser)) {
            // 관리자의 고유 번호(UserNum)를 ReportVO의 adminNo에 세팅합니다.
            report.setAdminNo(loginUser.getUserNum());
        } else {
            return "<script>" +
                   "alert('관리자 권한이 필요합니다.');" +
                   "location.href='../main.do?modal=forbidden';" +
                   "</script>";
        }

        if (!("01".equals(report.getReportStatus())
                || "02".equals(report.getReportStatus())
                || "03".equals(report.getReportStatus()))) {
            return "<script>alert('허용되지 않은 처리 상태입니다.');history.back();</script>";
        }
    	
        reportService.doUpdateStatus(report);
        
        return "<script>" + 
        "alert('수정되었습니다.');" +  
        "location.href='admin_doRetrieve.do';" + 
        "</script>";
    }
    
    // ================ 사용자 ================
    
    // 1. 상품 신고등록 페이지 GET 요청 
    @GetMapping("/report_product_form.do")
    public String reportProductForm(HttpSession session) {
        if (getLoginUser(session) == null) {
            return "redirect:/main.do?modal=login";
        }
        return "report/report_product_form";
    }

    // 2. 유저 신고등록 페이지 GET 요청
    @GetMapping("/reportUserForm.do")
    public String reportUserForm(HttpSession session) {
        if (getLoginUser(session) == null) {
            return "redirect:/main.do?modal=login";
        }
        return "report/report_user_form";
    }
    
    // 3. 나의 신고 내역 목록 조회 (내가 한 신고 / 내가 당한 신고)
    @GetMapping("/myReportList.do")
    public String myReportList(HttpSession session, Model model) {
        UserVO loginUser = getLoginUser(session);
        
        // 로그인 되어있지 않으면 로그인 페이지로 강제 리다이렉트
        if (loginUser == null) {
            return "redirect:/main.do?modal=login";
        }
        
        // 내가 신고한 목록 조회 및 바인딩
        long currentUserNum = loginUser.getUserNum();
        List<ReportVO> myReportList = reportService.doRetrieveMyReports(currentUserNum);
        model.addAttribute("myReportList", myReportList);
        
        // 내가 신고당한 목록 조회 및 바인딩 
        List<ReportVO> receivedReportList = reportService.doRetrieveReceivedReports(currentUserNum);
        model.addAttribute("receivedReportList", receivedReportList);
        
        return "report/my_report_list"; 
    }
    
    // 5. 신고 상세 조회
    @GetMapping("/doSelectOne.do")
    public String doSelectOne(ReportVO report, Model model, HttpSession session) {
        UserVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/main.do?modal=login";
        }

        // 신고번호(reportNo)를 기준으로 서비스에서 데이터를 조회해와
        ReportVO outVO = reportService.doSelectOne(report);

        if (outVO == null) {
            return "redirect:/report/myReportList.do";
        }

        boolean ownsReport = loginUser.getUserNum().equals(outVO.getReporterNo())
                || loginUser.getUserNum().equals(outVO.getReportedUserNo());
        if (!ownsReport && !isAdmin(loginUser)) {
            return "redirect:/main.do?modal=forbidden";
        }
        
        // 조회된 데이터를 모델에 담아서 JSP로 전달
        model.addAttribute("outVO", outVO);
        
        return "report/report_detail";
    }
    
 // 3. 신고 등록 POST 요청 (@ResponseBody 활용 문자열 반환)
    @PostMapping(value="/doInsert.do", produces="text/html; charset=UTF-8")
    @ResponseBody
    public String doInsert(ReportVO report, HttpSession session) {
        
        // [유효성 검사] 신고 유형 및 사유 미선택 검사
        if (report.getReportType() == null || report.getReportType().trim().isEmpty()) {
            return "<script>" +
                   "alert('신고양식을 제대로 작성해주세요. (신고 유형 누락)');" +
                   "history.back();" +
                   "</script>";
        }
        
        // [유효성 검사] 상세내용 미입력 검사
        if (report.getReason() == null || report.getReason().trim().isEmpty()) {
            return "<script>" +
                   "alert('신고양식을 제대로 작성해주세요. (상세 내용 누락)');" +
                   "history.back();" +
                   "</script>";
        }
        
        // 2026-07-14 [수정] 화면의 maxlength=1000과 동일하게 1000자까지 허용한다.
        if (report.getReason().length() > 1000) {
            return "<script>" +
                   "alert('신고양식은 1000자 이하로 작성해주세요.');" +
                   "history.back();" +
                   "</script>";
        }
        
        // 로그인 세션 체크 및 신고자 번호 설정
        UserVO loginUser = getLoginUser(session);
        if(loginUser == null) {
            return "<script>" +
                   "alert('로그인 후 이용 가능합니다.');" +
                   "location.href='../main.do?modal=login';" +
                   "</script>";
        }
        
        // 세션에서 꺼낸 로그인 유저의 고유 번호를 세팅
        report.setReporterNo(loginUser.getUserNum());

        if (!("PRODUCT".equals(report.getReportType()) || "USER".equals(report.getReportType()))) {
            return "<script>alert('허용되지 않은 신고 유형입니다.');history.back();</script>";
        }

        if ("PRODUCT".equals(report.getReportType())) {
            if (report.getTargetId() == null) {
                return "<script>alert('신고할 상품 정보가 없습니다.');history.back();</script>";
            }

            ProductVO targetProduct;
            try {
                targetProduct = productService.getProduct(Math.toIntExact(report.getTargetId()));
            } catch (ArithmeticException e) {
                targetProduct = null;
            }

            if (targetProduct == null) {
                return "<script>alert('신고할 상품을 찾을 수 없습니다.');history.back();</script>";
            }

            // 화면에서 전달된 회원번호를 신뢰하지 않고 상품의 실제 판매자로 보정한다.
            report.setReportedUserNo(targetProduct.getUserNum());
        } else {
            report.setTargetId(null);
        }

        if (report.getReportedUserNo() == null) {
            return "<script>alert('피신고 회원 정보가 없습니다.');history.back();</script>";
        }

        if (loginUser.getUserNum().equals(report.getReportedUserNo())) {
            return "<script>alert('자기 자신은 신고할 수 없습니다.');history.back();</script>";
        }
       
        // 서비스 단 비즈니스 로직 수행
        reportService.doInsert(report);
        
        // 💡 핵심 수정: @ResponseBody 상황이므로 redirect 문자열 대신 스크립트로 강제 이동 처리!
        // 중복 제출(F5) 방지를 위해 마이페이지 신고 내역 목록으로 보냅니다.
        return "<script>" +
               "alert('신고서 제출이 완료되었습니다.');" +
               "location.href='myReportList.do';" +
               "</script>";
    }

    private UserVO getLoginUser(HttpSession session) {
        Object value = session.getAttribute(SESSION_LOGIN_USER);
        return value instanceof UserVO ? (UserVO) value : null;
    }

    private boolean isAdmin(UserVO user) {
        return user != null && ROLE_ADMIN.equals(user.getUserRole());
    }

    private String accessDeniedRedirect(UserVO user) {
        return user == null
                ? "redirect:/main.do?modal=login"
                : "redirect:/main.do?modal=forbidden";
    }
}
