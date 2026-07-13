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
    TransactHistVO selectProductDetail(Long productNo);
    List<TransactHistVO> selectAllTransactList();
    List<ProductVO> getAllProducts();
    List<ProductVO> getAllProducts(String status);
}