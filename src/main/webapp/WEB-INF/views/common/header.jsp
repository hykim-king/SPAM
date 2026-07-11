<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty CP}">
    <c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
</c:if>

<!-- 공통 Header -->
<header class="site-header">
    <div class="header-main">
        <div class="header-inner">
            <a class="brand-logo" href="${CP}/main.do" aria-label="SPAM 메인으로 이동">
                <span>SPAM</span>
            </a>

            <form class="search-form" action="${CP}/product/list.do" method="get">
                <label class="sr-only" for="mainSearchWord">검색어</label>
                <input id="mainSearchWord" type="text" name="searchWord" value="<c:out value='${param.searchWord}'/>"
                       placeholder="검색어를 입력하세요 (상품명, 카테고리, 지역)">
                <button type="submit" aria-label="검색">
                    <span aria-hidden="true">⌕</span>
                </button>
            </form>

            <nav class="member-menu" aria-label="회원 메뉴">
                <c:choose>
                    <c:when test="${not empty sessionScope.loginUser}">
                        <a href="${CP}/user/mypage.do">마이페이지</a>
                        <a href="${CP}/chat/view.do">채팅</a>
                        <a class="js-confirm-logout" href="${CP}/user/logout.do">로그아웃</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${CP}/user/login.do">로그인</a>
                        <a href="${CP}/user/join.do">회원가입</a>
                        <a href="${CP}/user/mypage.do">마이페이지</a>
                    </c:otherwise>
                </c:choose>
                <a class="sell-link" href="${CP}/product/saveForm.do">판매하기</a>
            </nav>

            <div class="mobile-header-actions">
                <a href="${CP}/user/mypage.do" aria-label="마이페이지">♡</a>
                <button type="button" class="mobile-menu-button" aria-label="카테고리 메뉴 열기">☰</button>
            </div>
        </div>
    </div>
</header>
