package com.pcwk.ehr.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
        "file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
@Transactional
@Rollback
class UserServiceJUnit {

    @Autowired
    private UserService userService;

    private String uniqueValue;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        uniqueValue = String.valueOf(System.currentTimeMillis());
        logStart(testInfo.getDisplayName());
        log("테스트 고유값", uniqueValue);
    }

    @Test
    @DisplayName("Service Bean 주입 확인")
    void serviceBeanInjectionTest() {
        logStep("GIVEN", "root-context.xml에서 UserServiceImpl이 Spring Bean으로 등록되어 있어야 함");

        assertNotNull(userService, "userService가 null이면 component-scan 설정을 확인해야 합니다.");

        logPass("UserService Bean 주입 성공");
    }

    @Test
    @DisplayName("회원가입 후 로그인 성공")
    void joinAndLoginTest() {
        String userId = "svc" + uniqueValue;
        String rawPassword = "1234";
        UserVO joinUser = createJoinUser(userId, rawPassword, makePhone("2101"), "svc" + uniqueValue + "@test.com");

        logStep("GIVEN", "회원가입 입력값 준비");
        logUser("회원가입 입력값", joinUser);

        int joinCount = userService.join(joinUser);

        logStep("WHEN", "userService.join(joinUser) 실행");
        log("회원가입 처리 건수", joinCount);

        assertEquals(1, joinCount, "회원가입 INSERT는 1건이어야 합니다.");

        UserVO loginUser = userService.login(userId, rawPassword);

        logStep("WHEN", "userService.login(userId, rawPassword) 실행");
        logUser("로그인 결과", loginUser);

        assertNotNull(loginUser, "로그인 성공 시 UserVO가 반환되어야 합니다.");
        assertNotNull(loginUser.getUserNum(), "로그인 성공 회원은 USER_NUM이 있어야 합니다.");
        assertEquals(userId, loginUser.getUserId());
        assertEquals("01", loginUser.getUserRole());
        assertEquals("01", loginUser.getUserStatus());
        assertNull(loginUser.getPassword(), "로그인 후 세션용 객체에는 password가 null이어야 합니다.");

        logPass("회원가입 후 로그인 성공 확인 완료");
    }

    @Test
    @DisplayName("닉네임 미입력 시 이름 자동 저장 및 가입 직후 변경일자 NULL")
    void joinWithoutNicknameUsesUserNameTest() {
        UserVO joinUser = createJoinUser(
                "noNick" + uniqueValue,
                "1234",
                makePhone("2151"),
                "noNick" + uniqueValue + "@test.com"
        );

        // 2026-07-13 [추가] 빈 닉네임은 Service에서 USER_NAME으로 대체되어야 함
        joinUser.setNickname("   ");

        logStep("GIVEN", "닉네임 없이 회원가입 입력값 준비");
        int joinCount = userService.join(joinUser);
        assertEquals(1, joinCount, "회원가입 INSERT는 1건이어야 합니다.");

        UserVO loginUser = userService.login(joinUser.getUserId(), "1234");

        assertEquals(loginUser.getUserName(), loginUser.getNickname(),
                "닉네임 미입력 시 회원 이름과 동일한 값으로 저장되어야 합니다.");
        assertNull(loginUser.getUpdateDt(),
                "신규 가입 직후 UPDATE_DT는 NULL이어야 합니다.");
        assertNull(loginUser.getWithdrawDt(),
                "정상 회원의 WITHDRAW_DT는 NULL이어야 합니다.");

        logPass("닉네임 기본값 및 가입 직후 일자 정책 확인 완료");
    }

    @Test
    @DisplayName("01 정상 회원 아이디 중복 가입 방지")
    void duplicateUserIdJoinFailTest() {
        String sameUserId = "dupId" + uniqueValue;
        UserVO firstUser = createJoinUser(sameUserId, "1234", makePhone("2201"), "first" + uniqueValue + "@test.com");
        UserVO secondUser = createJoinUser(sameUserId, "1234", makePhone("2202"), "second" + uniqueValue + "@test.com");

        logStep("GIVEN", "첫 번째 회원가입 후 같은 USER_ID로 두 번째 가입 시도");
        userService.join(firstUser);
        logPass("첫 번째 회원가입 성공");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.join(secondUser)
        );

        log("발생한 예외 메시지", exception.getMessage());
        assertTrue(exception.getMessage().contains("아이디"));

        logPass("01 정상 회원 아이디 중복 방지 확인 완료");
    }

    @Test
    @DisplayName("01 정상 회원 전화번호 중복 가입 방지")
    void duplicatePhoneNumJoinFailTest() {
        String samePhone = makePhone("2301");
        UserVO firstUser = createJoinUser("phoneA" + uniqueValue, "1234", samePhone, "phoneA" + uniqueValue + "@test.com");
        UserVO secondUser = createJoinUser("phoneB" + uniqueValue, "1234", samePhone, "phoneB" + uniqueValue + "@test.com");

        logStep("GIVEN", "두 회원이 같은 전화번호를 사용하도록 준비");
        userService.join(firstUser);
        logPass("첫 번째 회원가입 성공");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.join(secondUser)
        );

        log("발생한 예외 메시지", exception.getMessage());
        assertTrue(exception.getMessage().contains("전화번호"));

        logPass("01 정상 회원 전화번호 중복 방지 확인 완료");
    }

    @Test
    @DisplayName("01 정상 회원 이메일 중복 가입 방지")
    void duplicateEmailJoinFailTest() {
        String sameEmail = "same" + uniqueValue + "@test.com";
        UserVO firstUser = createJoinUser("emailA" + uniqueValue, "1234", makePhone("2401"), sameEmail);
        UserVO secondUser = createJoinUser("emailB" + uniqueValue, "1234", makePhone("2402"), sameEmail);

        logStep("GIVEN", "두 회원이 같은 이메일을 사용하도록 준비");
        userService.join(firstUser);
        logPass("첫 번째 회원가입 성공");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.join(secondUser)
        );

        log("발생한 예외 메시지", exception.getMessage());
        assertTrue(exception.getMessage().contains("이메일"));

        logPass("01 정상 회원 이메일 중복 방지 확인 완료");
    }

    @Test
    @DisplayName("탈퇴 회원 아이디/전화번호/이메일 재사용 가입 가능")
    void withdrawnUserRejoinSameIdentityTest() {
        String sameUserId = "rejoin" + uniqueValue;
        String samePhone = makePhone("2501");
        String sameEmail = "rejoin" + uniqueValue + "@test.com";
        String rawPassword = "1234";

        UserVO firstUser = createJoinUser(sameUserId, rawPassword, samePhone, sameEmail);

        logStep("GIVEN", "첫 번째 회원가입 후 탈퇴 처리");
        userService.join(firstUser);

        UserVO firstLoginUser = userService.login(sameUserId, rawPassword);
        logUser("첫 번째 로그인 회원", firstLoginUser);

        int withdrawCount = userService.withdrawUser(firstLoginUser.getUserNum(), rawPassword);
        log("탈퇴 처리 건수", withdrawCount);
        assertEquals(1, withdrawCount, "탈퇴 처리는 1건이어야 합니다.");

        logStep("WHEN", "탈퇴한 계정과 같은 아이디/전화번호/이메일로 재가입 시도");
        UserVO secondUser = createJoinUser(sameUserId, "5678", samePhone, sameEmail);
        int rejoinCount = userService.join(secondUser);
        log("재가입 처리 건수", rejoinCount);

        assertEquals(1, rejoinCount, "탈퇴 회원의 아이디/전화번호/이메일은 재가입에 사용할 수 있어야 합니다.");

        UserVO secondLoginUser = userService.login(sameUserId, "5678");
        logUser("재가입 후 로그인 회원", secondLoginUser);

        assertNotNull(secondLoginUser);
        assertEquals("01", secondLoginUser.getUserStatus());

        logPass("탈퇴 회원 정보 재사용 가입 정책 확인 완료");
    }

    @Test
    @DisplayName("탈퇴한 기존 계정으로 로그인 불가")
    void withdrawnOldAccountLoginFailTest() {
        String userId = "wdLogin" + uniqueValue;
        String password = "1234";
        UserVO joinUser = createJoinUser(userId, password, makePhone("2601"), "wdLogin" + uniqueValue + "@test.com");

        logStep("GIVEN", "회원가입 후 탈퇴 처리");
        userService.join(joinUser);
        UserVO loginUser = userService.login(userId, password);
        userService.withdrawUser(loginUser.getUserNum(), password);

        logStep("WHEN", "탈퇴한 기존 계정으로 로그인 시도");
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> userService.login(userId, password)
        );

        log("발생한 예외 메시지", exception.getMessage());
        assertTrue(exception.getMessage().contains("탈퇴"));

        logPass("탈퇴 계정 로그인 차단 확인 완료");
    }

    @Test
    @DisplayName("회원정보 수정 시 01 정상 회원 전화번호 중복 방지")
    void updateDuplicatePhoneNumFailTest() {
        UserVO userA = createJoinUser("updPhoneA" + uniqueValue, "1234", makePhone("2701"), "updPhoneA" + uniqueValue + "@test.com");
        UserVO userB = createJoinUser("updPhoneB" + uniqueValue, "1234", makePhone("2702"), "updPhoneB" + uniqueValue + "@test.com");

        userService.join(userA);
        userService.join(userB);

        UserVO loginA = userService.login(userA.getUserId(), "1234");
        UserVO loginB = userService.login(userB.getUserId(), "1234");

        UserVO updateB = createUpdateUser(loginB.getUserNum(), "수정자", "수정닉", loginA.getPhoneNum(), "updChange" + uniqueValue + "@test.com");

        logStep("WHEN", "B 회원이 A 회원의 전화번호로 변경 시도");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUser(updateB)
        );

        log("발생한 예외 메시지", exception.getMessage());
        assertTrue(exception.getMessage().contains("전화번호"));

        logPass("회원정보 수정 시 전화번호 중복 방지 확인 완료");
    }

    @Test
    @DisplayName("회원정보 수정 시 탈퇴 회원 전화번호는 중복 검사 제외")
    void updateWithdrawnPhoneNumAllowedTest() {
        UserVO userA = createJoinUser("wdPhoneA" + uniqueValue, "1234", makePhone("2801"), "wdPhoneA" + uniqueValue + "@test.com");
        UserVO userB = createJoinUser("wdPhoneB" + uniqueValue, "1234", makePhone("2802"), "wdPhoneB" + uniqueValue + "@test.com");

        userService.join(userA);
        userService.join(userB);

        UserVO loginA = userService.login(userA.getUserId(), "1234");
        UserVO loginB = userService.login(userB.getUserId(), "1234");

        userService.withdrawUser(loginA.getUserNum(), "1234");

        UserVO updateB = createUpdateUser(loginB.getUserNum(), "수정자", "수정닉", userA.getPhoneNum(), "wdPhoneChange" + uniqueValue + "@test.com");

        logStep("WHEN", "B 회원이 탈퇴한 A 회원의 전화번호로 변경 시도");
        int updateCount = userService.updateUser(updateB);

        log("회원정보 수정 건수", updateCount);
        assertEquals(1, updateCount, "탈퇴 회원의 전화번호는 수정 시 재사용 가능해야 합니다.");

        logPass("회원정보 수정 시 탈퇴 회원 전화번호 재사용 확인 완료");
    }

    @Test
    @DisplayName("관리자 회원목록 조회")
    void userListTest() {
        UserSearchDTO searchDTO = new UserSearchDTO();
        searchDTO.setPageNo(1);
        searchDTO.setPageSize(10);

        logStep("WHEN", "관리자 회원목록 조회 실행");
        List<UserVO> userList = userService.getUserList(searchDTO);
        int totalCount = userService.getUserTotalCount(searchDTO);

        log("조회 목록 크기", userList.size());
        log("전체 회원 수", totalCount);

        assertNotNull(userList, "회원목록은 null이면 안 됩니다.");
        assertTrue(totalCount >= userList.size(), "전체 건수는 현재 페이지 목록 수보다 크거나 같아야 합니다.");

        logPass("관리자 회원목록 조회 확인 완료");
    }

    private UserVO createJoinUser(String userId, String password, String phoneNum, String email) {
        UserVO user = new UserVO();
        user.setUserId(userId);
        user.setPassword(password);
        user.setUserName("테스트");
        user.setNickname("테스터");
        user.setPhoneNum(phoneNum);
        user.setEmail(email);
        return user;
    }

    private UserVO createUpdateUser(Long userNum, String userName, String nickname, String phoneNum, String email) {
        UserVO user = new UserVO();
        user.setUserNum(userNum);
        user.setUserName(userName);
        user.setNickname(nickname);
        user.setPhoneNum(phoneNum);
        user.setEmail(email);
        return user;
    }

    private String makePhone(String middle) {
        String last4 = uniqueValue.substring(uniqueValue.length() - 4);
        return "010-" + middle + "-" + last4;
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
        System.out.println("    password   = " + user.getPassword());
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
