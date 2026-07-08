<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="CP" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} | SPAM</title>
    <link rel="stylesheet" href="${CP}/resources/css/index.css">
</head>
<body>
    <main class="placeholder-page">
        <a class="placeholder-logo" href="${CP}/main.do">SPAM</a>
        <section class="placeholder-card">
            <span class="placeholder-badge">준비 중</span>
            <h1><c:out value="${pageTitle}" /></h1>
            <p><c:out value="${pageDesc}" /></p>
            <a href="${CP}/main.do" class="btn btn-dark">메인으로 돌아가기</a>
        </section>
    </main>
</body>
</html>
