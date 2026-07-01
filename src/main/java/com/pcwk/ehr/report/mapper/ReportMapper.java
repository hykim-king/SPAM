/**
 * 파일명: ReportMapper.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-01 <br>
 * 설　명: 신고 관리 매퍼 인터페이스<br>
 */
package com.pcwk.ehr.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.report.domain.ReportVO;

@Mapper // MyBatis 매퍼 인터페이스 선언
public interface ReportMapper extends WorkDiv<ReportVO> {

    // int doSave(ReportVO param);
    // int doUpdate(ReportVO param);
    // int doDelete(ReportVO param);
    // ReportVO doSelectOne(ReportVO param);
    // List<ReportVO> doRetrieve(DTO param);
    
    /**
     * 전체 신고 건수 조회
     */
    int totalCnt();
}
