package com.pcwk.ehr.transact.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.pcwk.ehr.transact.domain.TransactHistVO;

@Mapper
public interface TransactHistMapper {
    int insertTransact(TransactHistVO vo);
    
    // @Param 추가하여 파라미터 매핑 에러 해결
    int updateTxStatus(@Param("txId") long txId, @Param("status") String status);
    
    int deleteTransact(Long txId);
    int deleteAll();
    int totalCount();
    TransactHistVO selectByTxId(Long txId);
    List<TransactHistVO> selectAllTransactList();
}