/**
 * 파일명: ReportServiceJUnit.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-01 <br>
 * 설　명: <br>
 */
package com.pcwk.ehr.report.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.pcwk.ehr.mapper.ReportMapper;
import com.pcwk.ehr.report.domain.ReportVO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { 
        "file:src/main/webapp/WEB-INF/spring/root-context.xml",
        "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"
})
class ReportServiceJUnit {

	final static Logger log = LogManager.getLogger(ReportServiceJUnit.class);

    @Autowired
    ReportService reportService;

    @Autowired
    ReportMapper reportMapper;

    ReportVO report01;

    @BeforeEach
    void setUp() throws Exception {
        // 테스트용 데이터
        report01 = new ReportVO();
        report01.setReporterNo(1);
        report01.setReportedUserNo(11);
        report01.setReportType("게시글");
        report01.setTargetId(100);
        report01.setReason("부적절한 내용");
        
        log.debug("테스트용 데이터: "+report01);
    }

    @Test
    void doSaveAndStatusCheck() {
        log.debug("---------------------");
        log.debug("@Test doSaveAndStatusCheck()");
        log.debug("---------------------");
        
        // 1. 전체 삭제
        reportMapper.deleteAll();
        assertEquals(0, reportService.totalCnt());

        // 2. 신고 등록 (reportStatus를 명시하지 않음)
        reportService.doSave(report01);
        
        // 3. 단건 조회하여 상태 확인
        // 주의: DB에서 가져올 때 reportNo가 필요하므로, 여기선 간단히 목록 조회로 확인합니다.
        ReportVO outVO = reportService.doRetrieve(report01).get(0);
        
        assertNotNull(outVO);
        // "신고접수"로 자동 세팅되었는지 확인
        assertEquals("신고접수", outVO.getReportStatus());
        log.debug("확인된 상태: " + outVO.getReportStatus());
    }
}
