/**
 * 파일명: TransactService.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-11 <br>
 * 설 명: 거래내역 서비스 인터페이스 <br>
 */
package com.pcwk.ehr.transact.service;

import java.util.List;
import java.util.Map;
import com.pcwk.ehr.transact.domain.TransactHistVO;

public interface TransactService {

    int insertTransact(TransactHistVO vo);
    
    int updateTxStatus(long txId, String status);
    
    int deleteTransact(Long txId);
    
    int deleteAll();
    
    int totalCount();
    
    TransactHistVO selectByTxId(Long txId);
    
    List<TransactHistVO> selectListByUser(Map<String, Object> param);
    
    List<TransactHistVO> selectListByProduct(Long productNo, Long loginUserNo);
}