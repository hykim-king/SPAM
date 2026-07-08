/**
 * 파일명: TransactServiceImpl.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-07 <br>
 * 설 명: 거래 비즈니스 로직 서비스 구현체 <br>
 */
package com.pcwk.ehr.transact.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pcwk.ehr.transact.domain.TransactHistVO;
import com.pcwk.ehr.transact.mapper.TransactHistMapper;

@Service
public class TransactServiceImpl implements TransactService {

    @Autowired
    private TransactHistMapper transactMapper;

    @Override
    public int insertTransact(TransactHistVO vo) {
        // 1. 유효성 검사: 데이터가 비어있거나 필수 정보가 없는 경우
        if (vo == null || vo.getSellerNo() == null || vo.getProductNo() == null || vo.getReceiverNo() == null) {
            throw new IllegalArgumentException("필수 거래 정보가 누락되었습니다.");
        }
        
        // 2. 비즈니스 규칙 검증: 판매자와 구매자가 같은지 확인
        if (vo.getSellerNo().equals(vo.getReceiverNo())) {
            throw new IllegalStateException("판매자와 구매자는 같을 수 없습니다.");
        }
        
        // 3. 정상 등록
        return transactMapper.insertTransact(vo);
    }

    @Override
    public TransactHistVO selectByTxId(Long txId) {
        return transactMapper.selectByTxId(txId);
    }

    @Override
    public int updateStatus(TransactHistVO vo) {
        return transactMapper.updateStatus(vo);
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
    public List<TransactHistVO> selectListByUser(Long userNum) {
        return transactMapper.selectListByUser(userNum);
    }

    @Override
    public List<TransactHistVO> selectListByProduct(Long productNo, String sort) {
        return transactMapper.selectListByProduct(productNo, sort);
    }

    @Override
    public int totalCount() {
        return transactMapper.totalCount();
    }
}