package com.pcwk.ehr.user.service;

import static com.pcwk.ehr.user.service.UserServiceImpl.MIN_LOGIN_COUNT_FOR_SILVER;
import static com.pcwk.ehr.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import com.pcwk.ehr.user.domain.Grade;
import com.pcwk.ehr.user.domain.UserVO;
import com.pcwk.ehr.user.mapper.UserMapper;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { 
		"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"
})
class UserServiceJUnit {

	final Logger log = LogManager.getLogger(UserServiceJUnit.class);
	
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
		log.debug("******************");
		log.debug("@@BeforeEach");
		log.debug("******************");

		members = Arrays.asList(
				new UserVO("pcwk01", "이상무01", "4321a", MIN_LOGIN_COUNT_FOR_SILVER-1, MIN_RECOMMEND_FOR_GOLD-30, "tnals5035@naver.com", Grade.BASIC, "등록일_사용안함"),
				// BASIC -> SILBER
				new UserVO("pcwk02", "이상무02", "4321a", MIN_LOGIN_COUNT_FOR_SILVER, MIN_RECOMMEND_FOR_GOLD-29, "tnals5035@naver.com", Grade.BASIC, "등록일_사용안함"),
				new UserVO("pcwk03", "이상무03", "4321a", MIN_LOGIN_COUNT_FOR_SILVER+1, MIN_RECOMMEND_FOR_GOLD-1, "tnals5035@naver.com", Grade.SILVER, "등록일_사용안함"),
				// SILVER -> GOLD
				new UserVO("pcwk04", "이상무04", "4321a", MIN_LOGIN_COUNT_FOR_SILVER+5, MIN_RECOMMEND_FOR_GOLD, "tnals5035@naver.com", Grade.SILVER, "등록일_사용안함"),
				new UserVO("pcwk05", "이상무05", "4321a", MIN_LOGIN_COUNT_FOR_SILVER+49, MIN_RECOMMEND_FOR_GOLD+1, "tnals5035@naver.com", Grade.GOLD, "등록일_사용안함")
		);
	}
	
	@Disabled
	@Test
	void doSave() {
		log.debug("******************");
		log.debug("@doSave()");
		log.debug("******************");
		// 테스트는 항상 동일한 결과가 나와야 하므로(Test Isolation) 데이터를 초기화 하고
		// 시작해야 합니다.
		// 1. 전체삭제
		// 2. 등급 있는 사용자 등록 비교
		// 3. 등급 없는 사용자 등록 비교: Grade.BASIC
		
		// 1.
		userMapper.deleteAll();
		assertEquals(0, userMapper.totalCnt());
		
		// 2. 등급 있는 사용자 등록 비교: pcwk02(SILVER)
		userService.doSave(members.get(1));
		assertEquals(1, userMapper.totalCnt());
		checkGrade(members.get(1), false);
		
		// 3. 등급 없는 사용자 등록 비교: pcwk05(BASIC)
		UserVO userNullGrade = members.get(4);
		userNullGrade.setGrade(null);
		
		log.debug("userNullGrade: ", userNullGrade);
		userService.doSave(userNullGrade);
		assertEquals(2, userMapper.totalCnt());
		checkGrade(userMapper.doSelectOne(userNullGrade), false);
		
	}
	
		
	//@Disabled
	@Test
	void upgradeLevels() throws SQLException {
		log.debug("******************");
		log.debug("@@upgradeLevels");
		log.debug("******************");
		// 테스트는 항상 동일한 결과가 나와야 하므로(Test Isolation) 데이터를 초기화 하고
		// 시작해야 합니다.
		// 1. 전체삭제
		// 2. 5명 등록
		// 3. 2명 등업
		// 4. 등업 대상 비교: pcwk02, pcwk04
		
		//1.
		userMapper.deleteAll();
		assertEquals(0, userMapper.totalCnt());
		
		//2.
		for(UserVO vo : members) {
			userMapper.doSave(vo);
		}
		assertEquals(members.size(), userMapper.totalCnt());
		
		//3.
		this.userService.upgradeLevels();
		
		//4.
		checkGrade(members.get(0),false);
		checkGrade(members.get(1),true);
		checkGrade(members.get(2),false);
		checkGrade(members.get(3),true);
		checkGrade(members.get(4),false);
	}

	/**
	 * 등업 확인
	 * @param user
	 * @param true(등업), false(not 등업)
	 */
	private void checkGrade(UserVO user, boolean upgraded) {
		UserVO updateGradeUser = userMapper.doSelectOne(user);

		//true(등업)
		if(true == upgraded) {
			assertEquals(updateGradeUser.getGrade(), user.getGrade().getNextLevel());
		//false(not 등업)	
		}else {
			assertEquals(updateGradeUser.getGrade(), user.getGrade());
		}
		
	}
	
	@AfterEach
	void tearDown() throws Exception {
		log.debug("******************");
		log.debug("@@AfterEach");
		log.debug("******************");
	}

	@Disabled
	@Test
	void beans() {
		log.debug("******************");
		log.debug("@Test beans()");
		log.debug("******************");

		assertNotNull(context);
		assertNotNull(userService);
		assertNotNull(userMapper);
		
		log.debug("context: " + context);
		log.debug("userService: " + userService);
		log.debug("userMapper: " + userMapper);

	}

}
