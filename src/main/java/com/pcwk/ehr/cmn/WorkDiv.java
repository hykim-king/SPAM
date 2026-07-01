package com.pcwk.ehr.cmn;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;

public interface WorkDiv<T> {

	/**
	 * 목록조회
	 * @param param
	 * @return List<T>
	 */
	List<T> doRetrieve(DTO param);
	
	
	/**
	 * 수정
	 * @param param
	 * @return 1(성공)/0(실패)
	 */
	int doUpdate(T param);

	
	/**
	 * 단건 삭제
	 * @param param
	 * @return 1(성공)/0(실패)
	 */
	int doDelete(T param);

	
	/**
	 * 등록
	 * @param param
	 * @return 1(성공)/0(실패)
	 */
	int doSave(T param);

	
	/**
	 * 단건조회
	 * @param param
	 * @return T(성공)/null(실패)
	 */
	T doSelectOne(T param) throws EmptyResultDataAccessException;
	
	/**
	 * 	이 파일은 그냥 단순한 클래스가 아니라, 프로젝트 전체의 '표준 설계도' 역할을 하고 있습니다.

		1. WorkDiv<T>의 정체: "표준 인터페이스"
			이 인터페이스는 제네릭(Generic, <T>)을 사용하여, 
			앞으로 만들 모든 기능(회원 관리, 게시판 관리, 상품 관리 등)이 반드시 가져야 할 5가지 핵심 기능(CRUD)을 강제로 명시하고 있습니다.

			표준화: 어떤 기능을 새로 만들더라도 WorkDiv를 상속(구현)받게 하면, 
				  목록 조회(doRetrieve), 수정(doUpdate), 삭제(doDelete), 등록(doSave), 단건 조회(doSelectOne) 메서드를 빼먹지 않고 모두 구현하도록 강제할 수 있습니다.
			유지보수의 마법: 나중에 코드가 수천 줄이 되어도, 
					   "회원 조회 기능 어디 있지?"라고 찾을 필요 없이 doRetrieve를 호출하면 된다는 규칙이 서버 전체에 적용되어 있는 것입니다.

		2. 코드 속 힌트들
			T의 의미: 'Type'의 약자입니다. 예를 들어 회원 관리를 할 때는 T가 UserVO가 되고, 게시판을 만들 때는 T가 BoardVO가 됩니다. 
			 		이렇게 하면 하나의 설계도로 여러 가지 객체를 다룰 수 있게 됩니다.

			DTO param (조회 메서드): 목록 조회 메서드인 doRetrieve만 파라미터가 DTO입니다. 
								 아까 분석했던 DTO.java에 검색 조건과 페이징 정보가 다 들어있기 때문에, 
								 어떤 기능이든 이 DTO 하나만 넘기면 공통적으로 검색/페이징이 가능해집니다.

			throws EmptyResultDataAccessException: 단건 조회(doSelectOne)시 데이터가 없으면 스프링이 제공하는 예외를 던지도록 설정하여, 
												   데이터가 없는 경우에 대한 에러 처리를 표준화했습니다.

		3. 이것이 왜 대단한가요?
			이런 인터페이스 구조를 잡는다는 건, "나중에 기능을 100개, 1000개를 만들어도 우리는 항상 이 규칙대로 움직일 거야"라는 강력한 약속입니다.
			컨트롤러는 서비스가 WorkDiv를 구현했다는 사실만 알면 되므로, 내부 구현이 어떻게 바뀌든 컨트롤러는 영향을 받지 않습니다. (결합도 감소)
			대장이 나중에 UserService를 만들 때, 이 인터페이스를 implements 하기만 하면 필요한 메서드 5개를 자동으로 챙길 수 있습니다.

		공통 패키지(cmn) 분석 완료!
		이제 대장의 프로젝트는 "데이터 전달(DTO), 결과 보고(MessageVO), 그리고 모든 기능의 규칙(WorkDiv)까지 완벽하게 정의된 튼튼한 토대"를 가지고 있습니다.
	 */
}