/**
 * 파일명: TransactController.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-07 <br>
 * 설　명: <br>
 */
package com.pcwk.ehr.transact.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pcwk.ehr.transact.domain.TransactHistVO;
import com.pcwk.ehr.transact.service.TransactService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Controller
@RequestMapping("/transact") 
public class TransactController {
	
	// log4j2 Logger 객체 생성
	private static final Logger log = LogManager.getLogger(TransactController.class);
	
    @Autowired
    private TransactService transactService;

    // 1. 거래 등록 페이지 이동
    @GetMapping("/view.do")
    public String view() {
        log.debug("transact view page");
        return "transact/transact_view"; // JSP 경로
    }

    // 2. 거래 등록 처리 (AJAX 사용 가정)
    @PostMapping("/doInsert.do")
    @ResponseBody
    public String doInsert(TransactHistVO vo) {
        log.debug("doInsert vo: " + vo);
        
        int flag = transactService.insertTransact(vo);
        String message = (flag > 0) ? "등록 성공" : "등록 실패";
        
        return message;
    }

    // 3. 상품별 거래 목록 조회
    @GetMapping("/list.do")
    public String selectListByProduct(Long productNo, Model model) {
        log.debug("productNo: " + productNo);
        
        List<TransactHistVO> list = transactService.selectListByProduct(productNo);
        model.addAttribute("list", list);
        
        return "transact/transact_list";
    }
}