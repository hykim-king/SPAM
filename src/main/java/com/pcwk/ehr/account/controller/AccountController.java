package com.pcwk.ehr.account.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pcwk.ehr.account.domain.AccountVO;
import com.pcwk.ehr.account.service.AccountService;
import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.cmn.MessageVO;

@Controller
@RequestMapping("/account")
public class AccountController {
	Logger log = LogManager.getLogger(getClass());

	//@Autowired
	//UserService service;
	
	@Autowired
	AccountService service;

	public AccountController() {
		super();
		log.debug("---------------------------");
		log.debug("UserController()*");
		log.debug("---------------------------");
	}
	
	
	@GetMapping("/account_reg.do")
	public String accountReg() {
		log.debug("---------------------------");
		log.debug("accountReg()*");
		log.debug("---------------------------");	
		
		return "account/account_reg";
	}
	
	//http://localhost:8080/ehr/user/doRetrieve.do
	@GetMapping("/doRetrieve.do")
	public String doRetrieve(
		@RequestParam(required = false, name="searchDiv", defaultValue = "") String searchDiv,
		@RequestParam(required = false, name="searchWorld", defaultValue = "") String searchWorld,
		@RequestParam(name="pageSize", defaultValue = "10") int pageSize,
		@RequestParam(name="pageNo", defaultValue = "1") int pageNo,
		Model model) {
		log.debug("---------------------------");
		log.debug("doRetrieve()*");
		log.debug("---------------------------");	
		log.debug("1.searchDiv: "+searchDiv);
		log.debug("2.searchWorld: "+searchWorld);
		
		DTO dto=new DTO();
		dto.setSearchDiv(searchDiv);
		dto.setSearchWorld(searchWorld);
		dto.setPageSize(pageSize);
		dto.setPageNo(pageNo);
		
		log.debug("2.1 dto: "+dto);
		
		List<AccountVO>  list=service.doRetrieve(dto);
		
		model.addAttribute("list", list);
		// 뷰 : /WEB-INF/views/'user/user_list'.jsp
		return "account/account_list";
	}
	
	
	//http://localhost:8080/ehr/user/doUpdate.do
	//post
	@PostMapping("/doUpdate.do")
	@ResponseBody
	public MessageVO doUpdate(@ModelAttribute AccountVO param) {
		log.debug("---------------------------");
		log.debug("doUpdate()*");
		log.debug("---------------------------");	
		log.debug("1.param: "+param);
		
		int flag=service.doUpdate(param);
		log.debug("2. return flag: "+flag);
		
		
		//JSON
		//flag == 1 -> 성공
		//flag == 0 -> 실패
		//message: id, message
		String message = "";
		
		if(1==flag) {
			message = param.getAccountId()+"님이 수정 되었습니다.";
		}else {
			message = param.getAccountId()+"님이 수정 실패 했습니다.";
		}
		MessageVO messsage=new MessageVO(flag+"",message);
		log.debug("3. messsage: "+messsage);
		
		return messsage;	
	}
	
	
	//http://localhost:8080/ehr/user/doDelete.do?userId=good
	@GetMapping("/doDelete.do")
	@ResponseBody
	public MessageVO doDelete(@RequestParam(required = true)int accountId) {
		log.debug("---------------------------");
		log.debug("doDelete()*");
		log.debug("---------------------------");	
		log.debug("1.userId: "+accountId);
		
		AccountVO  param=new AccountVO();
		param.setAccountId(accountId);
		log.debug("1.1 param: "+param);
		int flag = service.doDelete(param);
		log.debug("2. flag: "+flag);
		String message = "";
		
		if(1==flag) {
			message = param.getAccountId()+"님이 삭제 되었습니다.";
		}else {
			message = param.getAccountId()+"님이 삭제 실패 했습니다.";
		}		
		MessageVO messsage=new MessageVO(String.valueOf(flag),message);
		log.debug("3. messsage: "+messsage);
		
		return messsage;
	}
	
	
	//URL: http://localhost:8080/ehr/user/doSelectOne.do?userId=good
	//@RequestMapping(method = RequestMethod.GET, value = "/doSelectOne.do")
	@GetMapping("/doSelectOne.do")
	public String doSelectOne(
		@RequestParam(required = true)
	int accountId, Model model) throws Exception {
		log.debug("---------------------------");
		log.debug("doSelectOne()*");
		log.debug("---------------------------");
		
		log.debug("1.accountId: "+accountId);
		AccountVO  param=new AccountVO();
		param.setAccountId(accountId);
		log.debug("1.1 param: "+param);
		
		AccountVO outVO=service.doSelectOne(param);
		log.debug("2. outVO: "+outVO);
		
		model.addAttribute("account", outVO);
		///WEB-INF/views/'user/user_mng'.jsp
		return "account/account_mng";//
	}

	// 호출 URL http://localhost:8080/ehr/user/doSave.do?userId=good&password=4321a
	// 호출 URL http://localhost:8080/ehr/user/doRetrieve.do
	// 호출 URL http://localhost:8080/ehr/user/doDelete.do
	// 호출 URL http://localhost:8080/ehr/user/doSelectOne.do?userId=good
	// 호출 URL http://localhost:8080/ehr/user/doUpdate.do
	@PostMapping(value = "/doSave.do" )
	//@RequestMapping(method = RequestMethod.POST, value = "/doSave.do")
	@ResponseBody
	public MessageVO doSave(@ModelAttribute AccountVO param) {
		log.debug("---------------------------");
		log.debug("doSave()*");
		log.debug("---------------------------");


		log.debug("1. param: "+param);
		int flag = service.doSave(param);
		log.debug("2. return flag: "+flag);
		
		
		//JSON
		//flag == 1 -> 성공
		//flag != 1 -> 실패
		//message: id, message
		String message = "";
		
		if(1==flag) {
			message = param.getAccountId()+"님이 등록 되었습니다.";
		}else {
			message = param.getAccountId()+"님이 등록 실패 했습니다.";
		}
		MessageVO messsage=new MessageVO(flag+"",message);
		log.debug("3. messsage: "+messsage);
		
		return messsage;
	}
}
	
	/**
	드디어 마지막 관문인 UserController.java까지 정복했습니다!
	이 클래스는 우리 프로젝트의 '최전방 관제탑'입니다. 
	브라우저에서 들어오는 모든 요청(URL)을 받아, 
	적절한 서비스(UserService)를 호출하고, 
	그 결과를 다시 화면으로 보내주는 역할을 수행하죠.

	전체적인 구조를 아주 쉽게 풀어드릴게요.

	1. 관제탑의 핵심 규칙
	@Controller: 이 클래스가 웹 요청을 처리하는 컨트롤러임을 명시합니다.
	@RequestMapping("/user"): 이 클래스로 들어오는 모든 URL은 /user로 시작해야 한다는 규칙입니다(예: /user/doSave.do).
	@Autowired UserService service: 컨트롤러는 직접 DB를 건드리지 않고, 오직 UserService에게 일을 시킵니다. 이것이 바로 역할 분담이죠.

	2. 두 가지 응답 방식 (이게 제일 중요합니다!)
	UserController는 상황에 따라 두 가지 방식으로 응답을 보냅니다.
	- 페이지 이동 방식 (String 반환)
		doRetrieve, doSelectOne 등은 return "user/user_list"; 처럼 문자열을 반환합니다.
	 	이건 "화면(JSP)을 새로 그려라"라는 뜻입니다. Model에 데이터를 담아서 넘겨주면, JSP에서 그 데이터를 출력하는 전형적인 MVC 방식이죠.
	- 데이터 응답 방식 (@ResponseBody)
		doSave, doUpdate, doDelete 등은 @ResponseBody가 붙어 있고 MessageVO를 반환합니다.
		이건 "화면 이동 없이, 결과값(성공/실패 메시지)만 JSON으로 던져줄 테니 자바스크립트로 알아서 처리해라"라는 비동기(AJAX) 방식입니다.
		아까 분석했던 MessageVO가 여기서 바로 이 용도로 쓰이는 것이죠!

	3. 코드 속 힌트들
	@ModelAttribute: 화면에서 넘어온 파라미터(userId, password 등)를 자동으로 UserVO 객체에 매핑해 줍니다. 
					 일일이 request.getParameter()를 할 필요가 없으니 코드가 정말 깨끗하죠.
	@RequestParam: 메서드 파라미터마다 필요한 값을 직접 매칭합니다. 
				   defaultValue를 활용해 검색어가 없거나 페이지 번호가 없을 때 기본값을 넣어주는 방어 로직이 아주 훌륭합니다.
				   
	분석 총평: "탄탄한 기초공사"
	이제 이 프로젝트의 시작부터 끝까지(URL -> Controller -> Service -> Mapper -> DB -> Domain) 모든 경로를 완벽하게 파악했습니다.
	
	cmn: 공통 규칙
	domain: 데이터 구조
	mapper: DB 연결
	service: 업무 로직
	controller: 요청 처리
	
	이 5개 패키지가 유기적으로 맞물려 돌아가는 모습을 보면, 
	이제 어떤 기능을 새로 추가하더라도 "아, 이건 여기서 정의하고, 저기서 로직을 짜면 되겠구나!"라는 감이 바로 오실 거예요.
	
	src/test/java로 이동
	 */

