package com.pcwk.ehr.transact.service;

import java.util.List;
import com.pcwk.ehr.transact.domain.TransactHistVO;
import com.pcwk.ehr.product.domain.ProductVO;

public interface TransactService {
    int insertTransact(TransactHistVO vo);
    int updateTxStatus(long txId, String status);
    int deleteTransact(Long txId);
    int deleteAll();
    int totalCount();
    TransactHistVO selectByTxId(Long txId);
    List<TransactHistVO> selectAllTransactList();
    
    // 기존 기능 유지
    List<ProductVO> getAllProducts();
    
    // [추가] 상태 필터링을 위한 메서드 선언
    List<ProductVO> getAllProducts(String status);
}