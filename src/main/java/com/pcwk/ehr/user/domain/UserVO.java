package com.pcwk.ehr.user.domain;

import com.pcwk.ehr.cmn.DTO;

public class UserVO extends DTO{
	//카멜케이스
	//ctrl + shift + y -> 소문자
	//ctrl + shift + x -> 대문자
	private String userId      ;//사용자ID
	private String name        ;//이름
	private String password    ;//비밀번호

	private int login       ;//로그인
	private int recommend   ;//추천
	private String email       ;//이메일
	private Grade grade       ;//등급	

	private String regDt     ;//등록일		
	

	
	//전역변수
	//Default 생성자	: alt+shift+s
	//인자있는 생성자
	//get/setters
	//toString()
	
	public UserVO() {
		super();
	}
	
  	
	public UserVO(String userId, String name, String password, int login, int recommend, String email, Grade grade,
			String regDt) {
		super();
		this.userId = userId;
		this.name = name;
		this.password = password;
		this.login = login;
		this.recommend = recommend;
		this.email = email;
		this.grade = grade;
		this.regDt = regDt;
	}
	
	//다음 등급 가져오기
	public void upgradeLevel() {
		Grade nextGrade = grade.getNextLevel();
		
		if(null == nextGrade) {
			throw new IllegalArgumentException(this.grade+"은 등업 불가 합니다.");
		}
		else {
			this.grade = nextGrade;
		}
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getLogin() {
		return login;
	}
	public void setLogin(int login) {
		this.login = login;
	}
	public int getRecommend() {
		return recommend;
	}
	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Grade getGrade() {
		return grade;
	}
	public void setGrade(Grade grade) {
		this.grade = grade;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}


	@Override
	public String toString() {
		return "UserVO [userId=" + userId + ", name=" + name + ", password=" + password + ", login=" + login
				+ ", recommend=" + recommend + ", email=" + email + ", grade=" + grade + ", regDt=" + regDt
				+ ", toString()=" + super.toString() + "]";
	}
	
	
}
