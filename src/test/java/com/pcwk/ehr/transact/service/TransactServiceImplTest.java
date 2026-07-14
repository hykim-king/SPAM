package com.pcwk.ehr.transact.service;

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import org.junit.jupiter.api.*;
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
        
        // 1. 기존 데이터 삭제 (테스트 환경 초기화)
        jdbcTemplate.update("DELETE FROM TRANSACTIN_HIST");
        
        // 2. 테스트용 샘플 데이터 강제 삽입
        // NOT NULL 컬럼(TX_TYPE 등)을 포함하여 5개 값을 삽입합니다.
        String sql = "INSERT INTO TRANSACTIN_HIST (TX_ID, PRODUCT_NO, AMOUNT, TX_STATUS, TX_TYPE) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, 100L, 1L, 5000, "01", "01"); 
    }

//    @Test
//    @DisplayName("서비스: 상세 조회 성공 테스트")
//    void selectProductDetail() {
//        // 1. 서비스 메서드 호출 (setUp에서 넣은 1L 상품번호 사용)
//        TransactHistVO detail = service.selectProductDetail(1L); 
//        
//        // 2. 결과 검증 (null이 아니어야 함)
//        assertNotNull(detail, "조회 결과가 null입니다. DB에 데이터가 있는지, 파라미터가 정확한지 확인하세요.");
//        System.out.println("조회된 상세 정보: " + detail.toString());
//    }

    @Test
    @DisplayName("비즈니스 로직: 동일인 거래 차단 테스트")
    void insertServiceExceptionTest() {
        TransactHistVO vo = new TransactHistVO();
        vo.setSellerNo(10L);
        vo.setReceiverNo(10L); // 같은 사람 -> 서비스에서 예외를 던지도록 설계됨
        
        // 서비스의 예외 처리 로직이 잘 작동하는지 확인
        assertThrows(Exception.class, () -> {
            service.insertTransact(vo);
        });
    }
}