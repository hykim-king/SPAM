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
    int totalCount();
    TransactHistVO selectByTxId(Long txId);
    TransactHistVO selectProductDetail(Long productNo);
    List<TransactHistVO> selectAllTransactList();
    
    // [추가] 컨트롤러와 일치하는 메서드
    List<ProductVO> selectProductListPaged(TransacHistSearchDTO dto);
    int getTotalCount(TransacHistSearchDTO dto);
	//int getTototalCnt(TransacHistSearchDTO dto);
}