package com.pcwk.ehr.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.pcwk.ehr.user.domain.UserVO;
import com.pcwk.ehr.user.mapper.UserMapper;
import com.pcwk.ehr.user.service.UserService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AdminUserControllerJUnit.TestConfig.class)
@Transactional
@Rollback
class AdminUserControllerJUnit {

    final static Logger log = LogManager.getLogger(AdminUserControllerJUnit.class);

    @Configuration
    @ImportResource("file:src/main/webapp/WEB-INF/spring/root-context.xml")
    static class TestConfig {

        @Bean
        AdminUserController adminUserController() {
            return new AdminUserController();
        }
    }

    @Autowired
    private AdminUserController adminUserController;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    private MockMvc mockMvc;
    private String uniqueValue;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        logStart(testInfo);

        mockMvc = MockMvcBuilders.standaloneSetup(adminUserController).build();
        uniqueValue = String.valueOf(System.currentTimeMillis());
    }

    @Test
    @DisplayName("Controller Bean과 MockMvc 생성 확인")
    void beans() {

        log.debug("[STEP 1] Controller 관련 Bean 및 MockMvc 생성 확인");

        assertNotNull(adminUserController);
        assertNotNull(mockMvc);
        assertNotNull(userService);
        assertNotNull(userMapper);

        System.out.println("[확인] Controller Bean과 MockMvc 생성 확인 테스트 완료");
    }

    @Test
    @DisplayName("관리자 페이지 접근 권한 확인")
    void adminAccess() throws Exception {

        // 1. 비로그인 상태로 관리자 목록 요청
        // 2. 일반 회원으로 관리자 목록 요청
        // 3. 관리자로 관리자 목록 요청

        log.debug("[STEP 1] 비로그인 상태 관리자 목록 접근");

        MvcResult noLoginResult = mockMvc.perform(get("/admin/user/list.do")).andReturn();
        assertEquals("/main.do?modal=login", noLoginResult.getResponse().getRedirectedUrl());

        log.debug("[STEP 2] 일반 회원 관리자 목록 접근");

        UserVO normalUser = createSessionUser(1L, "normal", "01");
        MvcResult normalResult = mockMvc.perform(get("/admin/user/list.do")
                .sessionAttr("loginUser", normalUser))
                .andReturn();
		assertEquals("/main.do?modal=forbidden", normalResult.getResponse().getRedirectedUrl());
        log.debug("[STEP 3] 관리자 권한 관리자 목록 접근 및 결과 확인");

        UserVO adminUser = createSessionUser(1L, "admin", "02");
        MvcResult adminResult = mockMvc.perform(get("/admin/user/list.do")
                .sessionAttr("loginUser", adminUser))
                .andReturn();

        assertEquals(200, adminResult.getResponse().getStatus());
        assertEquals("admin/user/user_list", adminResult.getModelAndView().getViewName());
        assertNotNull(adminResult.getModelAndView().getModel().get("userList"));

        System.out.println("[확인] 관리자 페이지 접근 권한 확인 테스트 완료");
    }

    @Test
    @DisplayName("관리자 회원상세 조회")
    void userDetail() throws Exception {


        // 1. 테스트 회원 등록
        // 2. 관리자 세션 생성
        // 3. 회원상세 요청
        // 4. 화면과 회원정보 확인

        log.debug("[STEP 1] 조회 대상 테스트 회원 등록");

        UserVO targetUser = joinAndLogin("target" + uniqueValue, "1234", "1801");
        UserVO adminUser = createSessionUser(999L, "admin", "02");

        log.debug("[STEP 2] 관리자 세션 회원상세 화면 요청");

        MvcResult result = mockMvc.perform(get("/admin/user/detail.do")
                .sessionAttr("loginUser", adminUser)
                .param("userNum", String.valueOf(targetUser.getUserNum())))
                .andReturn();

        log.debug("[STEP 3] 화면 이름 및 회원정보 확인");

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("admin/user/user_detail", result.getModelAndView().getViewName());
        assertNotNull(result.getModelAndView().getModel().get("user"));

        System.out.println("[확인] 관리자 회원상세 조회 테스트 완료");
    }

    @Test
    @DisplayName("관리자 회원상태 변경")
    void updateStatus() throws Exception {


        // 1. 테스트 회원 등록
        // 2. 회원상태를 정지로 변경
        // 3. DB에서 변경 결과 확인

        log.debug("[STEP 1] 상태 변경 대상 테스트 회원 등록");

        UserVO targetUser = joinAndLogin("status" + uniqueValue, "1234", "1802");
        UserVO adminUser = createSessionUser(999L, "admin", "02");

        log.debug("[STEP 2] 관리자 권한 회원상태 정지 변경");

        MvcResult result = mockMvc.perform(post("/admin/user/statusUpdate.do")
                .sessionAttr("loginUser", adminUser)
                .param("userNum", String.valueOf(targetUser.getUserNum()))
                .param("userStatus", "04"))
                .andReturn();

        log.debug("[STEP 3] DB 회원상태 변경 결과 확인");

        assertEquals(302, result.getResponse().getStatus());
        assertEquals("/admin/user/detail.do?userNum=" + targetUser.getUserNum(),
                result.getResponse().getRedirectedUrl());
        assertEquals("04", userMapper.selectByUserNum(targetUser.getUserNum()).getUserStatus());

        System.out.println("[확인] 관리자 회원상태 변경 테스트 완료");
    }

    @Test
    @DisplayName("관리자 회원권한 변경")
    void updateRole() throws Exception {


        // 1. 테스트 회원 등록
        // 2. 일반 회원을 관리자로 변경
        // 3. DB에서 변경 결과 확인

        log.debug("[STEP 1] 권한 변경 대상 테스트 회원 등록");

        UserVO targetUser = joinAndLogin("role" + uniqueValue, "1234", "1803");
        UserVO adminUser = createSessionUser(999L, "admin", "02");

        log.debug("[STEP 2] 일반 회원 관리자 권한 변경");

        MvcResult result = mockMvc.perform(post("/admin/user/roleUpdate.do")
                .sessionAttr("loginUser", adminUser)
                .param("userNum", String.valueOf(targetUser.getUserNum()))
                .param("userRole", "02"))
                .andReturn();

        log.debug("[STEP 3] DB 회원권한 변경 결과 확인");

        assertEquals(302, result.getResponse().getStatus());
        assertEquals("02", userMapper.selectByUserNum(targetUser.getUserNum()).getUserRole());

        System.out.println("[확인] 관리자 회원권한 변경 테스트 완료");
    }

    private UserVO joinAndLogin(String userId, String password, String phoneMiddle) {
        UserVO user = new UserVO();
        user.setUserId(userId);
        user.setPassword(password);
        user.setUserName("테스트");
        user.setNickname("테스터");
        user.setPhoneNum(makePhone(phoneMiddle));
        user.setEmail(userId + "@test.com");

        userService.join(user);
        UserVO loginUser = userService.login(userId, password);
        logUser("로그인한 테스트 회원", loginUser);
        return loginUser;
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
