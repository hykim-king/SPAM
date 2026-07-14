package com.pcwk.ehr.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.pcwk.ehr.product.domain.ProductVO;
import com.pcwk.ehr.user.domain.UserVO;
import com.pcwk.ehr.user.mapper.UserMapper;
import com.pcwk.ehr.user.service.UserService;
import com.pcwk.ehr.user.util.PasswordUtil;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserControllerJUnit.TestConfig.class)
@Transactional
@Rollback
class UserControllerJUnit {

    final static Logger log = LogManager.getLogger(UserControllerJUnit.class);

    @Configuration
    @ImportResource("file:src/main/webapp/WEB-INF/spring/root-context.xml")
    static class TestConfig {

        @Bean
        UserController userController() {
            return new UserController();
        }
    }

    @Autowired
    private UserController userController;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    private MockMvc mockMvc;
    private String uniqueValue;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        logStart(testInfo);

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        uniqueValue = String.valueOf(System.currentTimeMillis());
    }

    @Test
    @DisplayName("Controller Bean과 MockMvc 생성 확인")
    void beans() {

        log.debug("[STEP 1] Controller 관련 Bean 및 MockMvc 생성 확인");

        assertNotNull(userController);
        assertNotNull(mockMvc);
        assertNotNull(userService);
        assertNotNull(userMapper);

        System.out.println("[확인] Controller Bean과 MockMvc 생성 확인 테스트 완료");
    }

    @Test
    @DisplayName("회원가입 화면 요청")
    void joinView() throws Exception {

        log.debug("[STEP 1] 회원가입 화면 요청");

        MvcResult result = mockMvc.perform(get("/user/join.do")).andReturn();

        log.debug("[STEP 2] 응답 상태 및 화면 이름 확인");

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("user/user_join", result.getModelAndView().getViewName());
        assertNotNull(result.getModelAndView().getModel().get("user"));

        System.out.println("[확인] 회원가입 화면 요청 테스트 완료");
    }

    @Test
    @DisplayName("회원가입 성공과 비밀번호 확인 불일치")
    void join() throws Exception {

        log.debug("[STEP 1] 회원가입 입력값 준비");

        String userId = "mvcJoin" + uniqueValue;

        log.debug("[STEP 2] 비밀번호 확인 일치 회원가입 요청");

        MvcResult successResult = mockMvc.perform(post("/user/join.do")
                .param("userId", userId)
                .param("password", "1234")
                .param("passwordConfirm", "1234")
                .param("userName", "테스트")
                .param("nickname", "테스터")
                .param("phoneNum", makePhone("1101"))
                .param("email", "mvcJoin" + uniqueValue + "@test.com"))
                .andReturn();

        log.debug("[STEP 3] 회원가입 성공 결과 확인");

        assertEquals(302, successResult.getResponse().getStatus());
        assertEquals("/user/login.do", successResult.getResponse().getRedirectedUrl());
        assertNotNull(userMapper.selectByUserId(userId));

        log.debug("[STEP 4] 비밀번호 확인 불일치 회원가입 실패 결과 확인");

        MvcResult failResult = mockMvc.perform(post("/user/join.do")
                .param("userId", "mismatch" + uniqueValue)
                .param("password", "1234")
                .param("passwordConfirm", "9999")
                .param("userName", "테스트")
                .param("nickname", "테스터")
                .param("phoneNum", makePhone("1102"))
                .param("email", "mismatch" + uniqueValue + "@test.com"))
                .andReturn();

        assertEquals(200, failResult.getResponse().getStatus());
        assertEquals("user/user_join", failResult.getModelAndView().getViewName());
        assertTrue(String.valueOf(failResult.getModelAndView().getModel().get("msg")).contains("비밀번호"));

        System.out.println("[확인] 회원가입 성공과 비밀번호 확인 불일치 테스트 완료");
    }

    @Test
    @DisplayName("로그인 화면 요청")
    void loginView() throws Exception {

        log.debug("[STEP 1] 로그인 화면 요청");

        MvcResult result = mockMvc.perform(get("/user/login.do")).andReturn();

        log.debug("[STEP 2] 응답 상태 및 화면 이름 확인");

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("user/user_login", result.getModelAndView().getViewName());

        System.out.println("[확인] 로그인 화면 요청 테스트 완료");
    }

    @Test
    @DisplayName("일반 회원 로그인 성공과 실패")
    void userLogin() throws Exception {

        log.debug("[STEP 1] 로그인 대상 일반 회원 등록");

        String userId = "mvcLogin" + uniqueValue;
        userService.join(createJoinUser(userId, "1234", makePhone("1201"),
                "mvcLogin" + uniqueValue + "@test.com"));

        log.debug("[STEP 2] 정상 비밀번호 로그인 요청");

        MvcResult successResult = mockMvc.perform(post("/user/login.do")
                .param("userId", userId)
                .param("password", "1234"))
                .andReturn();

        log.debug("[STEP 3] 세션 및 이동 경로 확인");

        assertEquals(302, successResult.getResponse().getStatus());
        assertEquals("/user/mypage.do", successResult.getResponse().getRedirectedUrl());

        UserVO loginUser = (UserVO) successResult.getRequest().getSession().getAttribute("loginUser");
        assertNotNull(loginUser);
        assertEquals(userId, loginUser.getUserId());
        assertNull(loginUser.getPassword());

        log.debug("[STEP 4] 잘못된 비밀번호 로그인 실패 결과 확인");

        MvcResult failResult = mockMvc.perform(post("/user/login.do")
                .param("userId", userId)
                .param("password", "9999"))
                .andReturn();

        assertEquals(200, failResult.getResponse().getStatus());
        assertEquals("user/user_login", failResult.getModelAndView().getViewName());
        assertNotNull(failResult.getModelAndView().getModel().get("msg"));

        System.out.println("[확인] 일반 회원 로그인 성공과 실패 테스트 완료");
    }

    @Test
    @DisplayName("관리자 로그인 후 관리자 회원목록으로 이동")
    void adminLogin() throws Exception {

        log.debug("[STEP 1] 테스트용 관리자 계정 등록");

        String adminId = "admin" + uniqueValue;
        UserVO admin = createMapperUser(adminId, "admin1234", makePhone("1301"),
                "admin" + uniqueValue + "@test.com", "02");
        userMapper.insertUser(admin);

        log.debug("[STEP 2] 관리자 계정 로그인 요청");

        MvcResult result = mockMvc.perform(post("/user/login.do")
                .param("userId", adminId)
                .param("password", "admin1234"))
                .andReturn();

        log.debug("[STEP 3] 관리자 회원목록 이동 확인");

        assertEquals(302, result.getResponse().getStatus());
        assertEquals("/admin/user/list.do", result.getResponse().getRedirectedUrl());

        UserVO loginUser = (UserVO) result.getRequest().getSession().getAttribute("loginUser");
        assertNotNull(loginUser);
        assertEquals("02", loginUser.getUserRole());

        System.out.println("[확인] 관리자 로그인 후 관리자 회원목록으로 이동 테스트 완료");
    }

    @Test
    @DisplayName("로그아웃")
    void logout() throws Exception {

        log.debug("[STEP 1] 로그인 회원 및 세션 준비");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loginUser", createSessionUser(1L, "logoutUser", "01"));

        log.debug("[STEP 2] 로그아웃 요청");

        MvcResult result = mockMvc.perform(get("/user/logout.do").session(session)).andReturn();

        log.debug("[STEP 3] 세션 제거 및 로그인 화면 이동 확인");

        assertEquals(302, result.getResponse().getStatus());
        assertEquals("/main.do", result.getResponse().getRedirectedUrl());
        assertTrue(session.isInvalid());

        System.out.println("[확인] 로그아웃 테스트 완료");
    }

    @Test
    @DisplayName("비로그인 회원 화면 접근 제한")
    void userPageWithoutLogin() throws Exception {

        log.debug("[STEP 1] 비로그인 상태 회원 전용 화면 접근");

        MvcResult mypageResult = mockMvc.perform(get("/user/mypage.do")).andReturn();
        assertEquals("/main.do?modal=login", mypageResult.getResponse().getRedirectedUrl());

        log.debug("[STEP 2] 회원정보 수정·비밀번호 변경·회원탈퇴 요청 로그인 모달 이동 확인");

        MvcResult updateResult = mockMvc.perform(get("/user/update.do")).andReturn();
        assertEquals("/main.do?modal=login", updateResult.getResponse().getRedirectedUrl());

        MvcResult passwordResult = mockMvc.perform(post("/user/password.do")
                .param("currentPassword", "1234")
                .param("newPassword", "5678"))
                .andReturn();
        assertEquals("/main.do?modal=login", passwordResult.getResponse().getRedirectedUrl());

        MvcResult withdrawResult = mockMvc.perform(post("/user/withdraw.do")
                .param("password", "1234"))
                .andReturn();
        assertEquals("/main.do?modal=login", withdrawResult.getResponse().getRedirectedUrl());

        System.out.println("[확인] 비로그인 회원 화면 접근 제한 테스트 완료");
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("로그인 회원 마이페이지 조회")
    void mypage() throws Exception {

        log.debug("[STEP 1] 로그인 회원 준비");

        UserVO loginUser = joinAndLogin("mypage" + uniqueValue, "1234", "1401");

        log.debug("[STEP 2] 마이페이지 요청");

        MvcResult result = mockMvc.perform(get("/user/mypage.do")
                .sessionAttr("loginUser", loginUser))
                .andReturn();

        log.debug("[STEP 3] Model 회원정보 및 상품목록 저장 확인");

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("user/user_mypage", result.getModelAndView().getViewName());
        assertNotNull(result.getModelAndView().getModel().get("user"));

        List<ProductVO> list = (List<ProductVO>) result.getModelAndView().getModel().get("list");
        assertNotNull(list);

        System.out.println("[확인] 로그인 회원 마이페이지 조회 테스트 완료");
    }

    @Test
    @DisplayName("회원정보 수정")
    void updateUser() throws Exception {

        log.debug("[STEP 1] 수정 대상 회원정보 준비");

        UserVO loginUser = joinAndLogin("mvcUpdate" + uniqueValue, "1234", "1501");

        MvcResult viewResult = mockMvc.perform(get("/user/update.do")
                .sessionAttr("loginUser", loginUser))
                .andReturn();
        assertEquals("user/user_update", viewResult.getModelAndView().getViewName());

        log.debug("[STEP 2] 회원정보 수정 요청");

        MvcResult updateResult = mockMvc.perform(post("/user/update.do")
                .sessionAttr("loginUser", loginUser)
                .param("userName", "수정이름")
                .param("nickname", "수정닉네임")
                .param("phoneNum", makePhone("1502"))
                .param("email", "mvcChange" + uniqueValue + "@test.com"))
                .andReturn();

        assertEquals(302, updateResult.getResponse().getStatus());
        assertEquals("/user/mypage.do", updateResult.getResponse().getRedirectedUrl());

        log.debug("[STEP 3] DB 회원정보 수정 결과 확인");

        UserVO outVO = userMapper.selectByUserNum(loginUser.getUserNum());
        assertEquals("수정이름", outVO.getUserName());
        assertEquals("수정닉네임", outVO.getNickname());

        System.out.println("[확인] 회원정보 수정 테스트 완료");
    }

    @Test
    @DisplayName("비밀번호 변경")
    void updatePassword() throws Exception {

        log.debug("[STEP 1] 비밀번호 변경 대상 회원 준비");

        String userId = "mvcPassword" + uniqueValue;
        UserVO loginUser = joinAndLogin(userId, "1234", "1601");

        log.debug("[STEP 2] 현재 비밀번호 및 새 비밀번호 전송");

        MvcResult result = mockMvc.perform(post("/user/password.do")
                .sessionAttr("loginUser", loginUser)
                .param("currentPassword", "1234")
                .param("newPassword", "5678"))
                .andReturn();

        log.debug("[STEP 3] 기존 비밀번호 실패 및 새 비밀번호 성공 확인");

        assertEquals(302, result.getResponse().getStatus());
        assertEquals("/user/update.do", result.getResponse().getRedirectedUrl());
        assertNotNull(userService.login(userId, "5678"));

        System.out.println("[확인] 비밀번호 변경 테스트 완료");
    }

    @Test
    @DisplayName("회원탈퇴")
    void withdrawUser() throws Exception {

        log.debug("[STEP 1] 탈퇴 대상 회원 및 세션 준비");

        UserVO loginUser = joinAndLogin("mvcWithdraw" + uniqueValue, "1234", "1701");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loginUser", loginUser);

        log.debug("[STEP 2] 회원탈퇴 요청");

        MvcResult result = mockMvc.perform(post("/user/withdraw.do")
                .session(session)
                .param("password", "1234"))
                .andReturn();

        log.debug("[STEP 3] 회원상태 및 세션 처리 결과 확인");

        assertEquals(302, result.getResponse().getStatus());
        assertEquals("/main.do", result.getResponse().getRedirectedUrl());
        assertTrue(session.isInvalid());

        UserVO outVO = userMapper.selectByUserNum(loginUser.getUserNum());
        assertEquals("02", outVO.getUserStatus());
        assertNotNull(outVO.getWithdrawDt());

        System.out.println("[확인] 회원탈퇴 테스트 완료");
    }

    private UserVO joinAndLogin(String userId, String password, String phoneMiddle) {
        UserVO user = createJoinUser(userId, password, makePhone(phoneMiddle),
                userId + "@test.com");
        userService.join(user);
        UserVO loginUser = userService.login(userId, password);
        logUser("로그인한 테스트 회원", loginUser);
        return loginUser;
    }

    private UserVO createJoinUser(String userId, String password, String phoneNum, String email) {
        UserVO user = new UserVO();
        user.setUserId(userId);
        user.setPassword(password);
        user.setUserName("테스트");
        user.setNickname("테스터");
        user.setPhoneNum(phoneNum);
        user.setEmail(email);
        logUser("회원가입 요청 회원", user);
        return user;
    }

    private UserVO createMapperUser(String userId, String password, String phoneNum, String email, String role) {
        UserVO user = createJoinUser(userId, PasswordUtil.hash(password), phoneNum, email);
        user.setUserRole(role);
        user.setUserStatus("01");
        logUser("Mapper 등록 회원", user);
        return user;
    }

    private UserVO createSessionUser(Long userNum, String userId, String role) {
        UserVO user = new UserVO();
        user.setUserNum(userNum);
        user.setUserId(userId);
        user.setUserName("테스트");
        user.setNickname("테스터");
        user.setUserRole(role);
        user.setUserStatus("01");
        logUser("세션 회원", user);
        return user;
    }


    private void logStart(TestInfo testInfo) {
        String testName = testInfo.getTestMethod().isPresent()
                ? testInfo.getTestMethod().get().getName()
                : "테스트";

        System.out.println();
        System.out.println("========================================");
        System.out.println("[ TEST " + testName + " ] " + testInfo.getDisplayName());
        System.out.println("========================================");
    }

    private void logUser(String title, UserVO user) {
        System.out.println("[ USER ] " + title);

        if (user == null) {
            System.out.println("  - 회원정보 없음");
            return;
        }

        System.out.println("  - userNum    : " + user.getUserNum());
        System.out.println("  - userId     : " + user.getUserId());
        System.out.println("  - userName   : " + user.getUserName());
        System.out.println("  - nickname   : " + user.getNickname());
        System.out.println("  - phoneNum   : " + user.getPhoneNum());
        System.out.println("  - email      : " + user.getEmail());
        System.out.println("  - birthDt    : " + user.getBirthDt());
        System.out.println("  - userRole   : " + user.getUserRole());
        System.out.println("  - userStatus : " + user.getUserStatus());
        System.out.println("  - createDt   : " + user.getCreateDt());
        System.out.println("  - updateDt   : " + user.getUpdateDt());
        System.out.println("  - withdrawDt : " + user.getWithdrawDt());
    }

    private String makePhone(String middle) {
        String last4 = uniqueValue.substring(uniqueValue.length() - 4);
        return "010-" + middle + "-" + last4;
    }
}
