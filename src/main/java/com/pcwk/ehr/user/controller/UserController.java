package com.pcwk.ehr.user.controller;

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

import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.cmn.MessageVO;
import com.pcwk.ehr.user.domain.UserVO;
import com.pcwk.ehr.user.service.UserService;


@Controller
@RequestMapping("/user")
public class UserController {
	Logger log = LogManager.getLogger(getClass());

	@Autowired
	UserService service;
	
	public UserController() {
		super();
		log.debug("-----------------------------");
		log.debug("UserController()*");
		log.debug("-----------------------------");
	}
	
	//http://localhost:8080/ehr/user/doRetrieve.do
	@GetMapping("/doRetrieve.do")
	public String doRetrieve(
			@RequestParam(required = false, name = "searchDiv", defaultValue = "")String searchDiv,
			@RequestParam(required = false, name = "searchWorld", defaultValue = "")String searchWorld,
			@RequestParam(name = "pageSize", defaultValue = "10")int pageSize,
			@RequestParam(name = "pageNo", defaultValue = "1")int pageNo,
			Model model) {
		log.debug("-----------------------------");
		log.debug("doRetrieve()*");
		log.debug("-----------------------------");
		log.debug("1. searchDiv: "+searchDiv);
		log.debug("2. searchWorld: "+searchWorld);
		
		DTO dto = new DTO();
		dto.setSearchDiv(searchDiv);
		dto.setSearchWorld(searchWorld);
		dto.setPageSize(pageSize);
		dto.setPageNo(pageNo);
		
		log.debug("2.1 dto: ", dto);
		
		List<UserVO> list = service.doRetrieve(dto);
		
		model.addAttribute("list", list);
		// 뷰 : /WEB-INF/views/'user/user_list'.jsp		
		return "user/user_list";
	}
	
	//http://localhost:8080/ehr/user/doUpdate.do
	//post
	@PostMapping("/doUpdate.do")
	@ResponseBody
	public MessageVO doUpdate(@ModelAttribute UserVO param) {
		log.debug("-----------------------------");
		log.debug("doUpdate()*");
		log.debug("-----------------------------");
		log.debug("1. param: "+param);
		
		int flag = service.doUpdate(param);
		log.debug("2. return flag: " + flag);
		
		
		//JSON
		//flag == 1 -> 성공
		//flag == 0 -> 실패
		//message: id, message 
		String message ="";
		if(1==flag) {
			message = param.getName()+"님이 등록 되었습니다.";
		}else {
			message = param.getName()+"님이 등록 실패했습니다.";
		}
		
		
		MessageVO messsage = new MessageVO(flag+"",message);
		log.debug("3. messsage: " + messsage);
		
		return messsage;
	}
	
	//http://localhost:8080/ehr/user/doDelete.do?userId=good
	@GetMapping("/doDelete.do")
	@ResponseBody
	public MessageVO doDelete(@RequestParam(required = true)String userId) {
		log.debug("-----------------------------");
		log.debug("doDelete()*");
		log.debug("-----------------------------");
		
		log.debug("1. userId: "+ userId);
		UserVO param = new UserVO();
		param.setUserId(userId);
		log.debug("1.1 param: "+ param);
		int flag = service.doDelete(param);
		log.debug("2 flag: "+ flag);
		String message ="";
		
		if(1==flag) {
			message = param.getUserId()+"님이 삭제 되었습니다.";
		}else {
			message = param.getUserId()+"님이 삭제 실패했습니다.";
		}
		
		MessageVO messsage = new MessageVO(String.valueOf(flag),message);
		log.debug("3. messsage: " + messsage);
		
		return messsage;
		
	}
	
	
	//호출 URL http://localhost:8080/ehr/user/doSelectOne.do?userId=good
	//@RequestMapping(method = RequestMethod.GET, value = "/doSelectOne.do")
	@GetMapping("/doSelectOne.do")
	public String doSelectOne(@RequestParam(required = true)String userId, Model model) {
		log.debug("-----------------------------");
		log.debug("doSelectOne()*");
		log.debug("-----------------------------");
		
		log.debug("1. userId: "+ userId);
		UserVO param = new UserVO();
		param.setUserId(userId);
		log.debug("1.1 param: "+ param);
		
		UserVO outVO = service.doSelectOne(param);
		log.debug("2 outVO: "+ outVO);
		
		model.addAttribute("user", outVO);
		///WEB-INF/views/'user/user_mng'.jsp
		return "user/user_mng";//
	}
	
	//호출 URL http://localhost:8080/ehr/user/doSave.do?userId=good&password=4321a
	//호출 URL http://localhost:8080/ehr/user/doRetrieve.do
	//호출 URL http://localhost:8080/ehr/user/doDelete.do
	//호출 URL http://localhost:8080/ehr/user/doSelectOne.do?userId=good
	//호출 URL http://localhost:8080/ehr/user/doUpdate.do
	@PostMapping(value = "/doSave.do")
	@ResponseBody
	public MessageVO doSave(UserVO param) {
		log.debug("-----------------------------");
		log.debug("doSave()*");
		log.debug("-----------------------------");
				
		
		log.debug("1. param: "+param);
		int flag = service.doSave(param);
		log.debug("2. return flag: " + flag);
		
		//JSON
		//flag == 1 -> 성공
		//flag != 1 -> 실패
		//message: id, message 
		String message ="";
		if(1==flag) {
			message = param.getName()+"님이 등록 되었습니다.";
		}else {
			message = param.getName()+"님이 등록 실패했습니다.";
		}
		
		
		MessageVO messsage = new MessageVO(flag+"",message);
		log.debug("3. messsage: " + messsage);
		
		return messsage;
	}
	
}
