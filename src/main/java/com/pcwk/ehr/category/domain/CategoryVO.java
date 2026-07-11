package com.pcwk.ehr.category.domain;

import java.io.Serializable;

public class CategoryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int categoryNo;
    private Integer parentCategoryNo;
    private String categoryName;
    private int categoryLevel;
    private Integer sortOrder;
    private String useYn;
    private String createDt;

    public CategoryVO() {
    }

    public int getCategoryNo() {
        return categoryNo;
    }

    public void setCategoryNo(int categoryNo) {
        this.categoryNo = categoryNo;
    }

    public Integer getParentCategoryNo() {
        return parentCategoryNo;
    }

    public void setParentCategoryNo(Integer parentCategoryNo) {
        this.parentCategoryNo = parentCategoryNo;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryLevel() {
        return categoryLevel;
    }

    public void setCategoryLevel(int categoryLevel) {
        this.categoryLevel = categoryLevel;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    @Override
    public String toString() {
        return "CategoryVO [categoryNo=" + categoryNo + ", parentCategoryNo=" + parentCategoryNo
                + ", categoryName=" + categoryName + ", categoryLevel=" + categoryLevel
                + ", sortOrder=" + sortOrder + ", useYn=" + useYn + ", createDt=" + createDt + "]";
    }
}
