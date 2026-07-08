package com.pcwk.ehr.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pcwk.ehr.product.domain.ProductImageVO;

@Mapper
public interface ProductImageMapper {
    /** 이미지 등록 (1건) */
    int doInsert(ProductImageVO image);

    /** 특정 상품의 이미지 목록 조회 */
    List<ProductImageVO> doRetrieveByProduct(@Param("productNo") int productNo);

    /** 특정 상품의 이미지 전체 삭제 (상품 삭제 시) */
    int doDeleteByProduct(@Param("productNo") int productNo);
}
