package com.pcwk.ehr.chat.handler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;
import com.pcwk.ehr.chat.domain.ChatMessageVO;
import com.pcwk.ehr.chat.service.ChatService;

public class ChatHandler extends TextWebSocketHandler {
	
	@Autowired
	private ChatService service;
	
	private Map<Integer, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		// 이 연결이 몇 번 방인지 알아내기
		int roomNo = getRoomNo(session);
		
		// 그 방 명단이 없으면 새로 만들고, 이 세션을 추가
		roomSessions
			.computeIfAbsent(roomNo, key -> new CopyOnWriteArraySet<>())
			.add(session);
	}

	

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		// 1. 받은 메시지를 꺼내서 VO로 변환
		String payLoad = message.getPayload();	// 프론트가 보낸 글자
		
		// JSON -> VO
		ChatMessageVO vo = new Gson().fromJson(payLoad, ChatMessageVO.class);
		
		// senderNo를 세션에서 꺼내 덮어쓰기
		Object senderNo = session.getAttributes().get("senderNo");
		vo.setSenderNo((Long) senderNo);
		
		// 2. DB에 저장
		ChatMessageVO saved = service.sendMessage(vo);
		
		// 3. 같은 방 사람에게 뿌리기
		int roomNo = vo.getChatRoomNo();
		Set<WebSocketSession> sessions = roomSessions.get(roomNo);
		
		if(sessions != null)
		{
			String json = new Gson().toJson(saved); // VO -> JSON 글자
			TextMessage broadCast = new TextMessage(json); // 보낼 메시지 만들기
			
			for(WebSocketSession s : sessions) // 그 방의 연결 모두에게
			{
				if(s.isOpen())
				{
					s.sendMessage(broadCast); // 하나씩 전달
				}
			}
		}
	}



	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		
		// 이 연결이 몇 번 방이었는지
		int roomNo = getRoomNo(session);
		
		// 그 방 명단을 가져와서
		Set<WebSocketSession> sessions = roomSessions.get(roomNo);
		
		// 명단이 있으면, 이 세션을 빼기
		if(sessions != null)
		{
			sessions.remove(session);
		}
		
		
	}
	
	// 임시 : 나중에 세션/파라미터에서 진짜 방번호를 꺼내도록 바꿀 것
	private int getRoomNo(WebSocketSession session) {
		
		Object roomNo = session.getAttributes().get("roomNo");
		return roomNo == null ? 0 : (int) roomNo;
	}

	
	
	

}
