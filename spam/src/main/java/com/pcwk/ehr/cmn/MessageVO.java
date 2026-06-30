package com.pcwk.ehr.cmn;

public class MessageVO {
	
	private String id;
	private String message;
	public MessageVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MessageVO(String id, String message) {
		super();
		this.id = id;
		this.message = message;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "MessageVO [id=" + id + ", message=" + message + "]";
	}
	
	

}
