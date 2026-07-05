package com.pcwk.ehr.chat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pcwk.ehr.chat.domain.ChatRoomVO;

@Mapper // 매퍼 인터페이스
public interface ChatRoomMapper {
	
	/**
	 * 상품 + 구매자로 기존 방 있나 조회
	 * @param param
	 * @return
	 */
	Integer selectRoomByUser(ChatRoomVO param);
	
	/**
	 * 방 번호로 방 조회
	 * @param chatRoomNo
	 * @return
	 */
	ChatRoomVO selectRoomByNo(int chatRoomNo);
	
	/**
	 * 내 채팅방 목록
	 * @param memberNo
	 * @return
	 */
	List<ChatRoomVO>selectRoomListByMember(Long userNo);
	
	/**
	 * 새 채팅방 저장
	 * @param param
	 * @return
	 */
	int insertRoom(ChatRoomVO param);
	
	/**
	 * 마지막 메시지 시각 갱신
	 * @param chatRoomNo
	 * @return
	 */
	int updateLastMsgDt(int chatRoomNo);
	
	/**
	 * 판매자 나가기 처리
	 * @param chatRoomNo
	 * @return
	 */
	int updateSellerExit(int chatRoomNo);
	
	/**
	 * 구매자 나가기 처리
	 * @param chatRoomNo
	 * @return
	 */
	int updateBuyerExit(int chatRoomNo);
	
	/**
	 * 방 삭제
	 * @param chatRoomNo
	 * @return
	 */
	int deleteRoom(int chatRoomNo);
	
	
	
}
