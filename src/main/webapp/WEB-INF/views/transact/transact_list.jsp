<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>내 상점 | SPAM</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/transact.css">
</head>
<body>
    <div class="main-container">
        <div class="store-tabs">
            <a href="list.do?tab=product" class="${empty param.tab or param.tab eq 'product' ? 'active' : ''}">판매상품</a>
            <a href="list.do?tab=review" class="${param.tab eq 'review' ? 'active' : ''}">거래후기</a>
        </div>

        <div class="store-header">
            <c:if test="${empty param.tab or param.tab eq 'product'}">
                <div class="product-count">상품 <span>${list.size()}</span></div>
                
                <c:set var="currentTab" value="${empty param.tab ? 'product' : param.tab}" />
                <div class="sort-options">
                    <a href="list.do?tab=${currentTab}&sort=latest" class="${empty param.sort or param.sort eq 'latest' ? 'active' : ''}">최신순</a>
                    <a href="list.do?tab=${currentTab}&sort=popular" class="${param.sort eq 'popular' ? 'active' : ''}">인기순</a>
                    <a href="list.do?tab=${currentTab}&sort=lowPrice" class="${param.sort eq 'lowPrice' ? 'active' : ''}">낮은 가격순</a>
                    <a href="list.do?tab=${currentTab}&sort=highPrice" class="${param.sort eq 'highPrice' ? 'active' : ''}">높은 가격순</a>
                </div>
            </c:if>
            <c:if test="${param.tab eq 'review'}">
                <div class="product-count">후기 <span>${reviewList.size()}</span></div>
            </c:if>
        </div>

        <c:choose>
            <%-- [판매상품 탭 선택 시] --%>
            <c:when test="${empty param.tab or param.tab eq 'product'}">
                <div class="product-grid">
                    <c:forEach var="vo" items="${list}">
                        <div class="product-card">
                            <div class="img-wrap">
                                <img src="${pageContext.request.contextPath}${vo.imagePath}" alt="${vo.productTitle}">
                                <button class="btn-like">♡</button>
                            </div>
                            <div class="info-wrap">
                                <div class="price"><fmt:formatNumber value="${vo.price}" pattern="#,###"/>원</div>
                                <div class="title">${vo.productTitle}</div>
                                <div class="meta">
                                    <span class="time">${vo.createDt}</span> 
                                    <span class="likes">♥ ${vo.likeCnt}</span>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                    
                    <c:if test="${empty list}">
                        <div class="no-data">등록된 판매 상품이 없습니다.</div>
                    </c:if>
                </div>
            </c:when>

            <%-- [거래후기 탭 선택 시] --%>
            <c:when test="${param.tab eq 'review'}">
                <div class="review-list">
                    <div class="no-data">등록된 거래 후기가 없습니다.</div>
                </div>
            </c:when>
        </c:choose>
    </div>
</body>
</html>