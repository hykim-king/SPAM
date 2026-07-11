package com.pcwk.ehr.transact.mapper;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.pcwk.ehr.transact.domain.TransactHistVO;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
	    "file:src/main/webapp/WEB-INF/spring/root-context.xml"
	})

public class TransactHistMapperTest {

    @Autowired
    TransactHistMapper mapper;
    
    @Autowired
    JdbcTemplate jdbcTemplate; 

    @BeforeEach
    void setUp() {
        mapper.deleteAll(); 
        try {
            jdbcTemplate.update("DELETE FROM TRANSACTIN_HIST"); 
            jdbcTemplate.update("DELETE FROM PRODUCT");
            jdbcTemplate.update("DELETE FROM USER_INFO");
            jdbcTemplate.update("DELETE FROM CATEGORY");
            
            jdbcTemplate.update("INSERT INTO CATEGORY (CATEGORY_NO, CATEGORY_NAME) VALUES (138, '아동복')");
            jdbcTemplate.update("INSERT INTO USER_INFO (USER_NUM, USER_NAME) VALUES (1, '판매자')");
            jdbcTemplate.update("INSERT INTO USER_INFO (USER_NUM, USER_NAME) VALUES (2, '구매자')");
            jdbcTemplate.update("INSERT INTO PRODUCT (PRODUCT_NO, PRODUCT_TITLE, USER_NUM, CATEGORY_NO, PRODUCT_CONTENT, PRICE, CREATE_DT) VALUES (100, '테스트상품', 1, 138, '테스트내용', 10000, SYSDATE)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testInsertAndSelect() {
        TransactHistVO vo = new TransactHistVO();
        vo.setSellerNo(1L);
        vo.setProductNo(100L); 
        vo.setReceiverNo(2L);
        vo.setTxType("01");
        vo.setAmount(15000L);
        vo.setTxStatus("01");

        int insertCnt = mapper.insertTransact(vo);
        
        assertEquals(1, insertCnt);
        assertEquals(1, mapper.totalCount());
    }
    
    @Test
    void testCRUD() {
        TransactHistVO vo = new TransactHistVO();
        vo.setSellerNo(1L);
        vo.setProductNo(100L);
        vo.setReceiverNo(2L);
        vo.setTxType("01");
        vo.setAmount(15000L);
        vo.setTxStatus("01");
        
        mapper.insertTransact(vo);
        
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("productNo", 100L);
        paramMap.put("loginUserNo", 1L); 
        
        List<TransactHistVO> list = mapper.selectListByProduct(paramMap);
        
        assertNotNull(list);
        assertFalse(list.isEmpty());
        
        TransactHistVO savedVo = list.get(0);
        Long txId = savedVo.getTxId();
        
        TransactHistVO foundVo = mapper.selectByTxId(txId);
        assertNotNull(foundVo);
        assertEquals(15000L, foundVo.getAmount());
    }
}