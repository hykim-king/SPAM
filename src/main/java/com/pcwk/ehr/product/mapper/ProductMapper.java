package com.pcwk.ehr.product.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.pcwk.ehr.product.domain.ProductVO;

@Mapper
public interface ProductMapper {

    /**
     * 상품 등록
     * @param product
     * @return 등록 성공 건수 (1이면 성공)
     */
    public int doInsert(ProductVO product);

    /**
     * 상품 수정
     * @param product
     * @return 수정 성공 건수 (1이면 성공)
     */
    public int doUpdate(ProductVO product);

    /**
     * 상품 삭제
     * @param product
     * @return 삭제 성공 건수 (1이면 성공)
     */
    public int doDelete(ProductVO product);

    /**
     * 상품 단건 상세 조회
     * @param product
     * @return ProductVO
     */
    public ProductVO doSelectOne(ProductVO product);

    /**
     * 상품 전체 목록/검색 조회 (부모 DTO의 페이징/검색 조건을 활용하기 위해 파라미터로 ProductVO를 받습니다)
     * @return List<ProductVO>
     */
    public List<ProductVO> doRetrieve();
    
    /**
     * 조회수 증가
     * @param product
     * @return 수정 성공 건수
     */
    public int updateViewCount(ProductVO product);
    
    /** 거래상태 변경 (판매중/예약중/판매완료) */
    int updateStatus(ProductVO product);
    
    /** 채팅 수 + 1 */
    int plusChatCnt(ProductVO product);
    
    /** 채팅 수 -1 */
    int minusChatCnt(ProductVO product);
}