package com.pcwk.ehr.chat.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.pcwk.ehr.user.domain.UserVO;

public class ChatHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request,
								   ServerHttpResponse response,
								   WebSocketHandler wsHandler,
								   Map<String, Object> attributes) throws Exception {
		
		// 로그인 세션에서 회원 번호 꺼내기
		// request가 확실히 HTTP 기반일 때만 실행됨
		if(request instanceof ServletServerHttpRequest)
		{
			// 형변환 후 진짜 요청 꺼냄(ServerHttpRequest는 넓은 타입이므로 구체적 타입으로 변환필요)
			HttpServletRequest servletRequest
								= ((ServletServerHttpRequest) request).getServletRequest();
			// false : 세션이 있으면 주고, 없으면 null 새로 만들지 않는다
			HttpSession session = servletRequest.getSession(false);
			
			if(session != null)
			{
				UserVO loginUser = (UserVO) session.getAttribute("loginUser");
				if(loginUser != null)
				{
					// 소켓에 담아둠
					attributes.put("senderNo", loginUser.getUserNum());
				}
			}
			
			// url에서 방번호 꺼내기
			String roomNoParam = servletRequest.getParameter("roomNo");
			if(roomNoParam != null)
			{
				// 소켓에 담아둠
				attributes.put("roomNo", Integer.parseInt(roomNoParam));
			}
			
		}

		return super.beforeHandshake(request, response, wsHandler, attributes);
	}
	
	

	
}
