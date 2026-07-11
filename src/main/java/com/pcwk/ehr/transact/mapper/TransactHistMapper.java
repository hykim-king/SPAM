package com.pcwk.ehr.transact.mapper;

import java.util.List;
import java.util.Map;
import com.pcwk.ehr.transact.domain.TransactHistVO;

public interface TransactHistMapper {
    int insertTransact(TransactHistVO vo);
    TransactHistVO selectByTxId(Long txId);
    int updateStatus(TransactHistVO vo);
    int updateTxStatus(Map<String, Object> paramMap);
    int deleteTransact(Long txId);
    int deleteAll();
    int totalCount();
    
    List<TransactHistVO> selectListByUser(Map<String, Object> param);
    List<TransactHistVO> selectListByProduct(Map<String, Object> paramMap);
}