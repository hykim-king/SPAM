<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SPAM 로그인</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/member.css">
    <script defer src="${pageContext.request.contextPath}/resources/js/member.js"></script>
</head>
<body class="auth-page">
    <main class="auth-card">
        <a class="brand" href="${pageContext.request.contextPath}/user/login.do" aria-label="SPAM 로그인">
            <span class="brand-mark">SP</span>
            <span>SPAM</span>
        </a>

        <h1 class="auth-title">로그인</h1>
        <p class="auth-desc">회원은 마이페이지로, 관리자는 회원관리 화면으로 이동합니다.</p>

        <c:if test="${not empty msg}">
            <p class="alert"><c:out value="${msg}" /></p>
        </c:if>

        <form id="loginForm" class="form-grid" action="${pageContext.request.contextPath}/user/login.do" method="post" novalidate>
            <div class="form-row">
                <label class="label" for="loginUserId">아이디 <span class="required">*</span></label>
                <input class="input" type="text" id="loginUserId" name="userId" value="<c:out value='${userId}'/>" maxlength="30" autocomplete="username" required>
            </div>

            <div class="form-row">
                <label class="label" for="loginPassword">비밀번호 <span class="required">*</span></label>
                <input class="input" type="password" id="loginPassword" name="password" autocomplete="current-password" required>
            </div>

            <div class="form-actions">
                <button class="btn primary block" type="submit">로그인</button>
                <a class="btn outline block" href="${pageContext.request.contextPath}/user/join.do">회원가입</a>
            </div>
        </form>
    </main>
</body>
</html>
