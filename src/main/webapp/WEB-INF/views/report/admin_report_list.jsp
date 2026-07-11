<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="CP" value="${pageContext.request.contextPath}"
	scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>신고센터 | SPAM</title>
<link rel="stylesheet" href="${CP}/resources/css/index.css">
<link rel="stylesheet" href="${CP}/resources/css/member.css">
<style>
.report-table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
}

.report-table th, .report-table td {
	padding: 12px;
	border-bottom: 1px solid #ddd;
	text-align: center;
}

.report-table th {
	background-color: #f8f9fa;
}

/* 행 전체 클릭 인터랙션 */
.report-row {
	cursor: pointer;
	transition: background-color 0.2s ease;
}

.report-row:hover {
	background-color: #f0f0f0;
}

/* 내용이 길 경우 말줄임 처리 */
.content-cell {
	max-width: 300px;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}
/* 링크 기본 스타일 제거 및 박스 모양 유지 */
.report-card-link {
	text-decoration: none;
	color: inherit;
	display: block; /* 전체 영역 클릭 가능하도록 블록 처리 */
	transition: background-color 0.2s ease; /* 색상 변화를 부드럽게 */
}

/* 마우스를 올렸을 때의 스타일 */
.report-card-link:hover .simple-list-card {
	background-color: #f0f0f0; /* 대장님이 원하시는 색상 코드로 변경 가능합니다 */
	cursor: pointer;
}

/* article 내의 텍스트 색상이 링크에 의해 바뀌지 않도록 보존 */
.report-card-link:hover {
	color: inherit;
}
</style>
<script defer src="${CP}/resources/js/index.js"></script>
</head>
<body>
	<div class="page-shell" id="top">
		<jsp:include page="../common/header.jsp" />
		<jsp:include page="../common/nav.jsp" />

		<main class="member-page-shell">
			<header class="page-header">
				<div>
					<a class="brand" href="${CP}/report/doRetrieve.do"
						aria-label="SPAM 신고센터"> <span class="brand-mark">SP</span> <span>SPAM
							Report</span>
					</a>
					<h1 class="page-title">신고센터</h1>
					<p class="page-desc">접수된 신고 내역과 처리 상태를 확인합니다.</p>
				</div>
				<nav class="header-actions">
					<a class="btn outline" href="${CP}/service/info.do?tab=safe">이용
						정책 안내</a> <a class="btn" href="${CP}/main.do">메인</a>
				</nav>
			</header>

			<section class="panel stack">
				<table class="report-table">
					<thead>
						<tr>
							<th>신고번호</th>
							<th>신고자</th>
							<th>피신고자</th>
							<th>신고내용</th>
							<th>접수일</th>
							<th>처리상태</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${empty list}">
								<tr>
									<td colspan="6">조회된 신고 내역이 없습니다.</td>
								</tr>
							</c:when>
							<c:otherwise>
								<c:forEach var="report" items="${list}">
									<tr class="report-row"
										onclick="location.href='${CP}/report/admin_report_detail.do?reportNo=<c:out value="${report.reportNo}" />'">
										<td><c:out value="${report.reportNo}" /></td>
										<td><c:out value="${report.reporterNickname}" /></td>
										<td><c:out value="${report.reportedNickname}" /></td>
										<td class="content-cell"><c:out value="${report.reason}" /></td>
										<td><c:out value="${report.createDt}" /></td>
										<td>
											<%-- 상태 코드에 따른 텍스트 매핑 --%> <c:choose>
												<c:when test="${report.reportStatus == '01'}">접수중</c:when>
												<c:when test="${report.reportStatus == '02'}">처리완료</c:when>
												<c:when test="${report.reportStatus == '03'}">반려</c:when>
												<c:otherwise>미분류</c:otherwise>
											</c:choose>
										</td>
									</tr>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
				<div style="display: flex; justify-content: center; align-items: center; margin: 30px 0; width: 100%;" class="pagination">
					
					<%-- 1. 정수 연산 보장을 위해 integerOnly 확실히 지정 --%>
					<fmt:parseNumber var="currentPage" value="${search.pageNo}" integerOnly="true" />
					<fmt:parseNumber var="pageSize" value="${search.pageSize}" integerOnly="true" />
					<c:set var="pageBlock" value="10" />
					
					<%-- 2. 총 페이지 수 구하기 --%>
					<fmt:parseNumber var="totalPage" value="${(totalCount + pageSize - 1) / pageSize}" integerOnly="true" />
					
					<%-- 3. 시작 페이지 구하기 --%>
					<fmt:parseNumber var="blockNum" value="${(currentPage - 1) / pageBlock}" integerOnly="true" />
					<fmt:parseNumber var="startPage" value="${blockNum * pageBlock + 1}" integerOnly="true" />
					
					<%-- 4. 끝 페이지 구하기 (소수점 원천 차단) --%>
					<fmt:parseNumber var="endPage" value="${startPage + pageBlock - 1}" integerOnly="true" />
					<c:if test="${endPage > totalPage}">
						<fmt:parseNumber var="endPage" value="${totalPage}" integerOnly="true" />
					</c:if>

					<%-- 공통 URL 파라미터 조립 (검색어 유지용) --%
					<c:set var="queryStr" value="&searchDiv=${search.searchDiv}&searchWord=${search.searchWord}" />

					<%-- [이전] 버튼 노출 조건 --%>
					<c:if test="${startPage > 1}">
						<a href="${CP}/report/admin_doRetrieve.do?pageNo=${startPage - 1}${queryStr}" 
						   style="margin: 0 10px; text-decoration: none; color: #333; font-weight: bold;">[이전]</a>
					</c:if>

					<%-- 페이지 번호 단추 출력 --%>
					<c:forEach var="i" begin="${startPage}" end="${endPage}">
						<c:choose>
							<c:when test="${i eq currentPage}">
								<strong style="margin: 0 8px; color: #ff4757; font-size: 18px; border-bottom: 2px solid #ff4757; padding-bottom: 2px;">${i}</strong>
							</c:when>
							<c:otherwise>
								<a href="${CP}/report/admin_doRetrieve.do?pageNo=${i}${queryStr}" 
								   style="margin: 0 8px; text-decoration: none; color: #666; font-size: 16px;">${i}</a>
							</c:otherwise>
						</c:choose>
					</c:forEach>

					<%-- [다음] 버튼 노출 조건 --%>
					<c:if test="${endPage < totalPage}">
						<a href="${CP}/report/admin_doRetrieve.do?pageNo=${endPage + 1}${queryStr}" 
						   style="margin: 0 10px; text-decoration: none; color: #333; font-weight: bold;">[다음]</a>
					</c:if>
				</div>
			</section>
		</main>

		<jsp:include page="../common/footer.jsp" />
		<jsp:include page="../common/floatingBar.jsp" />
		<jsp:include page="../common/mobileBottomNav.jsp" />
	</div>
</body>
</html>
