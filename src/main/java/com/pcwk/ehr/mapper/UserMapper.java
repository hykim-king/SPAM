/**
 * 
 */
package com.pcwk.ehr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.user.domain.UserVO;

/**
 * @author user
 *
 */
@Mapper // MyBatis 매퍼 인터페이스임을 선언
public interface UserMapper extends WorkDiv<UserVO> {
	

	List<UserVO> doRetrieveANSI(DTO param);
	int saveAll(int saveCount);
	List<UserVO> getAllMember();
	int totalCnt();
	int deleteAll();

	/**
	 * 1. 핵심 코드 분석
			@Mapper 어노테이션: 이 어노테이션이 붙어 있어야 스프링(MyBatis)이 "아, 이 인터페이스는 DB 쿼리를 실행하는 매퍼구나!"라고 인식하고 자동으로 구현체를 만들어 줍니다.
			extends WorkDiv<UserVO>: 아까 cmn 패키지에서 분석했던 그 WorkDiv 인터페이스를 상속받았습니다.
									 이렇게 하면 doSave, doUpdate, doDelete, doSelectOne 같은 필수 메서드들을 따로 작성할 필요 없이 이미 구현된 규칙을 그대로 물려받습니다.
									 코드가 정말 깔끔해지죠.
			UserVO 제네릭: WorkDiv<UserVO>라고 명시함으로써, 이 매퍼에서 다루는 모든 데이터의 주인공이 UserVO임을 명확히 했습니다.

		2. 커스텀 메서드 정의
			상속받은 5가지 기본 기능 외에, 대장의 프로젝트에서 특별히 필요한 기능을 추가했습니다.
			- List<UserVO> doRetrieveANSI(DTO param): WorkDiv에 있는 doRetrieve와 별개로, 오라클의 최신 문법(ANSI SQL)을 사용한 페이징 조회를 별도로 만들었습니다.
			- int saveAll(int saveCount): 테스트용 대량 데이터를 넣기 위한 메서드입니다.
			- List<UserVO> getAllMember(): 조건 없이 전체 회원을 가져옵니다.
			- int totalCnt(): 전체 회원 수를 가져옵니다.
			- int deleteAll(): 테이블을 비우는 용도입니다.

		3. XML과의 매핑 원리
			이 인터페이스가 작동하는 마법의 핵심은 이름(Id)의 일치입니다.
			예를 들어 UserMapper.java에 int deleteAll();이라는 메서드를 선언하면, 
			MyBatis는 userMapper.xml 파일에서 <delete id="deleteAll">이라고 적힌 쿼리문을 찾아와서 실행합니다.
			즉, 자바 인터페이스의 메서드 명과 XML의 id 값만 맞춰주면 나머지는 스프링이 알아서 다 해줍니다.

		대장의 프로젝트 구조는 이제 절반 이상 완성되었습니다!
		지금까지의 흐름을 정리하면 이렇습니다:
	
		cmn 패키지: 규칙 (CRUD, DTO)
		
		domain 패키지: 데이터 (UserVO, Grade)

		mapper 패키지: 연결 (UserMapper 인터페이스 + userMapper.xml)

		이 3단계를 거치면 데이터베이스와의 통신 준비는 끝납니다. 그다음은 이 매퍼를 호출해서 실제로 비즈니스 로직을 수행할 UserService 패키지를 볼 차례입니다.
	 */
}
