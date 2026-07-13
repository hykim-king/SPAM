package com.pcwk.ehr.transact.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.product.domain.ProductVO;
import com.pcwk.ehr.product.mapper.ProductMapper;
import com.pcwk.ehr.transact.domain.TransactHistVO;
import com.pcwk.ehr.transact.mapper.TransactHistMapper;

@Service
public class TransactServiceImpl implements TransactService {

    @Autowired
    private TransactHistMapper transactHistMapper;

    @Autowired
    private ProductMapper productMapper;

    // 기존 기능 유지 (전체 조회 시 null 전달)
    @Override
    public List<ProductVO> getAllProducts() {
        return productMapper.doRetrieveAll(null);
    }

    // [추가] 상태 필터링 기능 구현
    @Override
    public List<ProductVO> getAllProducts(String status) {
        return productMapper.doRetrieveAll(status);
    }

    @Override
    public int insertTransact(TransactHistVO vo) { return transactHistMapper.insertTransact(vo); }
    @Override
    public int updateTxStatus(long txId, String status) { return transactHistMapper.updateTxStatus(txId, status); }
    @Override
    public int deleteTransact(Long txId) { return transactHistMapper.deleteTransact(txId); }
    @Override
    public int deleteAll() { return transactHistMapper.deleteAll(); }
    @Override
    public int totalCount() { return transactHistMapper.totalCount(); }
    @Override
    public TransactHistVO selectByTxId(Long txId) { return transactHistMapper.selectByTxId(txId); }
    @Override
    public List<TransactHistVO> selectAllTransactList() { return transactHistMapper.selectAllTransactList(); }
}