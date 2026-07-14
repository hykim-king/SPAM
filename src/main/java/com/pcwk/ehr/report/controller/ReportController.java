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
import com.pcwk.ehr.user.domain.UserVO;

@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // ================ 관리자 ================
    // ================ 관리자 ================

    // 1. 신고 목록 조회 (관리자용 전체 조회)
    @GetMapping("/admin_doRetrieve.do")
    public String doRetrieve(ReportSearchDTO search, Model model, HttpSession session) {
    	// 1. 보안 유효성 검사 (관리자 권한 확인)
        UserVO loginUser = (UserVO) session.getAttribute("loginUser");
        if (loginUser == null || !"02".equals(loginUser.getUserRole())) { 
            return "redirect:/user/login.do"; 
        }

        // 2. 파라미터가 비어있거나 잘못 왔을 때 강제 보정
        int pNo = search.getPageNo() <= 0 ? 1 : search.getPageNo();
        int pSize = 10; // 대장이 요청한 한 페이지당 10개 고정
        
        // 3. 연산 순서 꼬임 방지를 위해 직접 연산 후 DTO 변수에 강제 주입
        int computedStart = (pNo - 1) * pSize + 1;
        int computedEnd = computedStart + pSize - 1;
        
        // DTO 멤버 변수에 정확한 값 세팅
        search.setPageNo(pNo);
        search.setPageSize(pSize);
        search.setStartRow(computedStart);
        search.setEndRow(computedEnd);
        
        // 💡 디버깅 로그: 이 숫자가 콘솔에 정확히 찍히는지 확인용
        System.out.println("====== [페이징 디버깅] ======");
        System.out.println("요청 PageNo : " + search.getPageNo());
        System.out.println("보낼 StartRow : " + search.getStartRow());
        System.out.println("보낼 EndRow : " + search.getEndRow());
        System.out.println("============================");

        // 4. 데이터 조회 및 총 건수 조회
        List<ReportVO> list = reportService.doRetrieve(search);
        int totalCount = reportService.totalCnt();

        // 5. JSP로 모델 전달    
        model.addAttribute("list", list);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("search", search);

        return "report/admin_report_list";
    }
    
    // 2. 신고 상세 조회 (관리자용)
    @GetMapping("/admin_report_detail.do")
    public String adminReportDetail(ReportVO report, Model model, HttpSession session) {
    	
    	// 1. 보안 유효성 검사 (관리자 권한 확인)
        UserVO loginUser = (UserVO) session.getAttribute("loginUser");
        if (loginUser == null || !"02".equals(loginUser.getUserRole())) { 
            // 프로젝트 설계에 맞춘 관리자 권한 체크 조건 (예: ROLE_ADMIN 또는 ADMIN)
            return "redirect:/user/login.do"; 
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
    	UserVO loginUser = (UserVO) session.getAttribute("loginUser");
    	if (loginUser != null) {
            // 관리자의 고유 번호(UserNum)를 ReportVO의 adminNo에 세팅합니다.
            report.setAdminNo(loginUser.getUserNum());
        } else {
            // 혹시 세션이 만료되었거나 로그인이 안 된 경우를 위한 예외 처리 (선택)
            return "<script>" +
                   "alert('로그인 세션이 만료되었습니다. 다시 로그인해주세요.');" +
                   "location.href='/ehr/user/login.do';" +
                   "</script>";
        }
    	
        reportService.doUpdateStatus(report);
        
        return "<script>" + 
        "alert('수정되었습니다.');" +  
        "location.href='admin_doRetrieve.do';" + 
        "</script>";
    }
    
    // ================ 사용자 ================
    // ================ 사용자 ================
    
    // 1. 상품 신고등록 페이지 GET 요청 
    @GetMapping("/report_product_form.do")
    public String reportProductForm() {
        return "report/report_product_form"; 
    }

    // 2. 유저 신고등록 페이지 GET 요청
    @GetMapping("/reportUserForm.do")
    public String reportUserForm() {
        return "report/report_user_form"; 
    }
    
    // 3. 나의 신고 내역 목록 조회 (내가 한 신고 / 내가 당한 신고)
    @GetMapping("/myReportList.do")
    public String myReportList(HttpSession session, Model model) {
        UserVO loginUser = (UserVO) session.getAttribute("loginUser");
        
        // 로그인 되어있지 않으면 로그인 페이지로 강제 리다이렉트
        if (loginUser == null) {
            return "redirect:/user/login.do";
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
    public String doSelectOne(ReportVO report, Model model) {
        // 신고번호(reportNo)를 기준으로 서비스에서 데이터를 조회해와
        ReportVO outVO = reportService.doSelectOne(report);
        
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
        
        // [유효성 검사] 상세내용 1000자 이상 검사
        if (report.getReason().length() >= 1000) {
            return "<script>" +
                   "alert('신고양식은 1000자 미만으로 작성해주세요.');" +
                   "history.back();" +
                   "</script>";
        }
        
        // 로그인 세션 체크 및 신고자 번호 설정
        UserVO loginUser = (UserVO) session.getAttribute("loginUser");
        if(loginUser == null) {
            return "<script>" +
                   "alert('로그인 후 이용 가능합니다.');" +
                   "location.href='/login/loginView.do';" +
                   "</script>";
        }
        
        // 세션에서 꺼낸 로그인 유저의 고유 번호를 세팅
        report.setReporterNo(loginUser.getUserNum()); 
       
        // 서비스 단 비즈니스 로직 수행
        reportService.doInsert(report);
        
        // 💡 핵심 수정: @ResponseBody 상황이므로 redirect 문자열 대신 스크립트로 강제 이동 처리!
        // 중복 제출(F5) 방지를 위해 마이페이지 신고 내역 목록으로 보냅니다.
        return "<script>" +
               "alert('신고서 제출이 완료되었습니다.');" +
               "location.href='myReportList.do';" +
               "</script>";
    }
}