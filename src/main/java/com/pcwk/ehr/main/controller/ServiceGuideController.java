package com.pcwk.ehr.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 서비스 안내 Controller
 *
 * 공지사항, 이용약관, FAQ, 안전거래 가이드를 하나의 탭 화면으로 제공한다.
 */
@Controller
public class ServiceGuideController {

    @GetMapping("/service/info.do")
    public String serviceInfo(@RequestParam(value = "tab", required = false, defaultValue = "notice") String tab,
                              Model model) {
        if (!"notice".equals(tab) && !"terms".equals(tab) && !"faq".equals(tab) && !"safe".equals(tab)) {
            tab = "notice";
        }

        model.addAttribute("activeTab", tab);
        return "service/service_info";
    }
}
