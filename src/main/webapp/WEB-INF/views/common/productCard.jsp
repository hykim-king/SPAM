<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty CP}">
    <c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
</c:if>

<a class="product-card" href="${CP}/product/view.do?productNo=${param.productNo}">
    <div class="product-thumb">
        <c:choose>
            <c:when test="${not empty param.thumbnailPath}">
                <img src="${CP}${param.thumbnailPath}" alt="<c:out value='${param.productTitle}' />">
            </c:when>
            <c:otherwise>
                <div class="product-image-placeholder"><span>상품 이미지 준비중</span></div>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="product-info">
        <strong class="product-title"><c:out value="${param.productTitle}" /></strong>
        <span class="product-price"><fmt:formatNumber value="${param.price}" pattern="#,##0" />원</span>
        <span class="product-meta"><c:out value="${empty param.location ? '지역 미입력' : param.location}" /> · <time class="js-product-date" data-product-date="${param.createDt}"><c:out value="${param.createDt}" /></time></span>
    </div>
</a>
