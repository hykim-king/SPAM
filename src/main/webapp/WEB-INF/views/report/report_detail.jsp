<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>신고 상세 | SPAM</title>
    <link rel="stylesheet" href="${CP}/resources/css/index.css?v=20260715">
    <link rel="stylesheet" href="${CP}/resources/css/report.css?v=20260715">
    <script defer src="${CP}/resources/js/index.js?v=20260715"></script>
</head>
<body>
<div class="page-shell" id="top">
    <jsp:include page="../common/header.jsp" />
    <jsp:include page="../common/nav.jsp" />

    <main class="report-page report-form-page">
        <header class="report-page-header">
            <div class="report-header-copy">
                <h1>신고 상세 내역</h1>
            </div>
        </header>

        <%-- 2026-07-14 [수정] 일반 회원 신고 상세를 공통 신고 UI로 정리한다. --%>
        <section class="report-card" aria-labelledby="reportDetailTitle">
            <div class="report-card-heading">
                <h2 class="report-card-title" id="reportDetailTitle">신고 정보</h2>
                <c:choose>
                    <c:when test="${outVO.reportStatus eq '01' or outVO.reportStatus eq '0'}"><span class="report-status is-pending">처리대기</span></c:when>
                    <c:when test="${outVO.reportStatus eq '02' or outVO.reportStatus eq '1'}"><span class="report-status is-complete">처리완료</span></c:when>
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
                    <th>신고 구분</th>
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
                    <td><c:out value="${empty outVO.reporterNickname ? '-' : outVO.reporterNickname}" /></td>
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
                    <th>신고 접수일</th>
                    <td><c:out value="${outVO.createDt}" /></td>
                </tr>
                <c:if test="${outVO.reportStatus eq '02' or outVO.reportStatus eq '03'}">
                    <tr>
                        <th>담당 관리자</th>
                        <td><c:out value="${empty outVO.adminNickName ? '-' : outVO.adminNickName}" /></td>
                    </tr>
                    <tr>
                        <th>처리 일시</th>
                        <td><c:out value="${empty outVO.processDt ? '-' : outVO.processDt}" /></td>
                    </tr>
                </c:if>
                </tbody>
            </table>

            <h3 class="report-reason-title">신고 사유 및 상세 내용</h3>
            <div class="report-reason-box"><c:out value="${outVO.reason}" /></div>

            <div class="report-detail-actions">
                <a class="report-button is-primary" href="${CP}/report/myReportList.do">목록으로 돌아가기</a>
            </div>
        </section>
    </main>

    <jsp:include page="../common/footer.jsp" />
    <jsp:include page="../common/floatingBar.jsp" />
    <jsp:include page="../common/mobileBottomNav.jsp" />
</div>
</body>
</html>
