package com.pcwk.ehr.account.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pcwk.ehr.account.domain.AccountVO;
import com.pcwk.ehr.account.mapper.AccountMapper;

/**
 * 계좌/자산 관리 Service
 *
 * 담당 핵심 정책:
 * 1. 계좌는 로그인한 회원 본인의 계좌만 조회/수정/삭제할 수 있습니다.
 * 2. 같은 회원 안에서는 은행명 + 계좌번호 조합이 중복될 수 없습니다.
 * 3. 잔액은 0원 미만이 될 수 없습니다.
 * 4. 잔액이 남아 있는 계좌는 삭제하지 않습니다.
 * 5. 잔액 변경은 계좌 기본정보 수정과 분리해 deposit/withdraw에서만 처리합니다.
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    private static final BigDecimal ZERO = BigDecimal.ZERO;

    @Autowired
    private AccountMapper accountMapper;

    /**
     * 계좌 등록 처리
     */
    @Override
    @Transactional
    public int createAccount(AccountVO account) {

        normalizeAccountInput(account);
        validateAccountForSave(account, true);

        // 화면에서 balance가 넘어오더라도 신규 계좌의 시작 잔액은 0원으로 고정
        account.setBalance(ZERO);

        if (accountMapper.countDuplicate(account) > 0) {
            throw new IllegalArgumentException("이미 등록된 계좌입니다.");
        }

        return accountMapper.insertAccount(account);
    }

    /**
     * 로그인 회원의 계좌 목록 조회
     */
    @Override
    public List<AccountVO> getAccountList(Long userNum) {
        validateUserNum(userNum);

        return accountMapper.selectListByUserNum(userNum);
    }

    /**
     * 로그인 회원의 계좌 단건 조회
     */
    @Override
    public AccountVO getAccount(Long accountId, Long userNum) {
        validateAccountId(accountId);
        validateUserNum(userNum);

        AccountVO account = accountMapper.selectByAccountIdAndUserNum(accountId, userNum);

        if (account == null) {
            throw new IllegalArgumentException("계좌 정보를 찾을 수 없습니다.");
        }

        return account;
    }

    /**
     * 계좌 기본정보 수정
     */
    @Override
    @Transactional
    public int updateAccount(AccountVO account) {

        normalizeAccountInput(account);
        validateAccountForSave(account, false);

        // 본인 계좌인지 먼저 확인
        getAccount(account.getAccountId(), account.getUserNum());

        if (accountMapper.countDuplicate(account) > 0) {
            throw new IllegalArgumentException("이미 등록된 계좌입니다.");
        }

        int result = accountMapper.updateAccount(account);

        if (result != 1) {
            throw new IllegalStateException("계좌정보 수정에 실패했습니다.");
        }

        return result;
    }

    /**
     * 계좌 삭제
     */
    @Override
    @Transactional
    public int deleteAccount(Long accountId, Long userNum) {
        validateAccountId(accountId);
        validateUserNum(userNum);

        // 본인 계좌인지 먼저 확인
        AccountVO account = getAccount(accountId, userNum);

        if (account.getBalance() != null && account.getBalance().compareTo(ZERO) > 0) {
            throw new IllegalStateException("잔액이 남아 있는 계좌는 삭제할 수 없습니다.");
        }

        int result = accountMapper.deleteAccount(accountId, userNum);

        if (result != 1) {
            throw new IllegalStateException("계좌 삭제에 실패했습니다.");
        }

        return result;
    }

    /**
     * 충전/입금
     */
    @Override
    @Transactional
    public int deposit(Long accountId, Long userNum, BigDecimal amount) {
        validateAmount(amount);

        // 본인 계좌인지 먼저 확인
        getAccount(accountId, userNum);

        int result = accountMapper.increaseBalance(accountId, userNum, amount);

        if (result != 1) {
            throw new IllegalStateException("잔액 충전에 실패했습니다.");
        }

        /*
         * 거래내역 담당 모듈이 합쳐지면 이 위치에서 TRANSACTION_HISTORY INSERT를 호출하면 됩니다.
         * 예: TX_TYPE='CHARGE', RECEIVER_NO=userNum, AMOUNT=amount, STATUS='COMPLETE'
         */
        return result;
    }

    /**
     * 출금/차감
     */
    @Override
    @Transactional
    public int withdraw(Long accountId, Long userNum, BigDecimal amount) {
        validateAmount(amount);

        // 본인 계좌인지 먼저 확인
        getAccount(accountId, userNum);

        int result = accountMapper.decreaseBalance(accountId, userNum, amount);

        if (result != 1) {
            throw new IllegalStateException("잔액이 부족합니다.");
        }

        /*
         * 거래내역 담당 모듈이 합쳐지면 이 위치에서 TRANSACTION_HISTORY INSERT를 호출하면 됩니다.
         * 예: TX_TYPE='WITHDRAW', SENDER_NO=userNum, AMOUNT=amount, STATUS='COMPLETE'
         */
        return result;
    }

    /**
     * 총 보유 자산 조회
     */
    @Override
    public BigDecimal getTotalBalance(Long userNum) {
        validateUserNum(userNum);

        BigDecimal totalBalance = accountMapper.selectTotalBalanceByUserNum(userNum);

        return totalBalance == null ? ZERO : totalBalance;
    }

    // 계좌 저장 시 필수값 검증
    private void validateAccountForSave(AccountVO account, boolean isCreate) {

        if (account == null) {
            throw new IllegalArgumentException("계좌정보가 없습니다.");
        }

        validateUserNum(account.getUserNum());

        if (!isCreate) {
            validateAccountId(account.getAccountId());
        }

        if (isBlank(account.getBankName())) {
            throw new IllegalArgumentException("은행명은 필수입니다.");
        }

        if (account.getBankName().length() > 20) {
            throw new IllegalArgumentException("은행명은 20자 이하로 입력하세요.");
        }

        if (isBlank(account.getAccountNum())) {
            throw new IllegalArgumentException("계좌번호는 필수입니다.");
        }

        if (account.getAccountNum().length() > 30) {
            throw new IllegalArgumentException("계좌번호는 30자 이하로 입력하세요.");
        }
    }

    // 회원번호 검증
    private void validateUserNum(Long userNum) {
        if (userNum == null) {
            throw new IllegalArgumentException("회원번호가 없습니다.");
        }
    }

    // 계좌고유번호 검증
    private void validateAccountId(Long accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("계좌고유번호가 없습니다.");
        }
    }

    // 금액 검증
    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(ZERO) <= 0) {
            throw new IllegalArgumentException("금액은 0보다 커야 합니다.");
        }

        if (amount.scale() > 0) {
            throw new IllegalArgumentException("금액은 정수로 입력하세요.");
        }
    }

    // 사용자 입력값 앞뒤 공백 정리
    private void normalizeAccountInput(AccountVO account) {
        if (account == null) {
            return;
        }

        account.setBankName(trimToNull(account.getBankName()));
        account.setAccountNum(trimToNull(account.getAccountNum()));
    }

    // 문자열 앞뒤 공백 제거 후 빈 문자열이면 null로 변경
    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();

        return trimmed.isEmpty() ? null : trimmed;
    }

    // 문자열이 null이거나 공백인지 확인
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
