/**
 * 파일명: TransactServiceImpl.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-11 <br>
 * 설 명: 거래내역 서비스 구현체 <br>
 */
package com.pcwk.ehr.transact.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pcwk.ehr.transact.domain.TransactHistVO;
import com.pcwk.ehr.transact.mapper.TransactHistMapper;

@Service("transactService")
@Transactional
public class TransactServiceImpl implements TransactService {

    @Autowired
    private TransactHistMapper transactMapper;

    @Override
    public int insertTransact(TransactHistVO vo) {
        // 비즈니스 로직: 판매자와 구매자 검증
        if (vo.getSellerNo().equals(vo.getReceiverNo())) {
            throw new IllegalStateException("판매자와 구매자는 같을 수 없습니다.");
        }
        vo.setTxStatus("01"); // 기본값 '거래중' 설정
        return transactMapper.insertTransact(vo);
    }

    @Override
    public int updateTxStatus(long txId, String status) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("txId", txId);
        paramMap.put("status", status);
        return transactMapper.updateTxStatus(paramMap);
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
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("productNo", productNo);
        paramMap.put("loginUserNo", loginUserNo);
        return transactMapper.selectListByProduct(paramMap);
    }
}