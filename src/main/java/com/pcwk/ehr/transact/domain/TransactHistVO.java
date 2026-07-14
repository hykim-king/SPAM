package com.pcwk.ehr.transact.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 거래내역 VO (Value Object)
 * TRANSACTIN_HIST 테이블의 컬럼과 1:1로 대응되는 데이터 모델입니다.
 */
public class TransactHistVO implements Serializable {
    
    // 객체 직렬화 시 버전을 확인하기 위한 고유 ID
    private static final long serialVersionUID = 1L;

    // 테이블 컬럼과 매핑되는 필드들
    private Long txId;          // 거래내역ID (Primary Key)
    private Long sellerNo;      // 판매자회원번호
    private Long productNo;     // 상품번호
    private Long receiverNo;    // 구매자회원번호
    private String txType;      // 거래유형 (01:판매, 02:구매)
    private Long amount;        // 거래금액
    private String txStatus;    // 거래상태 (01:거래중, 02:완료, 03:취소)
    private Date createDt;      // 거래생성일
    private Date completeDt;    // 완료일
    private Date cancelDt;      // 취소일
    
    // 조인 등으로 가져온 추가 데이터 필드
    private String userId;      // 판매자/구매자 아이디
    private String productName; // 상품명 (조인용 필드)
    private String partnerName; // 상대방 이름 (조인용 필드)

    // 기본 생성자
    public TransactHistVO() {
        super();
    }

    // --- Getter / Setter (데이터를 가져오거나 설정하는 메서드) ---
    public Long getTxId() { return txId; }
    public void setTxId(Long txId) { this.txId = txId; }

    public Long getSellerNo() { return sellerNo; }
    public void setSellerNo(Long sellerNo) { this.sellerNo = sellerNo; }

    public Long getProductNo() { return productNo; }
    public void setProductNo(Long productNo) { this.productNo = productNo; }

    public Long getReceiverNo() { return receiverNo; }
    public void setReceiverNo(Long receiverNo) { this.receiverNo = receiverNo; }

    public String getTxType() { return txType; }
    public void setTxType(String txType) { this.txType = txType; }

    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }

    public String getTxStatus() { return txStatus; }
    public void setTxStatus(String txStatus) { this.txStatus = txStatus; }

    public Date getCreateDt() { return createDt; }
    public void setCreateDt(Date createDt) { this.createDt = createDt; }

    public Date getCompleteDt() { return completeDt; }
    public void setCompleteDt(Date completeDt) { this.completeDt = completeDt; }

    public Date getCancelDt() { return cancelDt; }
    public void setCancelDt(Date cancelDt) { this.cancelDt = cancelDt; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getPartnerName() { return partnerName; }
    public void setPartnerName(String partnerName) { this.partnerName = partnerName; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    // 객체의 상태를 문자열로 쉽게 확인하기 위한 toString 메서드
	@Override
	public String toString() {
		return "TransactHistVO [txId=" + txId + ", sellerNo=" + sellerNo + ", productNo=" + productNo + ", receiverNo="
				+ receiverNo + ", txType=" + txType + ", amount=" + amount + ", txStatus=" + txStatus + ", createDt="
				+ createDt + ", completeDt=" + completeDt + ", cancelDt=" + cancelDt + ", userId=" + userId
				+ ", productName=" + productName + ", partnerName=" + partnerName + "]";
	}
}