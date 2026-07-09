<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="CP" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 목록</title>

<style>
    body {
        margin: 0;
        font-family: Arial, sans-serif;
        background: #f5f6f8;
        color: #222;
    }

    .container {
        width: 1100px;
        margin: 40px auto;
    }

    .top-area {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 25px;
    }

    .top-area h1 {
        margin: 0;
        font-size: 30px;
    }

    .btn-write {
        background: #ff7a00;
        color: white;
        padding: 12px 20px;
        border-radius: 8px;
        text-decoration: none;
        font-weight: bold;
    }

    .product-grid {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        gap: 22px;
    }

    .product-card {
        background: white;
        border-radius: 14px;
        overflow: hidden;
        box-shadow: 0 4px 14px rgba(0,0,0,0.08);
        transition: 0.2s;
    }

    .product-card:hover {
        transform: translateY(-5px);
    }

    .product-card a {
        text-decoration: none;
        color: inherit;
    }

    .thumb {
        width: 100%;
        height: 190px;
        background: #ddd;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #777;
        font-size: 15px;
    }

    .product-info {
        padding: 16px;
    }

    .title {
        font-size: 17px;
        font-weight: bold;
        margin-bottom: 10px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .price {
        font-size: 20px;
        font-weight: bold;
        color: #ff7a00;
        margin-bottom: 10px;
    }

    .meta {
        font-size: 13px;
        color: #666;
        line-height: 1.7;
    }

    .status {
        display: inline-block;
        margin-top: 10px;
        padding: 5px 9px;
        border-radius: 20px;
        background: #eef0f3;
        font-size: 12px;
    }

    .empty {
        padding: 80px 0;
        text-align: center;
        background: white;
        border-radius: 14px;
        color: #777;
    }
</style>
</head>

<body>

<div class="container">

    <div class="top-area">
        <h1>중고 상품 목록</h1>
        <a class="btn-write" href="${CP}/product/saveForm.do">상품 등록</a>
    </div>

    <c:choose>
        <c:when test="${empty list}">
            <div class="empty">
                등록된 상품이 없습니다.
            </div>
        </c:when>

        <c:otherwise>
            <div class="product-grid">
                <c:forEach var="product" items="${list}">
                    <div class="product-card">
                        <a href="${CP}/product/view.do?productNo=${product.productNo}&sallerNo=0">

                            <div class="thumb">
                                상품 이미지
                            </div>

                            <div class="product-info">
                                <div class="title">${product.productTitle}</div>

                                <div class="price">
                                    <fmt:formatNumber value="${product.price}" pattern="#,###" />원
                                </div>

                                <div class="meta">
                                    지역: ${product.location}<br>
                                    조회수: ${product.viewCount}
                                    · 찜: ${product.likeCnt}
                                    · 채팅: ${product.chatCnt}<br>
                                    등록일: ${product.createDt}
                                </div>

                                <div class="status">
                                    <c:choose>
                                        <c:when test="${product.status eq '01'}">판매중</c:when>
                                        <c:when test="${product.status eq '02'}">예약중</c:when>
                                        <c:when test="${product.status eq '03'}">판매완료</c:when>
                                        <c:otherwise>상태 미정</c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                        </a>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>

</div>

</body>
</html>