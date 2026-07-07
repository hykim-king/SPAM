package com.pcwk.ehr.product.domain;

import java.io.Serializable;

public class WishListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int wishNo;         // WISH_NO (PK, SEQUENCE)
    private int userNum;        // USER_NUM (FK)
    private int productNo;      // PRODUCT_NO (FK)
    private String createDt;    // CREATE_DT (DATE)

    public WishListVO() {}

    // Getters and Setters
    public int getWishNo() { return wishNo; }
    public void setWishNo(int wishNo) { this.wishNo = wishNo; }

    public int getUserNum() { return userNum; }
    public void setUserNum(int userNum) { this.userNum = userNum; }

    public int getProductNo() { return productNo; }
    public void setProductNo(int productNo) { this.productNo = productNo; }

    public String getCreateDt() { return createDt; }
    public void setCreateDt(String createDt) { this.createDt = createDt; }

    @Override
    public String toString() {
        return "WishListVO [wishNo=" + wishNo + ", userNum=" + userNum + ", productNo=" + productNo + "]";
    }
}