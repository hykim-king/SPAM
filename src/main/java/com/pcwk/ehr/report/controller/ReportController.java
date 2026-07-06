/**
 * 파일명: ReportController.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-06 <br>
 * 설　명: <br>
 */
package com.pcwk.ehr.report.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pcwk.ehr.report.domain.ReportSearchDTO;
import com.pcwk.ehr.report.domain.ReportVO;
import com.pcwk.ehr.report.service.ReportService;

@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // 1. 신고 목록 조회 (관리자용)
    @GetMapping("/doRetrieve.do")
    public String doRetrieve(ReportSearchDTO search, Model model) {
        model.addAttribute("list", reportService.doRetrieve(search));
        return "report/report_list";
    }

    // 2. 신고 등록 (사용자용)
    @PostMapping("/doInsert.do")
    public String doInsert(ReportVO report) {
        reportService.doInsert(report);
        return "redirect:/report/doRetrieve.do";
    }

    // 3. 신고 처리 상태 업데이트
    @PostMapping("/doUpdateStatus.do")
    public String doUpdateStatus(ReportVO report) {
        reportService.doUpdateStatus(report);
        return "redirect:/report/doRetrieve.do";
    }
}