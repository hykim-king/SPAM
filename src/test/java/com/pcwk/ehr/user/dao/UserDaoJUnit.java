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
import com.pcwk.ehr.user.domain.Grade;
import com.pcwk.ehr.user.domain.UserVO;
import com.pcwk.ehr.user.mapper.UserMapper;

//스프링 테스트(spring-test) 컨텍스트 프레임워크의 JUnit확장 기능
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { 
		"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"
})
public class UserDaoJUnit {
	final Logger log = LogManager.getLogger(getClass());

	// field 주입
	@Autowired
	private UserMapper userMapper;
	
	private UserVO user01;
	private UserVO user02;
	private UserVO user03;

	private DTO dto;// paging/검색

	
	@Disabled
	@Test
	void doSave() {
		log.debug("----------------------------");
		log.debug("*doSave()*");
		log.debug("----------------------------");	
		//1. 전체삭제
		//2. 단건등록
		
		//1.
		userMapper.deleteAll();
		assertEquals(0, userMapper.totalCnt());
		
		//2.
		userMapper.doSave(user01);
		assertEquals(1, userMapper.totalCnt());
		
		isSameUser(user01, userMapper.doSelectOne(user01));
			
		
	}
	
	@Disabled
	@Test
	void deleteAll() {
		log.debug("----------------------------");
		log.debug("*deleteAll()*");
		log.debug("----------------------------");
		
		int flag = userMapper.deleteAll();
		log.debug("flag : " + flag);
	}
	
	@Disabled
	@Test
	void beans() {
		log.debug("----------------------------");
		log.debug("*beans()*");
		log.debug("----------------------------");
		assertNotNull(userMapper);
		log.debug("userMapper: " + userMapper);
	}

	@BeforeEach
	public void setUp() throws Exception {
		log.debug("*****************************");
		log.debug("*@BeforeEach*");
		log.debug("*****************************");

		
		user01 = new UserVO("pcwk01", "이상무01", "4321a", 1, 0, "tnals@naver.com", Grade.BASIC, "등록일_사용않함");
		user02 = new UserVO("pcwk02", "이상무02", "4321a", 50, 29, "tnals@naver.com", Grade.SILVER, "등록일_사용않함");
		user03 = new UserVO("pcwk03", "이상무03", "4321a", 51, 31, "tnals@naver.com", Grade.GOLD, "등록일_사용않함");

		dto = new DTO();
	}


	//@Disabled
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

		dto.setSearchDiv("10");
		dto.setSearchWorld("pcwk1");

//		dto.setSearchDiv("20");
//		dto.setSearchWorld("이상무2");

//		dto.setSearchDiv("30");
//		dto.setSearchWorld("tnals1@naver.com");

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

		//(expected = EmptyResultDataAccessException.class)
		
		assertThrows(EmptyResultDataAccessException.class,()->{ 
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

	@Disabled
	@Test
	public void test() {
		log.debug("---------------------");
		log.debug("@Test");
		log.debug("---------------------");

	}

	@Disabled
	@Test
	public void test2() {
		log.debug("---------------------");
		log.debug("@Test2");
		log.debug("---------------------");
	}


}