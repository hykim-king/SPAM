<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>나의 신고 내역 관리 | SPAM</title>
    <link rel="stylesheet" href="${CP}/resources/css/index.css?v=20260715">
    <link rel="stylesheet" href="${CP}/resources/css/report.css?v=20260715">
    <script defer src="${CP}/resources/js/index.js?v=20260715"></script>
    <script defer src="${CP}/resources/js/report.js?v=20260715"></script>
</head>
<body>
<div class="page-shell" id="top">
    <jsp:include page="../common/header.jsp" />
    <jsp:include page="../common/nav.jsp" />

    <main class="report-page">
        <header class="report-page-header">
            <div class="report-header-copy">
                <h1>나의 신고 내역 관리</h1>
                <p>내가 접수한 신고와 나에게 접수된 신고를 확인합니다.</p>
            </div>
            <nav class="report-header-actions" aria-label="신고 내역 화면 바로가기">
                <a class="report-button" href="${CP}/user/mypage.do">마이페이지</a>
            </nav>
        </header>

        <%-- 2026-07-14 [수정] 인라인 스타일/이모지를 제거하고 공통 신고 UI로 통일한다. --%>
        <section class="report-panel">
            <div class="report-tabs" role="tablist" aria-label="신고 내역 구분">
                <button class="report-tab is-active" id="submittedReportTab" type="button" role="tab"
                        aria-selected="true" aria-controls="submittedReports" data-report-tab="submittedReports">
                    내가 신고한 목록
                </button>
                <button class="report-tab" id="receivedReportTab" type="button" role="tab"
                        aria-selected="false" aria-controls="receivedReports" data-report-tab="receivedReports">
                    내가 신고 당한 목록
                </button>
            </div>

            <div class="report-tab-panel is-active" id="submittedReports" role="tabpanel" aria-labelledby="submittedReportTab">
                <div class="report-table-wrap">
                    <table class="report-table">
                        <thead>
                        <tr>
                            <th class="report-col-number">번호</th>
                            <th class="report-col-type">신고유형</th>
                            <th class="report-col-reason">신고사유</th>
                            <th class="report-col-date">신고일자</th>
                            <th class="report-col-status">처리상태</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${empty myReportList}">
                                <tr class="report-empty-row">
                                    <td class="report-empty-cell" colspan="5">내가 접수한 신고 내역이 없습니다.</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="report" items="${myReportList}">
                                    <tr>
                                        <td><c:out value="${report.reportNo}" /></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${report.reportType eq 'USER'}">사용자</c:when>
                                                <c:when test="${report.reportType eq 'PRODUCT'}">상품</c:when>
                                                <c:otherwise><c:out value="${report.reportType}" /></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="report-cell-left report-cell-ellipsis">
                                            <a class="report-detail-link" href="${CP}/report/doSelectOne.do?reportNo=${report.reportNo}">
                                                <c:out value="${report.reason}" />
                                            </a>
                                        </td>
                                        <td><c:out value="${report.createDt}" /></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${report.reportStatus eq '01' or report.reportStatus eq '0'}"><span class="report-status is-pending">처리대기</span></c:when>
                                                <c:when test="${report.reportStatus eq '02' or report.reportStatus eq '1'}"><span class="report-status is-complete">처리완료</span></c:when>
                                                <c:when test="${report.reportStatus eq '03'}"><span class="report-status is-rejected">반려</span></c:when>
                                                <c:otherwise><span class="report-status is-unknown">미분류</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="report-tab-panel" id="receivedReports" role="tabpanel" aria-labelledby="receivedReportTab">
                <div class="report-table-wrap">
                    <table class="report-table">
                        <thead>
                        <tr>
                            <th class="report-col-number">번호</th>
                            <th class="report-col-type">신고유형</th>
                            <th class="report-col-reason">신고사유</th>
                            <th class="report-col-date">신고일자</th>
                            <th class="report-col-status">처리상태</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${empty receivedReportList}">
                                <tr class="report-empty-row">
                                    <td class="report-empty-cell" colspan="5">나에게 접수된 신고 내역이 없습니다.</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="report" items="${receivedReportList}">
                                    <tr>
                                        <td><c:out value="${report.reportNo}" /></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${report.reportType eq 'USER'}">사용자</c:when>
                                                <c:when test="${report.reportType eq 'PRODUCT'}">상품</c:when>
                                                <c:otherwise><c:out value="${report.reportType}" /></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="report-cell-left report-cell-ellipsis">
                                            <a class="report-detail-link" href="${CP}/report/doSelectOne.do?reportNo=${report.reportNo}">
                                                <c:out value="${report.reason}" />
                                            </a>
                                        </td>
                                        <td><c:out value="${report.createDt}" /></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${report.reportStatus eq '01' or report.reportStatus eq '0'}"><span class="report-status is-pending">처리대기</span></c:when>
                                                <c:when test="${report.reportStatus eq '02' or report.reportStatus eq '1'}"><span class="report-status is-complete">처리완료</span></c:when>
                                                <c:when test="${report.reportStatus eq '03'}"><span class="report-status is-rejected">반려</span></c:when>
                                                <c:otherwise><span class="report-status is-unknown">미분류</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    </main>

    <jsp:include page="../common/footer.jsp" />
    <jsp:include page="../common/floatingBar.jsp" />
    <jsp:include page="../common/mobileBottomNav.jsp" />
</div>

</body>
</html>
