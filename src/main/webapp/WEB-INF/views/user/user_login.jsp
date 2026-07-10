<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인 | SPAM</title>
    <link rel="stylesheet" href="${CP}/resources/css/index.css">
    <link rel="stylesheet" href="${CP}/resources/css/member.css">
    <script defer src="${CP}/resources/js/index.js"></script>
    <script defer src="${CP}/resources/js/member.js"></script>
</head>
<body class="auth-page">
    <div class="page-shell" id="top">
        <jsp:include page="../common/header.jsp" />
        <jsp:include page="../common/nav.jsp" />

        <main class="member-auth-main">
            <section class="auth-card">

        <h1 class="auth-title">로그인</h1>
        <div class="auth-message-slot">
            <c:if test="${not empty msg}">
                <p class="alert"><c:out value="${msg}" /></p>
            </c:if>
        </div>
        <c:if test="${not empty joinSuccessMsg}">
            <script>window.addEventListener('DOMContentLoaded', function(){ alert('회원가입이 완료되었습니다. 로그인하세요.'); });</script>
        </c:if>

        <form id="loginForm" class="form-grid" action="${CP}/user/login.do" method="post" novalidate>
            <div class="form-row">
                <label class="label" for="loginUserId">아이디 <span class="required">*</span></label>
                <input class="input" type="text" id="loginUserId" name="userId" value="<c:out value='${userId}'/>" maxlength="30" autocomplete="username" required>
                <p class="field-message" data-error-for="loginUserId"></p>
            </div>

            <div class="form-row">
                <label class="label" for="loginPassword">비밀번호 <span class="required">*</span></label>
                <input class="input" type="password" id="loginPassword" name="password" autocomplete="current-password" required>
                <p class="field-message" data-error-for="loginPassword"></p>
            </div>

            <div class="form-actions">
                <button class="btn primary block" type="submit">로그인</button>
                <a class="btn outline block" href="${CP}/user/join.do">회원가입</a>
            </div>
        </form>
            </section>
        </main>

        <jsp:include page="../common/footer.jsp" />
        <jsp:include page="../common/floatingBar.jsp" />
        <jsp:include page="../common/mobileBottomNav.jsp" />
    </div>
</body>
</html>
