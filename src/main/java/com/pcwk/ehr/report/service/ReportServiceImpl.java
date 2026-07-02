/**
 * 파일명: ReportServiceImpl.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-01 <br>
 * 설　명: <br>
 */
package com.pcwk.ehr.report.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.mapper.ReportMapper;
import com.pcwk.ehr.report.domain.ReportVO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class ReportServiceImpl implements ReportService {

	Logger log = LogManager.getLogger(getClass());

    @Autowired
    private ReportMapper reportMapper;

    public ReportServiceImpl() {
        super();
        log.debug("---------------------------");
        log.debug("ReportServiceImpl()*");
        log.debug("---------------------------");
    }

    @Override
    public int doSave(ReportVO param) {
        // 신고접수 상태 자동 세팅
        if (null == param.getReportStatus() || "".equals(param.getReportStatus())) {
            log.debug("reportStatus의 기본값을 신고접수로 세팅합니다.");
            param.setReportStatus("신고접수");
        }
        return reportMapper.doSave(param);
    }

    @Override
    public int doUpdate(ReportVO param) {
        return reportMapper.doUpdate(param);
    }

    @Override
    public int doDelete(ReportVO param) {
        return reportMapper.doDelete(param);
    }

    @Override
    public ReportVO doSelectOne(ReportVO param) {
        return reportMapper.doSelectOne(param);
    }

    @Override
    public List<ReportVO> doRetrieve(DTO param) {
        return reportMapper.doRetrieve(param);
    }

    @Override
    public int totalCnt() {
        return reportMapper.totalCnt();
    }

}
