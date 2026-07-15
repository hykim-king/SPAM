<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>거래 등록 | SPAM</title>
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
                    <a class="brand" href="${CP}/transact/view.do" aria-label="SPAM 거래 등록">
                        <span class="brand-mark">SP</span>
                        <span>SPAM Trade</span>
                    </a>
                    <h1 class="page-title">거래 등록</h1>
                    <p class="page-desc">거래내역 모듈과 연결할 임시 등록 화면입니다.</p>
                </div>
                <nav class="header-actions">
                    <a class="btn outline" href="${CP}/transact/list.do">거래내역</a>
                    <a class="btn" href="${CP}/main.do">메인</a>
                </nav>
            </header>

            <section class="panel">
                <h2 class="panel-title">거래 등록 준비 중</h2>
                <p class="help-text">상품관리/계좌관리와 최종 필드가 맞춰지면 상품번호, 구매자, 판매자, 거래상태 입력 폼을 연결하면 됩니다.</p>
            </section>
        </main>

        <jsp:include page="../common/footer.jsp" />
        <jsp:include page="../common/floatingBar.jsp" />
        <jsp:include page="../common/mobileBottomNav.jsp" />
    </div>
</body>
</html>
