package com.pcwk.ehr.transact.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
public class TransactHistMapperTest {

    @Autowired
    private TransactHistMapper mapper;
    
    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        
        // 1. 순서대로 삭제 (외래키 관계 역순)
        jdbcTemplate.update("DELETE FROM TRANSACTIN_HIST"); 
        jdbcTemplate.update("DELETE FROM PRODUCT");
        jdbcTemplate.update("DELETE FROM USER_INFO");
        jdbcTemplate.update("DELETE FROM CATEGORY"); // 카테고리도 삭제
        
        // 2. 카테고리 데이터 확보 (부모 키 생성!)
        jdbcTemplate.update("INSERT INTO CATEGORY (CATEGORY_NO, CATEGORY_NAME) VALUES (1, '기본카테고리')");
        
        // 3. USER_INFO 데이터 확보
        String insertUser1 = "INSERT INTO USER_INFO (USER_NUM, USER_ID, USER_NAME, PASSWORD, NICKNAME, PHONE_NUM, EMAIL, USER_ROLE, USER_STATUS) " +
                             "VALUES (1, 'seller', '판매자', '1234', '판매닉넴', '010-1111-1111', 'seller@test.com', '01', '01')";
        String insertUser2 = "INSERT INTO USER_INFO (USER_NUM, USER_ID, USER_NAME, PASSWORD, NICKNAME, PHONE_NUM, EMAIL, USER_ROLE, USER_STATUS) " +
                             "VALUES (2, 'buyer', '구매자', '1234', '구매닉넴', '010-2222-2222', 'buyer@test.com', '01', '01')";
                             
        jdbcTemplate.update(insertUser1);
        jdbcTemplate.update(insertUser2);
        
        // 4. PRODUCT 데이터 확보 (이제 FK 문제 해결!)
        jdbcTemplate.update("INSERT INTO PRODUCT (PRODUCT_NO, PRODUCT_TITLE, USER_NUM, CATEGORY_NO, PRODUCT_CONTENT, PRICE) " +
                            "VALUES (100, '매퍼테스트상품', 1, 1, '상품 상세 내용입니다.', 10000)");
    }
    @Test
    @DisplayName("1. 거래내역 단건 등록 및 조회 테스트")
    void insertAndSelectTest() {
        // given: 삽입할 데이터 준비
        TransactHistVO vo = new TransactHistVO();
        vo.setSellerNo(1L);
        vo.setProductNo(100L);
        vo.setReceiverNo(2L);
        vo.setTxType("01");
        vo.setAmount(50000L);
        vo.setTxStatus("01"); // 01: 거래중
        
        // when: 등록 실행
        int flag = mapper.insertTransact(vo);
        
        // then: 등록 성공 확인 (1건 삽입)
        assertEquals(1, flag, "등록 실패");
        
        // 추가 검증: 전체 건수가 1건인지 확인
        int totalCnt = mapper.totalCount();
        assertEquals(1, totalCnt);
    }

    @Test
    @DisplayName("2. 사용자 기준 거래내역 리스트 조회(구매내역) 테스트")
    void selectListByUserTest() {
        // given: 테스트용 데이터 1건 강제 삽입
        TransactHistVO vo = new TransactHistVO();
        vo.setSellerNo(1L);
        vo.setProductNo(100L);
        vo.setReceiverNo(2L);
        vo.setTxType("01");
        vo.setAmount(10000L);
        vo.setTxStatus("02");
        mapper.insertTransact(vo);
        
        // when: 구매자(2번) 입장에서 'purchase' 타입으로 조회
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userNum", 2L);
        paramMap.put("type", "purchase");
        
        List<TransactHistVO> list = mapper.selectListByUser(paramMap);
        
        // then: 리스트 검증
        assertNotNull(list);
        assertTrue(list.size() > 0, "조회된 구매 내역이 없습니다.");
        assertEquals("매퍼테스트상품", list.get(0).getProductName()); // 조인된 데이터 확인
        assertEquals("판매자", list.get(0).getPartnerName()); // 파트너(판매자) 이름 확인
    }
}