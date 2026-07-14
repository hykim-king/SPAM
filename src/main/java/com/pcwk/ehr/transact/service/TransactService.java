package com.pcwk.ehr.transact.service;

import java.util.List;
import com.pcwk.ehr.transact.domain.TransactHistVO;
import com.pcwk.ehr.transact.domain.TransacHistSearchDTO;
import com.pcwk.ehr.product.domain.ProductVO;

public interface TransactService {
    int insertTransact(TransactHistVO vo);
    int updateTxStatus(long txId, String status);
    int deleteTransact(Long txId);
    int deleteAll();
    
    // DTO를 받는 메서드로 통일
    int totalCount(TransacHistSearchDTO dto); 
    
    TransactHistVO selectByTxId(Long txId);
    TransactHistVO selectProductDetail(Long productNo);
    List<TransactHistVO> selectAllTransactList();
    
    List<ProductVO> selectProductListPaged(TransacHistSearchDTO dto);
    int getTotalCount(TransacHistSearchDTO dto);
}