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

    // 상품 전체 목록 조회 (필터링 기능 포함)
    @GetMapping("/list.do")
    public String list(@RequestParam(value = "status", required = false) String status, Model model) {
        
        List<ProductVO> list;
        
        if (status == null || status.equals("")) {
            list = transactService.getAllProducts();
        } else {
            // 이 기능이 동작하려면 TransactService에 오버로딩된 getAllProducts(String)가 있어야 합니다.
            // 만약 없으시다면 이전 단계에서 구현한 코드를 확인해 주세요.
            list = transactService.getAllProducts(status);
        }
        
        model.addAttribute("list", list);
        model.addAttribute("currentStatus", status);
        
        return "transact/transact_list";
    }
    
    // [추가] 모달 상세 조회용 JSON 데이터 반환 메서드
    @GetMapping("/getDetail.do")
    @ResponseBody
    public TransactHistVO getDetail(@RequestParam("productNo") Long productNo) {
        // 상품번호를 기준으로 상세 거래 내역을 조회
        return transactService.selectByTxId(productNo);
    }
}
