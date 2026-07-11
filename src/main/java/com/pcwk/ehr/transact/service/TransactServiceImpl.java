package com.pcwk.ehr.transact.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pcwk.ehr.transact.domain.TransactHistVO;
import com.pcwk.ehr.transact.mapper.TransactHistMapper;

@Service("transactService")
public class TransactServiceImpl implements TransactService {

    @Autowired
    private TransactHistMapper transactMapper;

    @Override
    public int insertTransact(TransactHistVO vo) {
        if (vo == null) throw new IllegalArgumentException("데이터가 없습니다.");
        if (vo.getSellerNo() == null || vo.getReceiverNo() == null || vo.getProductNo() == null) {
            throw new IllegalArgumentException("필수 데이터가 누락되었습니다.");
        }
        if (vo.getSellerNo().equals(vo.getReceiverNo())) {
            throw new IllegalStateException("판매자와 구매자는 같을 수 없습니다.");
        }
        vo.setTxStatus("01"); 
        return transactMapper.insertTransact(vo);
    }

    @Override
    public int updateStatus(TransactHistVO vo) {
        return transactMapper.updateStatus(vo);
    }

    @Override
    public int updateTxStatus(long txId, String status) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("txId", txId);
        paramMap.put("status", status);
        return transactMapper.updateTxStatus(paramMap);
    }

    @Override
    public TransactHistVO selectByTxId(Long txId) { 
        return transactMapper.selectByTxId(txId); 
    }

    @Override
    public List<TransactHistVO> selectListByUser(Map<String, Object> param) { 
        return transactMapper.selectListByUser(param); 
    }

    @Override
    public List<TransactHistVO> selectListByProduct(Long productNo, Long loginUserNo) {
        // [핵심 수정] 매퍼 인터페이스에 정의된 대로 Map을 생성하여 전달합니다.
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("productNo", productNo);
        paramMap.put("loginUserNo", loginUserNo);
        
        return transactMapper.selectListByProduct(paramMap);
    }

    @Override
    public int deleteTransact(Long txId) { 
        return transactMapper.deleteTransact(txId); 
    }

    @Override
    public int deleteAll() { 
        return transactMapper.deleteAll(); 
    }

    @Override
    public int totalCount() { 
        return transactMapper.totalCount(); 
    }
}