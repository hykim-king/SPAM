/**
 * 파일명: ReportService.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-06 <br>
 * 설　명: <br>
 */
package com.pcwk.ehr.report.service;

import java.util.List;

import com.pcwk.ehr.report.domain.ReportSearchDTO;
import com.pcwk.ehr.report.domain.ReportVO;

public interface ReportService {

    /**
     * 신고 등록
     */
    int doInsert(ReportVO report);

    /**
     * 신고 처리 상태 수정
     */
    int doUpdateStatus(ReportVO report);

    /**
     * 신고 삭제
     */
    int doDelete(ReportVO report);

    /**
     * 신고 단건 조회
     */
    ReportVO doSelectOne(ReportVO report);

    /**
     * 신고 목록 조회
     */
    List<ReportVO> doRetrieve(ReportSearchDTO search);

    /**
     * 신고 총 건수 조회
     */
    int totalCnt();

    /**
     * 이거 어카지?
     * @param currentMemberNo
     * @return
     */
	List<ReportVO> doRetrieveReceivedReports(int currentMemberNo);
	
    /**
     * 이거 어카지?
     * @param currentMemberNo
     * @return
     */
	List<ReportVO> doRetrieveMyReports(int currentMemberNo);
}