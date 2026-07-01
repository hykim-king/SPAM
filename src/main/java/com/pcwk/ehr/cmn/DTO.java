package com.pcwk.ehr.cmn;

import java.util.HashMap;
import java.util.Map;

public class DTO {
	
	// 필드
	private int pageNo;  //페이지번호
	private int pageSize;//페이지사이즈(10>20>30>50>100)
	private int no;      //글번호
	private int totalCnt;//총글수 
	private String searchDiv;  //검색구분
	private String searchWorld;//검색어
	private Map<String,String> searchMap=new HashMap<String,String>();
	
	// 생성자
	public DTO() {
		super();
	}
	
	// 게터세터, 투스트링
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public String getSearchDiv() {
		return searchDiv;
	}
	public void setSearchDiv(String searchDiv) {
		this.searchDiv = searchDiv;
	}
	public String getSearchWorld() {
		return searchWorld;
	}
	public void setSearchWorld(String searchWorld) {
		this.searchWorld = searchWorld;
	}
	public Map<String, String> getSearchMap() {
		return searchMap;
	}
	public void setSearchMap(Map<String, String> searchMap) {
		this.searchMap = searchMap;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	@Override
	public String toString() {
		return "DTO [pageNo=" + pageNo + ", pageSize=" + pageSize + ", no=" + no + ", totalCnt=" + totalCnt
				+ ", searchDiv=" + searchDiv + ", searchWorld=" + searchWorld + ", searchMap=" + searchMap + "]";
	}
}
	
	/**
	 	이 클래스는 단순히 데이터만 담는 그릇이 아니라, 
	 	'모든 목록 조회 화면에서 반드시 필요한 정보들'을 담아두는 부모 클래스입니다.
	 	
		1. 왜 이 클래스를 만들었을까요?
			보통 게시판이나 회원 목록 화면을 만들면 공통적으로 필요한 정보가 있습니다.
				- 현재 몇 페이지인가? (pageNo)
				- 한 페이지에 몇 건을 보여줄 것인가? (pageSize)
				- 전체 글은 몇 개인가? (totalCnt)
				- 무엇으로 검색할 것인가? (searchDiv)
				- 검색어는 무엇인가? (searchWorld)
			이런 정보들을 매번 각 화면마다 일일이 변수로 만들면 코드가 너무 지저분해지죠? 
			그래서 DTO라는 클래스에 다 몰아넣고, 앞으로 목록 조회가 필요한 모든 클래스(예: UserVO)가 이 DTO를 상속받아서 사용하도록 설계한 것입니다. 
			(상속받으면 pageNo, pageSize 등을 따로 선언할 필요가 없으니까요!)

		2. 코드 속 힌트
			Map<String,String> searchMap: 이 녀석이 아주 영리한 도구입니다. 
			때때로 검색 조건이 단순히 1~2개가 아니라 복잡해질 수 있는데, 
			그때 이 맵에다가 자유롭게 키-값 형태로 데이터를 넣어서 쿼리로 보내겠다는 의도가 보입니다.

			toString(): 대장이 나중에 개발하다가 "어? 왜 검색이 안 되지?" 싶을 때 System.out.println(dto.toString())만 찍어보면, 
			현재 페이지 번호와 검색 조건이 무엇인지 한눈에 바로 확인할 수 있게 해주는 아주 고마운 메서드입니다.

		3. userMapper.xml과의 관계
			userMapper.xml에서 parameterType="com.pcwk.ehr.cmn.DTO"라고 되어 있었죠?
			즉, 자바에서 컨트롤러가 UserVO를 넘기든 DTO를 넘기든, 
			이 파일에 정의된 변수들을 SQL 쿼리 안에서 #{pageNo}, #{searchWorld} 등의 이름으로 그대로 꺼내 쓸 수 있다는 뜻입니다.
	 */