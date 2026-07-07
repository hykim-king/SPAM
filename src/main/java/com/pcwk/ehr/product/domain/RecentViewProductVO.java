package com.pcwk.ehr.product.domain;

import java.io.Serializable;

public class RecentViewProductVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int recentViewNo;   // RECENT_VIEW_NO (PK, SEQUENCE)
    private int userNum;        // USER_NUM (FK)
    private int productNo;      // PRODUCT_NO (FK)
    private String viewDt;      // VIEW_DT (DATE)

    public RecentViewProductVO() {}

    // Getters and Setters
    public int getRecentViewNo() { return recentViewNo; }
    public void setRecentViewNo(int recentViewNo) { this.recentViewNo = recentViewNo; }

    public int getUserNum() { return userNum; }
    public void setUserNum(int userNum) { this.userNum = userNum; }

    public int getProductNo() { return productNo; }
    public void setProductNo(int productNo) { this.productNo = productNo; }

    public String getViewDt() { return viewDt; }
    public void setViewDt(String viewDt) { this.viewDt = viewDt; }

    @Override
    public String toString() {
        return "RecentViewProductVO [recentViewNo=" + recentViewNo + ", userNum=" + userNum + ", productNo=" + productNo + ", viewDt=" + viewDt + "]";
    }
}