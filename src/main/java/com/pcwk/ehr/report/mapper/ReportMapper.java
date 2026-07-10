/**
 * 파일명: ReportMapper.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-06 <br>
 * 설　명: <br>
 */
package com.pcwk.ehr.report.mapper;

import java.util.List;

import com.pcwk.ehr.report.domain.ReportSearchDTO;
import com.pcwk.ehr.report.domain.ReportVO;

public interface ReportMapper {

    // 신고 등록
    int insertReport(ReportVO report);

    // 신고 처리 상태 수정
    int updateReportStatus(ReportVO report);

    // 신고 삭제
    int deleteReport(ReportVO report);

    // 신고 단건 조회
    ReportVO doSelectOne(ReportVO report);

    // 신고 목록 조회
    List<ReportVO> doRetrieve(ReportSearchDTO search);

    // 신고 총 건수 조회
    int totalCnt();
    
    // 신고 전체 삭제
    int deleteAll();

    // 내가 한 신고 목록 조회
	List<ReportVO> doRetrieveMyReports(long currentUserNum);

	// 내가 당한 신고 목록 조회 
	List<ReportVO> doRetrieveReceivedReports(long currentUserNum);
}