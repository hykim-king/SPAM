<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>마이페이지</title>
</head>
<body>
<h2>마이페이지</h2>

<!--
    일반 회원이 자기 정보를 간단히 확인하는 화면 
    - 숨긴 항목: 회원번호, 이름, 생년월일, 권한/상태/가입일
-->
<table border="1">
    <tr>
        <th>아이디</th>
        <td>${user.userId}</td>
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
</table>

<!-- 회원정보 수정 -->
<a href="${pageContext.request.contextPath}/user/update.do">회원정보 수정</a>

<!--
    로그아웃은 GET /user/logout.do로 처리합
    이 요청은 세션을 제거한 뒤 로그인 화면으로 이동
-->
<a href="${pageContext.request.contextPath}/user/logout.do">로그아웃</a>
</body>
</html>
