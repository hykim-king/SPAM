<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty CP}">
    <c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
</c:if>

<!--
    메인화면 상품 카드 공통 조각
    [수정 필요] 상품관리 모듈 연동 후 ProductVO 필드명에 맞춰 조정
-->
<a class="product-card" href="${CP}/product/view.do?productNo=${param.productNo}&amp;sallerNo=0">
    <div class="product-thumb">
        <img src="${CP}/resources/images/products/${param.imageFile}" alt="${param.productTitle}">
    </div>

    <div class="product-info">
        <strong class="product-title">${param.productTitle}</strong>
        <span class="product-price"><fmt:formatNumber value="${param.price}" pattern="#,##0" />원</span>
        <span class="product-meta">${param.region} · ${param.regTime}</span>
    </div>
</a>
