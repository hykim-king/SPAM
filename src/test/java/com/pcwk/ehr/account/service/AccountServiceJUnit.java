package com.pcwk.ehr.account.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
class AccountServiceJUnit {

    @Autowired
    private AccountService accountService;

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
    @DisplayName("AccountService Bean 주입 확인")
    void serviceBeanInjectionTest() {
        logStep("GIVEN", "AccountServiceImpl이 Spring Bean으로 등록되어 있어야 함");

        assertNotNull(accountService, "accountService가 null이면 component-scan 설정을 확인해야 합니다.");

        logPass("AccountService Bean 주입 성공");
    }

    @Test
    @DisplayName("계좌 등록 시 시작 잔액 0원 고정")
    void createAccountInitialBalanceZeroTest() {
        Long userNum = createSavedUserNum("svcCreate");
        AccountVO account = createAccount(userNum, "  서비스은행  ", " 900-" + uniqueValue + "-001 ");
        account.setBalance(new BigDecimal("999999"));

        logStep("GIVEN", "화면에서 잔액이 넘어오더라도 신규 계좌는 0원으로 시작해야 함");
        logAccount("등록 입력값", account);

        int createCount = accountService.createAccount(account);
        log("계좌 등록 건수", createCount);

        assertEquals(1, createCount, "계좌 등록 결과는 1건이어야 합니다.");

        AccountVO savedAccount = findAccountByAccountNum(userNum, "900-" + uniqueValue + "-001");
        logAccount("등록 후 조회 결과", savedAccount);

        assertNotNull(savedAccount, "등록한 계좌가 조회되어야 합니다.");
        assertEquals("서비스은행", savedAccount.getBankName(), "은행명 앞뒤 공백은 제거되어야 합니다.");
        assertEquals("900-" + uniqueValue + "-001", savedAccount.getAccountNum(), "계좌번호 앞뒤 공백은 제거되어야 합니다.");
        assertBigDecimalEquals(BigDecimal.ZERO, savedAccount.getBalance(), "신규 계좌 시작 잔액은 0원이어야 합니다.");

        logPass("계좌 등록 정책 확인 완료");
    }

    @Test
    @DisplayName("동일 회원의 중복 계좌 등록 방지")
    void duplicateAccountCreateFailTest() {
        Long userNum = createSavedUserNum("svcDup");
        String bankName = "중복서비스은행";
        String accountNum = "910-" + uniqueValue + "-001";

        accountService.createAccount(createAccount(userNum, bankName, accountNum));
        logPass("첫 번째 계좌 등록 성공");

        logStep("WHEN", "같은 회원이 같은 은행명과 계좌번호로 다시 등록 시도");
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.createAccount(createAccount(userNum, bankName, accountNum))
        );

        log("발생한 예외 메시지", exception.getMessage());
        assertTrue(exception.getMessage().contains("이미 등록된 계좌"));

        logPass("중복 계좌 등록 방지 확인 완료");
    }

    @Test
    @DisplayName("계좌 목록 조회와 본인 계좌 단건 조회")
    void getAccountListAndGetAccountTest() {
        Long userNum = createSavedUserNum("svcList");
        Long otherUserNum = createSavedUserNum("svcListOther");

        accountService.createAccount(createAccount(userNum, "목록은행A", "920-" + uniqueValue + "-001"));
        accountService.createAccount(createAccount(userNum, "목록은행B", "920-" + uniqueValue + "-002"));
        accountService.createAccount(createAccount(otherUserNum, "타인은행", "920-" + uniqueValue + "-003"));

        logStep("WHEN", "로그인 회원의 계좌 목록 조회");
        List<AccountVO> accountList = accountService.getAccountList(userNum);
        log("조회된 계좌 수", accountList.size());

        assertEquals(2, accountList.size(), "로그인 회원의 계좌만 2건 조회되어야 합니다.");

        AccountVO firstAccount = accountList.get(0);
        AccountVO selectedAccount = accountService.getAccount(firstAccount.getAccountId(), userNum);
        logAccount("본인 계좌 단건 조회 결과", selectedAccount);

        assertNotNull(selectedAccount, "본인 계좌 단건 조회 결과는 null이면 안 됩니다.");
        assertEquals(userNum, selectedAccount.getUserNum());

        logStep("WHEN", "다른 회원번호로 같은 ACCOUNT_ID 조회 시도");
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.getAccount(firstAccount.getAccountId(), otherUserNum)
        );

        log("발생한 예외 메시지", exception.getMessage());
        assertTrue(exception.getMessage().contains("계좌 정보를 찾을 수 없습니다."));

        logPass("계좌 목록과 본인 계좌 단건 조회 확인 완료");
    }

    @Test
    @DisplayName("계좌 기본정보 수정")
    void updateAccountTest() {
        Long userNum = createSavedUserNum("svcUpdate");
        accountService.createAccount(createAccount(userNum, "수정서비스은행", "930-" + uniqueValue + "-001"));

        AccountVO savedAccount = findAccountByAccountNum(userNum, "930-" + uniqueValue + "-001");
        assertNotNull(savedAccount, "수정할 계좌가 조회되어야 합니다.");

        AccountVO updateAccount = createAccount(userNum, " 변경서비스은행 ", " 930-" + uniqueValue + "-999 ");
        updateAccount.setAccountId(savedAccount.getAccountId());

        logStep("WHEN", "계좌 은행명과 계좌번호 수정");
        int updateCount = accountService.updateAccount(updateAccount);
        log("수정 건수", updateCount);

        assertEquals(1, updateCount, "계좌 수정 결과는 1건이어야 합니다.");

        AccountVO selectedAccount = accountService.getAccount(savedAccount.getAccountId(), userNum);
        logAccount("수정 후 조회 결과", selectedAccount);

        assertEquals("변경서비스은행", selectedAccount.getBankName(), "수정 시 은행명 앞뒤 공백은 제거되어야 합니다.");
        assertEquals("930-" + uniqueValue + "-999", selectedAccount.getAccountNum(), "수정 시 계좌번호 앞뒤 공백은 제거되어야 합니다.");

        logPass("계좌 기본정보 수정 확인 완료");
    }

    @Test
    @DisplayName("계좌 수정 시 중복 계좌 방지")
    void duplicateAccountUpdateFailTest() {
        Long userNum = createSavedUserNum("svcUpdDup");
        String firstAccountNum = "940-" + uniqueValue + "-001";
        String secondAccountNum = "940-" + uniqueValue + "-002";

        accountService.createAccount(createAccount(userNum, "수정중복은행A", firstAccountNum));
        accountService.createAccount(createAccount(userNum, "수정중복은행B", secondAccountNum));

        AccountVO firstAccount = findAccountByAccountNum(userNum, firstAccountNum);
        AccountVO secondAccount = findAccountByAccountNum(userNum, secondAccountNum);
        assertNotNull(firstAccount);
        assertNotNull(secondAccount);

        AccountVO updateAccount = createAccount(userNum, firstAccount.getBankName(), firstAccount.getAccountNum());
        updateAccount.setAccountId(secondAccount.getAccountId());

        logStep("WHEN", "두 번째 계좌를 첫 번째 계좌와 같은 은행명/계좌번호로 수정 시도");
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.updateAccount(updateAccount)
        );

        log("발생한 예외 메시지", exception.getMessage());
        assertTrue(exception.getMessage().contains("이미 등록된 계좌"));

        logPass("계좌 수정 시 중복 방지 확인 완료");
    }

    @Test
    @DisplayName("잔액 충전과 차감")
    void depositAndWithdrawTest() {
        Long userNum = createSavedUserNum("svcMoney");
        String accountNum = "950-" + uniqueValue + "-001";
        accountService.createAccount(createAccount(userNum, "잔액서비스은행", accountNum));

        AccountVO savedAccount = findAccountByAccountNum(userNum, accountNum);
        assertNotNull(savedAccount, "잔액 변경할 계좌가 조회되어야 합니다.");

        logStep("WHEN", "10000원 충전");
        int depositCount = accountService.deposit(savedAccount.getAccountId(), userNum, new BigDecimal("10000"));
        log("충전 처리 건수", depositCount);

        assertEquals(1, depositCount, "충전 처리 결과는 1건이어야 합니다.");

        AccountVO afterDeposit = accountService.getAccount(savedAccount.getAccountId(), userNum);
        logAccount("충전 후 계좌", afterDeposit);
        assertBigDecimalEquals(new BigDecimal("10000"), afterDeposit.getBalance(), "충전 후 잔액은 10000원이어야 합니다.");

        logStep("WHEN", "4000원 차감");
        int withdrawCount = accountService.withdraw(savedAccount.getAccountId(), userNum, new BigDecimal("4000"));
        log("차감 처리 건수", withdrawCount);

        assertEquals(1, withdrawCount, "차감 처리 결과는 1건이어야 합니다.");

        AccountVO afterWithdraw = accountService.getAccount(savedAccount.getAccountId(), userNum);
        logAccount("차감 후 계좌", afterWithdraw);
        assertBigDecimalEquals(new BigDecimal("6000"), afterWithdraw.getBalance(), "차감 후 잔액은 6000원이어야 합니다.");

        BigDecimal totalBalance = accountService.getTotalBalance(userNum);
        log("총 보유 자산", totalBalance);
        assertBigDecimalEquals(new BigDecimal("6000"), totalBalance, "총 보유 자산은 6000원이어야 합니다.");

        logPass("잔액 충전과 차감 확인 완료");
    }

    @Test
    @DisplayName("잔액 부족 시 차감 실패")
    void withdrawFailWhenNotEnoughBalanceTest() {
        Long userNum = createSavedUserNum("svcNoMoney");
        String accountNum = "960-" + uniqueValue + "-001";
        accountService.createAccount(createAccount(userNum, "부족서비스은행", accountNum));

        AccountVO savedAccount = findAccountByAccountNum(userNum, accountNum);
        assertNotNull(savedAccount, "잔액 부족 테스트 계좌가 조회되어야 합니다.");

        accountService.deposit(savedAccount.getAccountId(), userNum, new BigDecimal("5000"));

        logStep("WHEN", "잔액보다 큰 금액 차감 시도");
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> accountService.withdraw(savedAccount.getAccountId(), userNum, new BigDecimal("6000"))
        );

        log("발생한 예외 메시지", exception.getMessage());
        assertTrue(exception.getMessage().contains("잔액이 부족합니다."));

        AccountVO selectedAccount = accountService.getAccount(savedAccount.getAccountId(), userNum);
        logAccount("차감 실패 후 계좌", selectedAccount);
        assertBigDecimalEquals(new BigDecimal("5000"), selectedAccount.getBalance(), "차감 실패 후 잔액은 그대로 유지되어야 합니다.");

        logPass("잔액 부족 차감 실패 확인 완료");
    }

    @Test
    @DisplayName("충전/차감 금액 검증")
    void amountValidationTest() {
        Long userNum = createSavedUserNum("svcAmount");
        String accountNum = "970-" + uniqueValue + "-001";
        accountService.createAccount(createAccount(userNum, "금액검증은행", accountNum));

        AccountVO savedAccount = findAccountByAccountNum(userNum, accountNum);
        assertNotNull(savedAccount, "금액 검증 테스트 계좌가 조회되어야 합니다.");

        logStep("WHEN", "0원 충전 시도");
        IllegalArgumentException zeroException = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.deposit(savedAccount.getAccountId(), userNum, BigDecimal.ZERO)
        );
        log("0원 예외 메시지", zeroException.getMessage());
        assertTrue(zeroException.getMessage().contains("금액은 0보다 커야 합니다."));

        logStep("WHEN", "음수 차감 시도");
        IllegalArgumentException negativeException = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.withdraw(savedAccount.getAccountId(), userNum, new BigDecimal("-1000"))
        );
        log("음수 예외 메시지", negativeException.getMessage());
        assertTrue(negativeException.getMessage().contains("금액은 0보다 커야 합니다."));

        logStep("WHEN", "소수 금액 충전 시도");
        IllegalArgumentException decimalException = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.deposit(savedAccount.getAccountId(), userNum, new BigDecimal("1000.5"))
        );
        log("소수 예외 메시지", decimalException.getMessage());
        assertTrue(decimalException.getMessage().contains("금액은 정수로 입력하세요."));

        logPass("충전/차감 금액 검증 확인 완료");
    }

    @Test
    @DisplayName("잔액 0원 계좌 삭제 가능")
    void deleteZeroBalanceAccountTest() {
        Long userNum = createSavedUserNum("svcDelete");
        String accountNum = "980-" + uniqueValue + "-001";
        accountService.createAccount(createAccount(userNum, "삭제서비스은행", accountNum));

        AccountVO savedAccount = findAccountByAccountNum(userNum, accountNum);
        assertNotNull(savedAccount, "삭제할 계좌가 조회되어야 합니다.");

        logStep("WHEN", "잔액 0원 계좌 삭제");
        int deleteCount = accountService.deleteAccount(savedAccount.getAccountId(), userNum);
        log("삭제 건수", deleteCount);

        assertEquals(1, deleteCount, "잔액 0원 계좌 삭제 결과는 1건이어야 합니다.");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.getAccount(savedAccount.getAccountId(), userNum)
        );

        log("삭제 후 조회 예외 메시지", exception.getMessage());
        assertTrue(exception.getMessage().contains("계좌 정보를 찾을 수 없습니다."));

        logPass("잔액 0원 계좌 삭제 확인 완료");
    }

    @Test
    @DisplayName("잔액이 남은 계좌 삭제 차단")
    void deleteAccountFailWhenBalanceRemainTest() {
        Long userNum = createSavedUserNum("svcDelMoney");
        String accountNum = "990-" + uniqueValue + "-001";
        accountService.createAccount(createAccount(userNum, "삭제차단은행", accountNum));

        AccountVO savedAccount = findAccountByAccountNum(userNum, accountNum);
        assertNotNull(savedAccount, "삭제 차단 테스트 계좌가 조회되어야 합니다.");
        accountService.deposit(savedAccount.getAccountId(), userNum, new BigDecimal("1000"));

        logStep("WHEN", "잔액이 남은 계좌 삭제 시도");
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> accountService.deleteAccount(savedAccount.getAccountId(), userNum)
        );

        log("발생한 예외 메시지", exception.getMessage());
        assertTrue(exception.getMessage().contains("잔액이 남아 있는 계좌"));

        AccountVO selectedAccount = accountService.getAccount(savedAccount.getAccountId(), userNum);
        logAccount("삭제 실패 후 계좌", selectedAccount);
        assertBigDecimalEquals(new BigDecimal("1000"), selectedAccount.getBalance(), "삭제 실패 후 잔액은 유지되어야 합니다.");

        logPass("잔액이 남은 계좌 삭제 차단 확인 완료");
    }

    private Long createSavedUserNum(String prefix) {
        String suffix = uniqueValue.substring(uniqueValue.length() - 6);
        String userId = prefix + suffix;

        UserVO user = new UserVO();
        user.setUserId(userId);
        user.setPassword("1234");
        user.setUserName("계좌서비스테스트회원");
        user.setNickname("계좌서비스테스트회원");
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

    private String makePhone(String prefix) {
        int hash = Math.floorMod(prefix.hashCode(), 10000);
        String middle = String.format("%04d", hash);
        String last = uniqueValue.substring(uniqueValue.length() - 4);

        return "010-" + middle + "-" + last;
    }

    private AccountVO findAccountByAccountNum(Long userNum, String accountNum) {
        List<AccountVO> accountList = accountService.getAccountList(userNum);

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
