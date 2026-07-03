package com.pcwk.ehr.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.pcwk.ehr.user.domain.UserVO;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
        "file:src/main/webapp/WEB-INF/spring/root-context.xml",
        "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"
})
@Transactional
@Rollback
class UserControllerJUnit {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private String uniqueValue;

    @BeforeEach
    void setUp(TestInfo testInfo) {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        uniqueValue = String.valueOf(System.currentTimeMillis());

        logStart(testInfo.getDisplayName());
        log("테스트 고유값", uniqueValue);
    }

    @Test
    @DisplayName("회원가입 화면 요청")
    void joinViewTest() throws Exception {
        logStep("GIVEN", "회원가입 화면 URL 준비");
        log("요청 URL", "/user/join.do");

        MvcResult result = mockMvc.perform(get("/user/join.do")).andReturn();

        logResult(result);

        assertEquals(200, result.getResponse().getStatus(), "회원가입 화면은 200 OK여야 합니다.");
        assertEquals("user/user_join", result.getModelAndView().getViewName(), "회원가입 JSP View 이름이 맞아야 합니다.");

        logPass("회원가입 화면 요청 성공");
    }

    @Test
    @DisplayName("로그인 화면 요청")
    void loginViewTest() throws Exception {
        logStep("GIVEN", "로그인 화면 URL 준비");
        log("요청 URL", "/user/login.do");

        MvcResult result = mockMvc.perform(get("/user/login.do")).andReturn();

        logResult(result);

        assertEquals(200, result.getResponse().getStatus(), "로그인 화면은 200 OK여야 합니다.");
        assertEquals("user/user_login", result.getModelAndView().getViewName(), "로그인 JSP View 이름이 맞아야 합니다.");

        logPass("로그인 화면 요청 성공");
    }

    @Test
    @DisplayName("회원가입 POST 성공 시 로그인 화면으로 redirect")
    void joinPostSuccessTest() throws Exception {
        String userId = "mvc" + uniqueValue;
        String phoneNum = makePhone("1101");
        String email = "mvc" + uniqueValue + "@test.com";

        logStep("GIVEN", "회원가입 POST 파라미터 준비");
        log("요청 URL", "/user/join.do");
        log("userId", userId);
        log("password", "1234");
        log("passwordConfirm", "1234");
        log("phoneNum", phoneNum);
        log("email", email);

        MvcResult result = mockMvc.perform(post("/user/join.do")
                .param("userId", userId)
                .param("password", "1234")
                .param("passwordConfirm", "1234")
                .param("userName", "테스트")
                .param("nickname", "테스터")
                .param("phoneNum", phoneNum)
                .param("email", email))
                .andReturn();

        logResult(result);

        assertEquals(302, result.getResponse().getStatus(), "회원가입 성공 후에는 redirect 응답이어야 합니다.");
        assertEquals("/user/login.do", result.getResponse().getRedirectedUrl(), "회원가입 성공 후 로그인 화면으로 이동해야 합니다.");

        logPass("회원가입 POST 성공 redirect 확인 완료");
    }

    @Test
    @DisplayName("회원가입 비밀번호 확인 불일치")
    void joinPasswordConfirmMismatchTest() throws Exception {
        String userId = "mismatch" + uniqueValue;

        logStep("GIVEN", "비밀번호와 비밀번호 확인 값이 다른 회원가입 요청 준비");
        log("password", "1234");
        log("passwordConfirm", "9999");

        MvcResult result = mockMvc.perform(post("/user/join.do")
                .param("userId", userId)
                .param("password", "1234")
                .param("passwordConfirm", "9999")
                .param("userName", "테스트")
                .param("nickname", "테스터")
                .param("phoneNum", makePhone("1201"))
                .param("email", "mismatch" + uniqueValue + "@test.com"))
                .andReturn();

        logResult(result);

        assertEquals(200, result.getResponse().getStatus(), "비밀번호 확인 불일치 시 회원가입 화면으로 forward되어야 합니다.");
        assertEquals("user/user_join", result.getModelAndView().getViewName(), "비밀번호 확인 불일치 시 회원가입 화면을 다시 보여줘야 합니다.");

        Object msg = result.getModelAndView().getModel().get("msg");
        assertNotNull(msg, "오류 메시지가 Model에 있어야 합니다.");
        assertTrue(String.valueOf(msg).contains("비밀번호"), "오류 메시지에 비밀번호 관련 내용이 있어야 합니다.");

        logPass("비밀번호 확인 불일치 방어 확인 완료");
    }

    @Test
    @DisplayName("관리자 로그인 성공 후 관리자 회원목록으로 redirect")
    void adminLoginRedirectTest() throws Exception {
        logStep("GIVEN", "관리자 계정으로 로그인 요청 준비");
        log("userId", "admin");
        log("password", "admin1234");

        MvcResult result = mockMvc.perform(post("/user/login.do")
                .param("userId", "admin")
                .param("password", "admin1234"))
                .andReturn();

        logResult(result);

        assertEquals(302, result.getResponse().getStatus(), "관리자 로그인 성공 후 redirect여야 합니다.");
        assertEquals("/admin/user/list.do", result.getResponse().getRedirectedUrl(), "관리자는 관리자 회원목록으로 이동해야 합니다.");

        logPass("관리자 로그인 후 관리자 회원목록 이동 확인 완료");
    }

    @Test
    @DisplayName("관리자 세션 없는 경우 관리자 회원목록 접근 제한")
    void adminListWithoutLoginTest() throws Exception {
        logStep("GIVEN", "로그인 세션 없이 관리자 회원목록 URL 접근");
        log("요청 URL", "/admin/user/list.do");

        MvcResult result = mockMvc.perform(get("/admin/user/list.do")).andReturn();

        logResult(result);

        assertEquals(302, result.getResponse().getStatus(), "비로그인 관리 페이지 접근은 redirect여야 합니다.");
        assertEquals("/user/login.do", result.getResponse().getRedirectedUrl(), "비로그인 사용자는 로그인 화면으로 이동해야 합니다.");

        logPass("관리자 세션 없는 경우 접근 제한 확인 완료");
    }

    @Test
    @DisplayName("관리자 세션 있는 경우 관리자 회원목록 화면 요청")
    void adminListWithAdminSessionTest() throws Exception {
        UserVO adminUser = new UserVO();
        adminUser.setUserNum(1L);
        adminUser.setUserId("admin");
        adminUser.setUserName("관리자");
        adminUser.setNickname("관리자");
        adminUser.setUserRole("02");
        adminUser.setUserStatus("01");

        logStep("GIVEN", "02 관리자 권한 loginUser 세션 준비");
        logUser("세션에 담을 관리자", adminUser);

        MvcResult result = mockMvc.perform(get("/admin/user/list.do")
                .sessionAttr("loginUser", adminUser))
                .andReturn();

        logResult(result);

        assertEquals(200, result.getResponse().getStatus(), "관리자 세션이 있으면 회원목록 화면을 볼 수 있어야 합니다.");
        assertEquals("admin/user/user_list", result.getModelAndView().getViewName(), "관리자 회원목록 JSP View 이름이 맞아야 합니다.");

        logPass("관리자 세션 있는 경우 회원목록 화면 요청 성공");
    }

    private String makePhone(String middle) {
        String last4 = uniqueValue.substring(uniqueValue.length() - 4);
        return "010-" + middle + "-" + last4;
    }

    private void logResult(MvcResult result) throws Exception {
        MockHttpServletResponse response = result.getResponse();
        ModelAndView modelAndView = result.getModelAndView();

        log("HTTP Status", response.getStatus());
        log("Redirect URL", response.getRedirectedUrl());
        log("Forwarded URL", response.getForwardedUrl());

        if (modelAndView != null) {
            log("View Name", modelAndView.getViewName());
            log("Model Keys", modelAndView.getModel().keySet());
        } else {
            log("View Name", null);
            log("Model Keys", null);
        }

        log("Response Encoding", response.getCharacterEncoding());
    }

    private void logUser(String title, UserVO user) {
        System.out.println("  - " + title + " :");
        System.out.println("    userNum    = " + user.getUserNum());
        System.out.println("    userId     = " + user.getUserId());
        System.out.println("    userName   = " + user.getUserName());
        System.out.println("    nickname   = " + user.getNickname());
        System.out.println("    phoneNum   = " + user.getPhoneNum());
        System.out.println("    email      = " + user.getEmail());
        System.out.println("    userRole   = " + user.getUserRole());
        System.out.println("    userStatus = " + user.getUserStatus());
    }

    private void logStart(String testName) {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("[TEST START] " + testName);
        System.out.println("============================================================");
    }

    private void logStep(String step, String message) {
        System.out.println("[" + step + "] " + message);
    }

    private void log(String label, Object value) {
        System.out.println("  - " + label + " : " + value);
    }

    private void logPass(String message) {
        System.out.println("[PASS] " + message);
        System.out.println("------------------------------------------------------------");
    }
}
