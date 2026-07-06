package com.pcwk.ehr.account.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AccountVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
     * ACCOUNT_ID
     * - 계좌/자산 정보를 구분하는 내부 고유번호
     * - DB에서는 PK이며 SEQ_USER_ACCOUNT.NEXTVAL로 생성
     */
    private Long accountId;

    /*
     * USER_NUM
     * - USER_INFO.USER_NUM을 참조하는 회원번호
     * - 로그인한 회원의 계좌만 조회/수정/삭제할 수 있도록 모든 주요 조회 조건에 포함
     */
    private Long userNum;

    /*
     * BANK_NAME
     * - 은행명
     * - USER_ACCOUNT.BANK_NAME NVARCHAR2(20)
     */
    private String bankName;

    /*
     * ACCOUNT_NUM
     * - 계좌번호
     * - USER_ACCOUNT.ACCOUNT_NUM VARCHAR2(30)
     */
    private String accountNum;

    /*
     * BALANCE
     * - 잔액
     * - NUMBER(12), 0 이상만 허용
     * - 금액 계산이므로 double/float 대신 BigDecimal 사용
     */
    private BigDecimal balance;

    /*
     * CREATE_DT
     * - DB INSERT 시 SYSDATE로 저장
     */
    private Date createDt;

    /*
     * UPDATE_DT
     * - 계좌정보 수정 또는 잔액 변경 시 SYSDATE로 갱신
     */
    private Date updateDt;

    public AccountVO() {
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getUserNum() {
        return userNum;
    }

    public void setUserNum(Long userNum) {
        this.userNum = userNum;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    @Override
    public String toString() {
        return "AccountVO{" +
                "accountId=" + accountId +
                ", userNum=" + userNum +
                ", bankName='" + bankName + "'" +
                ", accountNum='" + accountNum + "'" +
                ", balance=" + balance +
                ", createDt=" + createDt +
                ", updateDt=" + updateDt +
                '}';
    }
}
