package com.pcwk.ehr.account.controller;

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
import com.pcwk.ehr.account.domain.AccountVO;
import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.cmn.MessageVO;
import com.pcwk.ehr.mapper.AccountMapper;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { 
		"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"
})
class AccountControllerJUnit {

	Logger log = LogManager.getLogger(getClass());

	@Autowired
	WebApplicationContext webApplicationContext;
	
	MockMvc mockMvc;
	
	@Autowired
	AccountMapper accountMapper;
	
	List<AccountVO> accounts;
	
	DTO dto;
	
	@BeforeEach
	void setUp() throws Exception {
		log.debug("******************");
		log.debug("@@BeforeEach");
		log.debug("******************");
		
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		
		// 계좌 전용 더미 데이터 목록 세팅
		accounts = Arrays.asList(
				new AccountVO(1, 1, "기업은행", "100-111-1538", 0, "등록일 사용안함", "수정일 사용안함"),
				new AccountVO(2, 2, "기업은행", "100-222-1530", 10, "등록일 사용안함", "수정일 사용안함"),
				new AccountVO(3, 3, "기업은행", "100-333-1538", 109, "등록일 사용안함", "수정일 사용안함"),
				new AccountVO(4, 4, "기업은행", "100-444-1538", 9867, "등록일 사용안함", "수정일 사용안함"),
				new AccountVO(5, 5, "기업은행", "100-555-1538", 99538, "등록일 사용안함", "수정일 사용안함")
		);
		
		dto = new DTO();
	}
	
	@Disabled
	@Test
	void doRetrieve() throws Exception {
		log.debug("--------------------");
		log.debug("@Test doRetrieve()");
		log.debug("--------------------");
		
		final int SAVE_COUNT = 1002;

		// 1. 전체삭제
		accountMapper.deleteAll();
		assertEquals(0, accountMapper.totalCnt());

		// 2.  
		accountMapper.saveAll(SAVE_COUNT);
		assertEquals(SAVE_COUNT, accountMapper.totalCnt());

		dto.setPageNo(1);
		dto.setPageSize(10);
		
		// 3. url, method, param 매핑
		MockHttpServletRequestBuilder requestBuilder 
			= MockMvcRequestBuilders.get("/account/doRetrieve.do")
			.param("searchDiv", dto.getSearchDiv())
			.param("searchWorld", dto.getSearchWorld())
			.param("pageSize", String.valueOf(dto.getPageSize()))
			.param("pageNo", String.valueOf(dto.getPageNo()));
		
		// 3.1 호출
		ResultActions resultActions = mockMvc.perform(requestBuilder)
										.andExpect(status().isOk());
		
		// 4. Model 데이터 검증
		Map<String, Object> model = resultActions.andDo(print())
								.andReturn().getModelAndView().getModel();
		List<AccountVO> list = (List<AccountVO>) model.get("list");		
		for (AccountVO vo : list) {
			log.debug(vo);
		}		
	}
	@Disabled
	@Test
	void doSave() throws Exception {
		log.debug("--------------------");
		log.debug("@Test doSave()");
		log.debug("--------------------");
		
		// 1. 전체삭제
		accountMapper.deleteAll();
		assertEquals(0, accountMapper.totalCnt());
		
		AccountVO account01 = accounts.get(0);
		
		// 2. url, method, param
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/account/doSave.do")
			.param("accountId", account01.getAccountId() + "")
			.param("userNum", account01.getUserNum() + "")
			.param("bankName", account01.getBankName())
			.param("accountNum", account01.getAccountNum())
			.param("balance", account01.getBalance() + "");
		
		// 3. controller 호출
		ResultActions resultActions = mockMvc.perform(requestBuilder)
									.andExpect(status().isOk());
		
		// 4. 호출결과 JSON 꺼내기
		String jsonResult = resultActions.andDo(print())
				.andReturn().getResponse().getContentAsString();
		log.debug("jsonResult: \n" + jsonResult);
		
		// 5. JSON -> MessageVO 변환 및 검증
		MessageVO messageVO = new Gson().fromJson(jsonResult, MessageVO.class);
		assertEquals("1", messageVO.getId());
	}

	@Disabled
	@Test
	void doUpdate() throws Exception {
		log.debug("--------------------");
		log.debug("@Test doUpdate()");
		log.debug("--------------------");
		
		// 1. 전체삭제
		accountMapper.deleteAll();
		assertEquals(0, accountMapper.totalCnt());
		
		AccountVO account01 = accounts.get(0);
		
		// 2. 단건 등록
		accountMapper.doSave(account01);
		assertEquals(1, accountMapper.totalCnt());
		
		// 3. 수정
		String updateStr = "_U";
		int updateInt = 99;
		account01.setBankName(account01.getBankName() + updateStr);
		account01.setAccountNum(account01.getAccountNum() + updateStr);
		account01.setBalance(account01.getBalance() + updateInt);
		
		// 4. url, method, param
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/account/doUpdate.do")
			.param("accountId", account01.getAccountId() + "")
			.param("userNum", account01.getUserNum() + "")
			.param("bankName", account01.getBankName())
			.param("accountNum", account01.getAccountNum())
			.param("balance", account01.getBalance() + "");
		
		// 5. controller 호출
		ResultActions resultActions = mockMvc.perform(requestBuilder)
									.andExpect(status().isOk());
		
		// 6. 결과
		String jsonResult = resultActions.andDo(print())
				.andReturn().getResponse().getContentAsString();
		MessageVO messageVO = new Gson().fromJson(jsonResult, MessageVO.class);
		assertEquals("1", messageVO.getId());
	}
	
	@Disabled
	@Test
	void doDelete() throws Exception {
		log.debug("--------------------");
		log.debug("@Test doDelete()");
		log.debug("--------------------");
		
		// 1. 전체삭제
		accountMapper.deleteAll();
		assertEquals(0, accountMapper.totalCnt());
		
		// 2. 단건등록
		AccountVO account01 = accounts.get(0);
		accountMapper.doSave(account01);
		assertEquals(1, accountMapper.totalCnt());
		
		// 3. url, method, param
		MockHttpServletRequestBuilder requestBuilder 
					= MockMvcRequestBuilders.get("/account/doDelete.do")
					.param("accountId", account01.getAccountId() + "");
	
		// 3.1 호출
		ResultActions resultActions = mockMvc.perform(requestBuilder)
										.andExpect(status().isOk());
		
		// 4. 결과
		String jsonResult = resultActions.andDo(print())
				.andReturn().getResponse().getContentAsString();
		MessageVO messageVO = new Gson().fromJson(jsonResult, MessageVO.class);
		assertEquals("1", messageVO.getId());
	}
	
	@Test
	void doSelectOne() throws Exception {
		log.debug("--------------------");
		log.debug("@Test doSelectOne()");
		log.debug("--------------------");
		
		// 1. 전체삭제
		accountMapper.deleteAll();
		assertEquals(0, accountMapper.totalCnt());
		
		// 2. 단건등록
		AccountVO account01 = accounts.get(0);
		accountMapper.doSave(account01);
		assertEquals(1, accountMapper.totalCnt());
		
		// 3. url, method, param
		MockHttpServletRequestBuilder requestBuilder 
			= MockMvcRequestBuilders.get("/account/doSelectOne.do")
			.param("accountId", account01.getAccountId() + "");
		
		// 3.1 호출
		ResultActions resultActions = mockMvc.perform(requestBuilder)
										.andExpect(status().isOk());
		
		// 4. ModelAndView의 Model에서 데이터 꺼내기
		Map<String, Object> model = resultActions.andDo(print())
								.andReturn().getModelAndView().getModel();
		
		// 
		AccountVO outVO = (AccountVO) model.get("account");		
		isSameAccount(account01, outVO);
	}
	
	private void isSameAccount(AccountVO account01, AccountVO outVO) {
		assertEquals(account01.getAccountId(), outVO.getAccountId());
		assertEquals(account01.getUserNum(), outVO.getUserNum());
		assertEquals(account01.getBankName(), outVO.getBankName());
		assertEquals(account01.getAccountNum(), outVO.getAccountNum());
		assertEquals(account01.getBalance(), outVO.getBalance());
	}
	
	@Disabled
	@Test
	void beans() {
		log.debug("******************");
		log.debug("@@test beans()");
		log.debug("******************");
		
		assertNotNull(webApplicationContext);
		assertNotNull(accountMapper);
		assertNotNull(mockMvc);
	}
}