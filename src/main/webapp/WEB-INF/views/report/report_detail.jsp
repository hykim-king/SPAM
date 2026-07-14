<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>신고 상세 내역</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/member.css">
<style>
.detail-container {
	max-width: 750px;
	margin: 40px auto;
	padding: 30px;
	background: #fff;
	border-radius: 12px;
	box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
}

.detail-header {
	border-bottom: 2px solid #f1f3f5;
	padding-bottom: 15px;
	margin-bottom: 25px;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.detail-title {
	font-size: 24px;
	font-weight: bold;
	color: #333;
}

.status-badge {
	padding: 6px 14px;
	border-radius: 20px;
	font-size: 14px;
	font-weight: 600;
}
/* 접수/진행중 */
.status-ing {
	background-color: #e3fafc;
	color: #0b7285;
} 
/* 처리 완료 */
.status-done {
	background-color: #ebfbee;
	color: #2b8a3e;
} 
/* 반려 */
.status-reject {
	background-color: #fff0f6;
	color: #c2255c;
} 
.info-table {
	width: 100%;
	border-collapse: collapse;
	margin-bottom: 25px;
}

.info-table th {
	width: 25%;
	padding: 15px;
	background-color: #f8f9fa;
	border-bottom: 1px solid #dee2e6;
	text-align: left;
	color: #495057;
	font-weight: 600;
}

.info-table td {
	width: 75%;
	padding: 15px;
	border-bottom: 1px solid #dee2e6;
	color: #333;
}

.reason-box {
	background-color: #f8f9fa;
	border: 1px solid #e9ecef;
	border-radius: 8px;
	padding: 20px;
	min-height: 150px;
	white-space: pre-wrap;
	word-break: break-all;
	line-height: 1.6;
	color: #444;
}

.btn-area {
	text-align: center;
	margin-top: 30px;
}

.btn-list {
	display: inline-block;
	background-color: #4dabf7;
	color: white;
	padding: 12px 30px;
	border-radius: 6px;
	text-decoration: none;
	font-weight: bold;
	transition: background 0.2s;
}

.btn-list:hover {
	background-color: #228be6;
}
</style>
</head>
<body>

	<div class="detail-container">
		<div class="detail-header">
			<div class="detail-title">🚨 신고 상세 내역</div>
			<div>
				<%-- 💡 기존 reportVO를 컨트롤러에서 보낸 outVO로 명칭 통일 변경 --%>
				<c:choose>
					<c:when test="${outVO.reportStatus eq '01'}">
						<span class="status-badge status-ing">접수중</span>
					</c:when>
					<c:when test="${outVO.reportStatus eq '02'}">
						<span class="status-badge status-done">처리완료</span>
					</c:when>
					<c:when test="${outVO.reportStatus eq '03'}">
						<span class="status-badge status-reject">반려</span>
					</c:when>
					<c:otherwise>
						<span class="status-badge status-wait">${outVO.reportStatus}</span>
					</c:otherwise>
				</c:choose>
			</div>
		</div>

		<table class="info-table">
			<tr>
				<th>신고 번호</th>
				<td>${outVO.reportNo}</td>
			</tr>
			<tr>
				<th>신고 구분</th>
				<td><strong> 
					<c:choose>
						<c:when test="${outVO.reportType eq 'USER'}">유저 비매너 신고</c:when>
						<c:when test="${outVO.reportType eq 'PRODUCT'}">상품 게시글 신고</c:when>
						<c:otherwise>${outVO.reportType}</c:otherwise>
					</c:choose>
				</strong></td>
			</tr>
			<tr>
				<th>신고자 회원닉네임</th>
				<td><c:out value="${outVO.reporterNickname}" default="이름없음" /></td>
			</tr>
			<tr>
				<th>피신고자 회원닉네임</th>
				<td>
					<c:choose>
						<c:when test="${not empty outVO.reportedNickname}">
							<c:out value="${outVO.reportedNickname}" />
						</c:when>
						<c:otherwise>없음 (대상물 신고)</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<c:if test="${outVO.targetId ne 0}">
				<tr>
					<th>신고 대상 ID (PK)</th>
					<td>${outVO.targetId}</td>
				</tr>
			</c:if>
			<tr>
				<th>신고 접수일</th>
				<td>${outVO.createDt}</td>
			</tr>

			<c:if test="${outVO.reportStatus eq '03' or outVO.reportStatus eq '04'}">
				<tr>
					<th>담당 관리자</th>
					<td>
						<c:choose>
							<c:when test="${not empty outVO.adminNickName}">
								<c:out value="${outVO.adminNickName}" /> 관리자
							</c:when>
							<c:otherwise>
								${outVO.adminNickName}번 관리자
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th>처리가 완료된 날짜</th>
					<td>${outVO.processDt}</td>
				</tr>
			</c:if>
		</table>

		<h3 style="margin-bottom: 10px; color: #495057;">📄 신고 사유 및 상세 내용</h3>
		<div class="reason-box"><c:out value="${outVO.reason}"/></div>

		<div class="btn-area">
			<a href="${pageContext.request.contextPath}/report/myReportList.do" class="btn-list">목록으로 돌아가기</a>
		</div>
	</div>

</body>
</html>