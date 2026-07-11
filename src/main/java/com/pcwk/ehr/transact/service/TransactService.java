package com.pcwk.ehr.transact.service;

import java.util.List;
import java.util.Map;
import com.pcwk.ehr.transact.domain.TransactHistVO;

public interface TransactService {
    int insertTransact(TransactHistVO vo);
    TransactHistVO selectByTxId(Long txId);
    int updateStatus(TransactHistVO vo);
    
    // 추가된 상태 업데이트 메서드
    int updateTxStatus(long txId, String status);
    
    int deleteTransact(Long txId);
    int deleteAll();
    int totalCount();
    
    List<TransactHistVO> selectListByUser(Map<String, Object> param);
 // TransactService.java 인터페이스 내부
    List<TransactHistVO> selectListByProduct(Long productNo, Long loginUserNo);
}