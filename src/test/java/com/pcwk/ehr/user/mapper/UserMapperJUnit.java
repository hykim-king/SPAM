package com.pcwk.ehr.user.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import com.pcwk.ehr.user.util.PasswordUtil;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
        "file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
@Transactional
@Rollback
class UserMapperJUnit {

    final static Logger log = LogManager.getLogger(UserMapperJUnit.class);

    @Autowired
    private UserMapper userMapper;

    private String uniqueValue;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        logStart(testInfo);

        uniqueValue = String.valueOf(System.currentTimeMillis());
    }

    @Test
    @DisplayName("Mapper Bean 주입 확인")
    void beans() {

        log.debug("[STEP 1] UserMapper Bean 주입 확인");

        assertNotNull(userMapper);
        log.debug("userMapper: " + userMapper);

        System.out.println("[확인] Mapper Bean 주입 확인 테스트 완료");
    }

    @Test
    @DisplayName("회원 등록 후 아이디와 회원번호로 조회")
    void insertAndSelect() {

        log.debug("[STEP 1] 등록 대상 회원정보 준비");

        UserVO user = createUser("mapper" + uniqueValue, "1234", makePhone("3101"),
                "mapper" + uniqueValue + "@test.com");

        log.debug("[STEP 2] 회원정보 DB 등록");

        int flag = userMapper.insertUser(user);
        assertEquals(1, flag);

        log.debug("[STEP 3] 아이디 및 회원번호 조회 결과 비교");

        UserVO outVO = userMapper.selectByUserId(user.getUserId());
        assertNotNull(outVO);
        assertNotNull(outVO.getUserNum());
        assertEquals(user.getUserId(), outVO.getUserId());
        assertEquals(user.getPhoneNum(), outVO.getPhoneNum());
        assertEquals(user.getEmail(), outVO.getEmail());

        UserVO outVO2 = userMapper.selectByUserNum(outVO.getUserNum());
        assertNotNull(outVO2);
        assertEquals(outVO.getUserNum(), outVO2.getUserNum());

        log.debug("outVO: " + outVO);

        System.out.println("[확인] 회원 등록 후 아이디와 회원번호로 조회 테스트 완료");
    }

    @Test
    @DisplayName("정상 회원은 중복 검사에 포함되고 탈퇴 회원은 제외")
    void duplicateCheck() {


        UserVO user = createUser("dup" + uniqueValue, "1234", makePhone("3201"),
                "dup" + uniqueValue + "@test.com");

        log.debug("[STEP 1] 중복 검사 대상 정상 회원 등록");

        userMapper.insertUser(user);

        log.debug("[STEP 2] 아이디·전화번호·이메일 중복 건수 확인");

        assertEquals(1, userMapper.countByUserId(user.getUserId()));
        assertEquals(1, userMapper.countByPhoneNum(user.getPhoneNum()));
        assertEquals(1, userMapper.countByEmail(user.getEmail()));

        log.debug("[STEP 3] 회원탈퇴 후 중복 검사 제외 확인");

        UserVO savedUser = userMapper.selectByUserId(user.getUserId());
        int flag = userMapper.withdrawUser(savedUser.getUserNum());
        assertEquals(1, flag);

        assertEquals(0, userMapper.countByUserId(user.getUserId()));
        assertEquals(0, userMapper.countByPhoneNum(user.getPhoneNum()));
        assertEquals(0, userMapper.countByEmail(user.getEmail()));

        System.out.println("[확인] 정상 회원은 중복 검사에 포함되고 탈퇴 회원은 제외 테스트 완료");
    }

    @Test
    @DisplayName("회원정보 수정 및 자기 자신 중복 검사 제외")
    void updateUser() {

        log.debug("[STEP 1] 수정 대상 회원 등록 및 조회");

        UserVO user = createUser("update" + uniqueValue, "1234", makePhone("3301"),
                "update" + uniqueValue + "@test.com");
        userMapper.insertUser(user);

        UserVO updateUser = userMapper.selectByUserId(user.getUserId());

        log.debug("[STEP 2] 이름·닉네임·전화번호·이메일 수정");

        updateUser.setUserName("수정이름");
        updateUser.setNickname("수정닉네임");
        updateUser.setPhoneNum(makePhone("3302"));
        updateUser.setEmail("change" + uniqueValue + "@test.com");

        log.debug("[STEP 3] 본인 중복 제외 및 회원정보 수정 결과 확인");

        assertEquals(0, userMapper.countByPhoneNumForUpdate(updateUser));
        assertEquals(0, userMapper.countByEmailForUpdate(updateUser));

        int flag = userMapper.updateUser(updateUser);
        assertEquals(1, flag);

        UserVO outVO = userMapper.selectByUserNum(updateUser.getUserNum());
        assertEquals("수정이름", outVO.getUserName());
        assertEquals("수정닉네임", outVO.getNickname());
        assertEquals(updateUser.getPhoneNum(), outVO.getPhoneNum());
        assertEquals(updateUser.getEmail(), outVO.getEmail());
        assertNotNull(outVO.getUpdateDt());

        log.debug("outVO: " + outVO);

        System.out.println("[확인] 회원정보 수정 및 자기 자신 중복 검사 제외 테스트 완료");
    }

    @Test
    @DisplayName("비밀번호 변경")
    void updatePassword() {

        log.debug("[STEP 1] 비밀번호 변경 대상 회원 등록");

        UserVO user = createUser("password" + uniqueValue, "1234", makePhone("3401"),
                "password" + uniqueValue + "@test.com");
        userMapper.insertUser(user);

        UserVO savedUser = userMapper.selectByUserId(user.getUserId());
        String oldPassword = savedUser.getPassword();

        log.debug("[STEP 2] 새 비밀번호 해시값 변경");

        UserVO updateUser = new UserVO();
        updateUser.setUserNum(savedUser.getUserNum());
        updateUser.setPassword(PasswordUtil.hash("5678"));

        log.debug("[STEP 3] 비밀번호 변경 실행 및 DB 비밀번호·수정일자 확인");

        int flag = userMapper.updatePassword(updateUser);
        assertEquals(1, flag);

        UserVO outVO = userMapper.selectByUserNum(savedUser.getUserNum());
        assertNotEquals(oldPassword, outVO.getPassword());
        assertTrue(PasswordUtil.matches("5678", outVO.getPassword()));
        assertNotNull(outVO.getUpdateDt());

        System.out.println("[확인] 비밀번호 변경 테스트 완료");
    }

    @Test
    @DisplayName("회원탈퇴 시 상태와 탈퇴일자 변경")
    void withdrawUser() {

        log.debug("[STEP 1] 탈퇴 대상 회원 등록");

        UserVO user = createUser("withdraw" + uniqueValue, "1234", makePhone("3501"),
                "withdraw" + uniqueValue + "@test.com");
        userMapper.insertUser(user);

        log.debug("[STEP 2] 회원탈퇴 처리 실행");

        UserVO savedUser = userMapper.selectByUserId(user.getUserId());
        int flag = userMapper.withdrawUser(savedUser.getUserNum());
        assertEquals(1, flag);

        log.debug("[STEP 3] 회원상태 및 탈퇴일자 확인");

        UserVO outVO = userMapper.selectByUserNum(savedUser.getUserNum());
        assertEquals("02", outVO.getUserStatus());
        assertNotNull(outVO.getWithdrawDt());
        assertNotNull(outVO.getUpdateDt());

        log.debug("outVO: " + outVO);

        System.out.println("[확인] 회원탈퇴 시 상태와 탈퇴일자 변경 테스트 완료");
    }

    @Test
    @DisplayName("관리자 회원상태와 회원권한 변경")
    void updateStatusAndRole() {

        log.debug("[STEP 1] 상태·권한 변경 대상 회원 등록");

        UserVO user = createUser("adminUpdate" + uniqueValue, "1234", makePhone("3601"),
                "adminUpdate" + uniqueValue + "@test.com");
        userMapper.insertUser(user);

        log.debug("[STEP 2] 회원상태 탈퇴 변경");

        UserVO savedUser = userMapper.selectByUserId(user.getUserId());
        savedUser.setUserStatus("02");
        assertEquals(1, userMapper.updateUserStatus(savedUser));

        log.debug("[STEP 3] 탈퇴 상태 확인 후 정상 상태 및 관리자 권한 변경");

        UserVO outVO = userMapper.selectByUserNum(savedUser.getUserNum());
        assertEquals("02", outVO.getUserStatus());
        assertNotNull(outVO.getWithdrawDt());

        savedUser.setUserStatus("01");
        assertEquals(1, userMapper.updateUserStatus(savedUser));

        outVO = userMapper.selectByUserNum(savedUser.getUserNum());
        assertEquals("01", outVO.getUserStatus());
        assertNull(outVO.getWithdrawDt());

        savedUser.setUserRole("02");
        assertEquals(1, userMapper.updateUserRole(savedUser));

        outVO = userMapper.selectByUserNum(savedUser.getUserNum());
        assertEquals("02", outVO.getUserRole());

        System.out.println("[확인] 관리자 회원상태와 회원권한 변경 테스트 완료");
    }

    @Test
    @DisplayName("관리자 회원목록 조회")
    void selectUserList() {

        log.debug("[STEP 1] 관리자 회원목록 검색 조건 준비");

        UserSearchDTO searchDTO = new UserSearchDTO();
        searchDTO.setPageNo(1);
        searchDTO.setPageSize(10);

        log.debug("[STEP 2] 검색 조건 회원목록 조회");

        List<UserVO> list = userMapper.selectUserList(searchDTO);
        int totalCount = userMapper.selectUserTotalCount(searchDTO);

        log.debug("[STEP 3] 조회 건수 및 목록 데이터 확인");

        assertNotNull(list);
        assertTrue(totalCount >= list.size());

        log.debug("list.size(): " + list.size());
        log.debug("totalCount: " + totalCount);

        System.out.println("[확인] 관리자 회원목록 조회 테스트 완료");
    }

    private UserVO createUser(String userId, String password, String phoneNum, String email) {
        UserVO user = new UserVO();
        user.setUserId(userId);
        user.setPassword(PasswordUtil.hash(password));
        user.setUserName("테스트");
        user.setNickname("테스터");
        user.setPhoneNum(phoneNum);
        user.setEmail(email);
        user.setUserRole("01");
        user.setUserStatus("01");
        logUser("생성한 테스트 회원", user);
        return user;
    }


    private void logStart(TestInfo testInfo) {
        String testName = testInfo.getTestMethod().isPresent()
                ? testInfo.getTestMethod().get().getName()
                : "테스트";

        System.out.println();
        System.out.println("=================================================================");
        System.out.println("[ TEST " + testName + " ] " + testInfo.getDisplayName());
        System.out.println("=================================================================");
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
