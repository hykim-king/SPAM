package com.pcwk.ehr.transact.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pcwk.ehr.product.domain.ProductVO;
import com.pcwk.ehr.transact.domain.TransactHistVO;
import com.pcwk.ehr.transact.domain.TransacHistSearchDTO;
import com.pcwk.ehr.transact.mapper.TransactHistMapper;

@Service
public class TransactServiceImpl implements TransactService {
    @Autowired private TransactHistMapper transactHistMapper;

    @Override public int insertTransact(TransactHistVO vo) { return transactHistMapper.insertTransact(vo); }
    @Override public int updateTxStatus(long txId, String status) { return transactHistMapper.updateTxStatus(txId, status); }
    @Override public int deleteTransact(Long txId) { return transactHistMapper.deleteTransact(txId); }
    @Override public int deleteAll() { return transactHistMapper.deleteAll(); }
    @Override public int totalCount() { return transactHistMapper.totalCount(); }
    @Override public TransactHistVO selectByTxId(Long txId) { return transactHistMapper.selectByTxId(txId); }
    @Override public TransactHistVO selectProductDetail(Long productNo) { return transactHistMapper.selectProductDetail(productNo); }
    @Override public List<TransactHistVO> selectAllTransactList() { return transactHistMapper.selectAllTransactList(); }

    @Override
    public List<ProductVO> selectProductListPaged(TransacHistSearchDTO dto) {
        return transactHistMapper.selectProductListPaged(dto);
    }

    @Override
    public int getTotalCount(TransacHistSearchDTO dto) {
        // totalCount는 매개변수 없이 전체를 가져오는 기존 메서드 활용
        return transactHistMapper.totalCount();
    }
}