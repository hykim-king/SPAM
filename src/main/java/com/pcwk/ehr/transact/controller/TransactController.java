package com.pcwk.ehr.transact.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired; // 추가
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.pcwk.ehr.transact.domain.TransactHistVO;
import com.pcwk.ehr.transact.mapper.TransactHistMapper; // 추가

@Controller
@RequestMapping("/transact")
public class TransactController {

    @Autowired // 이 부분이 없으면 transactMapper가 null이라서 에러가 납니다.
    private TransactHistMapper transactMapper;

    @GetMapping("/list.do")
    public String list(@RequestParam(value = "type", defaultValue = "purchase") String type,
                       HttpSession session, Model model) {
        
        Long userNum = (Long) session.getAttribute("loginUserNo");
        
        if (userNum == null) {
            return "redirect:/user/login.do";
        }
        
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userNum", userNum);
        paramMap.put("type", type);
        
        List<TransactHistVO> list = transactMapper.selectListByUser(paramMap);
        
        model.addAttribute("list", list);
        model.addAttribute("type", type);
        
        return "transact/transact_list";
    }

    @RequestMapping(value = "/updateTxStatus.do", method = RequestMethod.POST)
    @ResponseBody
    public String updateTxStatus(@RequestParam("txId") long txId, 
                                 @RequestParam("status") String status) {
        
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("txId", txId);
        paramMap.put("status", status);
        
        int result = transactMapper.updateTxStatus(paramMap);
        
        return (result > 0) ? "success" : "fail";
    }
}