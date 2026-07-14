package com.pcwk.ehr.transact.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

// 사용하는 도메인(VO/DTO) 클래스들을 가져옵니다.
import com.pcwk.ehr.transact.domain.TransactHistVO;
import com.pcwk.ehr.product.domain.ProductVO;
import com.pcwk.ehr.transact.domain.TransacHistSearchDTO;

// 이 인터페이스가 MyBatis의 매퍼(SQL 실행기)임을 스프링에게 알려줍니다.
@Mapper
public interface TransactHistMapper {
    
    // 거래 내역을 데이터베이스에 새로 저장합니다.
    int insertTransact(TransactHistVO vo);
    
    // 특정 거래(txId)의 상태(status)를 업데이트합니다. (예: 거래중 -> 거래완료)
    int updateTxStatus(@Param("txId") long txId, @Param("status") String status);
    
    // 특정 거래 내역을 삭제합니다.
    int deleteTransact(Long txId);
    
    // 거래 내역 전체를 삭제합니다.
    int deleteAll();
    
    // 데이터베이스에 저장된 전체 거래 내역의 개수를 조회합니다.
    int totalCount(TransacHistSearchDTO dto);
    
    // 특정 거래 아이디(txId)로 단건의 거래 정보를 조회합니다.
    TransactHistVO selectByTxId(Long txId);
    
    // 상품 번호(productNo)를 통해 상세 거래 정보를 조회합니다.
    TransactHistVO selectProductDetail(Long productNo);
    
    // 전체 거래 내역 목록을 리스트 형태로 조회합니다.
    List<TransactHistVO> selectAllTransactList();
    
    // [핵심] 검색 조건(DTO)을 받아 페이징 처리가 완료된 상품 목록을 조회합니다.
    // DTO의 페이지 번호와 검색 조건을 사용하여 DB에서 필요한 만큼만 데이터를 가져옵니다.
    List<ProductVO> selectProductListPaged(TransacHistSearchDTO dto);
}