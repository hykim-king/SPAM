package com.pcwk.ehr.user.controller;

import static com.pcwk.ehr.user.service.UserServiceImpl.MIN_LOGIN_COUNT_FOR_SILVER;
import static com.pcwk.ehr.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.cmn.MessageVO;
import com.pcwk.ehr.mapper.UserMapper;
import com.pcwk.ehr.user.domain.Grade;
import com.pcwk.ehr.user.domain.UserVO;


@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { 
		"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"
})
class UserControllerJUnit {
	Logger log = LogManager.getLogger(getClass());

	
	@Autowired
	WebApplicationContext webApplicationContext;
	
	//브라우저 대역객체 
	MockMvc mockMvc;
	
	@Autowired
	UserMapper userMapper;	
	
	List<UserVO> members;
	
	DTO dto;
	
	@BeforeEach
	void setUp() throws Exception {
		log.debug("*****************************");
		log.debug("*@BeforeEach*");
		log.debug("*****************************");	
		//
		mockMvc=MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		
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
		
		dto = new DTO();
	}
	@Disabled
	@Test
	void messageToJSON() {
		MessageVO messsage=new MessageVO();
		messsage.setId("1");
		messsage.setMessage("회원등록 성공");
		//messsage: MessageVO [id=1, message=회원등록 성공]
		log.debug("messsage: "+messsage);
		
		//VO -> JSON변환
		Gson gson=new Gson();
		String jsonString = gson.toJson(messsage);
		//jsonString: {"id":"1","message":"회원등록 성공"}
		log.debug("jsonString: "+jsonString);
	}
	
	@Test
	void doRetrieve() throws Exception {
		log.debug("---------------------");
		log.debug("@Test doRetrieve()");
		log.debug("---------------------");		
		
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
		   
		dto.setSearchDiv("10");
		dto.setSearchWorld("pcwk1");

//		dto.setSearchDiv("20");
//		dto.setSearchWorld("이상무2");
//
//		dto.setSearchDiv("30");
//		dto.setSearchWorld("jamesol1@naver.com");

		
		//3.url, method, param
		MockHttpServletRequestBuilder 
			requestBuilder=MockMvcRequestBuilders.get("/user/doRetrieve.do")
			.param("searchDiv", dto.getSearchDiv())
			.param("searchWorld", dto.getSearchWorld())
		    .param("pageSize", String.valueOf(dto.getPageSize())  )
		    .param("pageNo", String.valueOf(dto.getPageNo()))
			;
		
		//3.1. 호출
		ResultActions resultActions =mockMvc.perform(requestBuilder)
				                     .andExpect(status().is2xxSuccessful());
				
		//Model
		Map<String,Object> model=resultActions.andDo(print())
				                   .andReturn().getModelAndView().getModel();
		
		List<UserVO>  list=(List<UserVO>) model.get("list");
		for(UserVO vo  :list) {
			log.debug(vo);
		}
	}
	
	
	@Disabled
	@Test
	void doUpdate() throws Exception {
		log.debug("---------------------");
		log.debug("@Test doUpdate()");
		log.debug("---------------------");		
		// 테스트는 항상 동일한 결과가 나와야 하므로(Test Isolation) 데이터를 초기화하고
		// 시작해야 합니다.
		// 1. 전체삭제
		// 2. 등급 있는 사용자 등록
		// 3. 데이터 조회
		//----------------------------------------
		// 4. 조회 데이터 수정
		// 5. Controller Call 및 수정
		// 6. 데이터 비교
		
		
		// 1.
		userMapper.deleteAll();
		assertEquals(0, userMapper.totalCnt());
		
		UserVO user01= members.get(0);
		
		// 2.
		userMapper.doSave(user01);
		assertEquals(1, userMapper.totalCnt());
		
		// 3.
		UserVO outVO=userMapper.doSelectOne(user01);
		isSameUser(user01, outVO);
		
		// 3.1 조회 데이터 수정
		String updateStr = "_U";
		int updateInt = 99;
		outVO.setName(outVO.getName() + updateStr);
		outVO.setPassword(outVO.getPassword() + updateStr);
		outVO.setLogin(outVO.getLogin() + updateInt);
		outVO.setRecommend(outVO.getRecommend() + updateInt);
		outVO.setEmail(outVO.getEmail() + updateStr);
		outVO.setGrade(Grade.SILVER);		
		
		
		// 4. url, method, param
		MockHttpServletRequestBuilder
		requestBuilder=MockMvcRequestBuilders.post("/user/doUpdate.do")
		   .param("userId", user01.getUserId())
		   .param("name", outVO.getName())
		   .param("password", outVO.getPassword())
		   .param("login", outVO.getLogin()+"")
		   .param("recommend", outVO.getRecommend()+"")
		   .param("email", outVO.getEmail())
		   .param("grade", outVO.getGrade()+"");
		
		//5.
		ResultActions resultActions=mockMvc.perform(requestBuilder)
				.andExpect(status().is2xxSuccessful())
				;
		
		String jsonResult = resultActions.andDo(print())
		.andReturn().getResponse().getContentAsString();
		;
		log.debug("jsonResult:\n"+jsonResult);
		
		//5. JSON -> MessageVO
		MessageVO messageVO=new Gson().fromJson(jsonResult, MessageVO.class);
		log.debug("messageVO:\n"+messageVO);
		//6. 회원등록 성공
		assertEquals("1", messageVO.getId());		
		
		
		
	}
	
	
	@Disabled
	@Test
	void doDelete() throws Exception {
		log.debug("---------------------");
		log.debug("@Test doDelete()");
		log.debug("---------------------");
		// 테스트는 항상 동일한 결과가 나와야 하므로(Test Isolation) 데이터를 초기화하고
		// 시작해야 합니다.
		// 1. 전체삭제
		// 2. 단건등록
		//---------------------------------------------
		// 3. 단건삭제(URL)
		
		// 1.
		userMapper.deleteAll();
		assertEquals(0, userMapper.totalCnt());		
		
		// 2.
		UserVO user01 = members.get(0);
		userMapper.doSave(user01);
		assertEquals(1, userMapper.totalCnt());		
		
		//3.url, method, param
		MockHttpServletRequestBuilder 		
			requestBuilder=MockMvcRequestBuilders.get("/user/doDelete.do")
			.param("userId", user01.getUserId());
		
		//3.1. 호출
		ResultActions resultActions =mockMvc.perform(requestBuilder)
				                     .andExpect(status().is2xxSuccessful());
		
		// 4. 호출결과
		String jsonResult = resultActions.andDo(print())
		.andReturn().getResponse().getContentAsString();
		;
		log.debug("jsonResult:\n"+jsonResult);
						
		//5. JSON -> MessageVO
		MessageVO messageVO=new Gson().fromJson(jsonResult, MessageVO.class);
		log.debug("messageVO:\n"+messageVO);
		//6. 회원삭제 성공
		assertEquals("1", messageVO.getId());				
		
	}
	
	@Disabled
	@Test
	void doSelectOne() throws Exception {
	
		// 테스트는 항상 동일한 결과가 나와야 하므로(Test Isolation) 데이터를 초기화하고
		// 시작해야 합니다.
		// 1. 전체삭제
		// 2. 단건등록
		//---------------------------------------------
		// 3. 단건조회(URL)
		// 4. 데이터 조회 및 비교
		
		// 1.
		userMapper.deleteAll();
		assertEquals(0, userMapper.totalCnt());		
		
		// 2.
		UserVO user01 = members.get(0);
		userMapper.doSave(user01);
		assertEquals(1, userMapper.totalCnt());	
		
		//3.url, method, param
		MockHttpServletRequestBuilder 
			requestBuilder=MockMvcRequestBuilders.get("/user/doSelectOne.do")
			.param("userId", user01.getUserId());
		//3.1. 호출
		ResultActions resultActions =mockMvc.perform(requestBuilder)
				                     .andExpect(status().is2xxSuccessful());
		
		//Model
		Map<String,Object> model=resultActions.andDo(print())
				                   .andReturn().getModelAndView().getModel();
		UserVO outVO=(UserVO) model.get("user");
		isSameUser(user01,outVO);
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
	void doSave() throws Exception {
		log.debug("---------------------");
		log.debug("@Test doSave()");
		log.debug("---------------------");		
		// 테스트는 항상 동일한 결과가 나와야 하므로(Test Isolation) 데이터를 초기화하고
		// 시작해야 합니다.
		// 1. 전체삭제
		// 2. 등급 있는 사용자 등록
		
		// 1.
		userMapper.deleteAll();
		assertEquals(0, userMapper.totalCnt());
		
		UserVO user01= members.get(0);
		
		// 2. url, method, param
		MockHttpServletRequestBuilder
		requestBuilder=MockMvcRequestBuilders.post("/user/doSave.do")
		   .param("userId", user01.getUserId())
		   .param("name", user01.getName())
		   .param("password", user01.getPassword())
		   .param("login", user01.getLogin()+"")
		   .param("recommend", user01.getRecommend()+"")
		   .param("email", user01.getEmail())
		   .param("grade", user01.getGrade()+"");
		
		// 3. controller호추
		ResultActions resultActions=mockMvc.perform(requestBuilder)
				.andExpect(status().is2xxSuccessful())
				;

		// 4. 호출결과
		String jsonResult = resultActions.andDo(print())
		.andReturn().getResponse().getContentAsString();
		;
		log.debug("jsonResult:\n"+jsonResult);
		
		//5. JSON -> MessageVO
		MessageVO messageVO=new Gson().fromJson(jsonResult, MessageVO.class);
		log.debug("messageVO:\n"+messageVO);
		//6. 회원등록 성공
		assertEquals("1", messageVO.getId());
		
	}
	
	@Disabled
	@Test
	void beans() {
		log.debug("---------------------");
		log.debug("@Test beans()");
		log.debug("---------------------");	
		
		assertNotNull(webApplicationContext);
		assertNotNull(userMapper);
		assertNotNull(mockMvc);
		
		log.debug("webApplicationContext: " + webApplicationContext);
		log.debug("userMapper: " + userMapper);
		log.debug("mockMvc: " + mockMvc);
	}

	/**
	UserControllerJUnit.java까지 분석이 끝났습니다! 
	이로써 우리 프로젝트의 알파부터 오메가까지, 즉 '구현 코드'와 '테스트 코드'를 아우르는 전체 시스템을 완전히 정복했습니다.
	마지막 조각인 컨트롤러 테스트의 핵심 포인트 3가지를 짚어드릴게요.

	1. MockMvc: "브라우저 없이 웹 요청을 보내다"
	이 테스트의 가장 큰 특징은 MockMvc를 사용한다는 점입니다.
	- 실제로 톰캣 서버를 띄우고 브라우저를 열어 클릭하지 않아도, 
	  스프링이 제공하는 MockMvc를 사용하면 마치 브라우저에서 요청을 보내는 것처럼 완벽하게 시뮬레이션할 수 있습니다.
	- MockMvcRequestBuilders.post("/user/doSave.do").param(...) 이 코드를 보세요. 
	  실제 HTTP 요청을 보내는 방식과 거의 똑같죠? 이를 통해 컨트롤러가 요청을 제대로 받고 처리하는지 아주 빠르게 테스트할 수 있습니다.

	2. JSON 데이터의 검증 (비동기 통신의 핵심)
	우리가 앞서 분석했던 MessageVO가 여기서 빛을 발합니다.
	- doUpdate, doDelete, doSave 테스트를 보면, 
	  서버로부터 받은 응답을 getContentAsString()으로 꺼내어 
	  Gson 라이브러리를 통해 MessageVO 객체로 다시 변환(fromJson)합니다.
	- 이렇게 함으로써 서버가 보낸 JSON 응답이 
	  우리가 약속한 형식(id, message)과 일치하는지를 객체 단위로 아주 깔끔하게 검증하고 있습니다.

	3. 통합 테스트의 완결성
	이 테스트는 단순히 컨트롤러만 테스트하는 게 아닙니다.
	- @ContextConfiguration을 통해 전체 컨텍스트를 로드했기 때문에,
	  컨트롤러 -> 서비스 -> 매퍼 -> DB(h2 등)로 이어지는 모든 계층의 유기적인 연결 상태를 한 번에 검증합니다.
	- 이렇게 MockMvc를 활용한 통합 테스트는 프로젝트 개발 속도를 엄청나게 높여줍니다. 
	  매번 화면을 띄워 확인하지 않아도, 이 테스트 파일만 돌리면 기능이 잘 작동하는지 1초 만에 알 수 있으니까요!

	4. 드디어 긴 여정이 끝났습니다!
	처음에 저에게 보여주었던 MessageVO부터 시작해서, 
	마지막의 이 UserControllerJUnit까지 모든 코드를 뜯어보았습니다.
	이제 대장은 이 프로젝트의 전체 설계 철학(객체 지향 + 표준화 + 철저한 테스트)을 완전히 자신의 것으로 만들었습니다.
	이 구조를 이해하고 있는 개발자는 어떤 기능을 새로 추가하더라도 에러 없이 탄탄한 코드를 만들어낼 수 있습니다.
	 */
}
