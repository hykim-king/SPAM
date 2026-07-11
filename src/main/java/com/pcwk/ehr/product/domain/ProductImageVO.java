package com.pcwk.ehr.product.domain;

import java.io.Serializable;

public class ProductImageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int imageNo;
    private int productNo;
    private String originName;
    private String changeName;
    private String filePath;
    private int imageOrder;
    private String thumbnailYn;
    private String createDt;

    public ProductImageVO() {
    }

    public int getImageNo() {
        return imageNo;
    }

    public void setImageNo(int imageNo) {
        this.imageNo = imageNo;
    }

    public int getProductNo() {
        return productNo;
    }

    public void setProductNo(int productNo) {
        this.productNo = productNo;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getChangeName() {
        return changeName;
    }

    public void setChangeName(String changeName) {
        this.changeName = changeName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getImageOrder() {
        return imageOrder;
    }

    public void setImageOrder(int imageOrder) {
        this.imageOrder = imageOrder;
    }

    public String getThumbnailYn() {
        return thumbnailYn;
    }

    public void setThumbnailYn(String thumbnailYn) {
        this.thumbnailYn = thumbnailYn;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    @Override
    public String toString() {
        return "ProductImageVO [imageNo=" + imageNo + ", productNo=" + productNo + ", changeName="
                + changeName + ", imageOrder=" + imageOrder + ", thumbnailYn=" + thumbnailYn + "]";
    }
}
