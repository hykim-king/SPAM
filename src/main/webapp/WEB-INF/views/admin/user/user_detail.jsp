<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>관리자 회원상세</title>
</head>
<body>
<h2>관리자 회원상세</h2>

<table border="1">
    <tr>
        <th>회원번호</th>
        <td>${user.userNum}</td>
    </tr>
    <tr>
        <th>아이디</th>
        <td>${user.userId}</td>
    </tr>
    <tr>
        <th>이름</th>
        <td>${user.userName}</td>
    </tr>
    <tr>
        <th>닉네임</th>
        <td>${user.nickname}</td>
    </tr>
    <tr>
        <th>전화번호</th>
        <td>${user.phoneNum}</td>
    </tr>
    <tr>
        <th>이메일</th>
        <td>${user.email}</td>
    </tr>
    <tr>
        <th>생년월일</th>
        <td><fmt:formatDate value="${user.birthDt}" pattern="yyyy-MM-dd" /></td>
    </tr>
    <tr>
        <th>권한</th>
        <td>
            <c:choose>
                <c:when test="${user.userRole == '02'}">관리자</c:when>
                <c:otherwise>일반회원</c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr>
        <th>상태</th>
        <td>
            <c:choose>
                <c:when test="${user.userStatus == '01'}">정상</c:when>
                <c:when test="${user.userStatus == '02'}">탈퇴</c:when>
                <c:when test="${user.userStatus == '03'}">휴먼</c:when>
                <c:when test="${user.userStatus == '04'}">정지</c:when>
                <c:otherwise>${user.userStatus}</c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr>
        <th>가입일</th>
        <td><fmt:formatDate value="${user.createDt}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
    </tr>
    <tr>
        <th>수정일</th>
        <td><fmt:formatDate value="${user.updateDt}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
    </tr>
    <tr>
        <th>탈퇴일</th>
        <td><fmt:formatDate value="${user.withdrawDt}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
    </tr>
</table>

<hr>

<h3>회원상태 변경</h3>

<form action="${pageContext.request.contextPath}/admin/user/statusUpdate.do" method="post">
    <input type="hidden" name="userNum" value="${user.userNum}">
    <select name="userStatus">
        <option value="01" ${user.userStatus == '01' ? 'selected' : ''}>정상</option>
        <option value="02" ${user.userStatus == '02' ? 'selected' : ''}>탈퇴</option>
        <option value="03" ${user.userStatus == '03' ? 'selected' : ''}>휴먼</option>
        <option value="04" ${user.userStatus == '04' ? 'selected' : ''}>정지</option>
    </select>
    <button type="submit">상태 변경</button>
</form>

<h3>회원권한 변경</h3>

<form action="${pageContext.request.contextPath}/admin/user/roleUpdate.do" method="post">
    <input type="hidden" name="userNum" value="${user.userNum}">
    <select name="userRole">
        <option value="01" ${user.userRole == '01' ? 'selected' : ''}>일반회원</option>
        <option value="02" ${user.userRole == '02' ? 'selected' : ''}>관리자</option>
    </select>
    <button type="submit">권한 변경</button>
</form>

<p>
    <a href="${pageContext.request.contextPath}/admin/user/list.do">목록</a>
</p>
</body>
</html>
