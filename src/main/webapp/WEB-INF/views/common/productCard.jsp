<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!--
    [추가] 메인화면 상품 카드 공통 조각
    [수정 필요] 상품관리 모듈 연동 후 productNo, imageFile, title, price 등의 필드명을 실제 ProductVO에 맞춰 조정한다.
-->
<a class="product-card" href="${CP}/product/detail.do?productNo=${param.productNo}">
    <span class="wish-button" aria-label="찜 개수">♡</span>

    <div class="product-thumb">
        <img src="${CP}/resources/images/products/${param.imageFile}" alt="${param.productTitle}">
    </div>

    <div class="product-info">
        <strong class="product-title">${param.productTitle}</strong>
        <span class="product-price"><fmt:formatNumber value="${param.price}" pattern="#,##0" />원</span>
        <span class="product-meta">${param.region} · ${param.regTime}</span>
        <span class="product-wish">♡ ${param.wishCount}</span>
    </div>
</a>
