package com.pcwk.ehr.account.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
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

import com.pcwk.ehr.account.domain.AccountVO;
import com.pcwk.ehr.user.domain.UserVO;
import com.pcwk.ehr.user.mapper.UserMapper;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
        "file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
@Transactional
@Rollback
class AccountMapperJUnit {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;

    private String uniqueValue;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        uniqueValue = String.valueOf(System.currentTimeMillis());
        logStart(testInfo.getDisplayName());
        log("테스트 고유값", uniqueValue);
    }

    @Test
    @DisplayName("AccountMapper Bean 주입 확인")
    void mapperBeanInjectionTest() {
        logStep("GIVEN", "root-context.xml에서 AccountMapper가 Spring Bean으로 등록되어 있어야 함");

        assertNotNull(accountMapper, "accountMapper가 null이면 MapperScannerConfigurer 설정을 확인해야 합니다.");

        logPass("AccountMapper Bean 주입 성공");
    }

    @Test
    @DisplayName("계좌 INSERT 후 회원번호로 목록 조회")
    void insertAndSelectListByUserNumTest() {
        Long userNum = createSavedUserNum("accList");
        AccountVO insertAccount = createAccount(userNum, "테스트은행", "100-" + uniqueValue + "-001");

        logStep("GIVEN", "USER_ACCOUNT INSERT용 AccountVO 준비");
        logAccount("INSERT 입력값", insertAccount);

        int insertCount = accountMapper.insertAccount(insertAccount);
        log("INSERT 건수", insertCount);

        assertEquals(1, insertCount, "계좌 INSERT 결과는 1건이어야 합니다.");

        List<AccountVO> accountList = accountMapper.selectListByUserNum(userNum);
        log("조회된 계좌 수", accountList.size());

        assertEquals(1, accountList.size(), "해당 회원의 계좌 목록은 1건이어야 합니다.");

        AccountVO selectedAccount = accountList.get(0);
        logAccount("SELECT 결과", selectedAccount);

        assertNotNull(selectedAccount.getAccountId(), "ACCOUNT_ID는 시퀀스로 생성되어야 합니다.");
        assertEquals(userNum, selectedAccount.getUserNum());
        assertEquals("테스트은행", selectedAccount.getBankName());
        assertEquals("100-" + uniqueValue + "-001", selectedAccount.getAccountNum());
        assertBigDecimalEquals(BigDecimal.ZERO, selectedAccount.getBalance(), "신규 계좌 잔액은 0원이어야 합니다.");

        logPass("계좌 INSERT 후 목록 조회 확인 완료");
    }

    @Test
    @DisplayName("계좌고유번호와 회원번호로 본인 계좌 단건 조회")
    void selectByAccountIdAndUserNumTest() {
        Long userNum = createSavedUserNum("accOne");
        Long otherUserNum = createSavedUserNum("accOther");
        AccountVO savedAccount = createSavedAccount(userNum, "단건은행", "200-" + uniqueValue + "-001");

        logStep("WHEN", "본인 회원번호로 계좌 단건 조회");
        AccountVO selectedAccount = accountMapper.selectByAccountIdAndUserNum(savedAccount.getAccountId(), userNum);
        logAccount("본인 조회 결과", selectedAccount);

        assertNotNull(selectedAccount, "본인 계좌는 조회되어야 합니다.");
        assertEquals(savedAccount.getAccountId(), selectedAccount.getAccountId());
        assertEquals(userNum, selectedAccount.getUserNum());

        logStep("WHEN", "다른 회원번호로 같은 ACCOUNT_ID 조회");
        AccountVO otherUserResult = accountMapper.selectByAccountIdAndUserNum(savedAccount.getAccountId(), otherUserNum);
        logAccount("타인 조회 결과", otherUserResult);

        assertNull(otherUserResult, "다른 회원번호로는 계좌가 조회되지 않아야 합니다.");

        logPass("본인 계좌 단건 조회 조건 확인 완료");
    }

    @Test
    @DisplayName("동일 회원의 은행명과 계좌번호 중복 검사")
    void countDuplicateTest() {
        Long userNum = createSavedUserNum("accDup");
        Long otherUserNum = createSavedUserNum("accDupOther");
        String bankName = "중복은행";
        String accountNum = "300-" + uniqueValue + "-001";

        AccountVO savedAccount = createSavedAccount(userNum, bankName, accountNum);

        AccountVO duplicateAccount = createAccount(userNum, bankName, accountNum);
        int duplicateCount = accountMapper.countDuplicate(duplicateAccount);
        log("동일 회원 중복 건수", duplicateCount);

        assertEquals(1, duplicateCount, "같은 회원의 같은 은행명/계좌번호는 중복으로 계산되어야 합니다.");

        AccountVO selfUpdateAccount = createAccount(userNum, bankName, accountNum);
        selfUpdateAccount.setAccountId(savedAccount.getAccountId());
        int selfDuplicateCount = accountMapper.countDuplicate(selfUpdateAccount);
        log("수정 시 자기 자신 제외 후 중복 건수", selfDuplicateCount);

        assertEquals(0, selfDuplicateCount, "수정 시 자기 자신의 계좌는 중복에서 제외되어야 합니다.");

        AccountVO otherUserAccount = createAccount(otherUserNum, bankName, accountNum);
        int otherUserDuplicateCount = accountMapper.countDuplicate(otherUserAccount);
        log("다른 회원 동일 계좌 중복 건수", otherUserDuplicateCount);

        assertEquals(0, otherUserDuplicateCount, "다른 회원의 같은 계좌번호는 중복으로 보지 않습니다.");

        logPass("계좌 중복 검사 확인 완료");
    }

    @Test
    @DisplayName("계좌 기본정보 수정")
    void updateAccountTest() {
        Long userNum = createSavedUserNum("accUpdate");
        AccountVO savedAccount = createSavedAccount(userNum, "수정전은행", "400-" + uniqueValue + "-001");

        AccountVO updateAccount = createAccount(userNum, "수정후은행", "400-" + uniqueValue + "-999");
        updateAccount.setAccountId(savedAccount.getAccountId());

        logStep("WHEN", "은행명과 계좌번호 수정");
        int updateCount = accountMapper.updateAccount(updateAccount);
        log("UPDATE 건수", updateCount);

        assertEquals(1, updateCount, "계좌 기본정보 수정 결과는 1건이어야 합니다.");

        AccountVO selectedAccount = accountMapper.selectByAccountIdAndUserNum(savedAccount.getAccountId(), userNum);
        logAccount("수정 후 SELECT 결과", selectedAccount);

        assertEquals("수정후은행", selectedAccount.getBankName());
        assertEquals("400-" + uniqueValue + "-999", selectedAccount.getAccountNum());
        assertNotNull(selectedAccount.getUpdateDt(), "수정 후 UPDATE_DT가 저장되어야 합니다.");

        logPass("계좌 기본정보 수정 확인 완료");
    }

    @Test
    @DisplayName("잔액 0원 계좌 삭제")
    void deleteZeroBalanceAccountTest() {
        Long userNum = createSavedUserNum("accDelete");
        AccountVO savedAccount = createSavedAccount(userNum, "삭제은행", "500-" + uniqueValue + "-001");

        logStep("WHEN", "잔액 0원 계좌 삭제");
        int deleteCount = accountMapper.deleteAccount(savedAccount.getAccountId(), userNum);
        log("DELETE 건수", deleteCount);

        assertEquals(1, deleteCount, "잔액 0원 계좌는 삭제되어야 합니다.");

        AccountVO deletedAccount = accountMapper.selectByAccountIdAndUserNum(savedAccount.getAccountId(), userNum);
        logAccount("삭제 후 SELECT 결과", deletedAccount);

        assertNull(deletedAccount, "삭제된 계좌는 조회되지 않아야 합니다.");

        logPass("잔액 0원 계좌 삭제 확인 완료");
    }

    @Test
    @DisplayName("잔액이 남은 계좌 삭제 SQL 실패")
    void deleteAccountFailWhenBalanceRemainTest() {
        Long userNum = createSavedUserNum("accDelMoney");
        AccountVO savedAccount = createSavedAccount(userNum, "삭제실패은행", "550-" + uniqueValue + "-001");

        accountMapper.increaseBalance(savedAccount.getAccountId(), userNum, new BigDecimal("1000"));

        logStep("WHEN", "잔액이 남은 계좌 삭제 시도");
        int deleteCount = accountMapper.deleteAccount(savedAccount.getAccountId(), userNum);
        log("DELETE 건수", deleteCount);

        assertEquals(0, deleteCount, "잔액이 남은 계좌는 Mapper DELETE 조건에서 제외되어야 합니다.");

        AccountVO selectedAccount = accountMapper.selectByAccountIdAndUserNum(savedAccount.getAccountId(), userNum);
        logAccount("삭제 실패 후 SELECT 결과", selectedAccount);

        assertNotNull(selectedAccount, "삭제 실패한 계좌는 계속 조회되어야 합니다.");
        assertBigDecimalEquals(new BigDecimal("1000"), selectedAccount.getBalance(), "삭제 실패 후 잔액은 유지되어야 합니다.");

        logPass("잔액이 남은 계좌 삭제 SQL 실패 확인 완료");
    }

    @Test
    @DisplayName("잔액 증가와 감소 SQL 동작")
    void increaseAndDecreaseBalanceTest() {
        Long userNum = createSavedUserNum("accBalance");
        AccountVO savedAccount = createSavedAccount(userNum, "잔액은행", "600-" + uniqueValue + "-001");

        logStep("WHEN", "잔액 10000원 증가");
        int increaseCount = accountMapper.increaseBalance(savedAccount.getAccountId(), userNum, new BigDecimal("10000"));
        log("잔액 증가 UPDATE 건수", increaseCount);

        assertEquals(1, increaseCount, "잔액 증가 결과는 1건이어야 합니다.");

        AccountVO afterIncrease = accountMapper.selectByAccountIdAndUserNum(savedAccount.getAccountId(), userNum);
        logAccount("충전 후 계좌", afterIncrease);
        assertBigDecimalEquals(new BigDecimal("10000"), afterIncrease.getBalance(), "충전 후 잔액은 10000원이어야 합니다.");

        logStep("WHEN", "잔액 3000원 감소");
        int decreaseCount = accountMapper.decreaseBalance(savedAccount.getAccountId(), userNum, new BigDecimal("3000"));
        log("잔액 감소 UPDATE 건수", decreaseCount);

        assertEquals(1, decreaseCount, "잔액 감소 결과는 1건이어야 합니다.");

        AccountVO afterDecrease = accountMapper.selectByAccountIdAndUserNum(savedAccount.getAccountId(), userNum);
        logAccount("차감 후 계좌", afterDecrease);
        assertBigDecimalEquals(new BigDecimal("7000"), afterDecrease.getBalance(), "차감 후 잔액은 7000원이어야 합니다.");

        logPass("잔액 증가와 감소 SQL 확인 완료");
    }

    @Test
    @DisplayName("잔액 부족 시 차감 UPDATE 실패")
    void decreaseBalanceFailWhenNotEnoughBalanceTest() {
        Long userNum = createSavedUserNum("accNoMoney");
        AccountVO savedAccount = createSavedAccount(userNum, "부족은행", "700-" + uniqueValue + "-001");

        accountMapper.increaseBalance(savedAccount.getAccountId(), userNum, new BigDecimal("5000"));

        logStep("WHEN", "현재 잔액보다 큰 금액 차감 시도");
        int decreaseCount = accountMapper.decreaseBalance(savedAccount.getAccountId(), userNum, new BigDecimal("6000"));
        log("잔액 부족 UPDATE 건수", decreaseCount);

        assertEquals(0, decreaseCount, "잔액이 부족하면 UPDATE 건수는 0이어야 합니다.");

        AccountVO selectedAccount = accountMapper.selectByAccountIdAndUserNum(savedAccount.getAccountId(), userNum);
        logAccount("차감 실패 후 계좌", selectedAccount);

        assertBigDecimalEquals(new BigDecimal("5000"), selectedAccount.getBalance(), "차감 실패 후 잔액은 그대로 유지되어야 합니다.");

        logPass("잔액 부족 시 차감 실패 확인 완료");
    }

    @Test
    @DisplayName("총 보유 자산 조회")
    void selectTotalBalanceByUserNumTest() {
        Long userNum = createSavedUserNum("accTotal");
        AccountVO firstAccount = createSavedAccount(userNum, "합계은행A", "800-" + uniqueValue + "-001");
        AccountVO secondAccount = createSavedAccount(userNum, "합계은행B", "800-" + uniqueValue + "-002");

        accountMapper.increaseBalance(firstAccount.getAccountId(), userNum, new BigDecimal("10000"));
        accountMapper.increaseBalance(secondAccount.getAccountId(), userNum, new BigDecimal("25000"));

        logStep("WHEN", "회원의 전체 계좌 잔액 합계 조회");
        BigDecimal totalBalance = accountMapper.selectTotalBalanceByUserNum(userNum);
        log("총 보유 자산", totalBalance);

        assertBigDecimalEquals(new BigDecimal("35000"), totalBalance, "총 보유 자산은 35000원이어야 합니다.");

        logPass("총 보유 자산 조회 확인 완료");
    }

    private Long createSavedUserNum(String prefix) {
        String suffix = uniqueValue.substring(uniqueValue.length() - 6);
        String userId = prefix + suffix;

        UserVO user = new UserVO();
        user.setUserId(userId);
        user.setPassword("1234");
        user.setUserName("계좌테스트회원");
        user.setNickname("계좌테스트회원");
        user.setPhoneNum(makePhone(prefix));
        user.setEmail(userId + "@test.com");
        user.setUserRole("01");
        user.setUserStatus("01");

        int insertCount = userMapper.insertUser(user);
        assertEquals(1, insertCount, "테스트 회원 INSERT는 1건이어야 합니다.");

        UserVO savedUser = userMapper.selectByUserId(userId);
        assertNotNull(savedUser, "테스트 회원이 조회되어야 합니다.");
        assertNotNull(savedUser.getUserNum(), "테스트 회원 USER_NUM이 생성되어야 합니다.");

        log("테스트 회원번호", savedUser.getUserNum());
        return savedUser.getUserNum();
    }

    private AccountVO createSavedAccount(Long userNum, String bankName, String accountNum) {
        AccountVO account = createAccount(userNum, bankName, accountNum);

        int insertCount = accountMapper.insertAccount(account);
        assertEquals(1, insertCount, "테스트 계좌 INSERT는 1건이어야 합니다.");

        AccountVO savedAccount = findAccountByAccountNum(userNum, accountNum);
        assertNotNull(savedAccount, "INSERT한 테스트 계좌가 조회되어야 합니다.");
        assertNotNull(savedAccount.getAccountId(), "테스트 계좌 ACCOUNT_ID가 생성되어야 합니다.");

        logAccount("테스트 계좌", savedAccount);
        return savedAccount;
    }

    private String makePhone(String prefix) {
        int hash = Math.floorMod(prefix.hashCode(), 10000);
        String middle = String.format("%04d", hash);
        String last = uniqueValue.substring(uniqueValue.length() - 4);

        return "010-" + middle + "-" + last;
    }

    private AccountVO findAccountByAccountNum(Long userNum, String accountNum) {
        List<AccountVO> accountList = accountMapper.selectListByUserNum(userNum);

        for (AccountVO account : accountList) {
            if (accountNum.equals(account.getAccountNum())) {
                return account;
            }
        }

        return null;
    }

    private AccountVO createAccount(Long userNum, String bankName, String accountNum) {
        AccountVO account = new AccountVO();
        account.setUserNum(userNum);
        account.setBankName(bankName);
        account.setAccountNum(accountNum);
        account.setBalance(BigDecimal.ZERO);
        return account;
    }

    private void assertBigDecimalEquals(BigDecimal expected, BigDecimal actual, String message) {
        assertNotNull(actual, message + " actual 값이 null이면 안 됩니다.");
        assertEquals(0, expected.compareTo(actual), message);
    }

    private void logAccount(String title, AccountVO account) {
        System.out.println("  - " + title + " : " + account);
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
    }
}
