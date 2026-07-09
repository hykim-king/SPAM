package com.pcwk.ehr.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 임시 빈 페이지 Controller
 *
 * [추가] 메인 화면의 링크가 404로 끊기지 않도록 임시 화면을 제공한다.
 * [중요] 추후 상품관리/거래내역/신고관리 페이지가 완성되면 중복 매핑을 피하기 위해 이 Controller의 해당 메서드를 삭제한다.
 */
@Controller
public class PlaceholderPageController {

    @GetMapping("/product/list.do")
    public String productList(Model model) {
        model.addAttribute("pageTitle", "상품 목록");
        model.addAttribute("pageDesc", "상품관리 모듈 연동 후 실제 상품 목록 화면으로 교체할 예정입니다.");
        return "placeholder/placeholder";
    }

    @GetMapping("/product/detail.do")
    public String productDetail(@RequestParam(value = "productNo", required = false) Integer productNo,
                                Model model) {
        model.addAttribute("pageTitle", "상품 상세");
        model.addAttribute("pageDesc", "상품번호 " + (productNo == null ? "-" : productNo) + " 상세 화면입니다. 추후 상품 상세 기능과 연결하세요.");
        return "placeholder/placeholder";
    }

    @GetMapping("/product/reg.do")
    public String productReg(Model model) {
        model.addAttribute("pageTitle", "판매 등록");
        model.addAttribute("pageDesc", "상품 등록 화면입니다. 추후 상품관리 등록 기능과 연결하세요.");
        return "placeholder/placeholder";
    }

    @GetMapping("/product/popular.do")
    public String popularProducts(Model model) {
        model.addAttribute("pageTitle", "인기 상품");
        model.addAttribute("pageDesc", "조회수/찜 수 기준 인기 상품 목록으로 연결할 예정입니다.");
        return "placeholder/placeholder";
    }

    @GetMapping("/product/recommend.do")
    public String recommendedProducts(Model model) {
        model.addAttribute("pageTitle", "오늘의 추천");
        model.addAttribute("pageDesc", "관리자 추천 또는 추천 알고리즘 기준 상품 목록으로 연결할 예정입니다.");
        return "placeholder/placeholder";
    }

    @GetMapping("/product/latest.do")
    public String latestProducts(Model model) {
        model.addAttribute("pageTitle", "최신 등록 상품");
        model.addAttribute("pageDesc", "등록일 최신순 상품 목록으로 연결할 예정입니다.");
        return "placeholder/placeholder";
    }

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
