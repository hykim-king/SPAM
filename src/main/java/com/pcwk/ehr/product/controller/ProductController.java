package com.pcwk.ehr.product.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.pcwk.ehr.product.domain.ProductVO;
import com.pcwk.ehr.product.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 상품 목록 및 검색 조회
     * URL: /product/doRetrieve.do
     */
    @GetMapping("/doRetrieve.do")
    public String doRetrieve(ProductVO product, Model model) {
        // 부모 DTO 객체 덕분에 페이징 및 검색 조건이 자동으로 product 파라미터에 바인딩됩니다.
        List<ProductVO> list = productService.doRetrieve(product);
        
        // 화면(JSP)으로 데이터 전달
        model.addAttribute("productList", list);
        
        // WEB-INF/views/product/product_list.jsp 등으로 포워딩
        return "product/product_list"; 
    }

    /**
     * 상품 단건 상세 조회
     * URL: /product/doSelectOne.do
     */
    @GetMapping("/doSelectOne.do")
    public String doSelectOne(ProductVO product, Model model) {
        ProductVO outVO = productService.doSelectOne(product);
        model.addAttribute("product", outVO);
        
        // WEB-INF/views/product/product_detail.jsp 등으로 포워딩
        return "product/product_detail";
    }

    /**
     * 상품 등록 처리
     * URL: /product/doInsert.do
     */
    @PostMapping("/doInsert.do")
    public String doInsert(ProductVO product) {
        productService.doInsert(product);
        
        // 등록 후 목록 화면으로 리다이렉트
        return "redirect:/product/doRetrieve.do";
    }

    /**
     * 상품 수정 처리
     * URL: /product/doUpdate.do
     */
    @PostMapping("/doUpdate.do")
    public String doUpdate(ProductVO product) {
        productService.doUpdate(product);
        
        // 수정 후 해당 상품의 상세 보기 화면으로 리다이렉트
        return "redirect:/product/doSelectOne.do?productNo=" + product.getProductNo();
    }

    /**
     * 상품 삭제 처리
     * URL: /product/doDelete.do
     */
    @PostMapping("/doDelete.do")
    public String doDelete(ProductVO product) {
        productService.doDelete(product);
        
        // 삭제 후 목록 화면으로 리다이렉트
        return "redirect:/product/doRetrieve.do";
    }
}