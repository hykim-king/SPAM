package com.pcwk.ehr.product.domain;

import com.pcwk.ehr.cmn.DTO;

public class ProductVO extends DTO {
	// 카멜케이스
	// ctrl+shift+y -> 소문자
	// ctrl+shift+x : 대문자
	private int productNo;        // 상품번호
	private int userNum;          // 판매자회원번호
	private int categoryNo;       // 카테고리번호

	private String productTitle;  // 상품명
	private String productContent;// 상품내용
	private String productCondition; // 상품상태

	private int price;            // 가격
	private String location;      // 거래지역
	private String status;        // 거래상태

	private String adminHideYn;   // 관리자숨김여부
	private String hideReason;    // 숨김사유
	private int viewCount;        // 조회수

	private String createDt;      // 등록일
	private String modifyDt;      // 수정일

	private int likeCnt;          // 찜수
	private int chatCnt;          // 채팅수

	// 전역변수
	// Default 생성자 : alt+shift+s
	// 인자있는 생성자
	// getters/setters
	// toString()

	public ProductVO() {
		super();
	}

	public ProductVO(int productNo, int userNum, int categoryNo, String productTitle, String productContent,
			String productCondition, int price, String location, String status, String adminHideYn, String hideReason,
			int viewCount, String createDt, String modifyDt, int likeCnt, int chatCnt) {
		super();
		this.productNo = productNo;
		this.userNum = userNum;
		this.categoryNo = categoryNo;
		this.productTitle = productTitle;
		this.productContent = productContent;
		this.productCondition = productCondition;
		this.price = price;
		this.location = location;
		this.status = status;
		this.adminHideYn = adminHideYn;
		this.hideReason = hideReason;
		this.viewCount = viewCount;
		this.createDt = createDt;
		this.modifyDt = modifyDt;
		this.likeCnt = likeCnt;
		this.chatCnt = chatCnt;
	}

	// [비즈니스 로직] 상품 상태 변경 (예시)
	public void changeStatus(String newStatus) {
		if (null == newStatus || newStatus.isEmpty()) {
			throw new IllegalArgumentException("올바르지 않은 상태 값입니다.");
		}
		this.status = newStatus;
	}

	public int getProductNo() {
		return productNo;
	}

	public void setProductNo(int productNo) {
		this.productNo = productNo;
	}

	public int getUserNum() {
		return userNum;
	}

	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}

	public int getCategoryNo() {
		return categoryNo;
	}

	public void setCategoryNo(int categoryNo) {
		this.categoryNo = categoryNo;
	}

	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	public String getProductContent() {
		return productContent;
	}

	public void setProductContent(String productContent) {
		this.productContent = productContent;
	}

	public String getProductCondition() {
		return productCondition;
	}

	public void setProductCondition(String productCondition) {
		this.productCondition = productCondition;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAdminHideYn() {
		return adminHideYn;
	}

	public void setAdminHideYn(String adminHideYn) {
		this.adminHideYn = adminHideYn;
	}

	public String getHideReason() {
		return hideReason;
	}

	public void setHideReason(String hideReason) {
		this.hideReason = hideReason;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public String getCreateDt() {
		return createDt;
	}

	public void setCreateDt(String createDt) {
		this.createDt = createDt;
	}

	public String getModifyDt() {
		return modifyDt;
	}

	public void setModifyDt(String modifyDt) {
		this.modifyDt = modifyDt;
	}

	public int getLikeCnt() {
		return likeCnt;
	}

	public void setLikeCnt(int likeCnt) {
		this.likeCnt = likeCnt;
	}

	public int getChatCnt() {
		return chatCnt;
	}

	public void setChatCnt(int chatCnt) {
		this.chatCnt = chatCnt;
	}

	@Override
	public String toString() {
		return "ProductVO [productNo=" + productNo + ", userNum=" + userNum + ", categoryNo=" + categoryNo
				+ ", productTitle=" + productTitle + ", productContent=" + productContent + ", productCondition="
				+ productCondition + ", price=" + price + ", location=" + location + ", status=" + status
				+ ", adminHideYn=" + adminHideYn + ", hideReason=" + hideReason + ", viewCount=" + viewCount
				+ ", createDt=" + createDt + ", modifyDt=" + modifyDt + ", likeCnt=" + likeCnt + ", chatCnt=" + chatCnt
				+ ", toString()=" + super.toString() + "]";
	}
}