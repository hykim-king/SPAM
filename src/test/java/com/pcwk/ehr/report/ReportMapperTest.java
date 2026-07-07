/**
 * 파일명: ReportMapperTest.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-06 <br>
 * 설　명: <br>
 */
package com.pcwk.ehr.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.pcwk.ehr.report.domain.ReportSearchDTO;
import com.pcwk.ehr.report.domain.ReportVO;
import com.pcwk.ehr.report.mapper.ReportMapper;

@ExtendWith(SpringExtension.class) // JUnit 5용 Spring 연동
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // @Order 순서대로 실행
public class ReportMapperTest {

    @Autowired
    ReportMapper reportMapper;
    
    @BeforeEach
    public void setUp() {
        // 각 테스트 메서드 실행 전 데이터 초기화
        reportMapper.deleteAll();
    }
    
    @Test
    @Order(1) // 순서 지정
    public void insertAndSelect() {
        // 1. 데이터 삽입 테스트
        ReportVO report = new ReportVO();
        report.setReporterNo(1L);
        report.setReportedUserNo(2L);
        report.setReportType("01");
        report.setTargetId(100L);
        report.setReason("욕설 및 비방");

        int flag = reportMapper.insertReport(report);
        assertEquals(1, flag);

        // 2. 전체 건수 확인
        int total = reportMapper.totalCnt();
        assertEquals(1, total);
    }

    @Test
    @Order(2)
    public void doRetrieve() {
        // 목록 조회 테스트
        ReportSearchDTO search = new ReportSearchDTO();
        List<ReportVO> list = reportMapper.doRetrieve(search);
        
        assertNotNull(list);
        for(ReportVO vo : list) {
            System.out.println(vo);
        }
    }
}