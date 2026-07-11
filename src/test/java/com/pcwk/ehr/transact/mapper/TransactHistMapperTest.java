/**
 * 파일명: TransactHistMapperTest.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-11 <br>
 * 설 명: 거래내역 매퍼 테스트  <br>
 */
package com.pcwk.ehr.transact.mapper;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.pcwk.ehr.transact.domain.TransactHistVO;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransactHistMapperTest {

    @Autowired
    private TransactHistMapper mapper;

    @BeforeEach
    void setUp() {
        mapper.deleteAll(); // 테스트 전 데이터 초기화
    }

    @Test
    @Order(1)
    @DisplayName("매퍼: 등록 및 전체 건수")
    void insertAndCount() {
        TransactHistVO vo = new TransactHistVO();
        vo.setSellerNo(10L);
        vo.setProductNo(200L);
        vo.setReceiverNo(20L);
        vo.setTxType("01");
        vo.setAmount(10000L);
        vo.setTxStatus("01");

        int flag = mapper.insertTransact(vo);
        assertEquals(1, flag);
        assertEquals(1, mapper.totalCount());
    }
}