package com.pcwk.ehr.product.service;

import java.util.List;
import com.pcwk.ehr.product.domain.ProductVO;

public interface ProductService {

    /** 상품 등록 */
    int doInsert(ProductVO product);

    /** 상품 수정 */
    int doUpdate(ProductVO product);

    /** 상품 삭제 */
    int doDelete(ProductVO product);

    /** 상품 단건 상세 조회 (조회수 증가 포함) */
    ProductVO doSelectOne(ProductVO product);

    /** 상품 목록/검색 조회 */
    List<ProductVO> doRetrieve();
    
    /** 거래상태 변경 (판매중/예약중/판매완료) */
    int updateStatus(ProductVO product);
    
    /** 채팅 수 + 1 */
    int plusChatCnt(ProductVO product);
    
    /** 채팅 수 -1 */
    int minusChatCnt(ProductVO product);
}