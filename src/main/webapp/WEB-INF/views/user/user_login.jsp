<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
</head>
<body>
<h2>로그인</h2>

<!--
    로그인 실패 또는 회원가입 성공 메시지를 출력 

    예:
    - 회원가입이 완료되었습니다. 로그인하세요.
    - 아이디 또는 비밀번호가 일치하지 않습니다.
    - 탈퇴 처리된 회원입니다.
-->
<c:if test="${not empty msg}">
    <p style="color:red;">${msg}</p>
</c:if>

<!--
    로그인 form 
    입력값은 UserController.login()의 @RequestParam("userId"), @RequestParam("password")로 전달 
-->
<form action="${pageContext.request.contextPath}/user/login.do" method="post">
    <table border="1">
        <tr>
            <th>아이디</th>
            <td>
                <!-- 로그인 실패 시 이전에 입력했던 아이디를 다시 보여줌 -->
                <input type="text" name="userId" value="${userId}" required>
            </td>
        </tr>
        <tr>
            <th>비밀번호</th>
            <td>
                <!-- 비밀번호는 실패해도 다시 보여주지 않음 -->
                <input type="password" name="password" required>
            </td>
        </tr>
    </table>

    <button type="submit">로그인</button>
    <a href="${pageContext.request.contextPath}/user/join.do">회원가입</a>
</form>
</body>
</html>
