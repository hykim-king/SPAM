package com.pcwk.ehr.chat.mapper;

import java.util.List;

import com.pcwk.ehr.chat.domain.ChatMessageVO;

public interface ChatMessageMapper {

	/**
	 * 메시지 저장
	 * @param chatMessageVO
	 * @return 
	 */
	int insertMessage(ChatMessageVO chatMessageVO);
	
	List<ChatMessageVO>selectMessageListByRoom();
	
}
