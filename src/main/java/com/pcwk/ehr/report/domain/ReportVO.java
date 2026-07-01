/**
 * 파일명: ReportVO.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-01 <br>
 * 설　명: <br>
 */
package com.pcwk.ehr.report.domain;

import com.pcwk.ehr.cmn.DTO;

public class ReportVO extends DTO {
	
	// 필드
	private int 	reportNo;         // 신고번호
    private int 	reporterNo;       // 신고회원번호
    private int 	reportedUserNo;   // 피신고회원번호
    private int 	adminNo;          // 처리관리자번호
    private String 	reportType;    // 신고대상유형
    private int 	targetId;         // 신고대상ID
    private String 	reason;        // 신고사유
    private String 	reportStatus;  // 처리상태
    private String 	createDt;      // 신고일
    private String 	processDt;     // 처리일

    
    // 생성자
    public ReportVO() {}
	public ReportVO(int reportNo, int reporterNo, int reportedUserNo,
					int adminNo, String reportType, int targetId,
					String reason, String reportStatus, String createDt, String processDt) {
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
	}
	
	
	// Getter, Setter, toString
	public int getReportNo() {
		return reportNo;
	}
	
	public void setReportNo(int reportNo) {
		this.reportNo = reportNo;
	}
	
	public int getReporterNo() {
		return reporterNo;
	}
	
	public void setReporterNo(int reporterNo) {
		this.reporterNo = reporterNo;
	}
	
	public int getReportedUserNo() {
		return reportedUserNo;
	}
	
	public void setReportedUserNo(int reportedUserNo) {
		this.reportedUserNo = reportedUserNo;
	}
	
	public int getAdminNo() {
		return adminNo;
	}
	
	public void setAdminNo(int adminNo) {
		this.adminNo = adminNo;
	}
	
	public String getReportType() {
		return reportType;
	}
	
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	
	public int getTargetId() {
		return targetId;
	}
	
	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getReportStatus() {
		return reportStatus;
	}
	
	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}
	
	public String getCreateDt() {
		return createDt;
	}
	
	public void setCreateDt(String createDt) {
		this.createDt = createDt;
	}
	
	public String getProcessDt() {
		return processDt;
	}
	
	public void setProcessDt(String processDt) {
		this.processDt = processDt;
	}
	
	@Override
	public String toString() {
		return "ReportVO [reportNo=" + reportNo + ", reporterNo=" + reporterNo + ", reportedUserNo=" + reportedUserNo
				+ ", adminNo=" + adminNo + ", reportType=" + reportType + ", targetId=" + targetId + ", reason="
				+ reason + ", reportStatus=" + reportStatus + ", createDt=" + createDt + ", processDt=" + processDt
				+ "]";
	}
}
