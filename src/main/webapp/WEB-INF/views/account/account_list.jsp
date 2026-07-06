<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>계좌/자산 관리</title>

    <script>
        function confirmDelete() {
            return confirm('이 계좌를 삭제하시겠습니까?\n잔액이 남아 있으면 삭제할 수 없습니다.');
        }
    </script>
</head>
<body>
<h2>계좌/자산 관리</h2>

<c:if test="${not empty msg}">
    <p style="color:red;">${msg}</p>
</c:if>

<p>
    총 보유 자산:
    <strong><fmt:formatNumber value="${totalBalance}" pattern="#,##0" />원</strong>
</p>

<p>
    <a href="${pageContext.request.contextPath}/account/add.do">계좌 등록</a>
    <a href="${pageContext.request.contextPath}/user/mypage.do">마이페이지</a>
</p>

<table border="1">
    <thead>
        <tr>
            <th>계좌고유번호</th>
            <th>은행명</th>
            <th>계좌번호</th>
            <th>잔액</th>
            <th>등록일</th>
            <th>수정</th>
            <th>충전</th>
            <th>차감</th>
            <th>삭제</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="account" items="${accountList}">
            <tr>
                <td>${account.accountId}</td>
                <td>${account.bankName}</td>
                <td>${account.accountNum}</td>
                <td><fmt:formatNumber value="${account.balance}" pattern="#,##0" />원</td>
                <td><fmt:formatDate value="${account.createDt}" pattern="yyyy-MM-dd" /></td>
                <td>
                    <a href="${pageContext.request.contextPath}/account/update.do?accountId=${account.accountId}">수정</a>
                </td>
                <td>
                    <form action="${pageContext.request.contextPath}/account/deposit.do" method="post">
                        <input type="hidden" name="accountId" value="${account.accountId}">
                        <input type="number" name="amount" min="1" step="1" placeholder="금액" required>
                        <button type="submit">충전</button>
                    </form>
                </td>
                <td>
                    <form action="${pageContext.request.contextPath}/account/withdraw.do" method="post">
                        <input type="hidden" name="accountId" value="${account.accountId}">
                        <input type="number" name="amount" min="1" step="1" placeholder="금액" required>
                        <button type="submit">차감</button>
                    </form>
                </td>
                <td>
                    <form action="${pageContext.request.contextPath}/account/delete.do" method="post" onsubmit="return confirmDelete();">
                        <input type="hidden" name="accountId" value="${account.accountId}">
                        <button type="submit">삭제</button>
                    </form>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty accountList}">
            <tr>
                <td colspan="9">등록된 계좌가 없습니다.</td>
            </tr>
        </c:if>
    </tbody>
</table>
</body>
</html>
