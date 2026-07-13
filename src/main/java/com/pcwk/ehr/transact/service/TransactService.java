package com.pcwk.ehr.transact.service; // 트랜잭션 서비스 패키지 선언[cite: 1]

import java.util.List; // 리스트 객체 사용을 위한 임포트[cite: 1]
import com.pcwk.ehr.transact.domain.TransactHistVO; // 트랜잭션 이력 VO 임포트[cite: 1]
import com.pcwk.ehr.transact.domain.TransacHistSearchDTO; // 트랜잭션 검색용 DTO 임포트[cite: 1]
import com.pcwk.ehr.product.domain.ProductVO; // 상품 VO 임포트[cite: 1]

public interface TransactService { // 트랜잭션 처리를 위한 서비스 인터페이스 정의[cite: 1]
    int insertTransact(TransactHistVO vo); // 새로운 트랜잭션 이력을 등록하는 메서드[cite: 1]
    int updateTxStatus(long txId, String status); // 트랜잭션 ID를 기준으로 상태를 수정하는 메서드[cite: 1]
    int deleteTransact(Long txId); // 특정 트랜잭션 ID의 이력을 삭제하는 메서드[cite: 1]
    int deleteAll(); // 모든 트랜잭션 이력을 삭제하는 메서드[cite: 1]
    int totalCount(); // 전체 트랜잭션 이력 개수를 조회하는 메서드[cite: 1]
    TransactHistVO selectByTxId(Long txId); // 트랜잭션 ID를 통해 상세 정보를 조회하는 메서드[cite: 1]
    TransactHistVO selectProductDetail(Long productNo); // 상품 번호를 통해 상품 상세 정보를 조회하는 메서드[cite: 1]
    List<TransactHistVO> selectAllTransactList(); // 모든 트랜잭션 이력 목록을 조회하는 메서드[cite: 1]
    
    // [추가] 컨트롤러와 일치하는 메서드 // 컨트롤러 연동을 위해 추가된 메서드 영역[cite: 1]
    List<ProductVO> selectProductListPaged(TransacHistSearchDTO dto); // 검색 조건을 포함하여 상품 목록을 페이징 조회하는 메서드[cite: 1]
    int getTotalCount(TransacHistSearchDTO dto); // 검색 조건에 맞는 전체 상품 개수를 조회하는 메서드[cite: 1]
	
}