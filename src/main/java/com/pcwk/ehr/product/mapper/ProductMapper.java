package com.pcwk.ehr.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pcwk.ehr.product.domain.ProductSearchDTO;
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
    public List<ProductVO> doRetrieve(ProductSearchDTO search);

    /** 2026-07-14 [추가] 메인 오늘의 추천에 표시할 판매중 상품을 무작위로 조회한다. */
    List<ProductVO> selectRandomProducts(@Param("limit") int limit);

    /** 검색 조건에 맞는 상품 총 건수 */
    public int totalCnt(ProductSearchDTO search);
    
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
   
 // ProductMapper.java 파일 내에 아래 메서드가 선언되어 있어야 합니다.
    public List<ProductVO> doRetrieveAll(String status);
    
}
