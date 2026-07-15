package com.pcwk.ehr.transact.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.pcwk.ehr.transact.domain.TransacHistSearchDTO;
import com.pcwk.ehr.transact.domain.TransactHistVO;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransactHistMapperTest {

    @Autowired
    private TransactHistMapper mapper;

    @Test
    @Order(1)
    @DisplayName("매퍼: 페이징 목록 조회 테스트")
    void selectTransactListPaged() {
        TransacHistSearchDTO dto = new TransacHistSearchDTO();
        dto.setPageNo(1);
        dto.setPageSize(10);

        List<TransactHistVO> list = mapper.selectTransactListPaged(dto);

        assertNotNull(list);
        System.out.println("조회된 거래 수: " + list.size());
    }

    @Test
    @Order(2)
    @DisplayName("매퍼: 전체 건수 조회 테스트")
    void totalCount() {
        TransacHistSearchDTO dto = new TransacHistSearchDTO();
        int count = mapper.totalCount(dto);
        
        assertTrue(count >= 0);
        System.out.println("전체 거래 수: " + count);
    }
}
