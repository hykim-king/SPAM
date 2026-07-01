package com.pcwk.ehr.user.service;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;

import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.user.domain.UserVO;

public interface UserService {

	/**
	 * 목록조회
	 * 
	 * @param param
	 * @return List<T>
	 */
	List<UserVO> doRetrieve(DTO param);

	/**
	 * 수정
	 * 
	 * @param param
	 * @return 1(성공)/0(실패)
	 */
	int doUpdate(UserVO param);

	/**
	 * 단건 삭제
	 * 
	 * @param param
	 * @return 1(성공)/0(실패)
	 */
	int doDelete(UserVO param);

	/**
	 * 단건조회
	 * 
	 * @param param
	 * @return T(성공)/null(실패)
	 */
	UserVO doSelectOne(UserVO param) throws EmptyResultDataAccessException;

	/**
	 * 회원 전체 등업
	 */
	public void upgradeLevels() throws Exception;

	/**
	 * 회원가입
	 * 
	 * @param param
	 * @return 1(성공)/0(실패)
	 */
	public int doSave(UserVO param);
	
	
	/**
	 	UserService.java는 프로젝트의 핵심 비즈니스 규칙이 모여 있는 명세서입니다.
		이 인터페이스를 보니, 이 프로젝트가 단순한 데이터 입력/수정을 넘어 '등급 관리'라는 독자적인 비즈니스 로직을 가지고 있다는 게 확실히 보이네요. 분석해 드릴게요!

		1. 매퍼 인터페이스와의 차이점 (Service vs Mapper)
		- Mapper: DB 쿼리를 단순히 실행하는 '기능 단위' (CRUD)입니다.
		- Service: 그 기능들을 여러 개 묶어서 '비즈니스 업무'를 완성하는 곳입니다.
		- 예: doSave는 DB 저장 기능뿐만 아니라, 가입 시 축하 이메일을 발송하는 등의 업무적 과정이 포함될 수 있습니다.

		2. 핵심 기능 분석
		- CRUD 메서드들 (doRetrieve, doUpdate, doDelete, doSelectOne, doSave): 이것들은 UserMapper가 가진 기능을 서비스 계층으로 그대로 가져온 것입니다. 
																			컨트롤러는 매퍼를 직접 호출하지 않고, 
																			항상 이 UserService를 통해서만 DB 작업을 수행하도록 설계되었습니다. 
																			(데이터의 무결성과 보안을 위해서죠!)
		- upgradeLevels() (이 프로젝트의 하이라이트): 이 메서드가 이 인터페이스의 존재 이유이자 핵심입니다.	
		- 왜 중요할까요?: 단순히 DB 값을 바꾸는 게 아니라, 회원들의 활동 데이터를 분석해서 등급을 자동으로 올려줘야 합니다. 
					 여러 명의 회원을 루프를 돌며 처리해야 하므로, '트랜잭션(Transaction)' 관리가 필수적입니다.
					 즉, 서비스 클래스인 UserServiceImpl에서 이 메서드를 구현할 때, @Transactional이라는 어노테이션이 반드시 붙게 될 것입니다.

		3. 왜 WorkDiv 인터페이스를 직접 구현하지 않았을까요?
		- 대장이 아까 WorkDiv 인터페이스를 만들었음에도 불구하고, UserService에서는 WorkDiv를 상속받지 않고 독자적인 인터페이스를 구성했네요.
		- 이유: UserService는 데이터베이스와 1:1로 매핑되는 기능(CRUD)뿐만 아니라, 
			  upgradeLevels()처럼 서비스만의 독립적인 업무 로직을 추가로 가져야 하기 때문입니다. 
			  즉, 인터페이스를 따로 분리함으로써 업무 로직의 유연성을 확보한 아주 좋은 선택입니다.

		4. 다음 단계: 구현체 확인
		- 이제 이 설계도(UserService)를 실제로 구현하는 UserServiceImpl.java가 궁금해집니다.
		  매퍼(UserMapper)를 호출해서 DB에서 데이터를 가져오고,
		  등급을 올리는 로직(upgradeLevels)을 어떻게 구현했는지,
		  또 아까 있었던 DummyMailService를 어떻게 활용하는지 확인하면 대장의 프로젝트 비즈니스 로직 핵심이 모두 파악됩니다.
	 */
}
