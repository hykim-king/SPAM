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
        .filter-btn.active { background: #3b82f6; color: white; border-color: #3b82f6; }
        .styled-table { width: 100%; border-collapse: collapse; background: white; border-radius: 8px; overflow: hidden; }
        .styled-table th, .styled-table td { padding: 15px; border-bottom: 1px solid #e2e8f0; text-align: center; }
        .styled-table th { background-color: #f8fafc; color: #334155; }
        
        .modal-bg { display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.5); z-index:999; overflow-y: auto; }
        .modal-content { position:absolute; top:50%; left:50%; transform:translate(-50%,-50%); width:350px; background:white; padding:30px; border-radius:10px; box-shadow:0 4px 6px rgba(0,0,0,0.1); height:auto; max-height:90vh; }
        .close-btn { display:block !important; width:100%; padding:12px; margin-top:20px; cursor:pointer; background:#e2e8f0; border:none; border-radius:5px; font-weight:bold; text-align:center; color:#000; }
        
        /* 페이징 스타일 수정: flex-wrap 적용하여 줄바꿈 해결 */
        .paging-area { display: flex; flex-wrap: wrap; justify-content: center; gap: 5px; margin-top: 30px; }
        .paging-area a { padding: 8px 16px; border: 1px solid #cbd5e1; text-decoration: none; color: #475569; border-radius: 4px; }
        .paging-area a.active { background-color: #3b82f6; color: white; border-color: #3b82f6; }
    </style>
</head>
<body>

<div class="admin-wrap">
    <div class="sidebar"><h2>관리자 페이지</h2></div>
    <div class="main-content">
        <h2 class="title">전체 상품 현황</h2>
        
        <div style="margin-bottom: 25px; padding-bottom: 15px; border-bottom: 1px solid #e2e8f0;">
            <a href="list.do" class="filter-btn ${empty currentStatus ? 'active' : ''}">전체</a>
            <a href="list.do?status=01" class="filter-btn ${currentStatus == '01' ? 'active' : ''}">판매중</a>
            <a href="list.do?status=02" class="filter-btn ${currentStatus == '02' ? 'active' : ''}">거래완료</a>
            <a href="list.do?status=03" class="filter-btn ${currentStatus == '03' ? 'active' : ''}">기타</a>
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
                        <td>${vo.userId}</td>
                        <td><fmt:formatNumber value="${vo.price}" pattern="#,###" />원</td>
                        <td>${vo.status == '01' ? '판매중' : (vo.status == '02' ? '거래완료' : '기타')}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <%-- 페이징 영역 전체를 아래 코드로 교체하세요 --%>
<div class="paging-area" style="display: flex; justify-content: center; gap: 10px; margin-top: 30px;">
    <%-- 이전 버튼 --%>
    <c:if test="${paging.startPage > 1}">
        <a href="list.do?pageNo=${paging.startPage - 1}&status=${currentStatus}" 
           style="padding: 8px 16px; border: 1px solid #cbd5e1; text-decoration: none; color: #475569; border-radius: 4px; background: #fff;">이전</a>
    </c:if>

    <%-- 페이지 번호 --%>
    <c:forEach var="i" begin="${paging.startPage}" end="${paging.endPage}">
        <a href="list.do?pageNo=${i}&status=${currentStatus}" 
           style="padding: 8px 16px; border: 1px solid #cbd5e1; text-decoration: none; color: #475569; border-radius: 4px; background: ${paging.pageNo == i ? '#3b82f6' : '#fff'}; color: ${paging.pageNo == i ? 'white' : '#475569'}; font-weight: ${paging.pageNo == i ? 'bold' : 'normal'};">
           ${i}
        </a>
    </c:forEach>

    <%-- 다음 버튼 --%>
    <c:if test="${paging.endPage < (totalCount + 9) / 10}">
        <a href="list.do?pageNo=${paging.endPage + 1}&status=${currentStatus}" 
           style="padding: 8px 16px; border: 1px solid #cbd5e1; text-decoration: none; color: #475569; border-radius: 4px; background: #fff;">다음</a>
    </c:if>
</div>
    </div>
</div>

<div id="detailModal" class="modal-bg">
    <div class="modal-content">
        <h3>거래 상세</h3>
        <p>상품명: <span id="mProductTitle"></span></p>
        <p>상태: <span id="mStatus"></span></p>
        <p>금액: <span id="mAmount"></span></p>
        <p>등록일: <span id="mDate"></span></p>
        <p>바로가기: <a id="mProductLink" href="#" target="_blank" style="color: blue; text-decoration: underline;">상품 상세 페이지 이동</a></p>
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
            $("#mStatus").text(data.txStatus == '01' ? '판매중' : '거래완료');
            $("#mAmount").text((data.amount ? data.amount.toLocaleString() : "0") + "원");
            $("#mDate").text(data.createDt ? data.createDt : "정보 없음");
            var linkUrl = "${pageContext.request.contextPath}/product/view.do?productNo=" + productNo;
            $("#mProductLink").attr("href", linkUrl);
            $("#detailModal").show();
        },
        error: function() { alert("정보를 불러오지 못했습니다."); }
    });
}
function closeModal() { $("#detailModal").hide(); }
</script>
</body>
</html>