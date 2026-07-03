package com.pcwk.ehr.product.mapper;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pcwk.ehr.mapper.ProductMapper;
import com.pcwk.ehr.product.domain.ProductVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml",
    "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"
})
public class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    @Test
    public void testDoRetrieve() {
        ProductVO vo = new ProductVO();
        List<ProductVO> list = productMapper.doRetrieve(vo);
        
        // 데이터가 비어있지 않은지, 콘솔에 잘 찍히는지 확인
        assertNotNull(list);
        System.out.println("==========================================");
        System.out.println("상품 목록 개수: " + list.size());
        for(ProductVO product : list) {
            System.out.println(product.toString());
        }
        System.out.println("==========================================");
    }
}