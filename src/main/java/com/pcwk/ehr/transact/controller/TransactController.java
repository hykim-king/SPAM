package com.pcwk.ehr.transact.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pcwk.ehr.product.domain.ProductVO;
import com.pcwk.ehr.transact.service.TransactService;

@Controller
@RequestMapping("/transact")
public class TransactController {

    @Autowired
    private TransactService transactService;

    // 상품 전체 목록을 조회하여 화면에 전달
    @GetMapping("/list.do")
    public String list(Model model) {
        
        // 서비스의 getAllProducts() 메서드를 호출하여 상품 리스트를 가져옴
        List<ProductVO> list = transactService.getAllProducts();
        
        // 모델에 담아서 JSP 페이지로 전달
        model.addAttribute("list", list);
        
        // 이동할 JSP 페이지 경로 (프로젝트 설정에 맞게 수정 필요)
        return "transact/transact_list";
    }
}
