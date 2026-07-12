<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>거래 내역</title>
   <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/transact.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<div class="container">
    <div class="tabs">
        <a href="list.do?tab=product" class="${currentTab == 'product' ? 'active' : ''}">구매 내역</a>
        <a href="list.do?tab=sale" class="${currentTab == 'sale' ? 'active' : ''}">판매 내역</a>
    </div>

    <table class="styled-table">
        <thead>
            <tr>
                <th>상품</th>
                <th>${currentTab == 'product' ? '판매자' : '구매자'}</th>
                <th>금액</th>
                <th>상태</th>
                <th>상세</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="vo" items="${list}">
                <tr id="row_${vo.txId}">
                    <td>${vo.productName}</td>
                    <td>${vo.partnerName}</td>
                    <td><fmt:formatNumber value="${vo.amount}" pattern="#,###" />원</td>
                    <td id="status_${vo.txId}">
                        ${vo.txStatus == '01' ? '판매중' : (vo.txStatus == '02' ? '예약중' : '판매완료')}
                    </td>
                    <td>
                        <button type="button" class="detailBtn" 
                                data-id="${vo.txId}" 
                                data-product="${vo.productName}" 
                                data-amount="${vo.amount}" 
                                data-status="${vo.txStatus}"
                                data-seller="${vo.sellerNo}">상세</button>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>