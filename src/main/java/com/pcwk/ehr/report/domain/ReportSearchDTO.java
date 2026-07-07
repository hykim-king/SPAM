package com.pcwk.ehr.report.domain;

import java.io.Serializable;

/**
 * 관리자 회원목록 조회 조건을 담는 DTO
 * : 목록 조회 조건과 페이징 계산값을 전달하기 위한 객체입니다.
 *
 * 사용 위치:
 * - AdminUserController.list()
 * - UserService.getUserList()
 * - UserMapper.selectUserList()
 * - userMapper.xml의 검색/페이징 SQL
 */
public class ReportSearchDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
     * searchDiv
     * - 검색 구분
     * - 예: userId, userName, nickname, phoneNum, email, userStatus
     * - JSP의 select box에서 넘어옴
     */
    private String searchDiv;

    /*
     * searchWord
     * - 검색어
     * - searchDiv가 userId라면 아이디 검색어,
     *   searchDiv가 userStatus라면 01/02/03/04 같은 회원상태 공통코드가 됨
     */
    private String searchWord;

    /*
     * pageNo
     * - 현재 페이지 번호
     * - 사용자가 값을 보내지 않으면 1페이지로 시작
     */
    private int pageNo = 1;

    /*
     * pageSize
     * - 한 페이지에 보여줄 회원 수
     * - 기본값 : 10명
     */
    private int pageSize = 10;

    private int startRow = 1; // 계산된 시작 행
    private int endRow = 10;   // 계산된 끝 행
    
    public String getSearchDiv() {
        return searchDiv;
    }

    public void setSearchDiv(String searchDiv) {
        this.searchDiv = searchDiv;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        // pageNo가 0 이하로 들어오면 잘못된 값 -> 잘못된 페이지 번호로 SQL을 만들지 않기 위해 1로 보정
        this.pageNo = pageNo <= 0 ? 1 : pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
    	// pageSize가 0 이하로 들어오면 잘못된 값 -> 기본값 10으로 보정
        this.pageSize = pageSize <= 0 ? 10 : pageSize;
    }

    // Oracle ROWNUM 페이징 시작 번호
    public int getStartRow() {
        return (pageNo - 1) * pageSize + 1;
    }

    // Oracle ROWNUM 페이징 끝 번호
    public int getEndRow() {
        return pageNo * pageSize;
    }
}
