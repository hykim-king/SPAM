package com.pcwk.ehr.product.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int productNo;
    private Long userNum;
    private int categoryNo;
    private String productTitle;
    private String productContent;
    private String productCondition;
    private long price;
    private String location;
    private String status;
    private String adminHideYn;
    private String hideReason;
    private String userId; //추가
    private int viewCount;
    private String createDt;
    private String modifyDt;
    private int likeCnt;
    private int chatCnt;

    /** 목록용 대표 이미지 경로. PRODUCT_IMAGE 조인으로 조회한다. */
    private String thumbnailPath;
    /** 상세용 전체 이미지 목록. */
    private List<ProductImageVO> imageList = new ArrayList<>();

    /** 상세 화면 카테고리 경로 표시용 필드. */
    private String largeName;
    private String middleName;
    private String smallName;

    public ProductVO() {
    }

    public int getProductNo() {
        return productNo;
    }

    public void setProductNo(int productNo) {
        this.productNo = productNo;
    }

    public Long getUserNum() {
        return userNum;
    }

    public void setUserNum(Long userNum) {
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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
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
    
    public String getuserId() { 
    	return userId; 
    }//추가
   
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
    
    public void setuserId(String userId) { 
    	this.userId = userId; 
    }//추가
    
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

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public List<ProductImageVO> getImageList() {
        return imageList;
    }

    public void setImageList(List<ProductImageVO> imageList) {
        this.imageList = imageList;
    }

    public String getLargeName() {
        return largeName;
    }

    public void setLargeName(String largeName) {
        this.largeName = largeName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSmallName() {
        return smallName;
    }

    public void setSmallName(String smallName) {
        this.smallName = smallName;
    }

	@Override
	public String toString() {
		return "ProductVO [productNo=" + productNo + ", userNum=" + userNum + ", categoryNo=" + categoryNo
				+ ", productTitle=" + productTitle + ", productContent=" + productContent + ", productCondition="
				+ productCondition + ", price=" + price + ", location=" + location + ", status=" + status
				+ ", adminHideYn=" + adminHideYn + ", hideReason=" + hideReason + ", userId=" + userId + ", viewCount="
				+ viewCount + ", createDt=" + createDt + ", modifyDt=" + modifyDt + ", likeCnt=" + likeCnt
				+ ", chatCnt=" + chatCnt + ", thumbnailPath=" + thumbnailPath + ", imageList=" + imageList
				+ ", largeName=" + largeName + ", middleName=" + middleName + ", smallName=" + smallName + "]";
	}

    
}
