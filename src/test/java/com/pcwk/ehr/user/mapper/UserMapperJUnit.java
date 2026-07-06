package com.pcwk.ehr.user.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.pcwk.ehr.user.util.PasswordUtil;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
        "file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
@Transactional
@Rollback
class UserMapperJUnit {

    @Autowired
    private UserMapper userMapper;

    // 테스트마다 고유한 값을 만들기 위한 현재 시간 문자열
    private String uniqueValue;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        uniqueValue = String.valueOf(System.currentTimeMillis());
        logStart(testInfo.getDisplayName());
        log("테스트 고유값", uniqueValue);
    }

    @Test
    @DisplayName("Mapper Bean 주입 확인")
    void mapperBeanInjectionTest() {
        logStep("GIVEN", "MapperScannerConfigurer가 UserMapper를 Spring Bean으로 등록해야 함");

        assertNotNull(userMapper, "userMapper가 null이면 root-context.xml의 MapperScannerConfigurer 설정을 확인해야 합니다.");

        logPass("UserMapper Bean 주입 성공");
    }

    @Test
    @DisplayName("회원 INSERT 후 USER_ID로 SELECT")
    void insertAndSelectByUserIdTest() {
        UserVO insertUser = createInsertUser("mapper" + uniqueValue, "1234", makePhone("3101"), "mapper" + uniqueValue + "@test.com");

        logStep("GIVEN", "USER_INFO INSERT용 UserVO 준비");
        logUser("INSERT 입력값", insertUser);

        int insertCount = userMapper.insertUser(insertUser);

        logStep("WHEN", "userMapper.insertUser(insertUser) 실행");
        log("INSERT 건수", insertCount);

        assertEquals(1, insertCount, "INSERT 결과는 1건이어야 합니다.");

        UserVO selectedUser = userMapper.selectByUserId(insertUser.getUserId());

        logStep("WHEN", "userMapper.selectByUserId(userId) 실행");
        logUser("SELECT 결과", selectedUser);

        assertNotNull(selectedUser, "SELECT 결과는 null이면 안 됩니다.");
        assertNotNull(selectedUser.getUserNum(), "USER_NUM은 시퀀스로 생성되어야 합니다.");
        assertEquals(insertUser.getUserId(), selectedUser.getUserId());
        assertEquals(insertUser.getPhoneNum(), selectedUser.getPhoneNum());
        assertEquals(insertUser.getEmail(), selectedUser.getEmail());

        logPass("INSERT 후 SELECT 확인 완료");
    }

    @Test
    @DisplayName("중복 검사 SQL은 01 정상 회원을 중복으로 계산")
    void countActiveDuplicateTest() {
        UserVO insertUser = createInsertUser("cntActive" + uniqueValue, "1234", makePhone("3201"), "cntActive" + uniqueValue + "@test.com");

        logStep("GIVEN", "01 정상 상태 회원 INSERT");
        userMapper.insertUser(insertUser);

        int userIdCount = userMapper.countByUserId(insertUser.getUserId());
        int phoneCount = userMapper.countByPhoneNum(insertUser.getPhoneNum());
        int emailCount = userMapper.countByEmail(insertUser.getEmail());

        log("아이디 중복 건수", userIdCount);
        log("전화번호 중복 건수", phoneCount);
        log("이메일 중복 건수", emailCount);

        assertEquals(1, userIdCount, "01 정상 회원의 USER_ID는 중복 검사에 포함되어야 합니다.");
        assertEquals(1, phoneCount, "01 정상 회원의 PHONE_NUM은 중복 검사에 포함되어야 합니다.");
        assertEquals(1, emailCount, "01 정상 회원의 EMAIL은 중복 검사에 포함되어야 합니다.");

        logPass("01 정상 회원 중복 검사 포함 확인 완료");
    }

    @Test
    @DisplayName("중복 검사 SQL은 02 탈퇴 회원을 제외")
    void countWithdrawnDuplicateExcludedTest() {
        UserVO insertUser = createInsertUser("cntWd" + uniqueValue, "1234", makePhone("3301"), "cntWd" + uniqueValue + "@test.com");

        logStep("GIVEN", "회원 INSERT 후 02 탈퇴 상태로 변경");
        userMapper.insertUser(insertUser);

        UserVO selectedUser = userMapper.selectByUserId(insertUser.getUserId());
        userMapper.withdrawUser(selectedUser.getUserNum());

        int userIdCount = userMapper.countByUserId(insertUser.getUserId());
        int phoneCount = userMapper.countByPhoneNum(insertUser.getPhoneNum());
        int emailCount = userMapper.countByEmail(insertUser.getEmail());

        log("탈퇴 후 아이디 중복 건수", userIdCount);
        log("탈퇴 후 전화번호 중복 건수", phoneCount);
        log("탈퇴 후 이메일 중복 건수", emailCount);

        assertEquals(0, userIdCount, "02 탈퇴 회원의 USER_ID는 중복 검사에서 제외되어야 합니다.");
        assertEquals(0, phoneCount, "02 탈퇴 회원의 PHONE_NUM은 중복 검사에서 제외되어야 합니다.");
        assertEquals(0, emailCount, "02 탈퇴 회원의 EMAIL은 중복 검사에서 제외되어야 합니다.");

        logPass("02 탈퇴 회원 중복 검사 제외 확인 완료");
    }

    @Test
    @DisplayName("회원정보 수정 중복 검사 SQL은 자기 자신 제외")
    void countForUpdateExcludeSelfTest() {
        UserVO insertUser = createInsertUser("updSelf" + uniqueValue, "1234", makePhone("3401"), "updSelf" + uniqueValue + "@test.com");

        logStep("GIVEN", "회원 INSERT 후 자기 자신의 전화번호/이메일로 수정 중복 검사");
        userMapper.insertUser(insertUser);

        UserVO selectedUser = userMapper.selectByUserId(insertUser.getUserId());
        selectedUser.setPhoneNum(insertUser.getPhoneNum());
        selectedUser.setEmail(insertUser.getEmail());

        int phoneCount = userMapper.countByPhoneNumForUpdate(selectedUser);
        int emailCount = userMapper.countByEmailForUpdate(selectedUser);

        log("자기 자신 제외 전화번호 중복 건수", phoneCount);
        log("자기 자신 제외 이메일 중복 건수", emailCount);

        assertEquals(0, phoneCount, "수정 시 자기 자신의 전화번호는 중복으로 보면 안 됩니다.");
        assertEquals(0, emailCount, "수정 시 자기 자신의 이메일은 중복으로 보면 안 됩니다.");

        logPass("회원정보 수정 중복 검사 자기 자신 제외 확인 완료");
    }

    @Test
    @DisplayName("관리자 회원목록 조회 SQL")
    void selectUserListTest() {
        UserSearchDTO searchDTO = new UserSearchDTO();
        searchDTO.setPageNo(1);
        searchDTO.setPageSize(10);

        logStep("WHEN", "userMapper.selectUserList(searchDTO) 실행");
        List<UserVO> userList = userMapper.selectUserList(searchDTO);

        logStep("WHEN", "userMapper.selectUserTotalCount(searchDTO) 실행");
        int totalCount = userMapper.selectUserTotalCount(searchDTO);

        log("조회 목록 크기", userList.size());
        log("전체 회원 수", totalCount);

        assertNotNull(userList, "회원목록은 null이면 안 됩니다.");
        assertTrue(totalCount >= userList.size(), "전체 건수는 현재 페이지 목록 수보다 크거나 같아야 합니다.");

        logPass("관리자 회원목록 조회 SQL 확인 완료");
    }

    private UserVO createInsertUser(String userId, String rawPassword, String phoneNum, String email) {
        UserVO user = new UserVO();
        user.setUserId(userId);
        user.setPassword(PasswordUtil.hash(rawPassword));
        user.setUserName("테스트");
        user.setNickname("테스터");
        user.setPhoneNum(phoneNum);
        user.setEmail(email);
        user.setUserRole("01");
        user.setUserStatus("01");
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
