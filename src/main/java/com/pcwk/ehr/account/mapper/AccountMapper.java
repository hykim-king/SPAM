package com.pcwk.ehr.account.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pcwk.ehr.account.domain.AccountVO;

public interface AccountMapper {

    /*
     * 계좌 등록
     * - ACCOUNT_ID는 XML에서 SEQ_USER_ACCOUNT.NEXTVAL로 생성
     * - BALANCE는 기본 0원으로 시작
     */
    int insertAccount(AccountVO account);

    /*
     * 로그인 회원의 계좌 목록 조회
     */
    List<AccountVO> selectListByUserNum(@Param("userNum") Long userNum);

    /*
     * 계좌고유번호 기준 단건 조회
     * - 반드시 USER_NUM 조건을 함께 걸어 본인 계좌만 조회되도록 함
     */
    AccountVO selectByAccountIdAndUserNum(@Param("accountId") Long accountId,
                                          @Param("userNum") Long userNum);

    /*
     * 같은 회원 안에서 같은 은행명 + 계좌번호가 이미 등록되어 있는지 확인
     * - 수정 시에는 accountId가 들어오므로 자기 자신은 제외
     */
    int countDuplicate(AccountVO account);

    /*
     * 계좌 기본정보 수정
     * - 은행명, 계좌번호만 수정
     * - 잔액은 충전/차감 메서드에서만 변경
     */
    int updateAccount(AccountVO account);

    /*
     * 계좌 삭제
     * - USER_NUM 조건을 함께 걸어 본인 계좌만 삭제
     */
    int deleteAccount(@Param("accountId") Long accountId,
                      @Param("userNum") Long userNum);

    /*
     * 잔액 증가
     * - 충전/입금 처리에서 사용
     */
    int increaseBalance(@Param("accountId") Long accountId,
                        @Param("userNum") Long userNum,
                        @Param("amount") BigDecimal amount);

    /*
     * 잔액 감소
     * - 결제/출금 처리에서 사용
     * - SQL에서 BALANCE >= amount 조건을 걸어 음수 잔액을 막음
     */
    int decreaseBalance(@Param("accountId") Long accountId,
                        @Param("userNum") Long userNum,
                        @Param("amount") BigDecimal amount);

    /*
     * 로그인 회원의 총 보유 자산 조회
     */
    BigDecimal selectTotalBalanceByUserNum(@Param("userNum") Long userNum);
}
