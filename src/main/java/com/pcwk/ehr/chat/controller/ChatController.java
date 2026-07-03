package com.pcwk.ehr.chat.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pcwk.ehr.chat.domain.ChatRoomVO;
import com.pcwk.ehr.chat.service.ChatService;
import com.pcwk.ehr.user.domain.UserVO;

@Controller
@RequestMapping("/chat")
public class ChatController {
	Logger log = LogManager.getLogger(getClass());
	
	@Autowired
	ChatService service;
	
	@PostMapping("/enterRoom.do")
	@ResponseBody
	public ChatRoomVO enterRoom(@ModelAttribute ChatRoomVO param, HttpSession session) {
		
		// 세션에서 로그인 사용자 꺼내기
		UserVO loginUser = (UserVO) session.getAttribute("loginUser");
		
		// param에 구매자번호를 세션 값으로 덮어쓰기
		param.setBuyerNo(loginUser.getUserNum());
		 
		// 서비스 호출해서 방 정보 받기
		ChatRoomVO outVO = service.enterRoom(param);
		 
		return outVO;
	}
	
	@GetMapping("/getRoomList.do")
	@ResponseBody
	public List<ChatRoomVO> getRoomList(HttpSession session) {
		
		// 세션에서 로그인 사용자 꺼내기
		UserVO loginUser = (UserVO) session.getAttribute("loginUser");
		
		// userNo 뽑기
		Long userNo = loginUser.getUserNum();
		
		// 채팅방 목록 뽑아서 반환
		return service.getRoomList(userNo);	
	}
	
	public List<ChatMessageVO> getmessageList(
			@)
	
	
}
