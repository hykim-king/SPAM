package com.pcwk.ehr.transact.service; // 트랜잭션 서비스 구현체 패키지 선언

import java.util.List; // 리스트 객체 임포트
import org.springframework.beans.factory.annotation.Autowired; // 의존성 주입을 위한 어노테이션 임포트
import org.springframework.stereotype.Service; // 서비스 계층임을 알리는 어노테이션 임포트
import com.pcwk.ehr.product.domain.ProductVO; // 상품 VO 임포트
import com.pcwk.ehr.transact.domain.TransactHistVO; // 트랜잭션 이력 VO 임포트
import com.pcwk.ehr.transact.domain.TransacHistSearchDTO; // 검색용 DTO 임포트
import com.pcwk.ehr.transact.mapper.TransactHistMapper; // 매퍼 인터페이스 임포트

@Service // 스프링 컨테이너에 서비스 빈으로 등록
public class TransactServiceImpl implements TransactService { // TransactService 인터페이스를 구현하는 클래스 정의
    @Autowired private TransactHistMapper transactHistMapper; // 매퍼 객체를 자동 의존성 주입(DI)

    @Override public int insertTransact(TransactHistVO vo) { return transactHistMapper.insertTransact(vo); } // 이력 등록 로직 수행
    @Override public int updateTxStatus(long txId, String status) { return transactHistMapper.updateTxStatus(txId, status); } // 상태 변경 로직 수행
    @Override public int deleteTransact(Long txId) { return transactHistMapper.deleteTransact(txId); } // 특정 이력 삭제 로직 수행
    @Override public int deleteAll() { return transactHistMapper.deleteAll(); } // 전체 이력 삭제 로직 수행
    @Override public int totalCount() { return transactHistMapper.totalCount(); } // 전체 이력 개수 조회 로직 수행
    @Override public TransactHistVO selectByTxId(Long txId) { return transactHistMapper.selectByTxId(txId); } // ID별 상세 조회 로직 수행
    @Override public TransactHistVO selectProductDetail(Long productNo) { return transactHistMapper.selectProductDetail(productNo); } // 상품 상세 조회 로직 수행
    @Override public List<TransactHistVO> selectAllTransactList() { return transactHistMapper.selectAllTransactList(); } // 전체 이력 목록 조회 로직 수행

    @Override
    public List<ProductVO> selectProductListPaged(TransacHistSearchDTO dto) { // 페이징 처리된 상품 목록 조회
        return transactHistMapper.selectProductListPaged(dto); // 매퍼를 통해 페이징 리스트 반환
    }

    @Override
    public int getTotalCount(TransacHistSearchDTO dto) { // 검색 조건에 따른 총 개수 조회
        // totalCount는 매개변수 없이 전체를 가져오는 기존 메서드 활용 // 기존 매퍼 메서드를 재사용하여 전체 개수 반환
        return transactHistMapper.totalCount(); // 매퍼의 전체 카운트 메서드 호출
    }
}