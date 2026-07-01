package com.pcwk.ehr.user.service;

import static com.pcwk.ehr.user.service.UserServiceImpl.MIN_LOGIN_COUNT_FOR_SILVER;
import static com.pcwk.ehr.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.pcwk.ehr.mapper.UserMapper;
import com.pcwk.ehr.user.domain.Grade;
import com.pcwk.ehr.user.domain.UserVO;

//스프링 테스트(spring-test) 컨텍스트 프레임워크의 JUnit확장 기능
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { 
		"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"
})
class UserServiceJUnit {

	final static Logger log = LogManager.getLogger(UserServiceJUnit.class);

	@Autowired
	private ApplicationContext context;

//	사용자 등급 : BASIC, SILVER, GOLD
//	- BASIC : 최초가입
//	- SILVER : 가입 후 50회 이상 로그
//	- GOLD   : SILVER이면서 추천 30회
//
//	------------------------------------------
//	- 사용자 등업은 정기적으로 일관 등업(트랜잭션)
//	- 등업 대상들에게 EMAIL로 알림
//	==========================================	

	List<UserVO> members;

	@Autowired
	UserServiceImpl userService;

	@Autowired
	UserMapper userMapper;


	@BeforeEach
	void setUp() throws Exception {
		log.debug("*****************************");
		log.debug("*@BeforeEach*");
		log.debug("*****************************");

		members = Arrays.asList(
				new UserVO("pcwk01", "이상무01", "4321a", MIN_LOGIN_COUNT_FOR_SILVER - 1, MIN_RECOMMEND_FOR_GOLD - 30,
						"jamesol@naver.com", Grade.BASIC, "등록일_사용않함"),
				// BASIC -> SILVER
				new UserVO("pcwk02", "이상무02", "4321a", MIN_LOGIN_COUNT_FOR_SILVER, MIN_RECOMMEND_FOR_GOLD - 29,
						"jamesol@naver.com", Grade.BASIC, "등록일_사용않함"),
				new UserVO("pcwk03", "이상무03", "4321a", MIN_LOGIN_COUNT_FOR_SILVER + 1, MIN_RECOMMEND_FOR_GOLD - 1,
						"jamesol@naver.com", Grade.SILVER, "등록일_사용않함"),
				// SILVER -> GOLD
				new UserVO("pcwk04", "이상무04", "4321a", MIN_LOGIN_COUNT_FOR_SILVER + 5, MIN_RECOMMEND_FOR_GOLD,
						"jamesol@naver.com", Grade.SILVER, "등록일_사용않함"),
				new UserVO("pcwk05", "이상무05", "4321a", MIN_LOGIN_COUNT_FOR_SILVER + 49, MIN_RECOMMEND_FOR_GOLD + 1,
						"jamesol@naver.com", Grade.GOLD, "등록일_사용않함")

		);
	}

	@Disabled
	@Test
	void doSave() {
		log.debug("---------------------");
		log.debug("@Test doSave()");
		log.debug("---------------------");
		// 테스트는 항상 동일한 결과가 나와야 하므로(Test Isolation) 데이터를 초기화하고
		// 시작해야 합니다.
		// 1. 전체삭제
		// 2. 등급 있는 사용자 등록 비교: pcwk02
		// 3. 등급 없는 사용자 등록 비교: Grade.BASIC

		// 1.
		userMapper.deleteAll();
		assertEquals(0, userMapper.totalCnt());

		// 2. 등급 있는 사용자 등록 비교: pcwk02(BASIC)
		userService.doSave(members.get(1));
		assertEquals(1, userMapper.totalCnt());
		checkGrade(members.get(1), false);

		// 3. 등급 있는 사용자 등록 비교: pcwk05(BASIC)
		UserVO userNullGrade = members.get(4);
		userNullGrade.setGrade(null);

		log.debug("userNullGrade: " + userNullGrade);
		userService.doSave(userNullGrade);
		assertEquals(2, userMapper.totalCnt());
		checkGrade(userMapper.doSelectOne(userNullGrade), false);
	}

	//@Disabled
	@Test
	void upgradeLevels() throws Exception {
		log.debug("---------------------");
		log.debug("@Test upgradeLevels()");
		log.debug("---------------------");
		// 테스트는 항상 동일한 결과가 나와야 하므로(Test Isolation) 데이터를 초기화하고
		// 시작해야 합니다.
		// 1. 전체삭제
		// 2. 5명 등록
		// 3. 2명 등업
		// 4. 등업 대상 비교: pcwk02, pcwk04

		// 1.
		userMapper.deleteAll();
		assertEquals(0, userMapper.totalCnt());

		// 2.
		for (UserVO vo : members) {
			userMapper.doSave(vo);
		}

		assertEquals(members.size(), userMapper.totalCnt());

		// 3.
		this.userService.upgradeLevels();

		// 4.
		checkGrade(members.get(0), false);
		checkGrade(members.get(1), true);
		checkGrade(members.get(2), false);
		checkGrade(members.get(3), true);
		checkGrade(members.get(4), false);

	}

	/**
	 * 등업 확인
	 * 
	 * @param user
	 * @param true(등업), false(not 등업)
	 */
	private void checkGrade(UserVO user, boolean upgraded) {

		UserVO updateGradeUser = userMapper.doSelectOne(user);

		// true(등업)
		if (true == upgraded) {
			assertEquals(updateGradeUser.getGrade(), user.getGrade().getNextLevel());
			// false(not 등업)
		} else {
			assertEquals(updateGradeUser.getGrade(), user.getGrade());
		}

	}

	@AfterEach
	void tearDown() throws Exception {
		log.debug("*****************************");
		log.debug("*@AfterEach*");
		log.debug("*****************************");
	}

	@Disabled
	@Test
	void beans() {
		log.debug("---------------------");
		log.debug("@Test beans()");
		log.debug("---------------------");

		assertNotNull(context);
		assertNotNull(userService);
		assertNotNull(userMapper);


		log.debug("context: " + context);
		log.debug("userService: " + userService);
		log.debug("userMapper: " + userMapper);

	}

	/**
	대장, UserServiceJUnit.java는 우리 프로젝트의 비즈니스 로직(업무 규칙)을 검증하는 핵심 성지입니다. 
	DAO 테스트가 "데이터가 잘 들어가는가?"를 묻는다면, 이 테스트는 "우리 회사의 등급 정책이 정확히 계산되는가?"를 증명하고 있습니다.
	이 클래스에서 특히 인상 깊은 부분들을 짚어 드릴게요.

	1. 테스트용 데이터의 정교함 (Boundary Test)
	setUp() 메서드에 정의된 members 리스트를 보세요. 단순히 아무 데이터나 넣은 것이 아닙니다!
	- 경계값 테스트(Boundary Value Analysis):
		- MIN_LOGIN_COUNT_FOR_SILVER - 1: 등업 기준치 바로 직전(BASIC 유지)
		- MIN_LOGIN_COUNT_FOR_SILVER: 등업 기준치 딱 맞음(SILVER로 등업)
		- MIN_RECOMMEND_FOR_GOLD: GOLD 등업 기준치 딱 맞음
	이렇게 조건의 경계에 있는 값들을 테스트 데이터로 사용하여, 로직이 조건문을 얼마나 정확하게 판단하는지 확인하고 있습니다. 이건 아주 수준 높은 테스트 설계입니다.

	2. checkGrade 메서드의 통찰력
	private void checkGrade(UserVO user, boolean upgraded) 이 메서드는 테스트 코드의 가독성을 극대화합니다.
	- 의도 명확화: if (upgraded)를 통해 "이 사람은 등급이 올라야 정상이다", "아니면 그대로여야 정상이다"라는 비즈니스적 의도를 테스트 코드에 명확히 표현했습니다.
	단순히 값을 비교하는 것을 넘어, '무엇이 성공인가'에 대한 기준을 정의한 것이죠.

	3. 업무 시나리오 검증: upgradeLevels()
	이 테스트 메서드는 UserServiceImpl의 가장 중요한 로직을 검증합니다.
	- 5명의 회원을 등록하고 upgradeLevels()를 실행한 뒤, 각각의 회원이 의도대로 등급이 올랐는지(혹은 그대로인지) checkGrade로 순서대로 확인합니다.
	- 통합 테스트의 예시: DB와 연동되어 있고, 서비스 로직이 동작하고, 다시 DB를 확인하는 흐름이므로 이 테스트가 통과한다는 것은 
					'우리의 비즈니스 규칙이 시스템적으로 완벽하게 구현되었다'는 강력한 보증수표가 됩니다.

	4. 분석 현황
	이제 DAO(데이터 저장)와 Service(비즈니스 로직)의 테스트까지 완벽하게 파악했습니다.
	마지막 남은 테스트는 com.pcwk.ehr.user.controller 패키지입니다. 
	컨트롤러 테스트는 실제 웹 브라우저처럼 요청을 보내고 응답을 검증하는 방식(보통 MockMvc를 사용하죠)일 텐데, 
	이 녀석까지 보면 대장의 프로젝트 분석은 완전 정복입니다!
	 */
}
