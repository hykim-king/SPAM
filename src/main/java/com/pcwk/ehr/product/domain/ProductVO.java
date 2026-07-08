package com.pcwk.ehr.product.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int productNo;          // PRODUCT_NO (PK, SEQUENCE)
    private int sallerNo;            // USER_NUM (FK)
    private int categoryNo;         // CATEGORY_NO (FK)
    private String productTitle;    // PRODUCT_TITLE (NVARCHAR2)
    private String productContent;  // PRODUCT_CONTENT (NVARCHAR2)
    private String productCondition;// PRODUCT_CONDITION (VARCHAR2, NULL 허용)
    private long price;             // PRICE (NUMBER)
    private String location;        // LOCATION (NVARCHAR2, NULL 허용)
    private String status;          // STATUS (VARCHAR2, DEFAULT 'SALE')
    private String adminHideYn;     // ADMIN_HIDE_YN (CHAR(1), DEFAULT 'N')
    private String hideReason;      // HIDE_REASON (NVARCHAR2, NULL 허용)
    private int viewCount;          // VIEW_COUNT (NUMBER, DEFAULT 0)
    private String createDt;        // CREATE_DT (DATE)
    private String modifyDt;        // MODIFY_DT (DATE)
    private int likeCnt;            // LIKE_CNT (NUMBER, DEFAULT 0)
    private int chatCnt;            // CHAT_CNT (NUMBER, DEFAULT 0)
    
    private List<ProductImageVO> imageList = new ArrayList<>();
    
 // 카테고리 경로 표시용 (조인으로 채움, 테이블 컬럼 아님)
    private String largeName;   // 대분류명
    private String middleName;  // 중분류명
    private String smallName;   // 소분류명

    public ProductVO() {}

    // Getters and Setters
    public int getProductNo() { return productNo; }
    public void setProductNo(int productNo) { this.productNo = productNo; }

    public int getSallerNo() { return sallerNo; }
    public void setSallerNo(int sallerNo) { this.sallerNo = sallerNo; }

    public int getCategoryNo() { return categoryNo; }
    public void setCategoryNo(int categoryNo) { this.categoryNo = categoryNo; }

    public String getProductTitle() { return productTitle; }
    public void setProductTitle(String productTitle) { this.productTitle = productTitle; }

    public String getProductContent() { return productContent; }
    public void setProductContent(String productContent) { this.productContent = productContent; }

    public String getProductCondition() { return productCondition; }
    public void setProductCondition(String productCondition) { this.productCondition = productCondition; }

    public long getPrice() { return price; }
    public void setPrice(long price) { this.price = price; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAdminHideYn() { return adminHideYn; }
    public void setAdminHideYn(String adminHideYn) { this.adminHideYn = adminHideYn; }

    public String getHideReason() { return hideReason; }
    public void setHideReason(String hideReason) { this.hideReason = hideReason; }

    public int getViewCount() { return viewCount; }
    public void setViewCount(int viewCount) { this.viewCount = viewCount; }

    public String getCreateDt() { return createDt; }
    public void setCreateDt(String createDt) { this.createDt = createDt; }

    public String getModifyDt() { return modifyDt; }
    public void setModifyDt(String modifyDt) { this.modifyDt = modifyDt; }

    public int getLikeCnt() { return likeCnt; }
    public void setLikeCnt(int likeCnt) { this.likeCnt = likeCnt; }

    public int getChatCnt() { return chatCnt; }
    public void setChatCnt(int chatCnt) { this.chatCnt = chatCnt; }
    
    

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
	

	public List<ProductImageVO> getImageList() {
		return imageList;
	}

	public void setImageList(List<ProductImageVO> imageList) {
		this.imageList = imageList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "ProductVO [productNo=" + productNo + ", sallerNo=" + sallerNo + ", categoryNo=" + categoryNo
				+ ", productTitle=" + productTitle + ", productContent=" + productContent + ", productCondition="
				+ productCondition + ", price=" + price + ", location=" + location + ", status=" + status
				+ ", adminHideYn=" + adminHideYn + ", hideReason=" + hideReason + ", viewCount=" + viewCount
				+ ", createDt=" + createDt + ", modifyDt=" + modifyDt + ", likeCnt=" + likeCnt + ", chatCnt=" + chatCnt
				+ ", imageList=" + imageList + ", largeName=" + largeName + ", middleName=" + middleName
				+ ", smallName=" + smallName + "]";
	}

	
}