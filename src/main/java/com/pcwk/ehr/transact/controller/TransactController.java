package com.pcwk.ehr.transact.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pcwk.ehr.product.domain.ProductVO;
import com.pcwk.ehr.transact.service.TransactService;

@Controller
@RequestMapping("/transact")
public class TransactController {

    @Autowired
    private TransactService transactService;

    @GetMapping("/list.do")
    public String list(@RequestParam(value = "status", required = false) String status, Model model) {
        
        // 상태값(status)을 넘겨서 조회합니다. null이거나 빈 값이면 전체 조회하도록 서비스에서 처리합니다.
        List<ProductVO> list = transactService.getAllProducts(status);
        
        model.addAttribute("list", list);
        model.addAttribute("currentStatus", status); // 현재 선택된 상태를 JSP에서 알기 위해 전달
        
        return "transact/transact_list";
    }
}
