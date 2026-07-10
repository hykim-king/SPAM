package com.pcwk.ehr.transact.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.pcwk.ehr.transact.domain.TransactHistVO;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class TransactServiceImplTest {

    @Autowired
    private TransactService service;
    
    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        
        // 1. 거래 내역 삭제 (테스트 시작 전 초기화)
        jdbcTemplate.update("DELETE FROM TRANSACTIN_HIST"); 
        
        // 2. CATEGORY 데이터 확보 (CATEGORY_LEVEL, USE_YN 추가!)
        jdbcTemplate.update("MERGE INTO CATEGORY c USING (SELECT 1 as num FROM DUAL) d ON (c.CATEGORY_NO = d.num) " +
                            "WHEN NOT MATCHED THEN INSERT (CATEGORY_NO, CATEGORY_NAME, CATEGORY_LEVEL, USE_YN) " +
                            "VALUES (1, '기본', 1, 'Y')");
        
        // 3. USER_INFO 데이터 확보 (10번, 20번)
        jdbcTemplate.update("MERGE INTO USER_INFO u USING (SELECT 10 as n1, 20 as n2 FROM DUAL) d ON (u.USER_NUM = d.n1 OR u.USER_NUM = d.n2) " +
                            "WHEN NOT MATCHED THEN INSERT (USER_NUM, USER_ID, USER_NAME, PASSWORD, NICKNAME, PHONE_NUM, EMAIL, USER_ROLE, USER_STATUS) " +
                            "VALUES (d.n1, 'srvSeller', '판매자', '1234', '판매닉', '010-1111-1111', 's@test.com', '01', '01')");

        // 4. PRODUCT 데이터 확보
        Integer productCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM PRODUCT WHERE PRODUCT_NO = 200", Integer.class);
        if (productCount == 0) {
            jdbcTemplate.update("INSERT INTO PRODUCT (PRODUCT_NO, PRODUCT_TITLE, USER_NUM, CATEGORY_NO, PRODUCT_CONTENT, PRICE) " +
                                "VALUES (200, '서비스테스트상품', 10, 1, '상세내용입니다', 10000)");
        }
    }
    @Test
    @DisplayName("1. 서비스 등록 성공 로직 테스트")
    void insertServiceSuccessTest() {
        TransactHistVO vo = new TransactHistVO();
        vo.setSellerNo(10L);
        vo.setProductNo(200L);
        vo.setReceiverNo(20L); // 판매자와 구매자가 다름 (정상)
        vo.setTxType("02");
        vo.setAmount(30000L);
        // vo.setTxStatus("01"); // Service 구현체에서 자동으로 01을 세팅하므로 생략해봄
        
        int flag = service.insertTransact(vo);
        assertEquals(1, flag, "서비스를 통한 등록 실패");
    }

    @Test
    @DisplayName("2. 비즈니스 로직 예외: 판매자와 구매자가 같은 경우")
    void insertServiceExceptionTest() {
        TransactHistVO vo = new TransactHistVO();
        vo.setSellerNo(10L);
        vo.setProductNo(200L);
        vo.setReceiverNo(10L); // 구매자와 판매자를 같게 세팅! (의도적인 에러 유발)
        vo.setAmount(15000L);
        
        // TransactServiceImpl.java의 로직에 의해 IllegalStateException이 발생해야 성공하는 테스트
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            service.insertTransact(vo);
        });
        
        // 예외 메시지도 정확한지 확인
        assertEquals("판매자와 구매자는 같을 수 없습니다.", exception.getMessage());
    }
    
    @Test
    @DisplayName("3. 상태 변경 업데이트(Ajax용) 테스트")
    void updateTxStatusTest() {
        // given: 먼저 1건 등록
        TransactHistVO vo = new TransactHistVO();
        vo.setSellerNo(10L);
        vo.setProductNo(200L);
        vo.setReceiverNo(20L);
        vo.setAmount(1000L);
        service.insertTransact(vo);
        
        // 방금 넣은 데이터의 txId를 알아내기 위해 상품조회 활용
        List<TransactHistVO> list = service.selectListByProduct(200L, 10L);
        Long insertedTxId = list.get(0).getTxId();
        
        // when: 상태를 '02'(완료)로 변경
        int flag = service.updateTxStatus(insertedTxId, "02");
        
        // then: 업데이트 1건 성공 및 상태값 검증
        assertEquals(1, flag);
        TransactHistVO updatedVo = service.selectByTxId(insertedTxId);
        assertEquals("02", updatedVo.getTxStatus(), "상태가 02로 변경되지 않았습니다.");
    }
}