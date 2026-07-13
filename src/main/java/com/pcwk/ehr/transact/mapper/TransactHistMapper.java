package com.pcwk.ehr.transact.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.pcwk.ehr.transact.domain.TransactHistVO;
import com.pcwk.ehr.product.domain.ProductVO;
import com.pcwk.ehr.transact.domain.TransacHistSearchDTO;

@Mapper
public interface TransactHistMapper {
    int insertTransact(TransactHistVO vo);
    int updateTxStatus(@Param("txId") long txId, @Param("status") String status);
    int deleteTransact(Long txId);
    int deleteAll();
    int totalCount();
    TransactHistVO selectByTxId(Long txId);
    TransactHistVO selectProductDetail(Long productNo);
    List<TransactHistVO> selectAllTransactList();
    
    // [추가] 페이징 쿼리 연결
    List<ProductVO> selectProductListPaged(TransacHistSearchDTO dto);
}