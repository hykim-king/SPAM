/**
 * 파일명: TransactHistMapper.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-11 <br>
 * 설 명: 거래내역 관리 매퍼 <br>
 */
package com.pcwk.ehr.transact.mapper;

import java.util.List;
import java.util.Map;
import com.pcwk.ehr.transact.domain.TransactHistVO;

public interface TransactHistMapper {

    /** 거래 내역 등록 */
    int insertTransact(TransactHistVO vo);

    /** 거래 내역 단건 조회 */
    TransactHistVO selectByTxId(Long txId);

    /** 거래 상태 변경 */
    int updateTxStatus(Map<String, Object> paramMap);

    /** 거래 내역 삭제 */
    int deleteTransact(Long txId);
    
    /** 전체 삭제 (테스트용) */
    int deleteAll();
    
    /** 총 건수 조회 */
    int totalCount();

    /** 사용자별 거래 목록 조회 */
    List<TransactHistVO> selectListByUser(Map<String, Object> param);

    /** 상품별 거래 목록 조회 */
    List<TransactHistVO> selectListByProduct(Map<String, Object> paramMap);
}