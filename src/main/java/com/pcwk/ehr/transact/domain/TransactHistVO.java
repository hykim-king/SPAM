/**
 * 파일명: TransactHistVO.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-07 <br>
 * 설　명: <br>
 */
package com.pcwk.ehr.transact.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 거래내역 VO
 * 테이블: TRANSACTIN_HIST
 */

/**
 * implements Serializable란? 
 * 프로젝트의 다른 VO들과 마찬가지로 세션 저장이나 네트워크 전송 시 직렬화를 위해 필수적으로 구현했습니다.
 * 데이터 타입 선정:
		Long: DB의 NUMBER 타입은 Java에서 Long으로 매핑하는 것이 안전합니다.
		String: VARCHAR2 타입인 txType, txStatus는 String으로 매핑했습니다.
		Date: DB의 DATE 타입은 java.util.Date를 사용하여 날짜 정보를 처리하도록 했습니다.
 * 왜 이 구조인가요?:
		현재 프로젝트에서 이미 UserVO와 ReportVO가 이 표준(필드 캡슐화, 직렬화)을 따르고 있습니다. 
		동일한 패턴을 유지함으로써 향후 TransactHistMapper.xml에서 resultMap을 구성하거나 유지보수할 때 
		코드 가독성과 통일성을 극대화할 수 있습니다.
 */
public class TransactHistVO implements Serializable {
	
    private static final long serialVersionUID = 1L;

    private Long txId;          // 거래내역ID (PK)
    private Long sellerNo;      // 보내는회원번호
    private Long productNo;     // 상품번호
    private Long receiverNo;    // 받는회원번호
    private String txType;      // 거래유형
    private Long amount;        // 거래금액
    private String txStatus;    // 거래상태
    private Date createDt;      // 거래생성일
    private Date completeDt;    // 완료일
    private Date cancelDt;      // 취소일
	
    public TransactHistVO() {
		super();
	}

	public Long getTxId() {
		return txId;
	}

	public void setTxId(Long txId) {
		this.txId = txId;
	}

	public Long getSellerNo() {
		return sellerNo;
	}

	public void setSellerNo(Long sellerNo) {
		this.sellerNo = sellerNo;
	}

	public Long getProductNo() {
		return productNo;
	}

	public void setProductNo(Long productNo) {
		this.productNo = productNo;
	}

	public Long getReceiverNo() {
		return receiverNo;
	}

	public void setReceiverNo(Long receiverNo) {
		this.receiverNo = receiverNo;
	}

	public String getTxType() {
		return txType;
	}

	public void setTxType(String txType) {
		this.txType = txType;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getTxStatus() {
		return txStatus;
	}

	public void setTxStatus(String txStatus) {
		this.txStatus = txStatus;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public Date getCompleteDt() {
		return completeDt;
	}

	public void setCompleteDt(Date completeDt) {
		this.completeDt = completeDt;
	}

	public Date getCancelDt() {
		return cancelDt;
	}

	public void setCancelDt(Date cancelDt) {
		this.cancelDt = cancelDt;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "TransactHistVO [txId=" + txId + ", sellerNo=" + sellerNo + ", productNo=" + productNo + ", receiverNo="
				+ receiverNo + ", txType=" + txType + ", amount=" + amount + ", txStatus=" + txStatus + ", createDt="
				+ createDt + ", completeDt=" + completeDt + ", cancelDt=" + cancelDt + "]";
	}
}