package com.pcwk.ehr.product.domain;

import java.io.Serializable;

/** 상품 목록의 검색, 필터, 정렬, 페이징 조건을 담는 DTO. */
public class ProductSearchDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String searchWord;
    private Integer categoryNo;
    private String location;
    private String priceRange;
    private String status;
    private boolean includeSold;
    private String sort = "latest";
    private Long userNum;
    private int pageNo = 1;
    private int pageSize = 12;

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = trimToNull(searchWord);
    }

    public Integer getCategoryNo() {
        return categoryNo;
    }

    public void setCategoryNo(Integer categoryNo) {
        this.categoryNo = categoryNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = trimToNull(location);
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = trimToNull(priceRange);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = trimToNull(status);
    }

    public boolean isIncludeSold() {
        return includeSold;
    }

    public void setIncludeSold(boolean includeSold) {
        this.includeSold = includeSold;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        String normalized = trimToNull(sort);
        if ("popular".equals(normalized)
                || "priceLow".equals(normalized)
                || "priceHigh".equals(normalized)) {
            this.sort = normalized;
            return;
        }
        // 2026-07-14 [수정] 화면에서 지원하는 네 가지 정렬값 외에는 최신순으로 보정한다.
        this.sort = "latest";
    }

    public Long getUserNum() {
        return userNum;
    }

    public void setUserNum(Long userNum) {
        this.userNum = userNum;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = Math.max(pageNo, 1);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize < 1 ? 12 : Math.min(pageSize, 100);
    }

    public int getStartRow() {
        return ((pageNo - 1) * pageSize) + 1;
    }

    public int getEndRow() {
        return pageNo * pageSize;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    @Override
    public String toString() {
        return "ProductSearchDTO [searchWord=" + searchWord + ", categoryNo=" + categoryNo
                + ", location=" + location + ", priceRange=" + priceRange + ", status=" + status
                + ", includeSold=" + includeSold + ", sort=" + sort + ", userNum=" + userNum + ", pageNo=" + pageNo
                + ", pageSize=" + pageSize + "]";
    }
}
