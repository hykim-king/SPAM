<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SPAM 마이페이지</title>
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
                <h1 class="page-title">마이페이지</h1>
                <p class="page-desc">로그인한 회원의 기본 정보를 확인합니다.</p>
            </div>
            <nav class="header-actions">
                <a class="btn outline" href="${pageContext.request.contextPath}/user/update.do">회원정보 수정</a>
                <a class="btn" href="${pageContext.request.contextPath}/user/logout.do">로그아웃</a>
            </nav>
        </header>

        <c:if test="${not empty msg}">
            <p class="alert success"><c:out value="${msg}" /></p>
        </c:if>

        <section class="info-card">
            <h2 class="card-title">내 정보</h2>
            <dl class="info-list">
                <div class="info-item">
                    <dt class="info-label">아이디</dt>
                    <dd class="info-value"><c:out value="${user.userId}" /></dd>
                </div>
                <div class="info-item">
                    <dt class="info-label">닉네임</dt>
                    <dd class="info-value"><c:out value="${user.nickname}" /></dd>
                </div>
                <div class="info-item">
                    <dt class="info-label">전화번호</dt>
                    <dd class="info-value"><c:out value="${user.phoneNum}" /></dd>
                </div>
                <div class="info-item">
                    <dt class="info-label">이메일</dt>
                    <dd class="info-value">
                        <c:choose>
                            <c:when test="${empty user.email}">미입력</c:when>
                            <c:otherwise><c:out value="${user.email}" /></c:otherwise>
                        </c:choose>
                    </dd>
                </div>
            </dl>
        </section>
    </main>
</body>
</html>
