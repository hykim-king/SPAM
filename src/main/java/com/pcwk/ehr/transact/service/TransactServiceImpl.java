package com.pcwk.ehr.transact.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pcwk.ehr.transact.domain.TransactHistVO;
import com.pcwk.ehr.transact.domain.TransacHistSearchDTO;
import com.pcwk.ehr.transact.mapper.TransactHistMapper;
import com.pcwk.ehr.product.domain.ProductVO;

@Service
public class TransactServiceImpl implements TransactService {
    
    @Autowired 
    private TransactHistMapper transactHistMapper;

    @Override
    public int insertTransact(TransactHistVO vo) {
        if (vo != null && vo.getSellerNo() != null && vo.getSellerNo().equals(vo.getReceiverNo())) {
            throw new IllegalArgumentException("판매자와 구매자는 같을 수 없습니다.");
        }
        return transactHistMapper.insertTransact(vo);
    }
    @Override public int updateTxStatus(long txId, String status) { return transactHistMapper.updateTxStatus(txId, status); }
    @Override public int deleteTransact(Long txId) { return transactHistMapper.deleteTransact(txId); }
    @Override public int deleteAll() { return transactHistMapper.deleteAll(); }
    
    @Override public int totalCount(TransacHistSearchDTO dto) { return transactHistMapper.totalCount(dto); }
    
    @Override public TransactHistVO selectByTxId(Long txId) { return transactHistMapper.selectByTxId(txId); }
    @Override public TransactHistVO selectProductDetail(Long productNo) { return transactHistMapper.selectProductDetail(productNo); }
    @Override public List<TransactHistVO> selectAllTransactList() { return transactHistMapper.selectAllTransactList(); }

    @Override
    public List<TransactHistVO> selectTransactListPaged(TransacHistSearchDTO dto) {
        return transactHistMapper.selectTransactListPaged(dto);
    }

    @Override
    public List<ProductVO> selectAdminProductListPaged(TransacHistSearchDTO dto) {
        return transactHistMapper.selectAdminProductListPaged(dto);
    }

    @Override
    public int adminProductTotalCount(TransacHistSearchDTO dto) {
        return transactHistMapper.adminProductTotalCount(dto);
    }

    @Override
    public int getTotalCount(TransacHistSearchDTO dto) {
        return transactHistMapper.totalCount(dto); 
    }
}
