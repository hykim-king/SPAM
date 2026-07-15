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
import com.pcwk.ehr.product.domain.ProductVO;
import com.pcwk.ehr.product.service.ProductService;

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

	@Autowired
	// 2026-07-13 [추가] 상품별 채팅방 수를 PRODUCT.CHAT_CNT와 동기화한다.
	private ProductService productService;

	@Override
	@Transactional
	public ChatRoomVO enterRoom(ChatRoomVO param) {
		
	    // 상품 + 구매자로 기존 방 조회 (나간 방도 찾음)
	    Integer roomNo = chatRoomMapper.selectRoomByUser(param);

	    if(null == roomNo) {
	        // 방 없음 -> 새로 생성
	        int insertFlag = chatRoomMapper.insertRoom(param);
	        
	        if (insertFlag != 1) {
	            throw new IllegalStateException("채팅방 생성에 실패했습니다.");
	        }

	        // 2026-07-13 [추가] 신규 채팅방이 실제 생성된 경우에만 상품 채팅 수를 증가시킨다.
	        ProductVO product = new ProductVO();
	        product.setProductNo(param.getProductNo());
	        int chatCntFlag = productService.plusChatCnt(product);
	        if (chatCntFlag != 1) {
	            throw new IllegalStateException("상품 채팅 수 증가에 실패했습니다.");
	        }
	    } else {
	        // 방 있음 -> 재사용
	        param.setChatRoomNo(roomNo);

	        // 구매자가 나갔던 방이면 재입장 처리 (EXIT 해제)
	        chatRoomMapper.updateBuyerReenter(roomNo);
	    }

	    return param;
	}

	@Override
	public List<ChatRoomVO> getRoomList(Long userNo) {
		
		// 회원 번호로 내가 판매자거나 구매자인 방 목록(List) 반환
		return chatRoomMapper.selectRoomListByMember(userNo);
	}

	@Override
	@Transactional
	public ChatMessageVO sendMessage(ChatMessageVO param) {
		
	    // 메시지 저장 (성공 시 1)
	    int flag = chatMessageMapper.insertMessage(param);
	    log.debug("insertMessage flag: " + flag);

	    // 저장 실패 시 예외 → @Transactional이 롤백 처리
	    if (flag != 1) {
	        throw new RuntimeException("메시지 저장에 실패했습니다.");
	    }
	    
	    // 실시간(웹소켓) 전송 시 시간 표시를 위해 현재 시각을 세팅
	    // (DB는 SYSDATE로 저장되지만 param에는 없으므로 같은 포맷으로 채워줌)	    
	    param.setSendDt(java.time.LocalDateTime.now()
	            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

	    // 방 마지막 시각 갱신 (부가 작업이라 실패해도 로그만)
	    int updateFlag = chatRoomMapper.updateLastMsgDt(param.getChatRoomNo());
	    log.debug("updateLastMsgDt flag: " + updateFlag);
	    
	    // 웹소켓으로 뿌릴 때 보낸사람 닉네임 표시를 위해 조회해서 채움
	    String senderNick = chatMessageMapper.selectNicknameByUserNum(param.getSenderNo());
	    param.setSenderNick(senderNick);

	    // 저장된 메시지 반환
	    return param;
	}

	@Override
	public List<ChatMessageVO> getMessageList(int chatRoomNo, Long readerNo) {
		
		// 이 방의 참여자(판매자/구매자)가 맞는지 먼저 확인
		ChatRoomVO searchRoom = chatRoomMapper.selectRoomByNo(chatRoomNo);
		if(searchRoom == null
				|| !(readerNo.equals(searchRoom.getSellerNo()) || readerNo.equals(searchRoom.getBuyerNo()))) {
			// 참여자가 아니면 메시지를 주지 않음
			return java.util.Collections.emptyList();
		}
		
		int flag = chatMessageMapper.updateReadYn(chatRoomNo, readerNo);
		
		log.debug("flag: "+ flag);
		
		List<ChatMessageVO> outVO = chatMessageMapper.selectMessageListByRoom(chatRoomNo);
		
		return outVO;
	}

	@Override
	@Transactional
	public void exitRoom(int chatRoomNo, Long userNo) {
		
		// 방 조회
		ChatRoomVO searchRoom = chatRoomMapper.selectRoomByNo(chatRoomNo);
		
		// 이미 없는 방이면 조용히 종료
		if (searchRoom == null) {
		    return;
		}		
		
		// 판매자냐 구매자냐 -> 해당 나가기 처리
		if(userNo.equals(searchRoom.getSellerNo()))
		{
		    chatRoomMapper.updateSellerExit(chatRoomNo);
		} else if(userNo.equals(searchRoom.getBuyerNo()))
		{
		    chatRoomMapper.updateBuyerExit(chatRoomNo);
		}
		
		// 나가기 후 최신 상태 다시 조회
		ChatRoomVO updated = chatRoomMapper.selectRoomByNo(chatRoomNo);
		
		// 나가기 처리 중 방이 이미 삭제된 경우에는 종료한다.
		if (updated == null) {
			return;
		}

		// 둘 다 나갔으면 삭제 (메시지 먼저 삭제 -> 방 삭제)
		if("Y".equals(updated.getSellerExitYn()) && 
				"Y".equals(updated.getBuyerExitYn()))
		{
			int flag = chatMessageMapper.deleteMessageByRoom(chatRoomNo);
			log.debug("deleteMessageByRoom flag: " + flag);
			
			flag = chatRoomMapper.deleteRoom(chatRoomNo);

			log.debug("deleteRoom flag: " + flag);
			if (flag != 1) {
				throw new IllegalStateException("채팅방 삭제에 실패했습니다.");
			}

			// 2026-07-13 [추가] 채팅방이 실제 삭제된 경우 상품 채팅 수를 감소시킨다.
			ProductVO product = new ProductVO();
			product.setProductNo(searchRoom.getProductNo());
			int chatCntFlag = productService.minusChatCnt(product);
			if (chatCntFlag != 1) {
				throw new IllegalStateException("상품 채팅 수 감소에 실패했습니다.");
			}
		}
	}
}