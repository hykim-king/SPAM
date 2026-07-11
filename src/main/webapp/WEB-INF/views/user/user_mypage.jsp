<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>마이페이지 | SPAM</title>
    <link rel="stylesheet" href="${CP}/resources/css/index.css">
    <link rel="stylesheet" href="${CP}/resources/css/member.css">
    <script defer src="${CP}/resources/js/index.js"></script>
    <script defer src="${CP}/resources/js/member.js"></script>
</head>
<body>
    <div class="page-shell" id="top">
        <jsp:include page="../common/header.jsp" />
        <jsp:include page="../common/nav.jsp" />

        <main class="member-page-shell">
        <header class="page-header">
            <div>
                <h1 class="page-title">마이페이지</h1>
            </div>
            <nav class="header-actions">
                <a class="btn outline" href="${CP}/product/myList.do">내 상품</a>
                <a class="btn outline" href="${CP}/user/update.do">회원정보 수정</a>
                <a class="btn js-confirm-logout" href="${CP}/user/logout.do">로그아웃</a>
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
                    <dt class="info-label">이름</dt>
                    <dd class="info-value"><c:out value="${user.userName}" /></dd>
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

        <jsp:include page="../common/footer.jsp" />
        <jsp:include page="../common/floatingBar.jsp" />
        <jsp:include page="../common/mobileBottomNav.jsp" />
    </div>
</body>
</html>
