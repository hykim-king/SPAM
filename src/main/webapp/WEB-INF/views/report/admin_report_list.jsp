<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>신고 센터 | SPAM 관리자</title>
    <link rel="stylesheet" href="${CP}/resources/css/index.css?v=20260715">
    <link rel="stylesheet" href="${CP}/resources/css/report.css?v=20260715">
    <link rel="stylesheet" href="${CP}/resources/css/admin.css?v=20260715">
    <script defer src="${CP}/resources/js/index.js?v=20260715"></script>
</head>
<body>
<div class="page-shell" id="top">
    <jsp:include page="../common/header.jsp" />
    <jsp:include page="../common/nav.jsp" />

    <main class="report-page is-admin">
        <%-- 2026-07-14 [수정] 안내 문구를 제거하고 신고 전용 공통 UI로 정리한다. --%>
        <header class="admin-page-header">
            <div class="admin-heading">
                <span class="admin-kicker">ADMIN</span>
                <h1>신고 센터</h1>
            </div>
            <nav class="admin-nav" aria-label="관리자 메뉴">
                <a class="admin-nav-link" href="${CP}/admin/user/list.do">회원 관리</a>
                <span class="admin-nav-link is-active" aria-current="page">신고 센터</span>
                <a class="admin-nav-link" href="${CP}/admin/transact/list.do">전체 상품</a>
            </nav>
        </header>

        <section class="report-panel" aria-label="접수된 신고 목록">
            <div class="admin-list-meta">
                <strong>총 신고 수: ${totalCount}</strong>
                <span>현재 페이지: ${search.pageNo}<c:if test="${totalPage gt 0}"> / ${totalPage}</c:if></span>
            </div>
            <div class="report-table-wrap">
                <table class="report-table">
                    <thead>
                    <tr>
                        <th class="report-col-number">신고번호</th>
                        <th class="report-col-person">신고자</th>
                        <th class="report-col-person">피신고자</th>
                        <th class="report-col-reason">신고내용</th>
                        <th class="report-col-date">접수일</th>
                        <th class="report-col-status">처리상태</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty list}">
                            <tr class="report-empty-row">
                                <td class="report-empty-cell" colspan="6">조회된 신고 내역이 없습니다.</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="report" items="${list}">
                                <tr>
                                    <td><c:out value="${report.reportNo}" /></td>
                                    <td><c:out value="${empty report.reporterNickname ? '-' : report.reporterNickname}" /></td>
                                    <td><c:out value="${empty report.reportedNickname ? '-' : report.reportedNickname}" /></td>
                                    <td class="report-cell-left report-cell-ellipsis">
                                        <a class="report-detail-link" href="${CP}/report/admin_report_detail.do?reportNo=${report.reportNo}">
                                            <c:out value="${report.reason}" />
                                        </a>
                                    </td>
                                    <td><c:out value="${report.createDt}" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${report.reportStatus eq '01'}"><span class="report-status is-pending">접수중</span></c:when>
                                            <c:when test="${report.reportStatus eq '02'}"><span class="report-status is-complete">처리완료</span></c:when>
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

            <c:if test="${totalPage gt 1}">
                <nav class="report-pagination" aria-label="신고 센터 페이지 이동">
                    <c:if test="${search.pageNo gt 1}">
                        <c:url var="prevUrl" value="/report/admin_doRetrieve.do">
                            <c:param name="pageNo" value="${search.pageNo - 1}" />
                            <c:param name="searchDiv" value="${search.searchDiv}" />
                            <c:param name="searchWord" value="${search.searchWord}" />
                        </c:url>
                        <a href="${prevUrl}" aria-label="이전 페이지">이전</a>
                    </c:if>

                    <c:forEach var="page" begin="${startPage}" end="${endPage}">
                        <c:url var="pageUrl" value="/report/admin_doRetrieve.do">
                            <c:param name="pageNo" value="${page}" />
                            <c:param name="searchDiv" value="${search.searchDiv}" />
                            <c:param name="searchWord" value="${search.searchWord}" />
                        </c:url>
                        <c:choose>
                            <c:when test="${search.pageNo eq page}">
                                <a class="is-current" href="${pageUrl}" aria-current="page">${page}</a>
                            </c:when>
                            <c:otherwise><a href="${pageUrl}">${page}</a></c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <c:if test="${search.pageNo lt totalPage}">
                        <c:url var="nextUrl" value="/report/admin_doRetrieve.do">
                            <c:param name="pageNo" value="${search.pageNo + 1}" />
                            <c:param name="searchDiv" value="${search.searchDiv}" />
                            <c:param name="searchWord" value="${search.searchWord}" />
                        </c:url>
                        <a href="${nextUrl}" aria-label="다음 페이지">다음</a>
                    </c:if>
                </nav>
            </c:if>
        </section>
    </main>

    <jsp:include page="../common/footer.jsp" />
    <jsp:include page="../common/floatingBar.jsp" />
    <jsp:include page="../common/mobileBottomNav.jsp" />
</div>
</body>
</html>
