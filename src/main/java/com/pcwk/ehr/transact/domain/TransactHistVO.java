package com.pcwk.ehr.transact.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 거래내역 VO
 * 테이블: TRANSACTIN_HIST
 */
public class TransactHistVO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long txId;          // 거래내역ID (PK)
    private Long sellerNo;      // 판매자회원번호
    private Long productNo;     // 상품번호
    private Long receiverNo;    // 구매자회원번호
    private String txType;      // 거래유형 (01:판매, 02:구매)
    private Long amount;        // 거래금액
    private String txStatus;    // 거래상태 (01:거래중, 02:완료, 03:취소)
    private Date createDt;      // 거래생성일
    private Date completeDt;    // 완료일
    private Date cancelDt;      // 취소일
    
    // 조인용 필드 (화면 표시용)
    private String productName; 
    private String partnerName; 

    public TransactHistVO() {
        super();
    }

    // --- Getter / Setter ---
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

    @Override
    public String toString() {
        return "TransactHistVO [txId=" + txId + ", sellerNo=" + sellerNo + ", productNo=" + productNo 
                + ", receiverNo=" + receiverNo + ", txType=" + txType + ", amount=" + amount 
                + ", txStatus=" + txStatus + "]";
    }
}