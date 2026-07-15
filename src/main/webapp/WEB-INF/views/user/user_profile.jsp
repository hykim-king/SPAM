<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>판매자 프로필 | SPAM</title>
    <link rel="stylesheet" href="${CP}/resources/css/index.css?v=20260715">
    <link rel="stylesheet" href="${CP}/resources/css/member.css?v=20260715">
    <link rel="stylesheet" href="${CP}/resources/css/product.css?v=20260715">
    <script defer src="${CP}/resources/js/index.js?v=20260715"></script>
</head>
<body>
<div class="page-shell" id="top">
    <jsp:include page="../common/header.jsp" />
    <jsp:include page="../common/nav.jsp" />

    <%-- 2026-07-14 [추가] 판매자 프로필을 product 화면에서 user 화면으로 이전한다. --%>
    <main class="member-page-shell user-profile-page">
        <header class="page-header user-profile-page-header">
            <div>
                <h1 class="page-title">판매자 프로필</h1>
            </div>
        </header>

        <div class="user-profile-layout">
            <aside class="user-profile-card" aria-label="판매자 정보">
                <span class="user-profile-avatar" aria-hidden="true">S</span>
                <h2 class="user-profile-nickname">
                    <c:choose>
                        <c:when test="${not empty seller.nickname}"><c:out value="${seller.nickname}"/></c:when>
                        <c:otherwise>판매자 #<c:out value="${sellerUserNum}"/></c:otherwise>
                    </c:choose>
                </h2>

                <div class="user-profile-stats">
                    <div><span>판매 상품</span><strong>${totalCnt}개</strong></div>
                    <div>
                        <span>가입일</span>
                        <strong>
                            <c:choose>
                                <c:when test="${not empty seller.createDt}"><fmt:formatDate value="${seller.createDt}" pattern="yyyy.MM.dd"/></c:when>
                                <c:otherwise>정보 없음</c:otherwise>
                            </c:choose>
                        </strong>
                    </div>
                </div>

                <%-- 본인은 자신을 신고할 수 없으며, 비로그인 사용자는 공통 로그인 모달을 표시한다. --%>
                <c:if test="${not isOwnProfile}">
                    <a class="user-profile-report-button"
                       href="${CP}/report/reportUserForm.do?reportedUserNo=${sellerUserNum}&amp;reportType=USER"
                       data-spam-modal="${empty sessionScope.loginUser ? 'login' : ''}"
                       data-login-url="${CP}/user/login.do">신고하기</a>
                </c:if>
            </aside>

            <section class="user-profile-products" aria-labelledby="sellerProductTitle">
                <div class="user-profile-products-toolbar">
                    <div class="user-profile-products-header">
                        <h2 id="sellerProductTitle">
                            <c:choose>
                                <c:when test="${not empty seller.nickname}">@<c:out value="${seller.nickname}"/>의 다른 상품</c:when>
                                <c:otherwise>판매자 #<c:out value="${sellerUserNum}"/>의 다른 상품</c:otherwise>
                            </c:choose>
                        </h2>
                        <span>${totalCnt}개</span>
                    </div>

                    <nav class="product-status-tabs user-profile-status-tabs" aria-label="판매자 상품 상태별 보기">
                        <a class="${search.status eq 'ACTIVE' ? 'is-active' : ''}"
                           href="${CP}/user/profile.do?userNum=${sellerUserNum}">전체</a>
                        <a class="${search.status eq '01' ? 'is-active' : ''}"
                           href="${CP}/user/profile.do?userNum=${sellerUserNum}&amp;status=01">판매중</a>
                        <a class="${search.status eq '02' ? 'is-active' : ''}"
                           href="${CP}/user/profile.do?userNum=${sellerUserNum}&amp;status=02">예약중</a>
                        <a class="${search.status eq '03' ? 'is-active' : ''}"
                           href="${CP}/user/profile.do?userNum=${sellerUserNum}&amp;status=03">판매완료</a>
                    </nav>
                </div>

                <c:choose>
                    <c:when test="${empty list}">
                        <div class="product-empty-state user-profile-empty-state">
                            <strong>해당 상태의 상품이 없습니다.</strong>
                            <p>현재 확인할 수 있는 상품이 없습니다.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="product-card-grid user-profile-product-grid">
                            <c:forEach var="product" items="${list}">
                                <article class="product-list-card">
                                    <a class="product-list-card-link" href="${CP}/product/view.do?productNo=${product.productNo}">
                                        <div class="product-list-thumb">
                                            <c:choose>
                                                <c:when test="${not empty product.thumbnailPath}">
                                                    <img src="${CP}${product.thumbnailPath}" alt="<c:out value='${product.productTitle}'/>">
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="product-image-placeholder"><span>상품 이미지 준비중</span></div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="product-list-info">
                                            <strong class="product-list-title"><c:out value="${product.productTitle}"/></strong>
                                            <span class="product-list-price"><fmt:formatNumber value="${product.price}" pattern="#,##0"/>원</span>
                                            <span class="product-list-meta"><c:out value="${empty product.location ? '지역 미입력' : product.location}"/> · <time class="js-product-date" data-product-date="${product.createDt}"><c:out value="${product.createDt}"/></time></span>
                                        </div>
                                    </a>
                                </article>
                            </c:forEach>
                        </div>

                        <c:if test="${totalPage gt 1}">
                            <nav class="product-pagination user-profile-pagination" aria-label="판매자 상품 페이지 이동">
                                <c:choose>
                                    <c:when test="${search.pageNo gt 1}">
                                        <a href="${CP}/user/profile.do?userNum=${sellerUserNum}&amp;status=${search.status}&amp;pageNo=${search.pageNo - 1}" aria-label="이전 페이지">‹</a>
                                    </c:when>
                                    <c:otherwise><span aria-hidden="true">‹</span></c:otherwise>
                                </c:choose>

                                <c:forEach var="page" begin="${startPage}" end="${endPage}">
                                    <c:choose>
                                        <c:when test="${search.pageNo eq page}">
                                            <a class="is-current" aria-current="page"
                                               href="${CP}/user/profile.do?userNum=${sellerUserNum}&amp;status=${search.status}&amp;pageNo=${page}">${page}</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${CP}/user/profile.do?userNum=${sellerUserNum}&amp;status=${search.status}&amp;pageNo=${page}">${page}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>

                                <c:choose>
                                    <c:when test="${search.pageNo lt totalPage}">
                                        <a href="${CP}/user/profile.do?userNum=${sellerUserNum}&amp;status=${search.status}&amp;pageNo=${search.pageNo + 1}" aria-label="다음 페이지">›</a>
                                    </c:when>
                                    <c:otherwise><span aria-hidden="true">›</span></c:otherwise>
                                </c:choose>
                            </nav>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </section>
        </div>
    </main>

    <jsp:include page="../common/footer.jsp" />
    <jsp:include page="../common/floatingBar.jsp" />
    <jsp:include page="../common/mobileBottomNav.jsp" />
</div>
</body>
</html>
