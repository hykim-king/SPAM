<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>마이페이지 | SPAM</title>
    <link rel="stylesheet" href="${CP}/resources/css/index.css?v=20260715">
    <link rel="stylesheet" href="${CP}/resources/css/member.css?v=20260715">
    <script defer src="${CP}/resources/js/index.js?v=20260715"></script>
    <script defer src="${CP}/resources/js/member.js?v=20260715"></script>
</head>
<body>
    <div class="page-shell" id="top">
        <jsp:include page="../common/header.jsp" />
        <jsp:include page="../common/nav.jsp" />

        <main class="member-page-shell">
        <header class="page-header">
            <div>
                <h1 class="page-title">회원정보 수정</h1>
            </div>
            <nav class="header-actions">
                <a class="btn outline" href="${CP}/user/mypage.do">마이페이지</a>
                <a class="btn js-confirm-logout" href="${CP}/user/logout.do">로그아웃</a>
            </nav>
        </header>

        <c:if test="${not empty msg}">
            <p class="alert"><c:out value="${msg}" /></p>
        </c:if>

        <fmt:formatDate value="${user.birthDt}" pattern="yyyy-MM-dd" var="birthDtText" />

        <div class="grid-2">
            <section class="panel">
                <h2 class="panel-title">기본 정보</h2>
                <form id="updateForm" class="form-grid js-confirm-update" action="${CP}/user/update.do" method="post" novalidate>
                    <input type="hidden" name="userNum" value="${user.userNum}">

                    <div class="form-row">
                        <label class="label" for="readonlyUserId">아이디</label>
                        <input class="input" type="text" id="readonlyUserId" value="<c:out value='${user.userId}'/>" readonly>
                        <p class="field-message" data-error-for="readonlyUserId">아이디는 변경할 수 없습니다.</p>
                    </div>

                    <div class="form-row">
                        <label class="label" for="updateUserName">이름 <span class="required">*</span></label>
                        <input class="input" type="text" id="updateUserName" name="userName" value="<c:out value='${user.userName}'/>" maxlength="20" required>
                        <p class="field-message" data-error-for="updateUserName"></p>
                    </div>

                    <div class="form-row">
                        <label class="label" for="updateNickname">닉네임</label>
                        <input class="input" type="text" id="updateNickname" name="nickname" value="<c:out value='${user.nickname}'/>" maxlength="30">
                        <p class="field-message" data-error-for="updateNickname"></p>
                    </div>

                    <div class="form-row">
                        <label class="label" for="updatePhoneNum">전화번호 <span class="required">*</span></label>
                        <input class="input" type="tel" id="updatePhoneNum" name="phoneNum" value="<c:out value='${user.phoneNum}'/>" maxlength="11" minlength="11" pattern="010[0-9]{8}" inputmode="numeric" placeholder="01012345678" data-format="phone" autocomplete="tel" required>
                        <p class="field-message" data-error-for="updatePhoneNum"></p>
                    </div>

                    <div class="form-row">
                        <label class="label" for="updateEmail">이메일</label>
                        <input class="input" type="email" id="updateEmail" name="email" value="<c:out value='${user.email}'/>" maxlength="100" placeholder="example@test.com">
                        <p class="field-message" data-error-for="updateEmail"></p>
                    </div>

                    <div class="form-row">
                        <label class="label" for="updateBirthDt">생년월일</label>
                        <input class="input" type="text" id="updateBirthDt" name="birthDt" value="${birthDtText}" placeholder="YYYY-MM-DD" pattern="\d{4}-\d{2}-\d{2}">
                        <p class="field-message" data-error-for="updateBirthDt"></p>
                    </div>

                    <div class="form-actions">
                        <button class="btn primary" type="submit">수정 저장</button>
                        <a class="btn outline" href="${CP}/user/mypage.do">취소</a>
                    </div>
                </form>
            </section>

            <div class="stack">
                <section class="panel">
                    <h2 class="panel-title">비밀번호 변경</h2>
                    <form id="passwordForm" class="form-grid" action="${CP}/user/password.do" method="post" novalidate>
                        <div class="form-row">
                            <label class="label" for="currentPassword">현재 비밀번호 <span class="required">*</span></label>
                            <input class="input" type="password" id="currentPassword" name="currentPassword" autocomplete="current-password" required>
                            <p class="field-message" data-error-for="currentPassword"></p>
                        </div>

                        <div class="form-row">
                            <label class="label" for="newPassword">새 비밀번호 <span class="required">*</span></label>
                            <input class="input" type="password" id="newPassword" name="newPassword" autocomplete="new-password" required>
                            <p class="field-message" data-error-for="newPassword"></p>
                        </div>

                        <div class="form-row">
                            <label class="label" for="newPasswordConfirm">새 비밀번호 확인 <span class="required">*</span></label>
                            <input class="input" type="password" id="newPasswordConfirm" name="newPasswordConfirm" autocomplete="new-password" required>
                            <p class="field-message" data-error-for="newPasswordConfirm"></p>
                        </div>

                        <div class="form-actions">
                            <button class="btn primary" type="submit">비밀번호 변경</button>
                        </div>
                    </form>
                </section>

                <section class="panel withdraw-panel">
                    <h2 class="panel-title">회원탈퇴</h2>
                    <%-- 2026-07-13 [수정] 탈퇴 회원의 아이디/전화번호/이메일 재사용 정책 반영 --%>
                    <p class="help-text">회원탈퇴 시 기존 계정으로 로그인할 수 없으며, 재가입하면 새로운 회원번호가 발급됩니다.</p>

                    <div id="withdrawStartArea" class="card-actions withdraw-start-actions ${withdrawError ? 'hidden' : ''}">
                        <button id="showWithdrawButton" class="btn ghost-danger" type="button">회원탈퇴</button>
                    </div>

                    <div id="withdrawConfirmArea" class="withdraw-box ${withdrawError ? '' : 'hidden'}">
                        <form id="withdrawForm" class="form-grid" action="${CP}/user/withdraw.do" method="post" novalidate>
                            <div class="form-row">
                                <label class="label" for="withdrawPassword">비밀번호 확인 <span class="required">*</span></label>
                                <input class="input" type="password" id="withdrawPassword" name="password" autocomplete="current-password" required>
                                <p class="field-message" data-error-for="withdrawPassword"></p>
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

        <jsp:include page="../common/footer.jsp" />
        <jsp:include page="../common/floatingBar.jsp" />
        <jsp:include page="../common/mobileBottomNav.jsp" />
    </div>
</body>
</html>
