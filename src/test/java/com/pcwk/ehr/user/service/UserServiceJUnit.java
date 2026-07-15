package com.pcwk.ehr.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.pcwk.ehr.user.domain.UserSearchDTO;
import com.pcwk.ehr.user.domain.UserVO;
import com.pcwk.ehr.user.mapper.UserMapper;
import com.pcwk.ehr.user.util.PasswordUtil;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
        "file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
@Transactional
@Rollback
class UserServiceJUnit {

    final static Logger log = LogManager.getLogger(UserServiceJUnit.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    private String uniqueValue;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        logStart(testInfo);

        uniqueValue = String.valueOf(System.currentTimeMillis());
    }

    @Test
    @DisplayName("Service Bean 주입 확인")
    void beans() {

        log.debug("[STEP 1] UserService 및 UserMapper Bean 주입 확인");

        assertNotNull(userService);
        assertNotNull(userMapper);

        log.debug("userService: " + userService);
        log.debug("userMapper: " + userMapper);

        System.out.println("[확인] Service Bean 주입 확인 테스트 완료");
    }

    @Test
    @DisplayName("회원가입 후 로그인 성공")
    void joinAndLogin() {

        log.debug("[STEP 1] 회원가입 데이터 준비");

        String userId = "service" + uniqueValue;
        UserVO user = createUser(userId, "1234", makePhone("2101"),
                "service" + uniqueValue + "@test.com");

        int flag = userService.join(user);
        assertEquals(1, flag);

        log.debug("[STEP 2] 회원가입 후 비밀번호 해시 저장 결과 확인");

        UserVO savedUser = userMapper.selectByUserId(userId);
        assertNotNull(savedUser);
        assertNotEquals("1234", savedUser.getPassword());
        assertTrue(PasswordUtil.matches("1234", savedUser.getPassword()));

        log.debug("[STEP 3] 로그인 결과 및 반환 회원정보 확인");

        UserVO loginUser = userService.login(userId, "1234");
        assertNotNull(loginUser);
        assertEquals("01", loginUser.getUserRole());
        assertEquals("01", loginUser.getUserStatus());
        assertNull(loginUser.getPassword());

        log.debug("loginUser: " + loginUser);

        System.out.println("[확인] 회원가입 후 로그인 성공 테스트 완료");
    }

    @Test
    @DisplayName("이메일과 닉네임 없이 회원가입")
    void joinWithoutEmailAndNickname() {

        log.debug("[STEP 1] 이메일·닉네임 미입력 회원정보 준비");

        UserVO user = createUser("noEmail" + uniqueValue, "1234", makePhone("2201"), null);
        user.setNickname("   ");

        log.debug("[STEP 2] 회원가입 및 로그인 실행");

        int flag = userService.join(user);
        assertEquals(1, flag);
        UserVO loginUser = userService.login(user.getUserId(), "1234");

        log.debug("[STEP 3] 닉네임 기본값 및 선택 입력값 확인");
        assertEquals(loginUser.getUserName(), loginUser.getNickname());
        assertNull(loginUser.getEmail());
        assertNull(loginUser.getUpdateDt());
        assertNull(loginUser.getWithdrawDt());

        System.out.println("[확인] 이메일과 닉네임 없이 회원가입 테스트 완료");
    }

    @Test
    @DisplayName("정상 회원 아이디 중복 가입 방지")
    void duplicateUserId() {

        log.debug("[STEP 1] 동일 아이디 회원 2명 준비");

        String userId = "dupId" + uniqueValue;
        userService.join(createUser(userId, "1234", makePhone("2301"),
                "dupA" + uniqueValue + "@test.com"));

        UserVO duplicateUser = createUser(userId, "1234", makePhone("2302"),
                "dupB" + uniqueValue + "@test.com");

        log.debug("[STEP 2] 첫 번째 회원가입 후 두 번째 회원가입 시도");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.join(duplicateUser));

        log.debug("[STEP 3] 아이디 중복 예외 메시지 확인");

        assertTrue(exception.getMessage().contains("아이디"));
        log.debug("exception: " + exception.getMessage());

        System.out.println("[확인] 정상 회원 아이디 중복 가입 방지 테스트 완료");
    }

    @Test
    @DisplayName("정상 회원 전화번호와 이메일 중복 가입 방지")
    void duplicatePhoneAndEmail() {

        log.debug("[STEP 1] 중복 전화번호·이메일 사용 회원 준비");

        String phoneNum = makePhone("2401");
        String email = "same" + uniqueValue + "@test.com";

        userService.join(createUser("dupA" + uniqueValue, "1234", phoneNum, email));

        log.debug("[STEP 2] 전화번호 중복 가입 시도 및 예외 확인");

        UserVO samePhoneUser = createUser("dupPhone" + uniqueValue, "1234", phoneNum,
                "other" + uniqueValue + "@test.com");
        IllegalArgumentException phoneException = assertThrows(IllegalArgumentException.class,
                () -> userService.join(samePhoneUser));
        assertTrue(phoneException.getMessage().contains("전화번호"));

        log.debug("[STEP 3] 이메일 중복 가입 시도 및 예외 확인");

        UserVO sameEmailUser = createUser("dupEmail" + uniqueValue, "1234", makePhone("2402"), email);
        IllegalArgumentException emailException = assertThrows(IllegalArgumentException.class,
                () -> userService.join(sameEmailUser));
        assertTrue(emailException.getMessage().contains("이메일"));

        System.out.println("[확인] 정상 회원 전화번호와 이메일 중복 가입 방지 테스트 완료");
    }

    @Test
    @DisplayName("탈퇴 회원 정보로 재가입 가능")
    void rejoinAfterWithdraw() {

        log.debug("[STEP 1] 재가입 대상 회원정보 준비");

        String userId = "rejoin" + uniqueValue;
        String phoneNum = makePhone("2501");
        String email = "rejoin" + uniqueValue + "@test.com";

        log.debug("[STEP 2] 첫 번째 회원가입 후 회원탈퇴 처리");

        userService.join(createUser(userId, "1234", phoneNum, email));
        UserVO firstUser = userService.login(userId, "1234");

        assertEquals(1, userService.withdrawUser(firstUser.getUserNum(), "1234"));

        log.debug("[STEP 3] 동일 정보 재가입 및 로그인 결과 확인");

        UserVO secondUser = createUser(userId, "5678", phoneNum, email);
        assertEquals(1, userService.join(secondUser));
        UserVO loginUser = userService.login(userId, "5678");
        assertNotNull(loginUser);
        assertEquals("01", loginUser.getUserStatus());

        log.debug("loginUser: " + loginUser);

        System.out.println("[확인] 탈퇴 회원 정보로 재가입 가능 테스트 완료");
    }

    @Test
    @DisplayName("잘못된 비밀번호와 탈퇴 회원 로그인 차단")
    void loginFail() {

        log.debug("[STEP 1] 로그인 실패 확인 대상 회원 등록");

        String userId = "loginFail" + uniqueValue;
        userService.join(createUser(userId, "1234", makePhone("2601"),
                "loginFail" + uniqueValue + "@test.com"));

        log.debug("[STEP 2] 잘못된 비밀번호 로그인 예외 확인");

        IllegalArgumentException passwordException = assertThrows(IllegalArgumentException.class,
                () -> userService.login(userId, "9999"));
        assertTrue(passwordException.getMessage().contains("일치하지 않습니다"));

        log.debug("[STEP 3] 회원탈퇴 후 로그인 차단 결과 확인");

        UserVO loginUser = userService.login(userId, "1234");
        userService.withdrawUser(loginUser.getUserNum(), "1234");

        IllegalStateException statusException = assertThrows(IllegalStateException.class,
                () -> userService.login(userId, "1234"));
        assertTrue(statusException.getMessage().contains("탈퇴"));

        System.out.println("[확인] 잘못된 비밀번호와 탈퇴 회원 로그인 차단 테스트 완료");
    }

    @Test
    @DisplayName("휴면 회원과 정지 회원 로그인 차단")
    void dormantAndBlockedLoginFail() {

        log.debug("[STEP 1] 휴면 대상 회원 준비 및 휴면 상태 변경");

        String dormantId = "dormant" + uniqueValue;
        userService.join(createUser(dormantId, "1234", makePhone("2701"),
                "dormant" + uniqueValue + "@test.com"));
        UserVO dormantUser = userService.login(dormantId, "1234");
        userService.changeUserStatus(dormantUser.getUserNum(), "03");

        log.debug("[STEP 2] 휴면 회원 로그인 차단 확인");

        IllegalStateException dormantException = assertThrows(IllegalStateException.class,
                () -> userService.login(dormantId, "1234"));
        assertTrue(dormantException.getMessage().contains("휴면"));

        String blockedId = "blocked" + uniqueValue;
        userService.join(createUser(blockedId, "1234", makePhone("2702"),
                "blocked" + uniqueValue + "@test.com"));
        UserVO blockedUser = userService.login(blockedId, "1234");
        userService.changeUserStatus(blockedUser.getUserNum(), "04");

        log.debug("[STEP 3] 정지 회원 준비 및 로그인 차단 확인");

        IllegalStateException blockedException = assertThrows(IllegalStateException.class,
                () -> userService.login(blockedId, "1234"));
        assertTrue(blockedException.getMessage().contains("정지"));

        System.out.println("[확인] 휴면 회원과 정지 회원 로그인 차단 테스트 완료");
    }

    @Test
    @DisplayName("회원 단건 조회와 회원정보 수정")
    void getUserAndUpdateUser() {

        log.debug("[STEP 1] 조회·수정 대상 회원 등록");

        String userId = "update" + uniqueValue;
        userService.join(createUser(userId, "1234", makePhone("2801"),
                "update" + uniqueValue + "@test.com"));

        UserVO loginUser = userService.login(userId, "1234");
        UserVO user = userService.getUser(loginUser.getUserNum());

        log.debug("[STEP 2] 회원 단건 조회 결과 확인");

        assertNotNull(user);
        assertNull(user.getPassword());

        UserVO updateUser = new UserVO();
        updateUser.setUserNum(user.getUserNum());
        updateUser.setUserName("수정이름");
        updateUser.setNickname("수정닉네임");
        updateUser.setPhoneNum(makePhone("2802"));
        updateUser.setEmail("change" + uniqueValue + "@test.com");

        log.debug("[STEP 3] 회원정보 수정 및 DB 결과 확인");

        assertEquals(1, userService.updateUser(updateUser));

        UserVO outVO = userService.getUser(user.getUserNum());
        assertEquals("수정이름", outVO.getUserName());
        assertEquals("수정닉네임", outVO.getNickname());
        assertEquals(updateUser.getPhoneNum(), outVO.getPhoneNum());
        assertNotNull(outVO.getUpdateDt());

        log.debug("outVO: " + outVO);

        System.out.println("[확인] 회원 단건 조회와 회원정보 수정 테스트 완료");
    }

    @Test
    @DisplayName("비밀번호 변경")
    void updatePassword() {

        log.debug("[STEP 1] 비밀번호 변경 대상 회원 등록");

        String userId = "password" + uniqueValue;
        userService.join(createUser(userId, "1234", makePhone("2901"),
                "password" + uniqueValue + "@test.com"));

        UserVO loginUser = userService.login(userId, "1234");

        log.debug("[STEP 2] 현재 비밀번호 확인 및 새 비밀번호 변경");

        assertEquals(1, userService.updatePassword(loginUser.getUserNum(), "1234", "5678"));

        log.debug("[STEP 3] 기존 비밀번호 실패 및 새 비밀번호 성공 확인");

        assertThrows(IllegalArgumentException.class, () -> userService.login(userId, "1234"));

        UserVO newLoginUser = userService.login(userId, "5678");
        assertNotNull(newLoginUser);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.updatePassword(loginUser.getUserNum(), "9999", "0000"));
        assertTrue(exception.getMessage().contains("현재 비밀번호"));

        System.out.println("[확인] 비밀번호 변경 테스트 완료");
    }

    @Test
    @DisplayName("회원탈퇴")
    void withdrawUser() {

        log.debug("[STEP 1] 탈퇴 대상 회원 등록");

        String userId = "withdraw" + uniqueValue;
        userService.join(createUser(userId, "1234", makePhone("3001"),
                "withdraw" + uniqueValue + "@test.com"));

        UserVO loginUser = userService.login(userId, "1234");

        log.debug("[STEP 2] 잘못된 비밀번호 회원탈퇴 차단 확인");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.withdrawUser(loginUser.getUserNum(), "9999"));
        assertTrue(exception.getMessage().contains("비밀번호"));

        log.debug("[STEP 3] 정상 비밀번호 회원탈퇴 후 회원상태·탈퇴일자 확인");

        assertEquals(1, userService.withdrawUser(loginUser.getUserNum(), "1234"));

        UserVO savedUser = userMapper.selectByUserNum(loginUser.getUserNum());
        assertEquals("02", savedUser.getUserStatus());
        assertNotNull(savedUser.getWithdrawDt());

        System.out.println("[확인] 회원탈퇴 테스트 완료");
    }

    @Test
    @DisplayName("관리자 회원상태와 회원권한 변경")
    void changeStatusAndRole() {

        log.debug("[STEP 1] 상태·권한 변경 대상 회원 등록");

        String userId = "adminChange" + uniqueValue;
        userService.join(createUser(userId, "1234", makePhone("3101"),
                "adminChange" + uniqueValue + "@test.com"));
        UserVO user = userService.login(userId, "1234");

        log.debug("[STEP 2] 관리자 기능 회원상태 변경 및 결과 확인");

        assertEquals(1, userService.changeUserStatus(user.getUserNum(), "04"));
        assertEquals("04", userMapper.selectByUserNum(user.getUserNum()).getUserStatus());

        log.debug("[STEP 3] 회원권한 변경 및 잘못된 상태값·권한값 차단 확인");

        assertEquals(1, userService.changeUserRole(user.getUserNum(), "02"));
        assertEquals("02", userMapper.selectByUserNum(user.getUserNum()).getUserRole());

        assertThrows(IllegalArgumentException.class,
                () -> userService.changeUserStatus(user.getUserNum(), "99"));
        assertThrows(IllegalArgumentException.class,
                () -> userService.changeUserRole(user.getUserNum(), "99"));

        System.out.println("[확인] 관리자 회원상태와 회원권한 변경 테스트 완료");
    }

    @Test
    @DisplayName("관리자 회원목록 조회")
    void getUserList() {

        log.debug("[STEP 1] 관리자 회원목록 검색 조건 준비");

        UserSearchDTO searchDTO = new UserSearchDTO();
        searchDTO.setPageNo(1);
        searchDTO.setPageSize(10);

        log.debug("[STEP 2] 검색 조건 설정 및 목록 조회");

        List<UserVO> list = userService.getUserList(searchDTO);
        int totalCount = userService.getUserTotalCount(searchDTO);

        log.debug("[STEP 3] 전체 건수 및 조회 결과 확인");

        assertNotNull(list);
        assertTrue(totalCount >= list.size());

        log.debug("list.size(): " + list.size());
        log.debug("totalCount: " + totalCount);

        System.out.println("[확인] 관리자 회원목록 조회 테스트 완료");
    }

    private UserVO createUser(String userId, String password, String phoneNum, String email) {
        UserVO user = new UserVO();
        user.setUserId(userId);
        user.setPassword(password);
        user.setUserName("테스트");
        user.setNickname("테스터");
        user.setPhoneNum(phoneNum);
        user.setEmail(email);
        logUser("생성한 테스트 회원", user);
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
