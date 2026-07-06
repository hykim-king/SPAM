package com.pcwk.ehr.chat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pcwk.ehr.chat.domain.ChatMessageVO;

@Mapper // 매퍼 인터페이스
public interface ChatMessageMapper {

	/**
	 * 메시지 저장
	 * @param chatMessageVO
	 * @return 
	 */
	int insertMessage(ChatMessageVO chatMessageVO);
	
	/**
	 * 방 메시지 목록 조회
	 * @return
	 */
	List<ChatMessageVO> selectMessageListByRoom(int chatRoomNo);
	
	/**
	 * 메시지 읽음 여부
	 * @param chatRoomNo
	 * @param readerNo
	 * @return
	 */
	int updateReadYn(@Param("chatRoomNo") int chatRoomNo,
					 @Param("readerNo") Long readerNo);
	
	/**
	 * 채팅방 메시지 전체 삭제
	 * @param chatRoomNo
	 * @return
	 */
	int deleteMessageByRoom(int chatRoomNo);
}
