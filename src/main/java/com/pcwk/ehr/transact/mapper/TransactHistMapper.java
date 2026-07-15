package com.pcwk.ehr.transact.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

// 사용하는 도메인(VO/DTO) 클래스들을 가져옵니다.
import com.pcwk.ehr.transact.domain.TransactHistVO;
import com.pcwk.ehr.transact.domain.TransacHistSearchDTO;
import com.pcwk.ehr.product.domain.ProductVO;

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
    
    /** 로그인 회원의 구매/판매 거래내역 페이징 조회 */
    List<TransactHistVO> selectTransactListPaged(TransacHistSearchDTO dto);

    /** 관리자 전체 상품 현황 페이징 조회 */
    List<ProductVO> selectAdminProductListPaged(TransacHistSearchDTO dto);

    int adminProductTotalCount(TransacHistSearchDTO dto);
}
