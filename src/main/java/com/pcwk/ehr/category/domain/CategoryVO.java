package com.pcwk.ehr.category.domain;

import java.io.Serializable;

public class CategoryVO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private int categoryNo;         // CATEGORY_NO (PK, SEQUENCE)
    private Integer parentCategoryNo; // PARENT_CATEGORY_NO (FK, NULL 허용되므로 Integer 사용)
    private String categoryName;    // CATEGORY_NAME (NVARCHAR2)
    private int categoryLevel;      // CATEGORY_LEVEL (1:대분류, 2:중분류, 3:소분류)
    private Integer sortOrder;      // SORT_ORDER (NULL 허용)
    private String createDt;        // CREATE_DT (DATE, String으로 관리하여 날짜 포맷팅 편의성 제고)
    private String updateDt;        // UPDATE_DT (DATE)

    public CategoryVO() {}

    public CategoryVO(int categoryNo, Integer parentCategoryNo, String categoryName, int categoryLevel,
            Integer sortOrder, String createDt, String updateDt) {
        this.categoryNo = categoryNo;
        this.parentCategoryNo = parentCategoryNo;
        this.categoryName = categoryName;
        this.categoryLevel = categoryLevel;
        this.sortOrder = sortOrder;
        this.createDt = createDt;
        this.updateDt = updateDt;
    }

    // Getters and Setters
    public int getCategoryNo() { return categoryNo; }
    public void setCategoryNo(int categoryNo) { this.categoryNo = categoryNo; }

    public Integer getParentCategoryNo() { return parentCategoryNo; }
    public void setParentCategoryNo(Integer parentCategoryNo) { this.parentCategoryNo = parentCategoryNo; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public int getCategoryLevel() { return categoryLevel; }
    public void setCategoryLevel(int categoryLevel) { this.categoryLevel = categoryLevel; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public String getCreateDt() { return createDt; }
    public void setCreateDt(String createDt) { this.createDt = createDt; }

    public String getUpdateDt() { return updateDt; }
    public void setUpdateDt(String updateDt) { this.updateDt = updateDt; }

    @Override
    public String toString() {
        return "CategoryVO [categoryNo=" + categoryNo + ", parentCategoryNo=" + parentCategoryNo + ", categoryName="
                + categoryName + ", categoryLevel=" + categoryLevel + ", sortOrder=" + sortOrder + ", createDt=" + createDt + ", updateDt=" + updateDt + "]";
    }
}