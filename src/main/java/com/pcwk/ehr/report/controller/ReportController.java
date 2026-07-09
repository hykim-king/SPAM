/**
 * 파일명: ReportController.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-06 <br>
 * 설　명: <br>
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


@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // ================관리자================
    // ================관리자================

    // 1. 신고 목록 조회 (관리자용)
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
    
    // ================사용자================
    // ================사용자================
    
    // 1. 신고등록 페이지 GET 요청 
    @GetMapping("/report_product_form.do")
    public String reportProductForm() {
        return "report/report_product_form"; 
    }
    
    @GetMapping("/report_user_form.do")
    public String reportUserForm() {
        return "report/report_user_form"; 
    }
    
    // 2. 신고 등록 POST 요청
    @PostMapping(value="/doInsert.do", produces="text/html; charset=UTF-8")
    @ResponseBody
    public String doInsert(ReportVO report) {
    	// [유효성 검사] 신고 사유 미선택
        // 폼에서 빈 값이나 공백만 넘어오거나 null일 때 체크
        if (report.getReportType() == null || report.getReportType().trim().isEmpty()) {
            return "<script>" +
                   "alert('신고양식을 제대로 작성해주세요.');" +
                   "history.back();" +
                   "</script>";
        }
        
        // [유효성 검사] 상세내용 미입력 검사
        if (report.getReason() == null || report.getReason().trim().isEmpty()) {
            return "<script>" +
                   "alert('신고양식을 제대로 작성해주세요.');" +
                   "history.back();" +
                   "</script>";
        }
        
        // [유효성 검사] 상세내용 1000자 이상 검사
        // 1000자 이상일 때 제출을 차단합니다.
        if (report.getReason().length() >= 1000) {
            return "<script>" +
                   "alert('신고양식은 1000자 미만으로 작성해주세요.');" +
                   "history.back();" +
                   "</script>";
        }
        
        // 💡 현재 로그인한 사용자의 고유 번호를 신고자(RegNo)로 설정하기 위해 세션 활용
        // (세션에 보관하는 키값과 VO 구조에 맞춰 변형해줄것!)
        /*
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
        if(loginUser == null) {
            return "<script>alert('로그인 후 이용 가능합니다.'); location.href='/login/loginView.do';</script>";
        }
        report.setRegNo(loginUser.getMemberNo()); 
        */
        reportService.doInsert(report);
        
        return "redirect:/report/doRetrieve.do";
    }
    
    // 3. 나의 신고 내역 목록 조회 (내가 한 신고 / 내가 당한 신고)
    @GetMapping("/myReportList.do")
    public String myReportList(HttpSession session, Model model) {
        
        // 💡 세션에서 현재 로그인한 유저의 식별 정보(No 또는 ID) 추출
        int currentMemberNo = 163; // TODO: 테스트용 하드코딩, 실제 세션 연동시 아래 주석 해제!
        /*
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
        if(loginUser == null) {
            return "redirect:/login/loginView.do"; // 로그인 안 되어 있으면 로그인 페이지로
        }
        currentMemberNo = loginUser.getMemberNo();
        */
        
        // ① 내가 신고한 목록 조회 (신고자 조건: currentMemberNo)
        // ReportService에 관련 메서드(예: doRetrieveMyReports)를 추가해서 뽑아오면 최고야!
        List<ReportVO> myReportList = reportService.doRetrieveMyReports(currentMemberNo);
        
        // ② 내가 신고 당한 목록 조회 (피신고자 조건: currentMemberNo)
        List<ReportVO> receivedReportList = reportService.doRetrieveReceivedReports(currentMemberNo);
        
        // JSP로 데이터 전달
        model.addAttribute("myReportList", myReportList);
        model.addAttribute("receivedReportList", receivedReportList);
        
        // 새로 만든 사용자용 탭 전환 목록 페이지로 이동
        return "report/my_report_list"; 
    }
}