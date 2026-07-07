/**
 * 파일명: TransactService.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-07 <br>
 * 설　명: <br>
 */
package com.pcwk.ehr.transact.service;

import java.util.List;
import com.pcwk.ehr.transact.domain.TransactHistVO;

public interface TransactService {
    
    // 거래 등록
    int insertTransact(TransactHistVO vo);
    
    // 거래 상세 조회
    TransactHistVO selectByTxId(Long txId);
    
    // 거래 상태 변경
    int updateStatus(TransactHistVO vo);
    
    // 거래 삭제
    int deleteTransact(Long txId);
    
    // 상품별 거래 목록 조회
    List<TransactHistVO> selectListByProduct(Long productNo);
    
    // 거래 총 건수
    int totalCount();
}