package com.pcwk.ehr.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 공통 메뉴에서 사용하는 기존 호환 URL을 실제 구현 화면으로 연결한다.
 */
@Controller
public class CommonRouteController {

    @GetMapping("/trade/history.do")
    public String tradeHistory() {
        return "redirect:/transact/list.do";
    }

    @GetMapping("/report/view.do")
    public String reportView() {
        // 2026-07-14 [수정] 존재하지 않는 임시 목록 URL 대신 실제 내 신고 목록으로 연결한다.
        return "redirect:/report/myReportList.do";
    }

    @GetMapping("/guide/safe.do")
    public String safeGuide() {
        return "redirect:/service/info.do?tab=safe";
    }
}
