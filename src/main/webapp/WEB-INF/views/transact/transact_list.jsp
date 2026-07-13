<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>관리자 전체 조회</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        * { box-sizing: border-box; }
        body, html { margin: 0; padding: 0; height: 100%; font-family: 'Noto Sans KR', sans-serif; background-color: #f4f5f7; }
        .admin-wrap { display: flex; min-height: 100vh; }
        .sidebar { width: 220px; background-color: #ffffff; border-right: 1px solid #e2e8f0; flex-shrink: 0; }
        .sidebar h2 { padding: 30px 20px; font-size: 18px; text-align: center; margin: 0; color: #1e293b; font-weight: 800; }
        .main-content { flex-grow: 1; padding: 40px; }
        .filter-btn { padding: 6px 16px; margin-right: 8px; text-decoration: none; color: #475569; background: #ffffff; border: 1px solid #cbd5e1; border-radius: 20px; font-size: 13px; transition: all 0.2s; }
        .filter-btn:hover { background: #f1f5f9; border-color: #94a3b8; }
        .styled-table { width: 100%; border-collapse: collapse; background: white; border-radius: 8px; overflow: hidden; }
        .styled-table th, .styled-table td { padding: 15px; border-bottom: 1px solid #e2e8f0; text-align: center; }
        .styled-table th { background-color: #f8fafc; color: #334155; }
        
        .modal-bg { display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.5); z-index:999; overflow-y: auto; }
        .modal-content { position:absolute; top:50%; left:50%; transform:translate(-50%,-50%); width:350px; background:white; padding:30px; border-radius:10px; box-shadow:0 4px 6px rgba(0,0,0,0.1); height:auto; max-height:90vh; }
        .close-btn { display:block !important; width:100%; padding:12px; margin-top:20px; cursor:pointer; background:#e2e8f0; border:none; border-radius:5px; font-weight:bold; text-align:center; color:#000; }
    </style>
</head>
<body>

<div class="admin-wrap">
    <div class="sidebar"><h2>관리자 페이지</h2></div>
    <div class="main-content">
        <h2 class="title">전체 상품 현황</h2>
        
        <div style="margin-bottom: 25px; padding-bottom: 15px; border-bottom: 1px solid #e2e8f0;">
            <a href="${pageContext.request.contextPath}/transact/list.do" class="filter-btn">전체</a>
            <a href="${pageContext.request.contextPath}/transact/list.do?status=01" class="filter-btn">판매중</a>
            <a href="${pageContext.request.contextPath}/transact/list.do?status=02" class="filter-btn">판매완료</a>
            <a href="${pageContext.request.contextPath}/transact/list.do?status=03" class="filter-btn">예약중</a>
        </div>

        <table class="styled-table">
            <thead>
                <tr><th>상품명</th><th>판매자 아이디</th><th>가격</th><th>상태</th></tr>
            </thead>
            <tbody>
                <c:forEach var="vo" items="${list}">
                    <tr>
                        <td style="text-align: left; padding-left: 20px;">
                            <a href="javascript:showDetail('${vo.productNo}')" style="text-decoration:none; color:#334155; cursor:pointer;">${vo.productTitle}</a>
                        </td>
                        <td>${vo.sellerId}</td>
                        <td><fmt:formatNumber value="${vo.price}" pattern="#,###" />원</td>
                        <td>${vo.status == '01' ? '판매중' : '판매완료'}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<div id="detailModal" class="modal-bg">
    <div class="modal-content">
        <h3>거래 상세</h3>
        <p>상품명: <span id="mProductTitle"></span></p>
        <p>상태: <span id="mStatus"></span></p>
        <p>금액: <span id="mAmount"></span></p>
        <p>등록일: <span id="mDate"></span></p>
        <button type="button" class="close-btn" onclick="closeModal()">닫기</button>
    </div>
</div>

<script>
function showDetail(productNo) {
    $.ajax({
        url: "${pageContext.request.contextPath}/transact/getDetail.do",
        data: { productNo: productNo },
        success: function(data) {
            $("#mProductTitle").text(data.productName);
            $("#mStatus").text(data.txStatus == '01' ? '판매중' : '판매완료');
            $("#mAmount").text((data.amount ? data.amount.toLocaleString() : "0") + "원");
            $("#mDate").text(data.createDt ? data.createDt : "정보 없음");
            $("#detailModal").show();
        },
        error: function() { alert("정보를 불러오지 못했습니다."); }
    });
}
function closeModal() { $("#detailModal").hide(); }
</script>
</body>
</html>