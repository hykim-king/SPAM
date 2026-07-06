package com.pcwk.ehr.account.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pcwk.ehr.account.domain.AccountVO;
import com.pcwk.ehr.account.service.AccountService;
import com.pcwk.ehr.user.domain.UserVO;

@Controller
@RequestMapping("/account")
public class AccountController {

    /*
     * UserController.login()에서 세션에 저장하는 key와 동일해야 함
     */
    private static final String SESSION_LOGIN_USER = "loginUser";

    @Autowired
    private AccountService accountService;


    /**
     * 계좌 컨트롤러 매핑 확인용
     *
     * 요청 URL: GET /account/ping.do
     */
    @ResponseBody
    @RequestMapping(value = "/ping.do", method = RequestMethod.GET)
    public String ping() {
        return "account ping ok";
    }

    /**
     * 계좌/자산 목록 화면
     *
     * 요청 URL: GET /account/list.do
     */
    @RequestMapping(value = "/list.do", method = RequestMethod.GET)
    public String list(HttpSession session, Model model) {
        UserVO loginUser = getLoginUser(session);

        if (loginUser == null) {
            return "redirect:/user/login.do";
        }

        List<AccountVO> accountList = accountService.getAccountList(loginUser.getUserNum());
        BigDecimal totalBalance = accountService.getTotalBalance(loginUser.getUserNum());

        model.addAttribute("accountList", accountList);
        model.addAttribute("totalBalance", totalBalance);

        return "account/account_list";
    }

    /**
     * 계좌 등록 화면
     *
     * 요청 URL: GET /account/add.do
     */
    @RequestMapping(value = "/add.do", method = RequestMethod.GET)
    public String addView(HttpSession session, Model model) {
        UserVO loginUser = getLoginUser(session);

        if (loginUser == null) {
            return "redirect:/user/login.do";
        }

        model.addAttribute("account", new AccountVO());
        model.addAttribute("mode", "add");

        return "account/account_form";
    }

    /**
     * 계좌 등록 처리
     *
     * 요청 URL: POST /account/add.do
     */
    @RequestMapping(value = "/add.do", method = RequestMethod.POST)
    public String add(@ModelAttribute AccountVO account,
                      HttpSession session,
                      Model model,
                      RedirectAttributes redirectAttributes) {
        UserVO loginUser = getLoginUser(session);

        if (loginUser == null) {
            return "redirect:/user/login.do";
        }

        try {
            // 화면에서 userNum을 조작해도 세션 회원번호로 강제 세팅
            account.setUserNum(loginUser.getUserNum());

            accountService.createAccount(account);

            redirectAttributes.addFlashAttribute("msg", "계좌가 등록되었습니다.");
            return "redirect:/account/list.do";

        } catch (RuntimeException e) {
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("account", account);
            model.addAttribute("mode", "add");
            return "account/account_form";
        }
    }

    /**
     * 계좌 수정 화면
     *
     * 요청 URL: GET /account/update.do?accountId=계좌고유번호
     */
    @RequestMapping(value = "/update.do", method = RequestMethod.GET)
    public String updateView(@RequestParam("accountId") Long accountId,
                             HttpSession session,
                             Model model) {
        UserVO loginUser = getLoginUser(session);

        if (loginUser == null) {
            return "redirect:/user/login.do";
        }

        AccountVO account = accountService.getAccount(accountId, loginUser.getUserNum());

        model.addAttribute("account", account);
        model.addAttribute("mode", "update");

        return "account/account_form";
    }

    /**
     * 계좌 수정 처리
     *
     * 요청 URL: POST /account/update.do
     */
    @RequestMapping(value = "/update.do", method = RequestMethod.POST)
    public String update(@ModelAttribute AccountVO account,
                         HttpSession session,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        UserVO loginUser = getLoginUser(session);

        if (loginUser == null) {
            return "redirect:/user/login.do";
        }

        try {
            // 계좌 소유자 조작 방지
            account.setUserNum(loginUser.getUserNum());

            accountService.updateAccount(account);

            redirectAttributes.addFlashAttribute("msg", "계좌정보가 수정되었습니다.");
            return "redirect:/account/list.do";

        } catch (RuntimeException e) {
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("account", account);
            model.addAttribute("mode", "update");
            return "account/account_form";
        }
    }

    /**
     * 계좌 삭제 처리
     *
     * 요청 URL: POST /account/delete.do
     */
    @RequestMapping(value = "/delete.do", method = RequestMethod.POST)
    public String delete(@RequestParam("accountId") Long accountId,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        UserVO loginUser = getLoginUser(session);

        if (loginUser == null) {
            return "redirect:/user/login.do";
        }

        try {
            accountService.deleteAccount(accountId, loginUser.getUserNum());
            redirectAttributes.addFlashAttribute("msg", "계좌가 삭제되었습니다.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("msg", e.getMessage());
        }

        return "redirect:/account/list.do";
    }

    /**
     * 계좌 충전/입금 처리
     *
     * 요청 URL: POST /account/deposit.do
     */
    @RequestMapping(value = "/deposit.do", method = RequestMethod.POST)
    public String deposit(@RequestParam("accountId") Long accountId,
                          @RequestParam("amount") BigDecimal amount,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        UserVO loginUser = getLoginUser(session);

        if (loginUser == null) {
            return "redirect:/user/login.do";
        }

        try {
            accountService.deposit(accountId, loginUser.getUserNum(), amount);
            redirectAttributes.addFlashAttribute("msg", "잔액이 충전되었습니다.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("msg", e.getMessage());
        }

        return "redirect:/account/list.do";
    }

    /**
     * 계좌 출금/차감 처리
     *
     * 요청 URL: POST /account/withdraw.do
     */
    @RequestMapping(value = "/withdraw.do", method = RequestMethod.POST)
    public String withdraw(@RequestParam("accountId") Long accountId,
                           @RequestParam("amount") BigDecimal amount,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        UserVO loginUser = getLoginUser(session);

        if (loginUser == null) {
            return "redirect:/user/login.do";
        }

        try {
            accountService.withdraw(accountId, loginUser.getUserNum(), amount);
            redirectAttributes.addFlashAttribute("msg", "잔액이 차감되었습니다.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("msg", e.getMessage());
        }

        return "redirect:/account/list.do";
    }

    /**
     * 세션에서 로그인 회원정보를 꺼내는 공통 메서드
     */
    private UserVO getLoginUser(HttpSession session) {
        Object loginUser = session.getAttribute(SESSION_LOGIN_USER);

        if (loginUser instanceof UserVO) {
            return (UserVO) loginUser;
        }

        return null;
    }
}
