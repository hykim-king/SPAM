package com.pcwk.ehr.transact.domain;

import java.io.Serializable;

/**
 * 거래내역 검색/페이징 DTO
 * 화면에서 넘겨준 검색 조건과 페이징 정보를 서버로 전달하는 역할을 합니다.
 */
public class TransacHistSearchDTO implements Serializable {
    private static final long serialVersionUID = 1L; // 객체 직렬화를 위한 고유 ID

    private String searchDiv;  // 검색 구분 (예: 제목 검색, 작성자 검색 등)
    private String searchWord; // 검색어 (사용자가 입력한 값)
    private int pageNo = 1;    // 현재 페이지 번호 (기본값 1)
    private int pageSize = 10; // 한 페이지에 보여줄 데이터 건수 (기본값 10)
    private String status;     // 상태값 (예: 판매중, 거래완료 등 필터링 조건)
    private String type = "purchase"; // purchase: 구매내역, sale: 판매내역
    private Long userNum;      // 로그인 회원 번호

    // 페이징 관련 필드 (화면에 표시할 페이지 버튼 정보를 계산하기 위함)
    private int startPage; // 페이지 블록의 시작 번호
    private int endPage;   // 페이지 블록의 끝 번호
    private int totalPage;

    public TransacHistSearchDTO() {
        super();
    }

    /**
     * [페이징 계산 로직]
     * 전달받은 전체 데이터 수(totalCount)를 기준으로 
     * 화면 하단에 표시할 페이지 버튼(startPage~endPage) 범위를 계산합니다.
     */
    public void calculatePaging(int totalCount) {
        int blockSize = 5; // 한 번에 보여줄 페이지 버튼 개수
        
        // 현재 페이지가 속한 페이지 블록의 끝 번호 계산
        this.endPage = (int) (Math.ceil(this.pageNo / (double)blockSize) * blockSize);
        // 페이지 블록의 시작 번호 계산
        this.startPage = this.endPage - (blockSize - 1);
        
        // 전체 데이터로 나눈 실제 마지막 페이지 번호
        int lastPage = (int) Math.ceil(totalCount / (double) pageSize);
        this.totalPage = lastPage;
        
        // 계산된 끝 페이지가 실제 마지막 페이지보다 크면 보정
        if (this.endPage > lastPage) {
            this.endPage = lastPage;
        }
    }

    // 데이터베이스 조회 시 가져올 데이터의 시작 행 번호 (예: 1, 11, 21...)
    public int getStartRow() { return (pageNo - 1) * pageSize + 1; }
    
    // 데이터베이스 조회 시 가져올 데이터의 끝 행 번호 (예: 10, 20, 30...)
    public int getEndRow() { return pageNo * pageSize; }
    
    // --- Getter / Setter 메서드들 (생략) ---
    // (이후 코드들은 필드값을 읽거나 수정하기 위한 기본적인 Getter/Setter들입니다.)

    
    // --- Getter / Setter ---
    public String getSearchDiv() { return searchDiv; }
    public void setSearchDiv(String searchDiv) { this.searchDiv = searchDiv; }

    public String getSearchWord() { return searchWord; }
    public void setSearchWord(String searchWord) { this.searchWord = searchWord; }

    public int getPageNo() { return pageNo; }
    public void setPageNo(int pageNo) { this.pageNo = pageNo <= 0 ? 1 : pageNo; }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize <= 0 ? 10 : Math.min(pageSize, 100); }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getType() { return type; }
    public void setType(String type) { this.type = "sale".equals(type) ? "sale" : "purchase"; }

    public Long getUserNum() { return userNum; }
    public void setUserNum(Long userNum) { this.userNum = userNum; }

    public int getStartPage() { return startPage; }
    public int getEndPage() { return endPage; }
    public int getTotalPage() { return totalPage; }
}
