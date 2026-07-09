package com.pcwk.ehr.transact.service;

import java.util.List;
import com.pcwk.ehr.transact.domain.TransactHistVO;

public interface TransactService {
    int insertTransact(TransactHistVO vo);
    TransactHistVO selectByTxId(Long txId);
    int updateStatus(TransactHistVO vo);
    int deleteTransact(Long txId);
    int deleteAll();
    List<TransactHistVO> selectListByUser(Long userNum);
    List<TransactHistVO> selectListByProduct(Long loginUserNo, String sort);
    int totalCount();
}