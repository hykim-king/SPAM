package com.pcwk.ehr.transact.domain;

import java.io.Serializable;

/**
 * 거래내역 검색/페이징 DTO
 */
public class TransacHistSearchDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String searchDiv;  // 검색 구분
    private String searchWord; // 검색어
    private int pageNo = 1;    // 현재 페이지 번호
    private int pageSize = 10; // 페이지당 건수

    public TransacHistSearchDTO() {
        super();
    }

    // 페이징 계산 로직
    public int getStartRow() { return (pageNo - 1) * pageSize + 1; }
    public int getEndRow() { return pageNo * pageSize; }
    
    // --- Getter / Setter ---
    public String getSearchDiv() { return searchDiv; }
    public void setSearchDiv(String searchDiv) { this.searchDiv = searchDiv; }

    public String getSearchWord() { return searchWord; }
    public void setSearchWord(String searchWord) { this.searchWord = searchWord; }

    public int getPageNo() { return pageNo; }
    public void setPageNo(int pageNo) { this.pageNo = pageNo <= 0 ? 1 : pageNo; }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize <= 0 ? 10 : pageSize; }
}