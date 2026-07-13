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
import com.pcwk.ehr.user.domain.UserVO;

@Controller
@RequestMapping("/transact")
public class TransactController {

    private static final String SESSION_LOGIN_USER = "loginUser";

    @Autowired
    private TransactHistMapper transactMapper;

    // 1. 거래 내역 리스트 조회
    @GetMapping("/list.do")
    public String list(@RequestParam(value = "type", defaultValue = "purchase") String type,
                       HttpSession session, Model model) {

        // 2026-07-13 [수정] 회원 Controller와 동일한 loginUser 세션을 사용한다.
        UserVO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/main.do?modal=login";
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userNum", loginUser.getUserNum());
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
                                 @RequestParam("status") String status,
                                 HttpSession session) {

        // 2026-07-13 [추가] 세션이 만료된 상태에서는 거래 상태를 변경하지 않는다.
        if (getLoginUser(session) == null) {
            return "NEED_LOGIN";
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("txId", txId);
        paramMap.put("status", status);

        // 매퍼 직접 호출 (서비스 계층이 없다면 이렇게 사용)
        int result = transactMapper.updateTxStatus(paramMap);

        // 결과 반환 ("success" 또는 "fail")
        return (result > 0) ? "success" : "fail";
    }

    /** 세션에서 로그인 회원정보를 안전하게 꺼낸다. */
    private UserVO getLoginUser(HttpSession session) {
        Object loginUser = session.getAttribute(SESSION_LOGIN_USER);
        return loginUser instanceof UserVO ? (UserVO) loginUser : null;
    }
}
