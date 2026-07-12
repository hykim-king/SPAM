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
        // 테스트 전 환경 강제 구축
        jdbcTemplate.update("DELETE FROM TRANSACTIN_HIST");
    }

    @Test
    @DisplayName("비즈니스 로직: 동일인 거래 차단")
    void insertServiceExceptionTest() {
        TransactHistVO vo = new TransactHistVO();
        vo.setSellerNo(10L);
        vo.setReceiverNo(10L); // 같은 사람 -> 에러 발생 예상

        assertThrows(IllegalStateException.class, () -> {
            service.insertTransact(vo);
        });
    }
}