package com.pcwk.ehr.user.domain;

public enum Grade {

	GOLD(3,null), SILVER(2,GOLD), BASIC(1,SILVER);
	
	private final int value;
	private final Grade nextLevel; //다음등급

	private Grade(int value, Grade nextLevel) {
		this.value = value;
		this.nextLevel = nextLevel;
	}

	//BASIC  -> 1
	//SILVER -> 2
	//GOLD   -> 3
	public int getValue() {
		return value;
	}
	
	public Grade getNextLevel() {
		return nextLevel;
	}

	public static Grade valueOf(int value) {
		switch (value) {
		case 1:
			return BASIC;
		case 2:
			return SILVER;
		case 3:
			return GOLD;			
		default:
			throw new AssertionError("Unknown Value: "+value);
		}
	}
	/**
	 * 	이 파일은 자바의 Enum(열거형) 기능을 아주 교과서적으로 활용한 사례입니다.

		1. Enum의 활용: 상수 그 이상의 역할
			보통 등급을 관리할 때 static final int BASIC = 1; 처럼 숫자로 관리하면 실수가 잦습니다. 
			하지만 이렇게 Enum을 사용하면 '등급은 반드시 BASIC, SILVER, GOLD 중 하나여야 한다'는 제약이 확실해집니다.
				- 상태 정의: GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER)를 통해 
				  		  등급의 순서와 다음 등급이 무엇인지 상태 정의(State Definition)를 한눈에 볼 수 있습니다.
				- 자기 참조(Self-referencing): SILVER 객체가 자신의 다음 등급으로 GOLD를 알고 있고, GOLD는 null을 가지고 있죠.
				  							이 구조 덕분에 UserVO에서 upgrageLevel()을 호출할 때 복잡한 if-else문 없이도 
				  							"다음 단계가 뭐야?"라고 물어보기만 하면 됩니다.

		2. 코드 속 꿀팁
			private final 변수: 등급값과 다음 등급 정보가 외부에서 함부로 바뀌지 않도록 final로 선언하여 안전성을 극대화했습니다.
			valueOf(int value) 메서드: DB에는 숫자(1, 2, 3)로 저장되어 있지만, 자바 프로그램에서는 Grade 객체로 사용해야 합니다. 
									 이 메서드는 DB의 숫자를 받아 우리 시스템의 Grade 객체로 안전하게 변환해 주는 공장(Factory) 역할을 합니다.
			AssertionError: 만약 DB에 1, 2, 3이 아닌 다른 숫자(예: 4)가 들어왔을 때, 
							조용히 넘기지 않고 시스템적으로 에러를 발생시켜 잘못된 데이터가 들어왔음을 확실히 알리는 방어 코드가 매우 훌륭합니다.

		3. 왜 이렇게 만들었을까요?
			이 구조는 확장성이 매우 뛰어납니다. 예를 들어 나중에 'PLATINUM' 등급을 추가하고 싶다면, 
			코드 전체를 뒤질 필요 없이 Grade 파일의 첫 줄에 PLATINUM(4, null)만 추가하고 GOLD의 다음 등급을 PLATINUM으로 바꾸기만 하면 끝납니다.

		분석 현황
		com.pcwk.ehr.cmn (완료): 시스템 규칙(CRUD 인터페이스, DTO, 메시지 표준)
		com.pcwk.ehr.user.domain (완료): 데이터 모델(UserVO, 등급관리시스템)
		이제 우리 프로젝트의 '뼈대'와 '데이터 구조' 분석은 끝났습니다.
		아까 말한 6개 패키지 중 이제 com.pcwk.ehr.mapper를 통해, 앞에서 만든 UserVO를 데이터베이스와 실제로 어떻게 연결하는지(UserMapper 인터페이스)를 완성할 차례입니다.
	 */
}
