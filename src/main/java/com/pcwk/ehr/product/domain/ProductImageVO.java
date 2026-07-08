package com.pcwk.ehr.product.domain;

import java.io.Serializable;

public class ProductImageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int imageNo;        // IMAGE_NO (PK)
    private int productNo;      // PRODUCT_NO (FK)
    private String originName;  // ORIGIN_NAME (원본 파일명)
    private String changeName;  // CHANGE_NAME (서버 저장 파일명)
    private String filePath;    // FILE_PATH (저장 경로)
    private int imageOrder;     // IMAGE_ORDER (순서)
    private String thumbnalYn;  // THUMBNAL_YN (대표이미지 Y/N) ★ 오타 주의
    private String createDt;    // CRATE_DT

    public ProductImageVO() {}

    public int getImageNo() { return imageNo; }
    public void setImageNo(int imageNo) { this.imageNo = imageNo; }

    public int getProductNo() { return productNo; }
    public void setProductNo(int productNo) { this.productNo = productNo; }

    public String getOriginName() { return originName; }
    public void setOriginName(String originName) { this.originName = originName; }

    public String getChangeName() { return changeName; }
    public void setChangeName(String changeName) { this.changeName = changeName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public int getImageOrder() { return imageOrder; }
    public void setImageOrder(int imageOrder) { this.imageOrder = imageOrder; }

    public String getThumbnalYn() { return thumbnalYn; }
    public void setThumbnalYn(String thumbnalYn) { this.thumbnalYn = thumbnalYn; }

    public String getCreateDt() { return createDt; }
    public void setCreateDt(String createDt) { this.createDt = createDt; }

    @Override
    public String toString() {
        return "ProductImageVO [imageNo=" + imageNo + ", productNo=" + productNo
                + ", changeName=" + changeName + ", imageOrder=" + imageOrder
                + ", thumbnalYn=" + thumbnalYn + "]";
    }
}