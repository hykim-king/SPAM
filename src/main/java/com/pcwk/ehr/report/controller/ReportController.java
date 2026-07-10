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
    @GetMapping("/doRetrieve.do")
    public String doRetrieve(ReportSearchDTO search, Model model) {
        model.addAttribute("list", reportService.doRetrieve(search));
        return "report/admin_report_list"; // 관리자 전용 목록 view
    }
    
    // 2. 관리자용 신고 처리 상태 변경 (접수/완료/반려)
    @PostMapping("/doUpdateStatus.do")
    public String doUpdateStatus(ReportVO report) {
        reportService.doUpdateStatus(report);
        return "redirect:/report/doRetrieve.do";
    }
    
    // ================ 사용자 ================
    // ================ 사용자 ================
    
    // 1. 상품 신고등록 페이지 GET 요청 
    @GetMapping("/reportProductForm.do")
    public String reportProductForm() {
        return "report/report_product_form"; 
    }

    // 2. 유저 신고등록 페이지 GET 요청
    @GetMapping("/reportUserForm.do")
    public String reportUserForm() {
        return "report/report_user_form"; 
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
               "location.href='/report/myReportList.do';" +
               "</script>";
    }
    
    // 4. 나의 신고 내역 목록 조회 (내가 한 신고 / 내가 당한 신고)
    @GetMapping("/myReportList.do")
    public String myReportList(HttpSession session, Model model) {
        UserVO loginUser = (UserVO) session.getAttribute("loginUser");
        
        // 로그인 되어있지 않으면 로그인 페이지로 강제 리다이렉트
        if (loginUser == null) {
            return "redirect:/login/loginView.do";
        }
        
        // 내가 신고한 목록 조회 및 바인딩
        long currentUserNum = loginUser.getUserNum();
        List<ReportVO> myReportList = reportService.doRetrieveMyReports(currentUserNum);
        model.addAttribute("myReportList", myReportList);
        
        // 내가 신고당한 목록 조회 및 바인딩 
        List<ReportVO> receivedReportList = reportService.doRetrieveReceivedReports(currentUserNum);
        model.addAttribute("receivedReportList", receivedReportList);
        
        return "report/report_list"; 
    }
    
    // 5. 신고 상세 조회
    @GetMapping("/doSelectOne.do")
    public String doSelectOne(ReportVO report, Model model) {
        // 신고번호(reportNo)를 기준으로 서비스에서 데이터를 조회해와
        ReportVO outVO = reportService.doSelectOne(report);
        
        // 조회된 데이터를 모델에 담아서 JSP로 전달
        model.addAttribute("reportVO", outVO);
        
        return "report/report_detail";
    }
}