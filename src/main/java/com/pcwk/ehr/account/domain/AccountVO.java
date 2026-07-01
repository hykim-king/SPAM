package com.pcwk.ehr.account.domain;

public class AccountVO {

	private int accountId;
	private int userNum;
	private String bankName;
	private String accountNum;
	private int balance;
	private String createDt;
	private String updateDt;
	    //.
	
	public AccountVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public AccountVO(int accountId, int userNum, String bankName, String accountNum, int balance, String createDt,
			String updateDt) {
		super();
		this.accountId = accountId;
		this.userNum = userNum;
		this.bankName = bankName;
		this.accountNum = accountNum;
		this.balance = balance;
		this.createDt = createDt;
		this.updateDt = updateDt;
	}
	
	public int getAccountId() {
		return accountId;
	}
	
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	
	public int getUserNum() {
		return userNum;
	}
	
	public void setUserNum(int userNum) {
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
	
	public int getBalance() {
		return balance;
	}
	
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	public String getCreateDt() {
		return createDt;
	}
	
	public void setCreateDt(String createDt) {
		this.createDt = createDt;
	}
	
	public String getUpdateDt() {
		return updateDt;
	}
	
	public void setUpdateDt(String updateDt) {
		this.updateDt = updateDt;
	}
	
	@Override
	public String toString() {
		return "AccountVO [accountId=" + accountId + ", userNum=" + userNum + ", bankName=" + bankName + ", accountNum="
				+ accountNum + ", balance=" + balance + ", createDt=" + createDt + ", updateDt=" + updateDt + "]";
	}
		
}
