/**
 * 파일명: ReportServiceImpl.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-06 <br>
 * 설　명: <br>
 */
package com.pcwk.ehr.report.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pcwk.ehr.report.domain.ReportSearchDTO;
import com.pcwk.ehr.report.domain.ReportVO;
import com.pcwk.ehr.report.mapper.ReportMapper;

@Service("reportService")
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Override
    public int doInsert(ReportVO report) {
        return reportMapper.insertReport(report);
    }

    @Override
    public int doUpdateStatus(ReportVO report) {
        return reportMapper.updateReportStatus(report);
    }

    @Override
    public int doDelete(ReportVO report) {
        return reportMapper.deleteReport(report);
    }

    @Override
    public ReportVO doSelectOne(ReportVO report) {
        return reportMapper.doSelectOne(report);
    }

    @Override
    public List<ReportVO> doRetrieve(ReportSearchDTO search) {
        return reportMapper.doRetrieve(search);
    }

    @Override
    public int totalCnt() {
        return reportMapper.totalCnt();
    }
}