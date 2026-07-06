<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>계좌 등록/수정</title>
</head>
<body>
<c:choose>
    <c:when test="${mode == 'update'}">
        <h2>계좌 수정</h2>
    </c:when>
    <c:otherwise>
        <h2>계좌 등록</h2>
    </c:otherwise>
</c:choose>

<c:if test="${not empty msg}">
    <p style="color:red;">${msg}</p>
</c:if>

<c:choose>
    <c:when test="${mode == 'update'}">
        <c:set var="formAction" value="${pageContext.request.contextPath}/account/update.do" />
    </c:when>
    <c:otherwise>
        <c:set var="formAction" value="${pageContext.request.contextPath}/account/add.do" />
    </c:otherwise>
</c:choose>

<form action="${formAction}" method="post">
    <input type="hidden" name="accountId" value="${account.accountId}">

    <table border="1">
        <tr>
            <th>은행명 *</th>
            <td>
                <input type="text" name="bankName" value="${account.bankName}" maxlength="20" required>
            </td>
        </tr>
        <tr>
            <th>계좌번호 *</th>
            <td>
                <input type="text" name="accountNum" value="${account.accountNum}" maxlength="30" placeholder="숫자 또는 - 포함 입력" required>
            </td>
        </tr>
        <c:if test="${mode == 'update'}">
            <tr>
                <th>잔액</th>
                <td>
                    ${account.balance}원
                    <span style="color:gray;">잔액은 목록 화면의 충전/차감 기능으로만 변경합니다.</span>
                </td>
            </tr>
        </c:if>
    </table>

    <button type="submit">저장</button>
    <a href="${pageContext.request.contextPath}/account/list.do">취소</a>
</form>
</body>
</html>
