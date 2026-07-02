/**
 * 파일명: ReportService.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-01 <br>
 * 설　명: <br>
 */
package com.pcwk.ehr.report.service;

import java.util.List;

import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.report.domain.ReportVO;

public interface ReportService {
	
	/**
     * 신고 목록 조회
     */
    List<ReportVO> doRetrieve(DTO param);

    /**
     * 신고 등록 (신고 접수)
     */
    int doSave(ReportVO param);

    /**
     * 신고 수정 (관리자 처리)
     */
    int doUpdate(ReportVO param);

    /**
     * 신고 삭제
     */
    int doDelete(ReportVO param);

    /**
     * 신고 단건 조회
     */
    ReportVO doSelectOne(ReportVO param);
    
    /**
     * 전체 신고 건수
     */
    int totalCnt();
    
}
