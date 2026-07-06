/**
 * 파일명: ReportServiceTest.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-06 <br>
 * 설　명: <br>
 */
package com.pcwk.ehr.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.pcwk.ehr.report.domain.ReportVO;
import com.pcwk.ehr.report.service.ReportService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReportServiceTest {

    @Autowired
    ReportService reportService;

    @BeforeEach
    public void setUp() {
        // 모든 신고 데이터를 비우고 시작 (Mapper의 deleteAll 필요)
        // reportService를 통해 삭제 로직을 호출하거나 Mapper를 직접 사용 가능
    }

    @Test
    @Order(1)
    public void addAndGet() {
        // 1. 등록 테스트
        ReportVO report = new ReportVO();
        report.setReporterNo(1L);
        report.setReportedUserNo(2L);
        report.setReportType("01");
        report.setTargetId(100L);
        report.setReason("서비스 테스트 이유");

        int flag = reportService.doInsert(report);
        assertEquals(1, flag);

        // 2. 단건 조회 테스트
        // (주의: 시퀀스로 생성된 번호를 알아야 하므로 전체 목록 조회 후 확인하는 방식 권장)
        int total = reportService.totalCnt();
        assertEquals(1, total);
    }
}