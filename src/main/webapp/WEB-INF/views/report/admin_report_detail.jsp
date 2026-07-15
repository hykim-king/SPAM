<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>신고 내역 상세 조회 | SPAM 관리자</title>
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
        <header class="admin-page-header">
            <div class="admin-heading">
                <span class="admin-kicker">ADMIN</span>
                <h1>신고 내역 상세 조회</h1>
            </div>
            <nav class="admin-nav" aria-label="관리자 메뉴">
                <a class="admin-nav-link" href="${CP}/admin/user/list.do">회원 관리</a>
                <a class="admin-nav-link" href="${CP}/report/admin_doRetrieve.do">신고 센터</a>
                <a class="admin-nav-link" href="${CP}/admin/transact/list.do">전체 상품</a>
            </nav>
        </header>

        <%-- 2026-07-14 [수정] 관리자 신고 상세/처리 UI를 공통 레이아웃으로 통일한다. --%>
        <div class="report-detail-grid">
            <section class="report-card" aria-labelledby="adminReportInfoTitle">
                <div class="report-card-heading">
                    <h2 class="report-card-title" id="adminReportInfoTitle">신고 접수 정보</h2>
                    <c:choose>
                        <c:when test="${outVO.reportStatus eq '01'}"><span class="report-status is-pending">접수중</span></c:when>
                        <c:when test="${outVO.reportStatus eq '02'}"><span class="report-status is-complete">처리완료</span></c:when>
                        <c:when test="${outVO.reportStatus eq '03'}"><span class="report-status is-rejected">반려</span></c:when>
                        <c:otherwise><span class="report-status is-unknown">미분류</span></c:otherwise>
                    </c:choose>
                </div>

                <table class="report-info-table">
                    <tbody>
                    <tr>
                        <th>신고 번호</th>
                        <td><c:out value="${outVO.reportNo}" /></td>
                    </tr>
                    <tr>
                        <th>신고 유형</th>
                        <td>
                            <c:choose>
                                <c:when test="${outVO.reportType eq 'USER'}">사용자 신고</c:when>
                                <c:when test="${outVO.reportType eq 'PRODUCT'}">상품 신고</c:when>
                                <c:otherwise><c:out value="${outVO.reportType}" /></c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th>신고자</th>
                        <td><c:out value="${empty outVO.reporterNickname ? outVO.reporterNo : outVO.reporterNickname}" /></td>
                    </tr>
                    <tr>
                        <th>피신고자</th>
                        <td><c:out value="${empty outVO.reportedNickname ? '-' : outVO.reportedNickname}" /></td>
                    </tr>
                    <c:if test="${not empty outVO.targetId and outVO.targetId ne 0}">
                        <tr>
                            <th>신고 대상</th>
                            <td>
                                <c:choose>
                                    <c:when test="${outVO.reportType eq 'PRODUCT'}">
                                        <a class="report-detail-link" href="${CP}/product/view.do?productNo=${outVO.targetId}">
                                            <c:out value="${empty outVO.productTitle ? outVO.targetId : outVO.productTitle}" />
                                        </a>
                                    </c:when>
                                    <c:otherwise><c:out value="${outVO.targetId}" /></c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:if>
                    <tr>
                        <th>신고 일시</th>
                        <td><c:out value="${outVO.createDt}" /></td>
                    </tr>
                    </tbody>
                </table>

                <h3 class="report-reason-title">신고 사유 및 상세 내용</h3>
                <div class="report-reason-box"><c:out value="${outVO.reason}" /></div>
            </section>

            <section class="report-card" aria-labelledby="adminReportProcessTitle">
                <h2 class="report-card-title" id="adminReportProcessTitle">처리 상태 관리</h2>
                <form action="${CP}/report/doUpdateStatus.do" method="post">
                    <input type="hidden" name="reportNo" value="${outVO.reportNo}">

                    <div class="report-form-group">
                        <span class="report-label">현재 진행 상태</span>
                        <div class="report-processing-status">
                            <c:choose>
                                <c:when test="${outVO.reportStatus eq '01'}"><span class="report-status is-pending">접수중</span></c:when>
                                <c:when test="${outVO.reportStatus eq '02'}"><span class="report-status is-complete">처리완료</span></c:when>
                                <c:when test="${outVO.reportStatus eq '03'}"><span class="report-status is-rejected">반려</span></c:when>
                                <c:otherwise><span class="report-status is-unknown">미분류</span></c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <div class="report-form-group">
                        <label class="report-label" for="reportStatus">상태 변경</label>
                        <select class="report-select" id="reportStatus" name="reportStatus">
                            <option value="01" ${outVO.reportStatus eq '01' ? 'selected' : ''}>접수중</option>
                            <option value="02" ${outVO.reportStatus eq '02' ? 'selected' : ''}>처리완료</option>
                            <option value="03" ${outVO.reportStatus eq '03' ? 'selected' : ''}>반려</option>
                        </select>
                    </div>

                    <div class="report-form-group">
                        <label class="report-label" for="reportAdmin">최종 처리 관리자</label>
                        <input class="report-input" id="reportAdmin" type="text"
                               value="${empty outVO.adminNickName ? '미처리' : outVO.adminNickName}" disabled>
                    </div>

                    <div class="report-form-group">
                        <label class="report-label" for="reportProcessDate">최종 처리 일시</label>
                        <input class="report-input" id="reportProcessDate" type="text"
                               value="${empty outVO.processDt ? '미처리' : outVO.processDt}" disabled>
                    </div>

                    <div class="report-detail-actions">
                        <button class="report-button is-primary" type="submit">처리 상태 저장</button>
                    </div>
                </form>
            </section>
        </div>
    </main>

    <jsp:include page="../common/footer.jsp" />
    <jsp:include page="../common/floatingBar.jsp" />
    <jsp:include page="../common/mobileBottomNav.jsp" />
</div>
</body>
</html>
