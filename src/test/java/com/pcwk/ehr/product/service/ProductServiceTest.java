package com.pcwk.ehr.product.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;

import com.pcwk.ehr.product.domain.ProductVO;
import com.pcwk.ehr.product.domain.ProductSearchDTO;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml",
    "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"
})
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    /** 상품 등록 */
    @Disabled
    @Test
    public void doInsert() {
        ProductVO param = new ProductVO();
        param.setUserNum(1L);
        param.setCategoryNo(101); // 소분류: 스마트폰
        param.setProductTitle("아이폰 17 팝니다");
        param.setProductContent("한 달 사용, 상태 좋아요");
        param.setProductCondition("01");
        param.setPrice(1200000);
        param.setLocation("서울 강남구");

        int flag = productService.doInsert(param, null);
        System.out.println("등록 결과 flag: " + flag);
        assertEquals(1, flag);
    }

    /** 전체 목록 조회 */
    @Disabled
    @Test
    public void doRetrieve() {
        List<ProductVO> list = productService.doRetrieve(new ProductSearchDTO());
        System.out.println("총 건수: " + list.size());
        for (ProductVO vo : list) {
            System.out.println(vo);
        }
    }

    /** 상세 조회 → 카테고리 경로 확인 */
    @Test
    public void doSelectOne() {
        ProductVO param = new ProductVO();
        param.setProductNo(19);   // 실제 등록된 상품번호로 변경

        ProductVO outVO = productService.doSelectOne(param);
        System.out.println("상세: " + outVO);
        if (outVO != null) {
            System.out.println("경로: "
                + outVO.getLargeName() + " > "
                + outVO.getMiddleName() + " > "
                + outVO.getSmallName());
        }
    }
}
