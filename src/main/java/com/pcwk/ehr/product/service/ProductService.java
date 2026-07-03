package com.pcwk.ehr.product.service;

import java.util.List;
import com.pcwk.ehr.product.domain.ProductVO;

public interface ProductService {

    /**
     * 상품 등록
     * @param product
     * @return 성공 시 1, 실패 시 0
     */
    public int doInsert(ProductVO product);

    /**
     * 상품 수정
     * @param product
     * @return 성공 시 1, 실패 시 0
     */
    public int doUpdate(ProductVO product);

    /**
     * 상품 삭제
     * @param product
     * @return 성공 시 1, 실패 시 0
     */
    public int doDelete(ProductVO product);

    /**
     * 상품 단건 상세 조회 (조회수 증가 로직 포함)
     * @param product
     * @return ProductVO
     */
    public ProductVO doSelectOne(ProductVO product);

    /**
     * 상품 목록 및 검색 조회
     * @param product
     * @return List<ProductVO>
     */
    public List<ProductVO> doRetrieve(ProductVO product);
}