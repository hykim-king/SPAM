package com.pcwk.ehr.user.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pcwk.ehr.user.domain.UserVO;
import com.pcwk.ehr.user.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    /*
     * 로그인한 회원 정보를 HttpSession에 저장할 때 사용하는 key 
     * 다른 Controller에서도 같은 key로 꺼내야 하므로 상수로 관리 
     */
    private static final String SESSION_LOGIN_USER = "loginUser";

    /*
     * 관리자 권한 코드 
     * DB USER_INFO.USER_ROLE 컬럼의 02 관리자 코드값과 동일해야 함
     */
    private static final String ROLE_ADMIN = "02";

    @Autowired
    private UserService userService;

    /**
     * 회원가입 화면 이동
     *
     * 요청 URL: GET /user/join.do
     * 반환 View: /WEB-INF/views/user/user_join.jsp
     */
    @GetMapping("/join.do")
    public String joinView(Model model) {
    	// null 오류를 피하기 위해 빈 UserVO를 미리 넣어둠
        model.addAttribute("user", new UserVO());

        return "user/user_join";
    }

    /**
     * 회원가입 처리
     *
     * 요청 URL: POST /user/join.do
     */
    @PostMapping("/join.do")
    public String join(@ModelAttribute UserVO user,
                       @RequestParam("passwordConfirm") String passwordConfirm,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        try {
            // 비밀번호와 비밀번호 확인값이 같은지 서버에서 검증
            if (user.getPassword() == null || !user.getPassword().equals(passwordConfirm)) {
                throw new IllegalArgumentException("비밀번호와 비밀번호 확인 값이 다릅니다.");
            }

            userService.join(user);

            // redirect 후 로그인 화면에서 보여줄 메시지
            redirectAttributes.addFlashAttribute("msg", "회원가입이 완료되었습니다. 로그인하세요.");

            return "redirect:/user/login.do";

        } catch (RuntimeException e) {
            // 회원가입 실패 시 다시 회원가입 화면으로 돌아감
            model.addAttribute("msg", e.getMessage());

            model.addAttribute("user", user);

            return "user/user_join";
        }
    }

    /**
     * 로그인 화면 이동
     *
     * 요청 URL: GET /user/login.do
     */
    @GetMapping("/login.do")
    public String loginView() {
        return "user/user_login";
    }

    /**
     * 로그인 처리
     *
     * 요청 URL: POST /user/login.do
     *
     * 로그인 성공 후 이동:
     * - 02 관리자 권한: /admin/user/list.do
     * - 01 일반회원 권한 : /user/mypage.do
     */
    @PostMapping("/login.do")
    public String login(@RequestParam("userId") String userId,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        try {
            UserVO loginUser = userService.login(userId, password);

            // 로그인 성공 회원정보를 세션에 저장
            session.setAttribute(SESSION_LOGIN_USER, loginUser);

            // 관리자 -> 로그인 후 관리자 회원목록으로 이동
            if (ROLE_ADMIN.equals(loginUser.getUserRole())) {
                return "redirect:/admin/user/list.do";
            }

            // 일반 회원 -> 로그인 후 마이페이지로 이동
            return "redirect:/user/mypage.do";

        } catch (RuntimeException e) {
            // 로그인 실패 시 로그인 화면으로 돌아감
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("userId", userId);
            return "user/user_login";
        }
    }

    /**
     * 로그아웃 처리
     *
     * 요청 URL: GET /user/logout.do
     */
    @GetMapping("/logout.do")
    public String logout(HttpSession session) {
        // 세션 전체 제거
        session.invalidate();

        return "redirect:/user/login.do";
    }

    /**
     * 마이페이지
     *
     * 요청 URL: GET /user/mypage.do
     */
    @GetMapping("/mypage.do")
    public String mypage(HttpSession session, Model model) {
        // 세션에서 로그인 회원을 꺼냄
        UserVO loginUser = getLoginUser(session);

        // 로그인하지 않은 사용자는 로그인 화면으로 이동
        if (loginUser == null) {
            return "redirect:/user/login.do";
        }

        // DB에서 최신 정보 재조회
        UserVO user = userService.getUser(loginUser.getUserNum());

        model.addAttribute("user", user);

        return "user/user_mypage";
    }

    /**
     * 회원정보 수정 화면 이동
     *
     * 요청 URL: GET /user/update.do
     */
    @GetMapping("/update.do")
    public String updateView(HttpSession session, Model model) {
        UserVO loginUser = getLoginUser(session);

        if (loginUser == null) {
            return "redirect:/user/login.do";
        }

        UserVO user = userService.getUser(loginUser.getUserNum());

        model.addAttribute("user", user);

        return "user/user_update";
    }

    /**
     * 회원정보 수정 처리
     *
     * 요청 URL: POST /user/update.do
     */
    @PostMapping("/update.do")
    public String update(@ModelAttribute UserVO user,
                         HttpSession session,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        UserVO loginUser = getLoginUser(session);

        if (loginUser == null) {
            return "redirect:/user/login.do";
        }

        try {
            // 세션의 회원번호로 강제로 덮어씀
            user.setUserNum(loginUser.getUserNum());

            userService.updateUser(user);

            // 수정 후 최신 정보를 다시 조회해 세션 갱신
            UserVO updatedUser = userService.getUser(loginUser.getUserNum());
            session.setAttribute(SESSION_LOGIN_USER, updatedUser);

            redirectAttributes.addFlashAttribute("msg", "회원정보가 수정되었습니다.");
            return "redirect:/user/mypage.do";

        } catch (RuntimeException e) {
            // 수정 실패 시 수정 화면 다시 보여줌
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("user", user);
            return "user/user_update";
        }
    }

    /**
     * 비밀번호 변경 처리
     *
     * 요청 URL: POST /user/password.do
     */
    @PostMapping("/password.do")
    public String updatePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        UserVO loginUser = getLoginUser(session);

        if (loginUser == null) {
            return "redirect:/user/login.do";
        }

        try {
            // 현재 비밀번호 확인 후, 새 비밀번호로 변경
            userService.updatePassword(loginUser.getUserNum(), currentPassword, newPassword);

            redirectAttributes.addFlashAttribute("msg", "비밀번호가 변경되었습니다.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("msg", e.getMessage());
        }

        return "redirect:/user/update.do";
    }

    /**
     * 회원탈퇴 처리
     *
     * 요청 URL: POST /user/withdraw.do
     */
    @PostMapping("/withdraw.do")
    public String withdraw(@RequestParam("password") String password,
                           HttpSession session,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        UserVO loginUser = getLoginUser(session);

        if (loginUser == null) {
            return "redirect:/user/login.do";
        }

        try {
            // 비밀번호 확인 후. 회원상태를 02 탈퇴로 변경
            userService.withdrawUser(loginUser.getUserNum(), password);

            // 탈퇴 후에는 로그인 상태가 유지되면 안 되므로 세션 제거
            session.invalidate();

            redirectAttributes.addFlashAttribute("msg", "회원탈퇴가 완료되었습니다.");
            return "redirect:/user/login.do";

        } catch (RuntimeException e) {
            // 비밀번호가 틀렸거나 탈퇴 처리에 실패하면 회원정보 수정 화면으로 돌아감
            model.addAttribute("msg", e.getMessage());

            // withdrawError=true이면 JSP에서 숨겨진 탈퇴 비밀번호 입력 영역을 다시 열어둠
            model.addAttribute("withdrawError", true);

            // 화면에 회원정보를 다시 보여주기 위해 최신 회원정보를 조회
            model.addAttribute("user", userService.getUser(loginUser.getUserNum()));

            return "user/user_update";
        }
    }

    /**
     * 세션에서 로그인 회원정보를 꺼내는 공통 메서드
     */
    private UserVO getLoginUser(HttpSession session) {
        // 세션에서 loginUser key의 값을 꺼냄
        Object loginUser = session.getAttribute(SESSION_LOGIN_USER);

        // loginUser가 UserVO 타입이면 로그인된 상태로 판단
        if (loginUser instanceof UserVO) {
            return (UserVO) loginUser;
        }

        // 세션에 값이 없거나 타입이 다르면 로그인하지 않은 상태
        return null;
    }
}
