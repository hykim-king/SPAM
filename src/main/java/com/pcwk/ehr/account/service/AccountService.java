package com.pcwk.ehr.account.service;

import java.math.BigDecimal;
import java.util.List;

import com.pcwk.ehr.account.domain.AccountVO;

public interface AccountService {

    /*
     * 계좌 등록
     * - 로그인 회원번호로 USER_NUM 세팅
     * - 은행명/계좌번호 필수값 검증
     * - 같은 회원의 은행명 + 계좌번호 중복 방지
     */
    int createAccount(AccountVO account);

    /*
     * 로그인 회원의 계좌 목록 조회
     */
    List<AccountVO> getAccountList(Long userNum);

    /*
     * 로그인 회원의 계좌 단건 조회
     */
    AccountVO getAccount(Long accountId, Long userNum);

    /*
     * 계좌 기본정보 수정
     * - 은행명/계좌번호만 수정
     */
    int updateAccount(AccountVO account);

    /*
     * 계좌 삭제
     * - 잔액이 남아 있으면 삭제하지 않음
     */
    int deleteAccount(Long accountId, Long userNum);

    /*
     * 충전/입금
     * - amount만큼 BALANCE 증가
     */
    int deposit(Long accountId, Long userNum, BigDecimal amount);

    /*
     * 출금/차감
     * - amount만큼 BALANCE 감소
     * - 잔액 부족 시 실패
     */
    int withdraw(Long accountId, Long userNum, BigDecimal amount);

    /*
     * 로그인 회원의 총 보유 자산 조회
     */
    BigDecimal getTotalBalance(Long userNum);
}
