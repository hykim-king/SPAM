package com.pcwk.ehr.transact.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.pcwk.ehr.transact.domain.TransactHistVO;

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
}