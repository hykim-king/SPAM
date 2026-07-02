package com.pcwk.ehr.chat.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pcwk.ehr.chat.domain.ChatMessageVO;
import com.pcwk.ehr.chat.domain.ChatRoomVO;
import com.pcwk.ehr.chat.mapper.ChatMessageMapper;
import com.pcwk.ehr.chat.mapper.ChatRoomMapper;

@Service
// 스프링이 이 클래스를 Service 빈으로 등록
public class ChatServiceImpl implements ChatService {
	
	Logger log = LogManager.getLogger(getClass());
	
	@Autowired
	// 방 매퍼 주입
	private ChatRoomMapper chatRoomMapper;
	
	@Autowired
	// 메시지 매퍼 주입
	private ChatMessageMapper chatMessageMapper;

	@Override
	public ChatRoomVO enterRoom(ChatRoomVO param) {
		
		// 방번호로 기존 방 있나 조회
		Integer roomNo = chatRoomMapper.selectRoomByUser(param);
		
		if(null == roomNo)
		{
			// 방 없음 -> 새로 생성
			chatRoomMapper.insertRoom(param);
		} else
		{
			// 방 있음 -> 그 방번호를 param에 세팅 (재사용)
			param.setChatRoomNo(roomNo);
		}
		
		
		return param;
	}

	@Override
	public List<ChatRoomVO> getRoomList(int userNo) {
		
		// 회원 번호로 내가 판매자거나 구매자인 방 목록(List) 반환
		return chatRoomMapper.selectRoomListByMember(userNo);
	}

	@Override
	@Transactional
	public ChatMessageVO sendMessage(ChatMessageVO param) {
		
		// 메시지 저장
		chatMessageMapper.insertMessage(param);
		
		// 방 마지막 시각 갱신
		chatRoomMapper.updateLastMsgDt(param.getChatRoomNo());
		
		// 저장된 메시지 반환
		return param;
	}

	@Override
	public List<ChatMessageVO> getMessageList(int chatRoomNo, int readerNo) {
		
		int flag = chatMessageMapper.updateReadYn(chatRoomNo, readerNo);
		
		log.debug("flag: "+ flag);
		
		List<ChatMessageVO> outVO = chatMessageMapper.selectMessageListByRoom(chatRoomNo);
		
		return outVO;
	}

	@Override
	@Transactional
	public void exitRoom(int chatRoomNo, int userNo) {
		
		// 방 조회
		ChatRoomVO searchRoom = chatRoomMapper.selectRoomByNo(chatRoomNo);
		
		// 판매자냐 구매자냐 -> 해당 나가기 처리
		if(userNo == searchRoom.getSellerNo())
		{
			chatRoomMapper.updateSellerExit(chatRoomNo);
		} else if(userNo == searchRoom.getBuyerNo())
		{
			chatRoomMapper.updateBuyerExit(chatRoomNo);
		}
		
		// 나가기 후 최신 상태 다시 조회
		ChatRoomVO updated = chatRoomMapper.selectRoomByNo(chatRoomNo);
		
		// 둘 다 나갔으면 삭제 (메시지 먼저 삭제 -> 방 삭제)
		if("Y".equals(updated.getSellerExitYn()) && 
				"Y".equals(updated.getBuyerExitYn()))
		{
			int flag = chatMessageMapper.deleteMessageByRoom(chatRoomNo);
			log.debug("flag: "+ flag);
			
			flag = chatRoomMapper.deleteRoom(chatRoomNo);
			log.debug("flag: "+ flag);
		}
	}
}