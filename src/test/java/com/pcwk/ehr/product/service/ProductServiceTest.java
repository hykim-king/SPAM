package com.pcwk.ehr.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.pcwk.ehr.product.domain.ProductSearchDTO;
import com.pcwk.ehr.product.domain.ProductVO;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    /**
     * 상품 등록 테스트
     */
@Test
public void doInsert() {
    ProductVO param = new ProductVO();

    param.setUserNum(1L);
    param.setCategoryNo(101);
    param.setProductTitle("아이폰 17 팝니다");
    param.setProductContent("한 달 사용, 상태 좋아요");

    // 상품 품질 상태
    param.setProductCondition("사용감 적음");

    // 거래 상태: 01 판매중
    param.setStatus("01");

    param.setPrice(1200000);
    param.setLocation("서울 강남구");

    int flag = productService.doInsert(param, null);

    System.out.println("등록 결과 flag: " + flag);
    assertEquals(1, flag);
}

    /**
     * 전체 상품 목록 조회 테스트
     */
    @Test
    public void doRetrieve() {

        ProductSearchDTO searchDTO = new ProductSearchDTO();

        List<ProductVO> list = productService.doRetrieve(searchDTO);

        System.out.println("총 건수: " + list.size());

        for (ProductVO vo : list) {
            System.out.println(vo);
        }
    }

    /**
     * 상품 상세 조회 및 카테고리 경로 확인
     */
    @Test
    public void doSelectOne() {

        ProductVO param = new ProductVO();

        // 실제 존재하는 상품 번호로 변경 가능
        param.setProductNo(19);

        ProductVO outVO = productService.doSelectOne(param);

        System.out.println("상세: " + outVO);

        if (outVO != null) {
            System.out.println(
                "경로: "
                + outVO.getLargeName()
                + " > "
                + outVO.getMiddleName()
                + " > "
                + outVO.getSmallName()
            );
        }
    }
}