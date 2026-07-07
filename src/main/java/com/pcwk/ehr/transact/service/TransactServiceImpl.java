/**
 * 파일명: TransactServiceImpl.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-07 <br>
 * 설　명: <br>
 */
package com.pcwk.ehr.transact.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pcwk.ehr.transact.domain.TransactHistVO;
import com.pcwk.ehr.transact.mapper.TransactHistMapper;

@Service("transactService")
@Transactional // 트랜잭션 관리
public class TransactServiceImpl implements TransactService {

    @Autowired
    private TransactHistMapper mapper;

    @Override
    public int insertTransact(TransactHistVO vo) {
        // 1. 필수값 체크
        if (vo == null || vo.getSellerNo() == null || vo.getProductNo() == null) {
            throw new IllegalArgumentException("거래 필수 정보가 누락되었습니다.");
        }
        
        // 2. 비즈니스 규칙 체크
        // 예: 본인 거래 확인
        if (vo.getSellerNo().equals(vo.getReceiverNo())) {
            throw new IllegalStateException("본인의 상품은 구매할 수 없습니다.");
        }
        
        // 3. 상품 상태/사용자 상태 체크
        // (보통 다른 Mapper를 호출하여 DB에서 상태를 조회한 뒤 비교)
        
        // 4. 이중 거래 방지
        // mapper.checkExistingTransaction(vo.getProductNo());
        
        return mapper.insertTransact(vo);
    }

    @Override
    public TransactHistVO selectByTxId(Long txId) {
        return mapper.selectByTxId(txId);
    }

    @Override
    public int updateStatus(TransactHistVO vo) {
        return mapper.updateStatus(vo);
    }

    @Override
    public int deleteTransact(Long txId) {
        return mapper.deleteTransact(txId);
    }

    @Override
    public List<TransactHistVO> selectListByProduct(Long productNo) {
        return mapper.selectListByProduct(productNo);
    }

    @Override
    public int totalCount() {
        return mapper.totalCount();
    }
}