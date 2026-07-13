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
import com.pcwk.ehr.transact.domain.TransacHistSearchDTO;
import com.pcwk.ehr.transact.domain.TransactHistVO;
import com.pcwk.ehr.transact.service.TransactService;

@Controller
@RequestMapping("/transact")
public class TransactController {

    @Autowired
    private TransactService transactService;

    // 1. 기존 필터 기능 + 페이징 기능 통합
    @GetMapping("/list.do")
    public String list(TransacHistSearchDTO dto, 
                       @RequestParam(value = "status", required = false) String status, 
                       Model model) {
        
        dto.setStatus(status); 
        
        // 1. 리스트 조회 및 전체 건수 조회
        List<ProductVO> list = transactService.selectProductListPaged(dto);
        int totalCount = transactService.getTotalCount(dto);
        
        // [수정] 페이징 계산 호출
        dto.calculatePaging(totalCount);
        
        model.addAttribute("list", list);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("paging", dto);
        model.addAttribute("currentStatus", status); 
        
        return "transact/transact_list";
    }

    // 2. 모달 상세 조회 기능 (유지)
    @GetMapping("/getDetail.do")
    @ResponseBody
    public TransactHistVO getDetail(@RequestParam("productNo") Long productNo) {
        return transactService.selectProductDetail(productNo);
    }
}