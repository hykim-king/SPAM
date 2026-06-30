package com.pcwk.ehr.user.controller;

import static com.pcwk.ehr.user.service.UserServiceImpl.MIN_LOGIN_COUNT_FOR_SILVER;
import static com.pcwk.ehr.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.cmn.MessageVO;
import com.pcwk.ehr.user.domain.Grade;
import com.pcwk.ehr.user.domain.UserVO;
import com.pcwk.ehr.user.mapper.UserMapper;

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
	
	MockMvc mockMvc;
	
	@Autowired
	UserMapper userMapper;
	
	List<UserVO> members;
	
	DTO dto;
	
	@BeforeEach
	void setUp() throws Exception {
		log.debug("******************");
		log.debug("@@BeforeEach");
		log.debug("******************");
		
		mockMvc=MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		
		members = Arrays.asList(
				new UserVO("pcwk01", "이상무01", "4321a", MIN_LOGIN_COUNT_FOR_SILVER-1, MIN_RECOMMEND_FOR_GOLD-30, "tnals5035@naver.com", Grade.BASIC, "등록일_사용안함"),
				// BASIC -> SILBER
				new UserVO("pcwk02", "이상무02", "4321a", MIN_LOGIN_COUNT_FOR_SILVER, MIN_RECOMMEND_FOR_GOLD-29, "tnals5035@naver.com", Grade.BASIC, "등록일_사용안함"),
				new UserVO("pcwk03", "이상무03", "4321a", MIN_LOGIN_COUNT_FOR_SILVER+1, MIN_RECOMMEND_FOR_GOLD-1, "tnals5035@naver.com", Grade.SILVER, "등록일_사용안함"),
				// SILVER -> GOLD
				new UserVO("pcwk04", "이상무04", "4321a", MIN_LOGIN_COUNT_FOR_SILVER+5, MIN_RECOMMEND_FOR_GOLD, "tnals5035@naver.com", Grade.SILVER, "등록일_사용안함"),
				new UserVO("pcwk05", "이상무05", "4321a", MIN_LOGIN_COUNT_FOR_SILVER+49, MIN_RECOMMEND_FOR_GOLD+1, "tnals5035@naver.com", Grade.GOLD, "등록일_사용안함")
		);
		
		dto = new DTO();
	}
	
	@Disabled
	@Test
	void messageToJSON() {
		MessageVO message = new MessageVO();
		message.setId("1");
		message.setMessage("회원등록 성공");
		
		log.debug("message: "+message);
		
		//VO -> JSON변환
		Gson gson = new Gson();
		String jsonString = gson.toJson(message);
		log.debug("jsonString: "+jsonString);
	}
	
	@Test
	void doRetrieve() throws Exception {
		log.debug("--------------------");
		log.debug("@Test doRetrieve()");
		log.debug("--------------------");
		
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
//		dto.setSearchWorld("tnals@naver.com");
		
		// 3. url, method, param
		MockHttpServletRequestBuilder requestBuilder 
			= MockMvcRequestBuilders.get("/user/doRetrieve.do")
			.param("searchDiv", dto.getSearchDiv())
			.param("searchWorld", dto.getSearchWorld())
			.param("pageSize", String.valueOf(dto.getPageSize()))
			.param("pageNo", String.valueOf(dto.getPageNo()));
		
		// 3.1 호출
		ResultActions resultActions = mockMvc.perform(requestBuilder)
										.andExpect(status().isOk());
		
		//Model
		Map<String,Object> model = resultActions.andDo(print())
								.andReturn().getModelAndView().getModel();
		List<UserVO> list = (List<UserVO>) model.get("list");		
		for(UserVO vo:list) {
			log.debug(vo);
		}		
		
	}
	
	@Disabled
	@Test
	void doUpdate() throws Exception {
		log.debug("--------------------");
		log.debug("@Test doUpdate()");
		log.debug("--------------------");
		// 테스트는 항상 동일한 결과가 나와야 하므로(Test Isolation) 데이터를 초기화 하고
		// 시작해야 합니다.
		// 1. 전체삭제
		// 2. 등급 있는 사용자 등록
		
		// 1.
		userMapper.deleteAll();
		assertEquals(0, userMapper.totalCnt());
		
		UserVO user01 = members.get(0);
		
		// 2.
		userMapper.doSave(user01);
		assertEquals(1, userMapper.totalCnt());
		
		// 3.
		UserVO outVO = userMapper.doSelectOne(user01);
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
		requestBuilder = MockMvcRequestBuilders.post("/user/doUpdate.do")
			.param("userId", user01.getUserId())
			.param("name", outVO.getName())
			.param("password", outVO.getPassword())
			.param("login", outVO.getLogin()+"")
			.param("recommend", outVO.getRecommend()+"")
			.param("email", outVO.getEmail())
			.param("grade", outVO.getGrade()+"");
		
		
		// 5. controller호출
		ResultActions resultActions = mockMvc.perform(requestBuilder)
									.andExpect(status().isOk());
		
		// 6. 호출결과
		String jsonResult = resultActions.andDo(print())
		.andReturn().getResponse().getContentAsString();
		log.debug("jsonResult: \n", jsonResult);
		
		// 5. JSON -> MessageVO
		MessageVO messageVO = new Gson().fromJson(jsonResult, MessageVO.class);
		log.debug("messageVO: \n"+messageVO);
		
		// 6. 회원등록 성공
		assertEquals("1", messageVO.getId());
		
		
		
		
	}
	
	@Disabled
	@Test
	void doDelete() throws Exception {
		log.debug("--------------------");
		log.debug("@Test doDelete()");
		log.debug("--------------------");
		// 테스트는 항상 동일한 결과가 나와야 하므로(Test Isolation) 데이터를 초기화 하고
		// 시작해야 합니다.
		// 1. 전체삭제
		// 2. 단건등록
		//------------------------------------------------
		// 3. 단건삭제(URL)
		
		// 1.
		userMapper.deleteAll();
		assertEquals(0, userMapper.totalCnt());
		
		// 2.
		UserVO user01 = members.get(0);
		userMapper.doSave(user01);
		assertEquals(1, userMapper.totalCnt());
		
		// 3. url, method, param
		MockHttpServletRequestBuilder requestBuilder 
					= MockMvcRequestBuilders.get("/user/doDelete.do")
					.param("userId", user01.getUserId());
	
		// 3.1 호출
		ResultActions resultActions = mockMvc.perform(requestBuilder)
										.andExpect(status().isOk());
		
		// 4. 호출결과
		String jsonResult = resultActions.andDo(print())
		.andReturn().getResponse().getContentAsString();
		log.debug("jsonResult: \n"+ jsonResult);
		
		// 5. JSON -> MessageVO
		MessageVO messageVO = new Gson().fromJson(jsonResult, MessageVO.class);
		log.debug("messageVO: \n"+messageVO);
		
		// 6. 회원삭제 성공
		assertEquals("1", messageVO.getId());
		
	}
	
	
	
	@Disabled
	@Test
	void doSelectOne() throws Exception {
		log.debug("--------------------");
		log.debug("@Test doSelectOne()");
		log.debug("--------------------");
		// 테스트는 항상 동일한 결과가 나와야 하므로(Test Isolation) 데이터를 초기화 하고
		// 시작해야 합니다.
		// 1. 전체삭제
		// 2. 단건등록
		//------------------------------------------------
		// 3. 단건조회(URL)
		// 4. 데이터 조회 및 비교
		
		// 1.
		userMapper.deleteAll();
		assertEquals(0, userMapper.totalCnt());
		
		// 2.
		UserVO user01 = members.get(0);
		userMapper.doSave(user01);
		assertEquals(1, userMapper.totalCnt());
		
		// 3. url, method, param
		MockHttpServletRequestBuilder requestBuilder 
			= MockMvcRequestBuilders.get("/user/doSelectOne.do")
			.param("userId", user01.getUserId());
		// 3.1 호출
		ResultActions resultActions = mockMvc.perform(requestBuilder)
										.andExpect(status().isOk());
		
		//Model
		Map<String,Object> model = resultActions.andDo(print())
								.andReturn().getModelAndView().getModel();
		UserVO outVO = (UserVO) model.get("user");		
		isSameUser(user01, outVO);
		
		
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
	void dsSave() throws Exception {
		log.debug("--------------------");
		log.debug("@Test dsSave()");
		log.debug("--------------------");
		// 테스트는 항상 동일한 결과가 나와야 하므로(Test Isolation) 데이터를 초기화 하고
		// 시작해야 합니다.
		// 1. 전체삭제
		// 2. 등급 있는 사용자 등록
		
		// 1.
		userMapper.deleteAll();
		assertEquals(0, userMapper.totalCnt());
		
		UserVO user01 = members.get(0);
		
		// 2. url, method, param
		MockHttpServletRequestBuilder
		requestBuilder = MockMvcRequestBuilders.post("/user/doSave.do")
			.param("userId", user01.getUserId())
			.param("name", user01.getName())
			.param("password", user01.getPassword())
			.param("login", user01.getLogin()+"")
			.param("recommend", user01.getRecommend()+"")
			.param("email", user01.getEmail())
			.param("grade", user01.getGrade()+"");
		
		// 3. controller호출
		ResultActions resultActions = mockMvc.perform(requestBuilder)
									.andExpect(status().isOk());
		
		// 4. 호출결과
		String jsonResult = resultActions.andDo(print())
		.andReturn().getResponse().getContentAsString();
		log.debug("jsonResult: \n", jsonResult);
		
		// 5. JSON -> MessageVO
		MessageVO messageVO = new Gson().fromJson(jsonResult, MessageVO.class);
		log.debug("messageVO: \n"+messageVO);
		
		// 6. 회원등록 성공
		assertEquals("1", messageVO.getId());
		
	}

	@Disabled
	@Test
	void beans() {
		log.debug("******************");
		log.debug("@@test beans()");
		log.debug("******************");
		
		assertNotNull(webApplicationContext);
		assertNotNull(userMapper);
		assertNotNull(mockMvc);
	}

}
