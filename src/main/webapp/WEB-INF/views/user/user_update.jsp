<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SPAM 회원정보 수정</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/member.css">
    <script defer src="${pageContext.request.contextPath}/resources/js/member.js"></script>
</head>
<body>
    <main class="page-shell">
        <header class="page-header">
            <div>
                <a class="brand" href="${pageContext.request.contextPath}/user/mypage.do" aria-label="SPAM 마이페이지">
                    <span class="brand-mark">SP</span>
                    <span>SPAM</span>
                </a>
                <h1 class="page-title">회원정보 수정</h1>
                <p class="page-desc">기본 정보, 비밀번호 변경, 회원탈퇴를 한 화면에서 처리합니다.</p>
            </div>
            <nav class="header-actions">
                <a class="btn outline" href="${pageContext.request.contextPath}/user/mypage.do">마이페이지</a>
                <a class="btn" href="${pageContext.request.contextPath}/user/logout.do">로그아웃</a>
            </nav>
        </header>

        <c:if test="${not empty msg}">
            <p class="alert"><c:out value="${msg}" /></p>
        </c:if>

        <fmt:formatDate value="${user.birthDt}" pattern="yyyy-MM-dd" var="birthDtText" />

        <div class="grid-2">
            <section class="panel">
                <h2 class="panel-title">기본 정보</h2>
                <form id="updateForm" class="form-grid" action="${pageContext.request.contextPath}/user/update.do" method="post" novalidate>
                    <input type="hidden" name="userNum" value="${user.userNum}">

                    <div class="form-row">
                        <label class="label" for="readonlyUserId">아이디</label>
                        <input class="input" type="text" id="readonlyUserId" value="<c:out value='${user.userId}'/>" readonly>
                        <p class="help-text">아이디는 변경할 수 없습니다.</p>
                    </div>

                    <div class="form-row">
                        <label class="label" for="updateUserName">이름 <span class="required">*</span></label>
                        <input class="input" type="text" id="updateUserName" name="userName" value="<c:out value='${user.userName}'/>" maxlength="7" required>
                    </div>

                    <div class="form-row">
                        <label class="label" for="updateNickname">닉네임</label>
                        <input class="input" type="text" id="updateNickname" name="nickname" value="<c:out value='${user.nickname}'/>" maxlength="30">
                    </div>

                    <div class="form-row">
                        <label class="label" for="updatePhoneNum">전화번호 <span class="required">*</span></label>
                        <input class="input" type="text" id="updatePhoneNum" name="phoneNum" value="<c:out value='${user.phoneNum}'/>" maxlength="13" placeholder="010-0000-0000" data-format="phone" required>
                    </div>

                    <div class="form-row">
                        <label class="label" for="updateEmail">이메일</label>
                        <input class="input" type="email" id="updateEmail" name="email" value="<c:out value='${user.email}'/>" maxlength="100" placeholder="example@test.com">
                    </div>

                    <div class="form-row">
                        <label class="label" for="updateBirthDt">생년월일</label>
                        <input class="input" type="date" id="updateBirthDt" name="birthDt" value="${birthDtText}">
                    </div>

                    <div class="form-actions">
                        <button class="btn primary" type="submit">수정 저장</button>
                        <a class="btn outline" href="${pageContext.request.contextPath}/user/mypage.do">취소</a>
                    </div>
                </form>
            </section>

            <div class="stack">
                <section class="panel">
                    <h2 class="panel-title">비밀번호 변경</h2>
                    <form id="passwordForm" class="form-grid" action="${pageContext.request.contextPath}/user/password.do" method="post" novalidate>
                        <div class="form-row">
                            <label class="label" for="currentPassword">현재 비밀번호 <span class="required">*</span></label>
                            <input class="input" type="password" id="currentPassword" name="currentPassword" autocomplete="current-password" required>
                        </div>

                        <div class="form-row">
                            <label class="label" for="newPassword">새 비밀번호 <span class="required">*</span></label>
                            <input class="input" type="password" id="newPassword" name="newPassword" autocomplete="new-password" required>
                        </div>

                        <div class="form-row">
                            <label class="label" for="newPasswordConfirm">새 비밀번호 확인 <span class="required">*</span></label>
                            <input class="input" type="password" id="newPasswordConfirm" name="newPasswordConfirm" autocomplete="new-password" required>
                        </div>

                        <div class="form-actions">
                            <button class="btn primary" type="submit">비밀번호 변경</button>
                        </div>
                    </form>
                </section>

                <section class="panel">
                    <h2 class="panel-title">회원탈퇴</h2>
                    <p class="help-text">탈퇴하면 계정 상태가 탈퇴로 변경되고 로그인이 제한됩니다.</p>

                    <div id="withdrawStartArea" class="card-actions ${withdrawError ? 'hidden' : ''}">
                        <button id="showWithdrawButton" class="btn ghost-danger" type="button">회원탈퇴</button>
                    </div>

                    <div id="withdrawConfirmArea" class="withdraw-box ${withdrawError ? '' : 'hidden'}">
                        <form id="withdrawForm" class="form-grid" action="${pageContext.request.contextPath}/user/withdraw.do" method="post" novalidate>
                            <div class="form-row">
                                <label class="label" for="withdrawPassword">비밀번호 확인 <span class="required">*</span></label>
                                <input class="input" type="password" id="withdrawPassword" name="password" autocomplete="current-password" required>
                            </div>
                            <div class="form-actions">
                                <button class="btn danger" type="submit">탈퇴 진행</button>
                                <button id="hideWithdrawButton" class="btn outline" type="button">취소</button>
                            </div>
                        </form>
                    </div>
                </section>
            </div>
        </div>
    </main>
</body>
</html>
