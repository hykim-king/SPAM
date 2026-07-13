package com.pcwk.ehr.chat.domain;

public class ChatRoomVO {

	    /** 채팅방 번호(PK) */
	    private int chatRoomNo;

	    /** 상품 번호 */
	    private int productNo;

	    /** 판매자 번호 */
	    private Long sellerNo;

	    /** 구매자 번호 */
	    private Long buyerNo;

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

	    /** 판매자 나간 시각 */
	    private String sellerExitDt;

	    /** 구매자 나간 시각 */
	    private String buyerExitDt;

	    /** 상품 이름 */
	    private String productTitle;

	    /** 거래 지역 */
	    private String location;

	    /** 상대방 닉네임 */
	    private String opponentNick;


	    public ChatRoomVO() {

	    }


	    public ChatRoomVO(int chatRoomNo, int productNo, Long sellerNo, Long buyerNo, String roomStatus,
				String sellerExitYn, String buyerExitYn, String createDt, String lastMsgDt, String sellerExitDt,
				String buyerExitDt) {
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
			this.sellerExitDt = sellerExitDt;
			this.buyerExitDt = buyerExitDt;
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

	    public Long getSellerNo() {
	        return sellerNo;
	    }

	    public void setSellerNo(Long sellerNo) {
	        this.sellerNo = sellerNo;
	    }

	    public Long getBuyerNo() {
	        return buyerNo;
	    }

	    public void setBuyerNo(Long buyerNo) {
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


	    public String getProductStatus() {
			return productStatus;
		}

		public void setProductStatus(String productStatus) {
			this.productStatus = productStatus;
		}


		public String getSellerExitDt() {
			return sellerExitDt;
		}

		public void setSellerExitDt(String sellerExitDt) {
			this.sellerExitDt = sellerExitDt;
		}


		public String getBuyerExitDt() {
			return buyerExitDt;
		}

		public void setBuyerExitDt(String buyerExitDt) {
			this.buyerExitDt = buyerExitDt;
		}


		public String getProductTitle() {
			return productTitle;
		}

		public void setProductTitle(String productTitle) {
			this.productTitle = productTitle;
		}


		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}


		public String getOpponentNick() {
			return opponentNick;
		}

		public void setOpponentNick(String opponentNick) {
			this.opponentNick = opponentNick;
		}


		@Override
		public String toString() {
			return "ChatRoomVO [chatRoomNo=" + chatRoomNo + ", productNo=" + productNo + ", sellerNo=" + sellerNo
					+ ", buyerNo=" + buyerNo + ", roomStatus=" + roomStatus + ", sellerExitYn=" + sellerExitYn
					+ ", buyerExitYn=" + buyerExitYn + ", createDt=" + createDt + ", lastMsgDt=" + lastMsgDt
					+ ", productStatus=" + productStatus + ", sellerExitDt=" + sellerExitDt + ", buyerExitDt="
					+ buyerExitDt + ", productTitle=" + productTitle + ", location=" + location + ", opponentNick="
					+ opponentNick + "]";
		}

	}