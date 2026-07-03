<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>SPAM 메인</title>
<link rel="stylesheet" href="/ehr/resources/css/common.css">
</head>
<body>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/nav.jsp" />

<div class="container">
    <h2>SPAM 메인 화면</h2>
    <p>회원 기능 테스트용 메인 화면입니다.</p>

    <h3>회원 기능</h3>
    <ul>
        <li><a href="/ehr/user/loginView.do">로그인</a></li>
        <li><a href="/ehr/user/regView.do">회원가입</a></li>
        <li><a href="/ehr/user/doRetrieve.do">회원 목록</a></li>
    </ul>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>