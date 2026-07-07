package com.pcwk.ehr.product.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pcwk.ehr.mapper.ProductMapper;
import com.pcwk.ehr.product.domain.ProductVO;

@Service
public class ProductServiceImpl implements ProductService {

    Logger log = LogManager.getLogger(getClass());
    
    private static final String STATUS_SELLING  = "01"; // 판매중
    private static final String STATUS_RESERVED = "02"; // 예약중
    private static final String STATUS_SOLD     = "03"; // 판매완료

    @Autowired
    private ProductMapper productMapper;

    @Override
    @Transactional
    public int doInsert(ProductVO product) {
        log.debug("1. doInsert(param): {}", product);

        // 1) 필수값/가격 검증
        validateForSave(product);

        // 2) 등록 기본값 세팅
        if (isEmpty(product.getStatus())) {
            product.setStatus(STATUS_SELLING); // 거래상태: 판매중
        }
        product.setViewCount(0); // 조회수
        product.setLikeCnt(0);   // 찜
        product.setChatCnt(0);   // 채팅수
        product.setAdminHideYn("N"); // 숨김상태

        int flag = productMapper.doInsert(product);
        log.debug("2. doInsert(flag): {}", flag);
        return flag;
    }

    @Override
    @Transactional
    public int doUpdate(ProductVO product) {
        log.debug("1. doUpdate(param): {}", product);

        checkOwner(product);        // 본인 상품만 수정
        validateForSave(product);   // 값 검증

        int flag = productMapper.doUpdate(product);
        log.debug("2. doUpdate(flag): {}", flag);
        return flag;
    }

    @Override
    @Transactional
    public int doDelete(ProductVO product) {
        log.debug("1. doDelete(param): {}", product);

        checkOwner(product);        // 본인 상품만 삭제

        int flag = productMapper.doDelete(product);
        log.debug("2. doDelete(flag): {}", flag);
        return flag;
    }

    @Override
    @Transactional
    public ProductVO doSelectOne(ProductVO product) {
        log.debug("1. doSelectOne(param): {}", product);

        // 상품이 존재하는지 확인
        ProductVO outVO = productMapper.doSelectOne(product);
        
        if (outVO != null) {
            // 판매자 본인이 아닐 때만 조회수 증가
            if (product.getSallerNo() != outVO.getSallerNo()) {
                productMapper.updateViewCount(product);
                outVO.setViewCount(outVO.getViewCount() + 1);
            }
        }

        log.debug("2. doSelectOne(outVO): {}", outVO);
        return outVO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVO> doRetrieve() {
    	
        List<ProductVO> list = productMapper.doRetrieve();
        return list;
    }
    
	@Override
	@Transactional
	public int updateStatus(ProductVO product) {
	    log.debug("1. updateStatus(param): {}", product);

	    checkOwner(product); // 본인 상품만 상태 변경 가능

	    String status = product.getStatus();
	    
	    if (!STATUS_SELLING.equals(status)
	            && !STATUS_RESERVED.equals(status)
	            && !STATUS_SOLD.equals(status)) {
	        throw new IllegalArgumentException("허용되지 않은 거래상태: " + status);
	    }

	    int flag = productMapper.updateStatus(product);
	    log.debug("2. updateStatus(flag): {}", flag);
	    
	    return flag;
	}

	@Override
	public int plusChatCnt(ProductVO product) {
		return productMapper.plusChatCnt(product);
	}

	@Override
	public int minusChatCnt(ProductVO product) {
		return productMapper.minusChatCnt(product);
	}
    
    // ───────────────────── 내부 로직 ─────────────────────

    /** 등록/수정 공통 검증 */
    private void validateForSave(ProductVO product) {
        if (product == null) {
            throw new IllegalArgumentException("상품 정보가 없습니다.");
        }
        if (isEmpty(product.getProductTitle())) {
            throw new IllegalArgumentException("상품명은 필수입니다.");
        }
        if (product.getCategoryNo() <= 0) {
            throw new IllegalArgumentException("카테고리를 선택하세요.");
        }
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        }
    }

    /** 로그인 회원이 해당 상품의 판매자인지 확인 */
    private void checkOwner(ProductVO product) {
        ProductVO dbVO = productMapper.doSelectOne(product);
        if (dbVO == null) {
            throw new IllegalStateException("존재하지 않는 상품입니다.");
        }
        if (dbVO.getSallerNo() != product.getSallerNo()) {
            throw new IllegalStateException("본인 상품만 수정/삭제할 수 있습니다.");
        }
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}