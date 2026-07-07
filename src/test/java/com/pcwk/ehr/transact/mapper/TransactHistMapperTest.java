/**
 * 파일명: TransactHistMapperTest.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-07 <br>
 * 설　명: <br>
 */
package com.pcwk.ehr.transact.mapper;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.pcwk.ehr.transact.domain.TransactHistVO;
import java.util.List;

@ExtendWith(SpringExtension.class) // JUnit5 설정
@ContextConfiguration(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
public class TransactHistMapperTest {

    @Autowired
    TransactHistMapper mapper;

    @BeforeEach
    void setUp() {
    	System.out.println("@Test가 실행되기전에 무조건 1번은 @BeforeEach가 실행됨.");
        mapper.deleteAll(); // 테스트 전 전체 삭제로 데이터 초기화
    }

    //@Disabled
    @Test
    void testInsertAndSelect() {
        // 1. 데이터 생성
        TransactHistVO vo = new TransactHistVO();
        vo.setSellerNo(1L);
        vo.setProductNo(100L);
        vo.setReceiverNo(2L);
        vo.setTxType("01"); // 직거래
        vo.setAmount(15000L);

        // 2. Insert 테스트
        int insertCnt = mapper.insertTransact(vo);
        assertEquals(1, insertCnt);

        // 3. 전체 개수 확인
        assertEquals(1, mapper.totalCount());
    }
    
    @Disabled
    @Test
    void testCRUD() {
        // 1. 데이터 생성
        TransactHistVO vo = new TransactHistVO();
        vo.setSellerNo(1L);
        vo.setProductNo(100L);
        vo.setReceiverNo(2L);
        vo.setTxType("01");
        vo.setAmount(15000L);
        
        // [CREATE] 등록
        mapper.insertTransact(vo);
        
        // 방금 넣은 데이터의 PK(시퀀스값)를 알기 어렵다면 
        // 리스트 조회로 데이터를 가져와서 테스트합니다.
        List<TransactHistVO> list = mapper.selectListByProduct(100L);
        TransactHistVO savedVo = list.get(0);
        Long txId = savedVo.getTxId();
        
        // [READ] 상세 조회
        TransactHistVO foundVo = mapper.selectByTxId(txId);
        assertNotNull(foundVo);
        assertEquals(15000L, foundVo.getAmount());
        
        // [UPDATE] 상태 변경
        foundVo.setTxStatus("02"); // 완료로 변경
        int updateCnt = mapper.updateStatus(foundVo);
        assertEquals(1, updateCnt);
        
        // 변경 확인
        TransactHistVO updatedVo = mapper.selectByTxId(txId);
        assertEquals("02", updatedVo.getTxStatus());
        
        // [DELETE] 단건 삭제
        int deleteCnt = mapper.deleteTransact(txId);
        assertEquals(1, deleteCnt);
        assertEquals(0, mapper.totalCount());
    }
}