package com.pcwk.ehr.product.domain;

import java.io.Serializable;

public class ProductTagVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int tagId;          // TAG_ID (PK, SEQUENCE)
    private int productNo;      // PRODUCT_NO (FK)
    private String tagName;     // TAG_NAME (NVARCHAR2)
    private String createDt;    // CREATE_DT (DATE)

    public ProductTagVO() {}

    // Getters and Setters
    public int getTagId() { return tagId; }
    public void setTagId(int tagId) { this.tagId = tagId; }

    public int getProductNo() { return productNo; }
    public void setProductNo(int productNo) { this.productNo = productNo; }

    public String getTagName() { return tagName; }
    public void setTagName(String tagName) { this.tagName = tagName; }

    public String getCreateDt() { return createDt; }
    public void setCreateDt(String createDt) { this.createDt = createDt; }

    @Override
    public String toString() {
        return "ProductTagVO [tagId=" + tagId + ", productNo=" + productNo + ", tagName=" + tagName + "]";
    }
}