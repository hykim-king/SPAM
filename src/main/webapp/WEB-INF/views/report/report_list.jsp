<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>신고센터 | SPAM</title>
    <link rel="stylesheet" href="${CP}/resources/css/index.css">
    <link rel="stylesheet" href="${CP}/resources/css/member.css">
    <script defer src="${CP}/resources/js/index.js"></script>
</head>
<body>
    <div class="page-shell" id="top">
        <jsp:include page="../common/header.jsp" />
        <jsp:include page="../common/nav.jsp" />

        <main class="member-page-shell">
            <header class="page-header">
                <div>
                    <h1 class="page-title">신고센터</h1>
                    <p class="page-desc">접수된 신고 내역과 처리 상태를 확인합니다.</p>
                </div>
                <nav class="header-actions">
                    <a class="btn outline" href="${CP}/service/info.do?tab=safe">이용 정책 안내</a>
                    <a class="btn" href="${CP}/main.do">메인</a>
                </nav>
            </header>

            <section class="panel stack">
                <div class="simple-list">
                    <c:choose>
                        <c:when test="${empty list}">
                            <article class="simple-list-card">
                                <h3>조회된 신고 내역이 없습니다.</h3>
                                <p>신고관리 모듈 데이터가 연결되면 이 영역에 신고 목록이 표시됩니다.</p>
                            </article>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="report" items="${list}">
                                <article class="simple-list-card">
                                    <h3>신고번호 <c:out value="${report.reportNo}" /></h3>
                                    <p><c:out value="${report}" /></p>
                                </article>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </section>
        </main>

        <jsp:include page="../common/footer.jsp" />
        <jsp:include page="../common/floatingBar.jsp" />
        <jsp:include page="../common/mobileBottomNav.jsp" />
    </div>
</body>
</html>
