package com.pcwk.ehr.user.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pcwk.ehr.user.domain.UserSearchDTO;
import com.pcwk.ehr.user.domain.UserVO;
import com.pcwk.ehr.user.service.UserService;

/**
 * 관리자 전용 회원관리 Controller
 *
 * 일반 회원 화면:
 * - /user/...
 * - /WEB-INF/views/user/...
 *
 * 관리자 회원관리 화면:
 * - /admin/user/...
 * - /WEB-INF/views/admin/user/...
 */
@Controller
@RequestMapping("/admin/user")
public class AdminUserController {

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
     * 관리자 회원목록 화면
     *
     * 요청 URL:  GET /admin/user/list.do
     * 사용 기능: 회원목록 조회, 검색 조건 처리, 페이징 처리
     */
    @GetMapping("/list.do")
    public String list(UserSearchDTO searchDTO, HttpSession session, Model model) {
        // 02 관리자 권한인지 확인
        if (!isAdmin(session)) {
            return "redirect:/user/login.do";
        }

        // 2026-07-13 [수정] 전체 건수를 먼저 계산해 존재하지 않는 페이지 번호를 보정한다.
        int totalCount = userService.getUserTotalCount(searchDTO);
        int totalPage = totalCount == 0
                ? 1
                : (int) Math.ceil(totalCount / (double) searchDTO.getPageSize());

        if (searchDTO.getPageNo() > totalPage) {
            searchDTO.setPageNo(totalPage);
        }

        // 페이지 번호 보정 후 회원 목록을 조회한다.
        List<UserVO> userList = userService.getUserList(searchDTO);

        // 조회 결과 JSP로 전달
        model.addAttribute("userList", userList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("searchDTO", searchDTO);

        return "admin/user/user_list";
    }

    /**
     * 관리자 회원상세 화면
     *
     * 요청 URL: GET /admin/user/detail.do?userNum=회원번호
     */
    @GetMapping("/detail.do")
    public String detail(@RequestParam("userNum") Long userNum,
                         HttpSession session,
                         Model model) {
        // 관리자 권한이 없으면 로그인 화면으로 보냄
        if (!isAdmin(session)) {
            return "redirect:/user/login.do";
        }

        // 회원 단건 조회
        UserVO user = userService.getUser(userNum);

        model.addAttribute("user", user);

        return "admin/user/user_detail";
    }

    /**
     * 관리자 회원상태 변경
     *
     * 요청 URL: POST /admin/user/statusUpdate.do
     *
     * 변경 가능 상태:
     * - 01 : 정상
     * - 02 : 탈퇴
     * - 03 : 휴면
     * - 04 : 정지
     */
    @PostMapping("/statusUpdate.do")
    public String statusUpdate(@RequestParam("userNum") Long userNum,
                               @RequestParam("userStatus") String userStatus,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        // 관리자 권한이 없으면 변경을 막음
        if (!isAdmin(session)) {
            return "redirect:/user/login.do";
        }

        userService.changeUserStatus(userNum, userStatus);

        redirectAttributes.addFlashAttribute("statusMsg", "회원상태가 변경되었습니다.");
        return "redirect:/admin/user/detail.do?userNum=" + userNum;
    }

    /**
     * 관리자 회원권한 변경
     *
     * 요청 URL: POST /admin/user/roleUpdate.do
     *
     * 변경 가능 권한:
     * - 01 : 일반회원
     * - 02 : 관리자
     */
    @PostMapping("/roleUpdate.do")
    public String roleUpdate(@RequestParam("userNum") Long userNum,
                             @RequestParam("userRole") String userRole,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        if (!isAdmin(session)) {
            return "redirect:/user/login.do";
        }

        userService.changeUserRole(userNum, userRole);

        redirectAttributes.addFlashAttribute("roleMsg", "회원권한이 변경되었습니다.");
        return "redirect:/admin/user/detail.do?userNum=" + userNum;
    }

    /**
     * 현재 세션 사용자가 02 관리자인지 확인하는 내부 메서드
     */
    private boolean isAdmin(HttpSession session) {
        // UserController.login()에서 저장한 loginUser를 꺼냄
        Object loginUser = session.getAttribute(SESSION_LOGIN_USER);

        // 값이 없거나 UserVO 타입이 아니면 로그인하지 않은 상태
        if (!(loginUser instanceof UserVO)) {
            return false;
        }

        // 권한 확인
        UserVO user = (UserVO) loginUser;

        return ROLE_ADMIN.equals(user.getUserRole());
    }
}
