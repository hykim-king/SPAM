package com.pcwk.ehr.transact.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pcwk.ehr.product.domain.ProductVO;
import com.pcwk.ehr.transact.domain.TransactHistVO;
import com.pcwk.ehr.transact.service.TransactService;

@Controller
@RequestMapping("/transact")
public class TransactController {

    @Autowired
    private TransactService transactService;

    @GetMapping("/list.do")
    public String list(@RequestParam(value = "status", required = false) String status, Model model) {
        List<ProductVO> list = (status == null || status.equals("")) ? transactService.getAllProducts() : transactService.getAllProducts(status);
        model.addAttribute("list", list);
        model.addAttribute("currentStatus", status);
        return "transact/transact_list";
    }

    @GetMapping("/getDetail.do")
    @ResponseBody
    public TransactHistVO getDetail(@RequestParam("productNo") Long productNo) {
        return transactService.selectProductDetail(productNo);
    }
}