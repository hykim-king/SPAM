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
                    <td><fmt:formatNumber value="${vo.amount}"/>원</td>
                    <td class="status-text">
                        ${vo.txStatus == '01' ? '판매중' : (vo.txStatus == '02' ? '예약중' : '판매완료')}
                    </td>
                    <td>
                        <button type="button" class="btn-detail detailBtn" 
                                data-id="${vo.txId}" 
                                data-name="${vo.productName}" 
                                data-amount="${vo.amount}" 
                                data-status="${vo.txStatus}">상세</button>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<div id="detailModal" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-header"><span>거래 상세 정보</span><button class="closeModalBtn">✕</button></div>
        <div class="modal-body">
            <table class="modal-table">
                <tr><th>상품명</th><td id="m_name"></td></tr>
                <tr><th>금액</th><td id="m_amount"></td></tr>
                <tr><th>상태</th><td>
                    <span id="m_status_text"></span>
                    <select id="m_status_select" style="display:none;">
                        <option value="01">판매중</option>
                        <option value="02">예약중</option>
                        <option value="03">판매완료</option>
                    </select>
                </td></tr>
            </table>
            <button type="button" id="saveBtn" class="btn-list" style="display:none;" onclick="saveStatus()">상태 변경</button>
        </div>
    </div>
</div>

<script>
var currentTxId = "";
var isSeller = "${currentTab}" === "sale"; // 판매 내역인지 확인

$(document).ready(function() {
    // 상세 버튼 클릭 시 모달 오픈
    $('.detailBtn').on('click', function() {
        currentTxId = $(this).data('id');
        var status = $(this).data('status');
        
        $('#m_name').text($(this).data('name'));
        $('#m_amount').text($(this).data('amount') + '원');
        
        // 판매자만 상태 변경 가능
        if(isSeller) {
            $('#m_status_text').hide();
            $('#m_status_select').show().val(status);
            $('#saveBtn').show();
        } else {
            // 구매자는 상태 텍스트만 확인 가능
            var statusText = status == '01' ? '판매중' : (status == '02' ? '예약중' : '판매완료');
            $('#m_status_text').text(statusText).show();
            $('#m_status_select').hide();
            $('#saveBtn').hide();
        }
        $('#detailModal').fadeIn();
    });

    $('.closeModalBtn').on('click', function() { $('#detailModal').fadeOut(); });
});

// 상태 변경 저장 (AJAX)
function saveStatus() {
    var status = $('#m_status_select').val();
    var statusText = $('#m_status_select option:selected').text();
    
    $.ajax({
        url: "${pageContext.request.contextPath}/transact/updateTxStatus.do",
        type: "POST",
        data: { txId: currentTxId, status: status },
        success: function(res) {
            if(res === "success") {
                alert("상태가 '" + statusText + "'(으)로 변경되었습니다.");
                // 화면 즉시 반영
                $('#row_' + currentTxId + ' .status-text').text(statusText);
                $('#detailModal').fadeOut();
            } else {
                alert("데이터 업데이트에 실패했습니다.");
            }
        },
        error: function() { alert("서버 오류가 발생했습니다."); }
    });
}
</script>
</body>
</html>