package com.pcwk.ehr.cmn;

import java.util.HashMap;
import java.util.Map;

public class DTO {
	private int pageNo;	  //페이지번호
	private int pageSize; //페이지사이즈(10>20>30>50>100)
	private int no; 	  //글번호
	private int totalCnt; //총글수
	
	//----------------------------------
	private String searchDiv;  //검색구분
	private String searchWorld;//검색어
	
	private Map<String,String> searchMap = new HashMap<String,String>();
	//----------------------------------
	
	public DTO() {
		super();
		// TODO Auto-generated constructor stub
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
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
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
		return "DTO [pageNo=" + pageNo + ", pageSize=" + pageSize + ", no=" + no + ", totalCnt=" + totalCnt + "]";
	}	

}
