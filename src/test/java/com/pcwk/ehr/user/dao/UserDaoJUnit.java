package com.pcwk.ehr.user.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.mapper.UserMapper;
import com.pcwk.ehr.user.domain.Grade;
import com.pcwk.ehr.user.domain.UserVO;

//스프링 테스트(spring-test) 컨텍스트 프레임워크의 JUnit확장 기능
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })
public class UserDaoJUnit {
	final static Logger log = LogManager.getLogger(UserDaoJUnit.class);

	@Autowired
	private UserMapper userMapper;

	private UserVO user01;
	private UserVO user02;
	private UserVO user03;

	private DTO dto;// paging/검색

	@Disabled
	@Test
	void doSave() {
		log.debug("---------------------------");
		log.debug("*doSave()*");
		log.debug("---------------------------");
		// 1. 전체 삭제
		// 2. 단건등록
		// 3. 데이터 조회및 비교

		// 1.
		userMapper.deleteAll();
		assertEquals(0, userMapper.totalCnt());

		// 2.
		userMapper.doSave(user01);
		assertEquals(1, userMapper.totalCnt());

		isSameUser(user01, userMapper.doSelectOne(user01));
	}

	@Disabled
	@Test
	void deleteAll() {
		log.debug("---------------------------");
		log.debug("*deleteAll()*");
		log.debug("---------------------------");

		int flag = userMapper.deleteAll();
		log.debug("flag : " + flag);
	}

	@Disabled
	@Test
	void beans() {
		log.debug("---------------------------");
		log.debug("*beans()*");
		log.debug("---------------------------");
		assertNotNull(userMapper);
		log.debug("userMapper: " + userMapper);
	}

	@BeforeEach
	public void setUp() throws Exception {
		log.debug("*****************************");
		log.debug("*@BeforeEach*");
		log.debug("*****************************");

		user01 = new UserVO("pcwk01", "이상무01", "4321a", 1, 0, "jamesol@naver.com", Grade.BASIC, "등록일_사용않함");
		user02 = new UserVO("pcwk02", "이상무02", "4321a", 50, 29, "jamesol@naver.com", Grade.SILVER, "등록일_사용않함");
		user03 = new UserVO("pcwk03", "이상무03", "4321a", 51, 31, "jamesol@naver.com", Grade.GOLD, "등록일_사용않함");

		dto = new DTO();
	}

	// @Disabled
	@Test
	public void doRetrieveANSI() {
		log.debug("---------------------------");
		log.debug("*doRetrieveANSI()*");
		log.debug("---------------------------");
		// 테스트는 항상 동일한 결과가 나와야 하므로(Test Isolation) 데이터를 초기화하고
		// 시작해야 합니다.
		// 1. 전체삭제
		// 2. 1002건 입력
		// 3. 페이징 조회

		final int SAVE_COUNT = 1002;

		// 1.
		userMapper.deleteAll();
		assertEquals(0, userMapper.totalCnt());

		// 2.
		userMapper.saveAll(SAVE_COUNT);
		assertEquals(SAVE_COUNT, userMapper.totalCnt());

		dto.setPageNo(1);
		dto.setPageSize(10);

//		dto.setSearchDiv("10");
//		dto.setSearchWorld("pcwk1");

//		dto.setSearchDiv("20");
//		dto.setSearchWorld("이상무2");

//		dto.setSearchDiv("30");
//		dto.setSearchWorld("jamesol1@naver.com");

		List<UserVO> list = userMapper.doRetrieveANSI(dto);
		for (UserVO vo : list) {
			log.debug(vo);
		}
		assertEquals(10, list.size());

	}

	// @Disabled
	@Test
	public void doRetrieve() {
		log.debug("---------------------------");
		log.debug("*doRetrieve()*");
		log.debug("---------------------------");
		// 테스트는 항상 동일한 결과가 나와야 하므로(Test Isolation) 데이터를 초기화하고
		// 시작해야 합니다.
		// 1. 전체삭제
		// 2. 1002건 입력
		// 3. 페이징 조회

		final int SAVE_COUNT = 1002;

		// 1.
		userMapper.deleteAll();
		assertEquals(0, userMapper.totalCnt());

		// 2.
		userMapper.saveAll(SAVE_COUNT);
		assertEquals(SAVE_COUNT, userMapper.totalCnt());

		dto.setPageNo(2);
		dto.setPageSize(10);

//		dto.setSearchDiv("10");
//		dto.setSearchWorld("pcwk1");

		dto.setSearchDiv("20");
		dto.setSearchWorld("이상무2");

//		dto.setSearchDiv("30");
//		dto.setSearchWorld("jamesol1@naver.com");

		List<UserVO> list = userMapper.doRetrieve(dto);
		for (UserVO vo : list) {
			log.debug(vo);
		}
		assertEquals(10, list.size());

	}

	@AfterEach
	public void tearDown() throws Exception {
		log.debug("*****************************");
		log.debug("*@AfterEach*");
		log.debug("*****************************");
	}

	@Disabled
	@Test
	public void doUpdate() {

		log.debug("---------------------------");
		log.debug("*doUpdate()*");
		log.debug("---------------------------");
		// 테스트는 항상 동일한 결과가 나와야 하므로(Test Isolation) 데이터를 초기화하고
		// 시작해야 합니다.
		// 1. 전체삭제
		// 2. 단건등록(user01) - 수정 사용자
		// 3. 단건등록(user02) - 수정 않을 사용자
		// 4. 단건조회(user01)
		// 5. 단건조회(user01) 수정
		// 6. update(5.수정)
		// 7. 단건조회(user01)
		// 8. (5. 단건조회(user01) 수정) == (7. 단건조회(user01))

		// 1.
		userMapper.deleteAll();
		assertEquals(0, userMapper.totalCnt());

		// 2.
		int flag = userMapper.doSave(user01);
		assertEquals(1, flag);
		assertEquals(1, userMapper.totalCnt());

		// 3.
		flag = userMapper.doSave(user02);
		assertEquals(1, flag);
		assertEquals(2, userMapper.totalCnt());

		// 4.
		UserVO updateUser = userMapper.doSelectOne(user01);
		assertNotNull(updateUser);

		// 5.
		String updateStr = "_U";
		int updateInt = 99;
		updateUser.setName(updateUser.getName() + updateStr);
		updateUser.setPassword(updateUser.getPassword() + updateStr);
		updateUser.setLogin(updateUser.getLogin() + updateInt);
		updateUser.setRecommend(updateUser.getRecommend() + updateInt);
		updateUser.setEmail(updateUser.getEmail() + updateStr);
		updateUser.setGrade(Grade.SILVER);

		// 6. update(5.수정)
		flag = userMapper.doUpdate(updateUser);
		assertEquals(1, flag);

		// 7.
		UserVO outVO = userMapper.doSelectOne(updateUser);
		assertNotNull(outVO);

		// 8.
		isSameUser(updateUser, outVO);

	}

	@Disabled
	@Test
	public void doDelete() {
		log.debug("---------------------------");
		log.debug("*doDelete()*");
		log.debug("---------------------------");
		// 매번 동일 결과가 도출 되도록 작성
		// 1. 전체삭제
		// 2. 단건등록(user01)
		// 3. 단건삭제(user01)
		// 4. 건수비교

		// 1.
		userMapper.deleteAll();
		assertEquals(0, userMapper.totalCnt());

		// 2.
		int flag = userMapper.doSave(user01);
		assertEquals(1, flag);
		assertEquals(1, userMapper.totalCnt());

		// 3.
		flag = userMapper.doDelete(user01);
		assertEquals(1, flag);

		// 4.
		assertEquals(0, userMapper.totalCnt());
	}

	@Disabled
	@Test
	public void getAllMember() {
		log.debug("---------------------------");
		log.debug("*getAllMember()*");
		log.debug("---------------------------");
		// 매번 동일 결과가 도출 되도록 작성
		// 1. 전체삭제
		// 2. 데이터 1건 등록(user01)
		// 3. 데이터 1건 등록(user02)
		// 4. 데이터 1건 등록(user03)
		// 5. 전체조회 : 데이터 건수 3건 확인

		// 1.
		userMapper.deleteAll();
		assertEquals(0, userMapper.totalCnt());

		// 2.
		int flag = userMapper.doSave(user01);
		assertEquals(1, flag);
		assertEquals(1, userMapper.totalCnt());

		// 3.
		flag = userMapper.doSave(user02);
		assertEquals(1, flag);
		assertEquals(2, userMapper.totalCnt());

		// 4.
		flag = userMapper.doSave(user03);
		assertEquals(1, flag);
		assertEquals(3, userMapper.totalCnt());
		// 5.
		List<UserVO> list = userMapper.getAllMember();
		assertEquals(3, list.size());

		for (UserVO vo : list) {
			log.debug(vo);
		}

		isSameUser(user01, list.get(0));
		isSameUser(user02, list.get(1));
		isSameUser(user03, list.get(2));
	}

	// NullPointerException이 발생하면 성공
	@Disabled
	@Test
	public void selectOneFailure() {
		// 매번 동일 결과가 도출 되도록 작성
		// 0.전체삭제
		// 1.단건 등록
		// 2.한건 조회
		// 3.등록 데이터 비교

		// (expected = EmptyResultDataAccessException.class)

		assertThrows(EmptyResultDataAccessException.class, () -> {
			// 0.
			userMapper.deleteAll();
			assertEquals(0, userMapper.totalCnt());

			// 1.
			userMapper.doSave(user01);
			assertEquals(1, userMapper.totalCnt());

			// 2. 실패
			user01.setUserId(user01.getUserId() + "_99");
			UserVO outVO = userMapper.doSelectOne(user01);

			// 3.
			isSameUser(user01, outVO);
		});
	}

	@Disabled
	@Test
	public void saveAndSelectOne() {
		// 매번 동일 결과가 도출 되도록 작성
		// 0.전체삭제
		// 1.단건 등록
		// 2.한건 조회
		// 3.등록 데이터 비교

		log.debug("---------------------");
		log.debug("@Test saveAndSelectOne");
		log.debug("---------------------");

		// 0.
		userMapper.deleteAll();
		int count = userMapper.totalCnt();
		assertEquals(0, count);

		// 1.
		userMapper.doSave(user01);
		assertEquals(1, userMapper.totalCnt());

		// 1.1
		userMapper.doSave(user02);
		assertEquals(2, userMapper.totalCnt());

		// 1.2
		userMapper.doSave(user03);
		assertEquals(3, userMapper.totalCnt());

		// 2.
		UserVO outVO = userMapper.doSelectOne(user01);
		assertNotNull(outVO);

		// 2.1
		UserVO outVO2 = userMapper.doSelectOne(user02);
		assertNotNull(outVO2);

		// 2.2
		UserVO outVO3 = userMapper.doSelectOne(user03);
		assertNotNull(outVO3);

		// 3.
		isSameUser(user01, outVO);
		isSameUser(user02, outVO2);
		isSameUser(user03, outVO3);

	}

	private void isSameUser(UserVO user01, UserVO outVO) {
		assertEquals(user01.getUserId(), outVO.getUserId());
		assertEquals(user01.getName(), outVO.getName());
		assertEquals(user01.getPassword(), outVO.getPassword());
		// ---------------------------------------------------------
		assertEquals(user01.getLogin(), outVO.getLogin());
		assertEquals(user01.getRecommend(), outVO.getRecommend());
		assertEquals(user01.getEmail(), outVO.getEmail());
		assertEquals(user01.getGrade(), outVO.getGrade());
		// ---------------------------------------------------------

	}

	/**
	 * UserDaoJUnit.java를 보니 우리 프로젝트의 테스트 철학이 아주 분명하게 드러나네요. 이 테스트 클래스는 단순한 확인용이
	 * 아니라, "실패는 절대 용납하지 않는다"는 의지가 담긴 아주 강력한 도구입니다. 이 코드에서 주목해야 할 테스트의 정석 3가지를 분석해
	 * 드릴게요.
	 * 
	 * 1. 테스트 격리 (Test Isolation) - 가장 중요한 포인트 테스트 코드들을 보면 하나같이
	 * userMapper.deleteAll()로 시작합니다. 이유: 데이터베이스는 이전 테스트에서 입력된 데이터 때문에 상태가 바뀔 수
	 * 있습니다. 만약 테스트 A가 데이터를 넣고 지우지 않으면, 테스트 B가 실행될 때 그 데이터 때문에 에러가 발생할 수 있죠. 해결책: 매
	 * 테스트 메서드마다 "데이터를 싹 지우고(Clean Slate), 나만의 테스트 데이터를 넣어서 검증한다"는 전략을 취하고 있습니다. 이것이
	 * 테스트 코드의 신뢰성을 보장하는 핵심입니다.
	 * 
	 * 2. isSameUser 메서드의 활용 (중복 제거) 마지막에 있는 private void isSameUser(UserVO user01,
	 * UserVO outVO) 메서드가 아주 훌륭합니다. 여러 테스트에서 assertEquals를 반복적으로 작성하면 코드가 지저분해집니다.
	 * 이렇게 isSameUser로 묶어두면, 나중에 UserVO에 새로운 필드가 추가되어도 이 메서드 하나만 수정하면 모든 테스트가 자동으로
	 * 반영됩니다. 유지보수 측면에서 최고입니다.
	 * 
	 * 3. JUnit 5의 최신 문법 활용 @ExtendWith(SpringExtension.class): 스프링 컨테이너를 테스트에 올립니다.
	 * 이 덕분에 @Autowired를 사용하여 userMapper를 쉽게 가져올 수 있죠. assertThrows:
	 * selectOneFailure() 메서드에서 EmptyResultDataAccessException을 잡는 방식을 보세요. 예전엔 주석
	 * 처리된 expected 방식을 썼지만, 지금은 assertThrows라는 람다식을 사용해 훨씬 더 명확하게 예외 상황을 검증하고 있습니다.
	 * 
	 * @Disabled: 대장이 지금은 잠시 꺼둔 테스트들이 많네요! 아마 지금은 페이징 기능(doRetrieve) 위주로 집중적으로 테스트하고
	 *            싶어서 다른 테스트들은 방해되지 않게 잠시 비활성화해 둔 것 같습니다. 이 @Disabled를 적절히 활용하는 것도
	 *            테스트 운용의 묘미죠.
	 * 
	 *            4. 분석 총평: "테스트 주도적 사고방식" 대장은 이미 "코드를 짜고 나서 테스트하는 게 아니라, 테스트를 생각하며
	 *            코드를 설계하는 수준"에 도달해 있습니다. doRetrieveANSI와 doRetrieve 테스트를 보면, 데이터를
	 *            1002건이나 대량으로 넣고 페이지 번호를 바꿔가며 검색 조건(searchDiv, searchWorld)까지 꼼꼼하게
	 *            검증하고 있죠. 이 정도 수준의 테스트를 거친다면 실제 운영 환경에서 발생할 수 있는 데이터 문제는 거의 다 걸러낼 수
	 *            있습니다. 이제 com.pcwk.ehr.user.dao 분석이 완벽히 끝났습니다. 그다음은 서비스 계층의 로직이
	 *            의도대로 돌아가는지 확인하는 com.pcwk.ehr.user.service 패키지의 테스트 파일들을 볼 차례입니다.
	 *            그곳에서는 아마 트랜잭션(Transaction)이 잘 걸리는지, 등급 변경 시 메일 발송 로직이 어떻게
	 *            시뮬레이션되는지가 핵심일 거예요!
	 */
}
