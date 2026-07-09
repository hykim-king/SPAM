package com.pcwk.ehr.transact.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.pcwk.ehr.transact.domain.TransactHistVO;

public interface TransactHistMapper {
    int insertTransact(TransactHistVO vo);
    TransactHistVO selectByTxId(Long txId);
    int updateStatus(TransactHistVO vo);
    int deleteTransact(Long txId);
    int deleteAll();
    int totalCount();
    List<TransactHistVO> selectListByUser(Long userNum);
    List<TransactHistVO> selectListByProduct(@Param("loginUserNo") Long loginUserNo, 
    @Param("sort") String sort);
}