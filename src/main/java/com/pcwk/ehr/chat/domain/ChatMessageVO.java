package com.pcwk.ehr.chat.domain;

public class ChatMessageVO {

    /** 채팅 메시지 번호(PK) */
    private int chatMsgNo;

    /** 채팅방 번호(FK) */
    private int chatRoomNo;

    /** 보낸 회원 번호(FK) */
    private Long senderNo;

    /** 채팅 내용 */
    private String chatContent;

    /** 읽음 여부(Y/N) */
    private String readYn;

    /** 메시지 전송일 */
    private String sendDt;

    public ChatMessageVO() {
    	
    }
    
    

    public ChatMessageVO(int chatMsgNo, int chatRoomNo, Long senderNo, String chatContent, String readYn,
			String sendDt) {
		super();
		this.chatMsgNo = chatMsgNo;
		this.chatRoomNo = chatRoomNo;
		this.senderNo = senderNo;
		this.chatContent = chatContent;
		this.readYn = readYn;
		this.sendDt = sendDt;
	}



	public int getChatMsgNo() {
        return chatMsgNo;
    }

    public void setChatMsgNo(int chatMsgNo) {
        this.chatMsgNo = chatMsgNo;
    }

    public int getChatRoomNo() {
        return chatRoomNo;
    }

    public void setChatRoomNo(int chatRoomNo) {
        this.chatRoomNo = chatRoomNo;
    }

    public Long getSenderNo() {
        return senderNo;
    }

    public void setSenderNo(Long senderNo) {
        this.senderNo = senderNo;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    public String getReadYn() {
        return readYn;
    }

    public void setReadYn(String readYn) {
        this.readYn = readYn;
    }

    public String getSendDt() {
        return sendDt;
    }

    public void setSendDt(String sendDt) {
        this.sendDt = sendDt;
    }

    @Override
    public String toString() {
        return "ChatMessageVO [chatMsgNo=" + chatMsgNo
                + ", chatRoomNo=" + chatRoomNo
                + ", senderNo=" + senderNo
                + ", chatContent=" + chatContent
                + ", readYn=" + readYn
                + ", sendDt=" + sendDt + "]";
    }
}