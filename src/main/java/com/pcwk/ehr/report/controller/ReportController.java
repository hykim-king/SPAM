/**
 * 파일명: ReportController.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-01 <br>
 * 설　명: <br>
 */
package com.pcwk.ehr.report.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pcwk.ehr.cmn.MessageVO;
import com.pcwk.ehr.report.domain.ReportVO;
import com.pcwk.ehr.report.service.ReportService;


@Controller
@RequestMapping("/report")
public class ReportController {
	
	Logger log = LogManager.getLogger(getClass());

    @Autowired
    ReportService reportService;

    public ReportController() {
        super();
        log.debug("---------------------------");
        log.debug("ReportController()*");
        log.debug("---------------------------");
    }

    @GetMapping("/report_reg.do")
    public String reportReg() {
        log.debug("---------------------------");
        log.debug("reportReg()*");
        log.debug("---------------------------");
        return "report/report_reg"; // /WEB-INF/views/report/report_reg.jsp
    }
    
    /**
     * 신고 등록 (AJAX 요청 처리)
     */
    @PostMapping(value = "/doSave.do")
    @ResponseBody
    public MessageVO doSave(@ModelAttribute ReportVO param) {
        log.debug("---------------------------");
        log.debug("doSave()*");
        log.debug("---------------------------");
        log.debug("1. param: " + param);

        int flag = reportService.doSave(param);
        log.debug("2. return flag: " + flag);

        String message = "";
        if (1 == flag) {
            message = "신고가 정상적으로 접수되었습니다.";
        } else {
            message = "신고 접수에 실패했습니다.";
        }
        
        MessageVO messageVO = new MessageVO(flag + "", message);
        log.debug("3. messageVO: " + messageVO);
        
        return messageVO;
    }
}
