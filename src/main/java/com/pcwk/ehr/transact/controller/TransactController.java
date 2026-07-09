package com.pcwk.ehr.transact.controller;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
            HttpSession session,
            Model model) {

        Long loginUserNo = (Long) session.getAttribute("userNum");
        log.debug("tab: {}, sort: {}, loginUserNo: {}", tab, sort, loginUserNo);

        if ("product".equals(tab)) {
            List<TransactHistVO> list = transactService.selectListByProduct(loginUserNo, sort);
            model.addAttribute("list", list);
        }
        return "transact/transact_list";
    }
}