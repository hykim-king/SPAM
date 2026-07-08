/**
 * 파일명: TransactController.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-07 <br>
 * 설 명: <br>
 */
package com.pcwk.ehr.transact.controller;

import java.util.List;

import javax.servlet.http.HttpSession; // 세션 활용을 위해 추가

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pcwk.ehr.transact.domain.TransactHistVO;
import com.pcwk.ehr.transact.service.TransactService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Controller
@RequestMapping("/transact") 
public class TransactController {
	
	private static final Logger log = LogManager.getLogger(TransactController.class);
	
    @Autowired
    private TransactService transactService;

    @GetMapping("/view.do")
    public String view() {
        return "transact/transact_view";
    }

    @PostMapping("/doInsert.do")
    @ResponseBody
    public String doInsert(TransactHistVO vo) {
        int flag = transactService.insertTransact(vo);
        return (flag > 0) ? "등록 성공" : "등록 실패";
    }

    @GetMapping("/list.do")
    public String selectListByProduct(
            @RequestParam(value = "tab", defaultValue = "product") String tab,
            @RequestParam(value = "sort", defaultValue = "latest") String sort,
            Long productNo,
            HttpSession session, // 로그인 정보를 가져오기 위한 세션
            Model model) {
        
        // 로그인한 사용자 정보 (세션에서 가져옴)
        Long loginUserNo = (Long) session.getAttribute("userNum");
        
        log.debug("tab: {}, sort: {}, productNo: {}, loginUserNo: {}", tab, sort, productNo, loginUserNo);
        
        if ("product".equals(tab)) {
            // 서비스로 productNo, loginUserNo(판매자 권한 확인용), sort 전달
            List<TransactHistVO> list = transactService.selectListByProduct(productNo, sort);
            model.addAttribute("list", list);
        } else if ("review".equals(tab)) {
            log.debug("거래후기 탭 선택됨");
        }
        
        return "transact/transact_list";
    }
}