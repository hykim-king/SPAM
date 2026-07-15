package com.pcwk.ehr.product.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.pcwk.ehr.product.domain.ProductSearchDTO;
import com.pcwk.ehr.product.domain.ProductVO;

public interface ProductService {

	/** 상품 등록 (이미지 파일 포함) */
	int doInsert(ProductVO product, List<MultipartFile> files);

    /** 상품 수정 */
    int doUpdate(ProductVO product, List<MultipartFile> files);

    /** 상품 삭제 */
    int doDelete(ProductVO product);

    /** 상품 단건 상세 조회 (조회수 증가 포함) */
    ProductVO doSelectOne(ProductVO product);

    /** 조회수 증가 없이 상품 기본정보 조회 (내부 연동용) */
    ProductVO getProduct(int productNo);

    /** 상품 목록/검색 조회 */
    List<ProductVO> doRetrieve(ProductSearchDTO search);

    /** 메인 오늘의 추천용 판매중 상품 무작위 조회 */
    List<ProductVO> getRandomProducts(int limit);

    /** 상품 목록 총 건수 */
    int totalCnt(ProductSearchDTO search);
    
    /** 거래상태 변경 (판매중/예약중/판매완료) */
    int updateStatus(ProductVO product);
    
    /** 채팅 수 + 1 */
    int plusChatCnt(ProductVO product);
    
    /** 채팅 수 -1 */
    int minusChatCnt(ProductVO product);
}
