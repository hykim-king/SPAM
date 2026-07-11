package com.pcwk.ehr.transact.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pcwk.ehr.transact.domain.TransactHistVO;
import com.pcwk.ehr.transact.mapper.TransactHistMapper;

@Controller
@RequestMapping("/transact")
public class TransactController {

    @Autowired
    private TransactHistMapper transactMapper;

    // 1. 거래 내역 리스트 조회
    @GetMapping("/list.do")
    public String list(@RequestParam(value = "type", defaultValue = "purchase") String type,
                       HttpSession session, Model model) {
        
        Long userNum = (Long) session.getAttribute("loginUserNo");
        
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userNum", userNum);
        paramMap.put("type", type);
        
        List<TransactHistVO> list = transactMapper.selectListByUser(paramMap);
        
        model.addAttribute("list", list);
        model.addAttribute("type", type);
        
        return "transact/transact_list";
    }

    // 2. 상태 변경 (AJAX 요청 처리 - 사용자님 코드 반영)
    @RequestMapping(value = "/updateTxStatus.do", method = RequestMethod.POST)
    @ResponseBody
    public String updateTxStatus(@RequestParam("txId") long txId, 
                                 @RequestParam("status") String status) {
        
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("txId", txId);
        paramMap.put("status", status);
        
        // 매퍼 직접 호출 (서비스 계층이 없다면 이렇게 사용)
        int result = transactMapper.updateTxStatus(paramMap);
        
        // 결과 반환 ("success" 또는 "fail")
        return (result > 0) ? "success" : "fail";
    }
}