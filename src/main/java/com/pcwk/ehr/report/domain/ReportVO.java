/**
 * 파일명: ReportVO.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-06 <br>
 * 설　명: <br>
 */
package com.pcwk.ehr.report.domain;

import java.io.Serializable;

public class ReportVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long reportNo; // 신고번호
	private Long reporterNo; // 신고회원번호 (USER_INFO.USER_NUM)
	private Long reportedUserNo; // 피신고회원번호 (USER_INFO.USER_NUM)
	private Long adminNo; // 처리관리자번호
	private String reportType; // 신고대상유형
	private Long targetId; // 신고대상ID
	private String reason; // 신고사유
	private String reportStatus; // 처리상태 (대기/완료 등)
	private String createDt; // 신고일
	private String processDt; // 처리일

	private String reporterNickname; // 신고자 닉네임을 담을 필드 추가
	private String reportedNickname; // 피신고자 닉네임을 담을 필드 추가
	private String adminNickName; // 담당 관리자 이름/닉네임
	private String productTitle; // 상품 제목 추가

	public ReportVO() {
		super();
	}

	public ReportVO(Long reportNo, Long reporterNo, Long reportedUserNo, Long adminNo, String reportType, Long targetId,
			String reason, String reportStatus, String createDt, String processDt, String reporterNickname,
			String reportedNickname, String adminNickName, String productTitle) {
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
		this.reportedNickname = reportedNickname;
		this.adminNickName = adminNickName;
		this.productTitle = productTitle;
	}

	public Long getReportNo() {
		return reportNo;
	}

	public void setReportNo(Long reportNo) {
		this.reportNo = reportNo;
	}

	public Long getReporterNo() {
		return reporterNo;
	}

	public void setReporterNo(Long reporterNo) {
		this.reporterNo = reporterNo;
	}

	public Long getReportedUserNo() {
		return reportedUserNo;
	}

	public void setReportedUserNo(Long reportedUserNo) {
		this.reportedUserNo = reportedUserNo;
	}

	public Long getAdminNo() {
		return adminNo;
	}

	public void setAdminNo(Long adminNo) {
		this.adminNo = adminNo;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
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

	public String getReporterNickname() {
		return reporterNickname;
	}

	public void setReporterNickname(String reporterNickname) {
		this.reporterNickname = reporterNickname;
	}

	public String getReportedNickname() {
		return reportedNickname;
	}

	public void setReportedNickname(String reportedNickname) {
		this.reportedNickname = reportedNickname;
	}

	public String getAdminNickName() {
		return adminNickName;
	}

	public void setAdminNickName(String adminNickName) {
		this.adminNickName = adminNickName;
	}

	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "ReportVO [reportNo=" + reportNo + ", reporterNo=" + reporterNo + ", reportedUserNo=" + reportedUserNo
				+ ", adminNo=" + adminNo + ", reportType=" + reportType + ", targetId=" + targetId + ", reason="
				+ reason + ", reportStatus=" + reportStatus + ", createDt=" + createDt + ", processDt=" + processDt
				+ ", reporterNickname=" + reporterNickname + ", reportedNickname=" + reportedNickname
				+ ", adminNickName=" + adminNickName + ", productTitle=" + productTitle + "]";
	}
	
}