<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>판매자 프로필 | SPAM</title>
    <link rel="stylesheet" href="${CP}/resources/css/index.css">
    <link rel="stylesheet" href="${CP}/resources/css/product.css">
    <script defer src="${CP}/resources/js/index.js"></script>
    <script defer src="${CP}/resources/js/product.js"></script>
</head>
<body>
<div class="page-shell" id="top">
    <jsp:include page="../common/header.jsp" />
    <jsp:include page="../common/nav.jsp" />

    <main class="product-page product-seller-page">
        <header class="product-page-header">
            <div class="product-page-heading">
                <h1>판매자 프로필</h1>
                <p>판매자 기본 정보와 현재 판매 중인 다른 상품을 확인합니다.</p>
            </div>
        </header>

        <div class="product-seller-layout">
            <aside class="product-profile-card" aria-label="판매자 정보">
                <span class="product-profile-avatar" aria-hidden="true">S</span>
                <h2>
                    <c:choose>
                        <c:when test="${not empty seller.nickname}"><c:out value="${seller.nickname}"/></c:when>
                        <c:otherwise>판매자 #<c:out value="${param.sallerNo}"/></c:otherwise>
                    </c:choose>
                </h2>
                <div class="product-profile-stats">
                    <div><span>판매 상품</span><strong>${fn:length(list)}개</strong></div>
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
            </aside>

            <section class="product-seller-products" aria-labelledby="sellerProductTitle">
                <div class="product-seller-products-header">
                    <h2 id="sellerProductTitle">판매자의 다른 상품</h2>
                    <span>${fn:length(list)}개</span>
                </div>

                <c:choose>
                    <c:when test="${empty list}">
                        <div class="product-empty-state">
                            <strong>판매 중인 상품이 없습니다.</strong>
                            <p>현재 확인할 수 있는 다른 상품이 없습니다.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="product-card-grid">
                            <c:forEach var="product" items="${list}">
                                <article class="product-list-card">
                                    <a class="product-list-card-link" href="${CP}/product/view.do?productNo=${product.productNo}&amp;sallerNo=0">
                                        <div class="product-list-thumb">
                                            <c:choose>
                                                <c:when test="${not empty product.imageList}">
                                                    <img src="${CP}${product.imageList[0].filePath}" alt="<c:out value='${product.productTitle}'/>">
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="product-image-placeholder"><span>상품 이미지 준비중</span></div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="product-list-info">
                                            <strong class="product-list-title"><c:out value="${product.productTitle}"/></strong>
                                            <span class="product-list-price"><fmt:formatNumber value="${product.price}" pattern="#,##0"/>원</span>
                                            <span class="product-list-meta"><c:out value="${empty product.location ? '지역 미입력' : product.location}"/> · <c:out value="${product.createDt}"/></span>
                                        </div>
                                    </a>
                                </article>
                            </c:forEach>
                        </div>
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
