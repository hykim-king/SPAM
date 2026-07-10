/**
 * 파일명: ReportVO.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-06 <br>
 * 설　명: <br>
 */
package com.pcwk.ehr.report.domain;

import java.io.Serializable;
import java.util.Date;

public class ReportVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long reportNo;         // 신고번호
    private Long reporterNo;       // 신고회원번호 (USER_INFO.USER_NUM)
    private Long reportedUserNo;   // 피신고회원번호 (USER_INFO.USER_NUM)
    private Long adminNo;          // 처리관리자번호
    private String reportType;     // 신고대상유형
    private Long targetId;         // 신고대상ID
    private String reason;         // 신고사유
    private String reportStatus;   // 처리상태 (대기/완료 등)
    private String createDt;         // 신고일
    private String processDt;        // 처리일
    
    private String reporterNickname;  // 신고자 닉네임을 담을 필드 추가
    private String reportedNickname; // 피신고자 닉네임을 담을 필드 추가
    
	public ReportVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param reportNo
	 * @param reporterNo
	 * @param reportedUserNo
	 * @param adminNo
	 * @param reportType
	 * @param targetId
	 * @param reason
	 * @param reportStatus
	 * @param createDt
	 * @param processDt
	 * @param reporterNickname
	 */
	public ReportVO(Long reportNo, Long reporterNo, Long reportedUserNo, Long adminNo, String reportType, Long targetId,
			String reason, String reportStatus, String createDt, String processDt, String reporterNickname) {
		super();
		this.reportNo = reportNo;
		this.reporterNo = reporterNo;
		this.reportedUserNo = reportedUserNo;
		this.adminNo = adminNo;
		this.reportType = reportType;
		this.targetId = targetId;
		this.reason = reason;
		this.reportStatus = reportStatus;
		this.createDt = createDt;
		this.processDt = processDt;
		this.reporterNickname = reporterNickname;
	}

	/**
	 * @return the reporterNickname
	 */
	public String getReporterNickname() {
		return reporterNickname;
	}
	/**
	 * @param reporterNickname the reporterNickname to set
	 */
	public void setReporterNickname(String reporterNickname) {
		this.reporterNickname = reporterNickname;
	}
	/**
	 * @return the reportNo
	 */
	public Long getReportNo() {
		return reportNo;
	}
	/**
	 * @param reportNo the reportNo to set
	 */
	public void setReportNo(Long reportNo) {
		this.reportNo = reportNo;
	}
	/**
	 * @return the reporterNo
	 */
	public Long getReporterNo() {
		return reporterNo;
	}
	/**
	 * @param reporterNo the reporterNo to set
	 */
	public void setReporterNo(Long reporterNo) {
		this.reporterNo = reporterNo;
	}
	/**
	 * @return the reportedUserNo
	 */
	public Long getReportedUserNo() {
		return reportedUserNo;
	}
	/**
	 * @param reportedUserNo the reportedUserNo to set
	 */
	public void setReportedUserNo(Long reportedUserNo) {
		this.reportedUserNo = reportedUserNo;
	}
	/**
	 * @return the adminNo
	 */
	public Long getAdminNo() {
		return adminNo;
	}
	/**
	 * @param adminNo the adminNo to set
	 */
	public void setAdminNo(Long adminNo) {
		this.adminNo = adminNo;
	}
	/**
	 * @return the reportType
	 */
	public String getReportType() {
		return reportType;
	}
	/**
	 * @param reportType the reportType to set
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	/**
	 * @return the targetId
	 */
	public Long getTargetId() {
		return targetId;
	}
	/**
	 * @param targetId the targetId to set
	 */
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * @return the reportStatus
	 */
	public String getReportStatus() {
		return reportStatus;
	}
	/**
	 * @param reportStatus the reportStatus to set
	 */
	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}
	/**
	 * @return the createDt
	 */
	public String getCreateDt() {
		return createDt;
	}
	/**
	 * @param createDt the createDt to set
	 */
	public void setCreateDt(String createDt) {
		this.createDt = createDt;
	}
	/**
	 * @return the processDt
	 */
	public String getProcessDt() {
		return processDt;
	}
	/**
	 * @param processDt the processDt to set
	 */
	public void setProcessDt(String processDt) {
		this.processDt = processDt;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	/**
	 * @return the reportedNickname
	 */
	public String getReportedNickname() {
		return reportedNickname;
	}

	/**
	 * @param reportedNickname the reportedNickname to set
	 */
	public void setReportedNickname(String reportedNickname) {
		this.reportedNickname = reportedNickname;
	}

	@Override
	public String toString() {
		return "ReportVO [reportNo=" + reportNo + ", reporterNo=" + reporterNo + ", reportedUserNo=" + reportedUserNo
				+ ", adminNo=" + adminNo + ", reportType=" + reportType + ", targetId=" + targetId + ", reason="
				+ reason + ", reportStatus=" + reportStatus + ", createDt=" + createDt + ", processDt=" + processDt
				+ "]";
	}
    
}