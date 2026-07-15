package com.pcwk.ehr.chat.service;

import java.util.List;

import com.pcwk.ehr.chat.domain.ChatMessageVO;
import com.pcwk.ehr.chat.domain.ChatRoomVO;

public interface ChatService {

	/**
	 * 입장한 방 정보를 화면에 보여주기
	 * @param chatRoomVO
	 * @return
	 */
	ChatRoomVO enterRoom(ChatRoomVO param);
	
	/**
	 * 내 채팅방 목록
	 * @param userNo
	 * @return
	 */
	List<ChatRoomVO> getRoomList(Long userNo);
	
	/**
	 * 저장된 메시지를 상대에게 전송
	 * @param chatMessageVO
	 * @return
	 */
	ChatMessageVO sendMessage(ChatMessageVO param);
	
	/**
	 * 이전 대화 목록을 화면에 뿌리기
	 * @param chatRoomNo
	 * @param readerNo
	 * @return
	 */
	List<ChatMessageVO> getMessageList(int chatRoomNo, Long readerNo);
	
	/**
	 * 채팅방 나가기
	 *
	 * 처리 순서:
	 *  1) selectRoomByNo로 방 정보 조회 (판매자/구매자 번호, 나가기 여부 확인)
	 *  2) 나가는 사람이 판매자면 updateSellerExit, 구매자면 updateBuyerExit 호출
	 *  3) 위 처리 후 판매자·구매자가 모두 나갔는지 판단
	 *  4) 둘 다 나갔으면 방 삭제 (deleteMessageByRoom → deleteRoom 순서)
	 *     - 한쪽만 나갔으면 나가기 표시만 하고 방은 유지
	 *
	 * @param chatRoomNo 나가려는 방 번호
	 * @param memberNo   나가는 회원 번호 (세션에서 꺼낸 로그인 회원번호)
	 * @return 처리 결과 행 수
	 */
	void exitRoom(int chatRoomNo, Long userNo);
	
	
}
