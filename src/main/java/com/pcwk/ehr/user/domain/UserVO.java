package com.pcwk.ehr.user.domain;

import com.pcwk.ehr.cmn.DTO;

public class UserVO extends DTO {
	// 카멜케이스
	// ctrl+shift+y -> 소문자
	// ctrl+shift+x : 대문자
	private String userId;// 사용자ID
	private String name;// 이름
	private String password;// 비밀번호

	private int login;// 로그인
	private int recommend;// 추천
	private String email;// 이메일
	private Grade grade;// 등급

	private String regDt;// 등록일
	

	
	// 전역변수
	// Default 생성자 : alt+shift+s
	// 인자있는 생성자
	// getters/setters
	// toString()

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
	public void upgrageLevel() {
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

/**
 * 	단순한 데이터 그릇을 넘어 객체 지향적인 설계가 아주 잘 반영되어 있습니다. 

	1. DTO 상속 (부모의 유산 활용)
		public class UserVO extends DTO를 통해 아까 분석했던 DTO의 모든 기능(페이징, 검색 조건 등)을 그대로 물려받았습니다. 
		덕분에 컨트롤러에서 UserVO 하나만 넘겨도 검색 결과와 페이지 정보를 모두 처리할 수 있게 된 거죠.

	2. 깔끔한 카멜케이스(Camel Case) 적용
		DB 테이블 컬럼명은 USER_ID, REG_DT처럼 언더바를 쓰지만, 자바 객체에서는 userId, regDt로 완벽하게 변환했습니다. 
		아까 mybatis-config.xml에서 설정했던 <setting name="mapUnderscoreToCamelCase" value="true"/> 설정 덕분에, MyBatis가 자동으로 이 매핑을 해줄 겁니다.

	3. 핵심은 upgrageLevel() 메서드
		이 부분이 가장 인상적입니다! 보통 데이터만 담는 VO(Value Object)는 getter/setter만 있어야 한다고 생각하기 쉬운데, 
		여기엔 '사용자의 등급을 올린다'는 비즈니스 로직이 직접 포함되어 있습니다.
			- Grade라는 객체 내부의 getNextLevel()을 호출해서 다음 등급을 확인합니다.
			- 만약 더 이상 등급을 올릴 수 없다면(null), IllegalArgumentException을 던져서 프로그램이 잘못된 로직으로 등급을 올리지 못하도록 안전장치를 걸어두었습니다.
			- 객체 스스로 자신의 상태(등급)를 관리하는 아주 훌륭한 객체 지향 설계입니다.

	4. Grade 객체 사용
		등급을 단순히 int 숫자(1, 2, 3)로만 관리하지 않고, Grade라는 별도의 객체 타입으로 관리하고 있네요. 
		이건 등급의 종류가 바뀔 때 수정 범위를 최소화할 수 있는 아주 좋은 방법입니다.
 */

}
