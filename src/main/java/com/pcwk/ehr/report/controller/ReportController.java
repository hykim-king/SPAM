/**
 * 파일명: ReportController.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-06 <br>
 * 설 명: 신고 기능 컨트롤러 (관리자 / 사용자 기능 통합) <br>
 */
package com.pcwk.ehr.report.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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

    private static final String SESSION_LOGIN_USER = "loginUser";
    private static final String ROLE_ADMIN = "02";

    @Autowired
    private ReportService reportService;

    /**
     * 2026-07-13 [추가] 공통 신고 메뉴 진입점.
     * 관리자는 전체 신고 목록, 일반 회원은 자신의 신고 목록으로 이동한다.
     */
    @GetMapping("/doRetrieve.do")
    public String reportHome(HttpSession session) {
        UserVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/main.do?modal=login";
        }

        return ROLE_ADMIN.equals(loginUser.getUserRole())
                ? "redirect:/report/admin_doRetrieve.do"
                : "redirect:/report/myReportList.do";
    }

    // ================ 관리자 ================

    // 1. 신고 목록 조회 (관리자용 전체 조회)
    @GetMapping("/admin_doRetrieve.do")
    public String doRetrieve(ReportSearchDTO search, Model model, HttpSession session) {
        // 2026-07-13 [수정] 비로그인과 권한 부족을 공통 안내 모달로 구분한다.
        if (!isAdmin(session)) {
            return accessDeniedRedirect(session);
        }

        // 2. 파라미터가 비어있거나 잘못 왔을 때 강제 보정
        int pNo = search.getPageNo() <= 0 ? 1 : search.getPageNo();
        int pSize = 10; // 한 페이지당 10개 고정

        // 3. 연산 순서 꼬임 방지를 위해 직접 연산 후 DTO 변수에 강제 주입
        int computedStart = (pNo - 1) * pSize + 1;
        int computedEnd = computedStart + pSize - 1;

        // DTO 멤버 변수에 정확한 값 세팅
        search.setPageNo(pNo);
        search.setPageSize(pSize);
        search.setStartRow(computedStart);
        search.setEndRow(computedEnd);

        // 디버깅 로그: 페이징 계산값 확인용
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
        if (!isAdmin(session)) {
            return accessDeniedRedirect(session);
        }

        ReportVO outVO = reportService.doSelectOne(report);
        model.addAttribute("outVO", outVO);

        return "report/admin_report_detail";
    }

    // 3. 관리자용 신고 처리 상태 변경 (접수/완료/반려)
    @PostMapping(value = "/doUpdateStatus.do", produces = "text/html; charset=UTF-8")
    @ResponseBody
    public String doUpdateStatus(ReportVO report,
                                 HttpSession session,
                                 HttpServletRequest request) {
        UserVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return scriptRedirect(
                    "",
                    request.getContextPath() + "/main.do?modal=login");
        }
        if (!ROLE_ADMIN.equals(loginUser.getUserRole())) {
            return scriptRedirect(
                    "",
                    request.getContextPath() + "/main.do?modal=forbidden");
        }

        report.setAdminNo(loginUser.getUserNum());
        reportService.doUpdateStatus(report);

        return scriptRedirect(
                "수정되었습니다.",
                request.getContextPath() + "/report/admin_doRetrieve.do");
    }

    // ================ 사용자 ================

    // 1. 상품 신고등록 페이지 GET 요청
    @GetMapping({"/reportProductForm.do", "/report_product_form.do"})
    public String reportProductForm(HttpSession session) {
        if (getLoginUser(session) == null) {
            return "redirect:/main.do?modal=login";
        }
        return "report/report_product_form";
    }

    // 2. 유저 신고등록 페이지 GET 요청
    @GetMapping({"/reportUserForm.do", "/report_user_form.do"})
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

        if (loginUser == null) {
            return "redirect:/main.do?modal=login";
        }

        long currentUserNum = loginUser.getUserNum();
        List<ReportVO> myReportList = reportService.doRetrieveMyReports(currentUserNum);
        model.addAttribute("myReportList", myReportList);

        List<ReportVO> receivedReportList = reportService.doRetrieveReceivedReports(currentUserNum);
        model.addAttribute("receivedReportList", receivedReportList);

        return "report/my_report_list";
    }

    // 4. 신고 상세 조회
    @GetMapping("/doSelectOne.do")
    public String doSelectOne(ReportVO report,
                              Model model,
                              HttpSession session) {
        if (getLoginUser(session) == null) {
            return "redirect:/main.do?modal=login";
        }

        ReportVO outVO = reportService.doSelectOne(report);
        model.addAttribute("outVO", outVO);

        return "report/report_detail";
    }

    // 5. 신고 등록 POST 요청 (@ResponseBody 활용 문자열 반환)
    @PostMapping(value = "/doInsert.do", produces = "text/html; charset=UTF-8")
    @ResponseBody
    public String doInsert(ReportVO report,
                           HttpSession session,
                           HttpServletRequest request) {

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
        UserVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return scriptRedirect(
                    "",
                    request.getContextPath() + "/main.do?modal=login");
        }

        report.setReporterNo(loginUser.getUserNum());
        reportService.doInsert(report);

        return scriptRedirect(
                "신고서 제출이 완료되었습니다.",
                request.getContextPath() + "/report/myReportList.do");
    }

    /** 2026-07-13 [추가] 비로그인과 관리자 권한 부족의 이동 화면을 구분한다. */
    private String accessDeniedRedirect(HttpSession session) {
        return getLoginUser(session) == null
                ? "redirect:/main.do?modal=login"
                : "redirect:/main.do?modal=forbidden";
    }

    private boolean isAdmin(HttpSession session) {
        UserVO loginUser = getLoginUser(session);
        return loginUser != null && ROLE_ADMIN.equals(loginUser.getUserRole());
    }

    private UserVO getLoginUser(HttpSession session) {
        Object loginUser = session.getAttribute(SESSION_LOGIN_USER);
        return loginUser instanceof UserVO ? (UserVO) loginUser : null;
    }

    private String scriptRedirect(String message, String targetUrl) {
        String alertScript = (message == null || message.trim().isEmpty())
                ? ""
                : "alert('" + message + "');";

        return "<script>" +
               alertScript +
               "location.href='" + targetUrl + "';" +
               "</script>";
    }
}
