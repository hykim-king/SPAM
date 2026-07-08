/**
 * 파일명: TransactService.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-07 <br>
 * 설 명: 거래 비즈니스 로직 서비스 인터페이스 <br>
 */
package com.pcwk.ehr.transact.service;

import java.util.List;
import com.pcwk.ehr.transact.domain.TransactHistVO;

public interface TransactService {

    // 1. 거래 내역 등록
    int insertTransact(TransactHistVO vo);

    // 2. 특정 거래내역 조회
    TransactHistVO selectByTxId(Long txId);

    // 3. 거래 상태 업데이트
    int updateStatus(TransactHistVO vo);

    // 4. 특정 거래내역 삭제
    int deleteTransact(Long txId);

    // 5. 전체 거래내역 삭제
    int deleteAll();

    // 6. 특정 회원의 거래내역 조회
    List<TransactHistVO> selectListByUser(Long userNum);

    // 7. 상품별 거래 목록 조회 (정렬 조건인 sort 파라미터를 추가 선언)
    List<TransactHistVO> selectListByProduct(Long productNo, String sort);

    // 8. 전체 데이터 총 개수 조회
    int totalCount();
}