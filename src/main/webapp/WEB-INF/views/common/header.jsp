<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!--
    [추가] 공통 Header
    구성: 이벤트 바, 로고, 검색창, 로그인/회원가입/마이페이지/채팅, 판매하기 버튼
-->
<header class="site-header">
    <div class="notice-bar">
        <div class="notice-inner">
            <a href="${CP}/guide/safe.do">🔥 지금 가입하고 첫 거래 시 5,000원 쿠폰 받아가세요!</a>
            <div class="notice-links">
                <a href="${CP}/guide/safe.do">고객센터</a>
                <a href="${CP}/guide/safe.do">공지사항</a>
            </div>
        </div>
    </div>

    <div class="header-main">
        <div class="header-inner">
            <a class="brand-logo" href="${CP}/main.do" aria-label="SPAM 메인으로 이동">
                <span>SPAM</span>
            </a>

            <form class="search-form" action="${CP}/product/list.do" method="get">
                <label class="sr-only" for="mainSearchWord">검색어</label>
                <input id="mainSearchWord" type="text" name="searchWord" placeholder="검색어를 입력하세요 (상품명, 카테고리, 지역)">
                <button type="submit" aria-label="검색">⌕</button>
            </form>

            <nav class="member-menu" aria-label="회원 메뉴">
                <c:choose>
                    <c:when test="${not empty sessionScope.loginUser}">
                        <a href="${CP}/user/mypage.do">마이페이지</a>
                        <a href="${CP}/chat/view.do">채팅</a>
                        <a href="${CP}/user/logout.do">로그아웃</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${CP}/user/login.do">로그인</a>
                        <a href="${CP}/user/join.do">회원가입</a>
                        <a href="${CP}/user/mypage.do">마이페이지</a>
                    </c:otherwise>
                </c:choose>
                <a class="sell-link" href="${CP}/product/reg.do">판매하기</a>
            </nav>

            <div class="mobile-header-actions">
                <a href="${CP}/user/mypage.do" aria-label="마이페이지">♡</a>
                <button type="button" class="mobile-menu-button" aria-label="모바일 메뉴 열기">☰</button>
            </div>
        </div>
    </div>
</header>
