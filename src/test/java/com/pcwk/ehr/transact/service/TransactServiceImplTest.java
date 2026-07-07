/**
 * 파일명: TransactServiceImplTest.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-07 <br>
 * 설　명: <br>
 */
package com.pcwk.ehr.transact.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pcwk.ehr.transact.domain.TransactHistVO;
import com.pcwk.ehr.transact.mapper.TransactHistMapper;

@ExtendWith(MockitoExtension.class) 
public class TransactServiceImplTest {

    @Mock
    private TransactHistMapper mapper; // Mock 객체 생성

    @InjectMocks
    private TransactServiceImpl service; // Mock 객체 주입

    private TransactHistVO validVO;

    @BeforeEach
    void setUp() {
        validVO = new TransactHistVO();
        validVO.setSellerNo(1L);
        validVO.setProductNo(100L);
        validVO.setReceiverNo(2L);
        validVO.setTxType("01");
        validVO.setAmount(10000L);
    }

    @Test
    void testInsertTransact_Success() {
        // Mock 동작 정의: 어떤 TransactHistVO가 들어와도 1을 반환하도록 설정
        when(mapper.insertTransact(any(TransactHistVO.class))).thenReturn(1);
        
        int result = service.insertTransact(validVO);
        
        assertEquals(1, result);
        verify(mapper, times(1)).insertTransact(any(TransactHistVO.class));
    }

    @Test
    void testInsertTransact_InvalidVO() {
        TransactHistVO invalidVO = new TransactHistVO();
        
        // 필수 값 누락 시 IllegalArgumentException 발생하는지 확인
        assertThrows(IllegalArgumentException.class, () -> {
            service.insertTransact(invalidVO);
        });
    }

    @Test
    void testInsertTransact_SelfTransaction() {
        validVO.setReceiverNo(1L); // 판매자와 구매자 동일
        
        // 본인 거래 시 IllegalStateException 발생하는지 확인
        assertThrows(IllegalStateException.class, () -> {
            service.insertTransact(validVO);
        });
    }
}