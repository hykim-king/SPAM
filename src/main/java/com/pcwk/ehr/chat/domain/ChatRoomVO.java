package com.pcwk.ehr.chat.domain;

public class ChatRoomVO {
	
	    /** 채팅방 번호(PK) */
	    private int chatRoomNo;

	    /** 상품 번호(FK) */
	    private int productNo;

	    /** 판매자 번호(FK) */
	    private int sellerNo;

	    /** 구매자 번호(FK) */
	    private int buyerNo;

	    /** 채팅방 상태 */
	    private String roomStatus;

	    /** 판매자 채팅방 나가기 여부(Y/N) */
	    private String sellerExitYn;

	    /** 구매자 채팅방 나가기 여부(Y/N) */
	    private String buyerExitYn;

	    /** 채팅방 생성일 */
	    private String createDt;

	    /** 마지막 메시지 전송일 */
	    private String lastMsgDt;
	    
	    /** 상품 거래 상태 */
	    private String productStatus;

	    public ChatRoomVO() {
	    	
	    }
	    
	    

	    public ChatRoomVO(int chatRoomNo, int productNo, int sellerNo, int buyerNo, String roomStatus,
				String sellerExitYn, String buyerExitYn, String createDt, String lastMsgDt) {
			super();
			this.chatRoomNo = chatRoomNo;
			this.productNo = productNo;
			this.sellerNo = sellerNo;
			this.buyerNo = buyerNo;
			this.roomStatus = roomStatus;
			this.sellerExitYn = sellerExitYn;
			this.buyerExitYn = buyerExitYn;
			this.createDt = createDt;
			this.lastMsgDt = lastMsgDt;
		}



		public int getChatRoomNo() {
	        return chatRoomNo;
	    }

	    public void setChatRoomNo(int chatRoomNo) {
	        this.chatRoomNo = chatRoomNo;
	    }

	    public int getProductNo() {
	        return productNo;
	    }

	    public void setProductNo(int productNo) {
	        this.productNo = productNo;
	    }

	    public int getSellerNo() {
	        return sellerNo;
	    }

	    public void setSellerNo(int sellerNo) {
	        this.sellerNo = sellerNo;
	    }

	    public int getBuyerNo() {
	        return buyerNo;
	    }

	    public void setBuyerNo(int buyerNo) {
	        this.buyerNo = buyerNo;
	    }

	    public String getRoomStatus() {
	        return roomStatus;
	    }

	    public void setRoomStatus(String roomStatus) {
	        this.roomStatus = roomStatus;
	    }

	    public String getSellerExitYn() {
	        return sellerExitYn;
	    }

	    public void setSellerExitYn(String sellerExitYn) {
	        this.sellerExitYn = sellerExitYn;
	    }

	    public String getBuyerExitYn() {
	        return buyerExitYn;
	    }

	    public void setBuyerExitYn(String buyerExitYn) {
	        this.buyerExitYn = buyerExitYn;
	    }

	    public String getCreateDt() {
	        return createDt;
	    }

	    public void setCreateDt(String createDt) {
	        this.createDt = createDt;
	    }

	    public String getLastMsgDt() {
	        return lastMsgDt;
	    }

	    public void setLastMsgDt(String lastMsgDt) {
	        this.lastMsgDt = lastMsgDt;
	    }

	    @Override
	    public String toString() {
	        return "ChatRoomVO [chatRoomNo=" + chatRoomNo
	                + ", productNo=" + productNo
	                + ", sellerNo=" + sellerNo
	                + ", buyerNo=" + buyerNo
	                + ", roomStatus=" + roomStatus
	                + ", sellerExitYn=" + sellerExitYn
	                + ", buyerExitYn=" + buyerExitYn
	                + ", createDt=" + createDt
	                + ", lastMsgDt=" + lastMsgDt + "]";
	    }
	}
