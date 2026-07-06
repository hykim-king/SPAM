<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SPAM 회원가입</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/member.css">
    <script defer src="${pageContext.request.contextPath}/resources/js/member.js"></script>
</head>
<body class="auth-page">
    <main class="auth-card wide">
        <a class="brand" href="${pageContext.request.contextPath}/user/login.do" aria-label="SPAM 로그인">
            <span class="brand-mark">SP</span>
            <span>SPAM</span>
        </a>

        <h1 class="auth-title">회원가입</h1>
        <p class="auth-desc">아이디, 전화번호, 이메일은 탈퇴 회원을 제외하고 중복 검사됩니다.</p>

        <c:if test="${not empty msg}">
            <p class="alert"><c:out value="${msg}" /></p>
        </c:if>

        <form id="joinForm" class="form-grid two-col" action="${pageContext.request.contextPath}/user/join.do" method="post" novalidate>
            <div class="form-row full">
                <label class="label" for="userId">아이디 <span class="required">*</span></label>
                <input class="input" type="text" id="userId" name="userId" value="<c:out value='${user.userId}'/>" maxlength="30" autocomplete="username" required>
            </div>

            <div class="form-row">
                <label class="label" for="password">비밀번호 <span class="required">*</span></label>
                <input class="input" type="password" id="password" name="password" maxlength="100" autocomplete="new-password" required>
                <p class="help-text">4자 이상 입력하세요.</p>
            </div>

            <div class="form-row">
                <label class="label" for="passwordConfirm">비밀번호 확인 <span class="required">*</span></label>
                <input class="input" type="password" id="passwordConfirm" name="passwordConfirm" maxlength="100" autocomplete="new-password" required>
            </div>

            <div class="form-row">
                <label class="label" for="userName">이름 <span class="required">*</span></label>
                <input class="input" type="text" id="userName" name="userName" value="<c:out value='${user.userName}'/>" maxlength="7" required>
            </div>

            <div class="form-row">
                <label class="label" for="nickname">닉네임</label>
                <input class="input" type="text" id="nickname" name="nickname" value="<c:out value='${user.nickname}'/>" maxlength="30">
                <p class="help-text">비워두면 이름과 같은 값으로 저장됩니다.</p>
            </div>

            <div class="form-row">
                <label class="label" for="phoneNum">전화번호 <span class="required">*</span></label>
                <input class="input" type="text" id="phoneNum" name="phoneNum" value="<c:out value='${user.phoneNum}'/>" maxlength="13" placeholder="010-0000-0000" data-format="phone" autocomplete="tel" required>
            </div>

            <div class="form-row">
                <label class="label" for="email">이메일</label>
                <input class="input" type="email" id="email" name="email" value="<c:out value='${user.email}'/>" maxlength="100" placeholder="example@test.com" autocomplete="email">
            </div>

            <div class="form-row full">
                <label class="label" for="birthDt">생년월일</label>
                <input class="input" type="date" id="birthDt" name="birthDt">
            </div>

            <div class="form-actions full">
                <button class="btn primary" type="submit">회원가입</button>
                <a class="btn outline" href="${pageContext.request.contextPath}/user/login.do">로그인으로 이동</a>
            </div>
        </form>
    </main>
</body>
</html>
