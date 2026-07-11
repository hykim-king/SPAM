package com.pcwk.ehr.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** 아직 별도 화면이 없는 공통 링크를 실제 구현 URL로 연결한다. */
@Controller
public class PlaceholderPageController {

    @GetMapping("/trade/history.do")
    public String tradeHistory() {
        return "redirect:/transact/list.do";
    }

    @GetMapping("/report/view.do")
    public String reportView() {
        return "redirect:/report/doRetrieve.do";
    }

    @GetMapping("/guide/safe.do")
    public String safeGuide() {
        return "redirect:/service/info.do?tab=safe";
    }
}
