package com.pcwk.ehr.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.pcwk.ehr.product.domain.ProductSearchDTO;
import com.pcwk.ehr.product.service.ProductService;

/** 메인 화면에 실제 상품 데이터를 연결하는 Controller. */
@Controller
public class MainController {

    @Autowired
    private ProductService productService;

    /** 2026-07-14 [수정] 메인 URL은 /main.do 하나만 사용한다. */
    @GetMapping("/main.do")
    public String index(Model model) {
        model.addAttribute("popularProductList", productService.doRetrieve(mainSearch("popular")));
        model.addAttribute("recommendedProductList", productService.getRandomProducts(4));
        model.addAttribute("latestProductList", productService.doRetrieve(mainSearch("latest")));
        return "index";
    }

    private ProductSearchDTO mainSearch(String sort) {
        ProductSearchDTO search = new ProductSearchDTO();
        search.setStatus("01");
        search.setSort(sort);
        search.setPageNo(1);
        search.setPageSize(4);
        return search;
    }
}
