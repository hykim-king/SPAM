<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>거래내역 | SPAM</title>
    <link rel="stylesheet" href="${CP}/resources/css/index.css?v=20260714">
    <link rel="stylesheet" href="${CP}/resources/css/transact.css?v=20260714">
    <script defer src="${CP}/resources/js/index.js?v=20260714"></script>
</head>
<body>
<div class="page-shell" id="top">
    <jsp:include page="../common/header.jsp" />
    <jsp:include page="../common/nav.jsp" />

    <main class="transact-page">
        <header class="transact-page-header">
            <div>
                <h1>거래내역</h1>
                <p>구매한 상품과 판매한 상품의 거래 상태를 확인합니다.</p>
            </div>
            <nav class="transact-header-actions" aria-label="거래내역 화면 바로가기">
                <a class="transact-button" href="${CP}/user/mypage.do">마이페이지</a>
                <a class="transact-button is-primary" href="${CP}/main.do">메인</a>
            </nav>
        </header>

        <%-- 2026-07-14 [수정] tab/currentTab 혼용을 없애고 type=purchase|sale로 통일한다. --%>
        <nav class="transact-tabs" aria-label="구매 및 판매 내역">
            <a class="${type eq 'purchase' ? 'is-active' : ''}" href="${CP}/transact/list.do?type=purchase">구매내역</a>
            <a class="${type eq 'sale' ? 'is-active' : ''}" href="${CP}/transact/list.do?type=sale">판매내역</a>
        </nav>

        <section class="transact-panel" aria-label="거래내역 목록">
            <div class="transact-toolbar">
                <p>총 <strong><fmt:formatNumber value="${totalCount}" pattern="#,##0" /></strong>건</p>
                <nav class="transact-status-filters" aria-label="거래 상태 필터">
                    <c:url var="statusAllUrl" value="/transact/list.do">
                        <c:param name="type" value="${type}" />
                    </c:url>
                    <c:url var="statusProgressUrl" value="/transact/list.do">
                        <c:param name="type" value="${type}" />
                        <c:param name="status" value="01" />
                    </c:url>
                    <c:url var="statusCompleteUrl" value="/transact/list.do">
                        <c:param name="type" value="${type}" />
                        <c:param name="status" value="02" />
                    </c:url>
                    <c:url var="statusCancelUrl" value="/transact/list.do">
                        <c:param name="type" value="${type}" />
                        <c:param name="status" value="03" />
                    </c:url>
                    <a class="${empty paging.status ? 'is-active' : ''}" href="${statusAllUrl}">전체</a>
                    <a class="${paging.status eq '01' ? 'is-active' : ''}" href="${statusProgressUrl}">거래중</a>
                    <a class="${paging.status eq '02' ? 'is-active' : ''}" href="${statusCompleteUrl}">완료</a>
                    <a class="${paging.status eq '03' ? 'is-active' : ''}" href="${statusCancelUrl}">취소</a>
                </nav>
            </div>

            <div class="transact-table-wrap">
                <table class="transact-table">
                    <thead>
                    <tr>
                        <th>상품 정보</th>
                        <th>상대방</th>
                        <th>거래 금액</th>
                        <th>거래 상태</th>
                        <th>거래일</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty list}">
                            <tr class="transact-empty-row">
                                <td colspan="5">해당 조건의 거래내역이 없습니다.</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="transaction" items="${list}">
                                <tr>
                                    <td>
                                        <a class="transact-product" href="${CP}/product/view.do?productNo=${transaction.productNo}">
                                            <span class="transact-product-thumb">
                                                <c:choose>
                                                    <c:when test="${not empty transaction.thumbnailPath}">
                                                        <img src="${CP}${transaction.thumbnailPath}" alt="">
                                                    </c:when>
                                                    <c:otherwise><span aria-hidden="true">SP</span></c:otherwise>
                                                </c:choose>
                                            </span>
                                            <span class="transact-product-copy">
                                                <strong><c:out value="${transaction.productName}" /></strong>
                                                <small>거래번호 <c:out value="${transaction.txId}" /></small>
                                            </span>
                                        </a>
                                    </td>
                                    <td><c:out value="${empty transaction.partnerName ? '회원 정보 없음' : transaction.partnerName}" /></td>
                                    <td class="transact-amount"><fmt:formatNumber value="${transaction.amount}" pattern="#,##0" />원</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${transaction.txStatus eq '01'}"><span class="transact-status is-progress">거래중</span></c:when>
                                            <c:when test="${transaction.txStatus eq '02'}"><span class="transact-status is-complete">완료</span></c:when>
                                            <c:when test="${transaction.txStatus eq '03'}"><span class="transact-status is-cancelled">취소</span></c:when>
                                            <c:otherwise><span class="transact-status is-unknown">미분류</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><fmt:formatDate value="${transaction.createDt}" pattern="yyyy.MM.dd" /></td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>

            <c:if test="${paging.totalPage gt 1}">
                <nav class="transact-pagination" aria-label="거래내역 페이지">
                    <c:if test="${paging.pageNo gt 1}">
                        <c:url var="prevUrl" value="/transact/list.do">
                            <c:param name="type" value="${type}" />
                            <c:param name="status" value="${paging.status}" />
                            <c:param name="pageNo" value="${paging.pageNo - 1}" />
                            <c:param name="pageSize" value="${paging.pageSize}" />
                        </c:url>
                        <a href="${prevUrl}" aria-label="이전 페이지">이전</a>
                    </c:if>

                    <c:forEach var="page" begin="${paging.startPage}" end="${paging.endPage}">
                        <c:url var="pageUrl" value="/transact/list.do">
                            <c:param name="type" value="${type}" />
                            <c:param name="status" value="${paging.status}" />
                            <c:param name="pageNo" value="${page}" />
                            <c:param name="pageSize" value="${paging.pageSize}" />
                        </c:url>
                        <a class="${paging.pageNo eq page ? 'is-current' : ''}" href="${pageUrl}">${page}</a>
                    </c:forEach>

                    <c:if test="${paging.pageNo lt paging.totalPage}">
                        <c:url var="nextUrl" value="/transact/list.do">
                            <c:param name="type" value="${type}" />
                            <c:param name="status" value="${paging.status}" />
                            <c:param name="pageNo" value="${paging.pageNo + 1}" />
                            <c:param name="pageSize" value="${paging.pageSize}" />
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
