<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty CP}">
    <c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
</c:if>

<!-- 모바일 하단 고정 탭바 -->
<nav class="mobile-bottom-nav" aria-label="모바일 하단 메뉴">
    <a href="${CP}/main.do" class="mobile-tab active">
        <img src="${CP}/resources/images/icons/09_home_active.png" alt="" aria-hidden="true">
        <span>홈</span>
    </a>
    <a href="${CP}/product/list.do" class="mobile-tab">
        <img src="${CP}/resources/images/icons/10_category_inactive.png" alt="" aria-hidden="true">
        <span>카테고리</span>
    </a>
    <a href="${CP}/product/saveForm.do" class="mobile-tab mobile-sell-tab"
       data-spam-modal="${empty sessionScope.loginUser ? 'login' : ''}"
       data-login-url="${CP}/user/login.do">
        <span class="mobile-sell-icon">+</span>
        <span>판매</span>
    </a>
    <a href="${CP}/chat/view.do" class="mobile-tab"
       data-spam-modal="${empty sessionScope.loginUser ? 'login' : ''}"
       data-login-url="${CP}/user/login.do">
        <img src="${CP}/resources/images/icons/11_chat_inactive.png" alt="" aria-hidden="true">
        <span>채팅</span>
    </a>
    <a href="${CP}/user/mypage.do" class="mobile-tab"
       data-spam-modal="${empty sessionScope.loginUser ? 'login' : ''}"
       data-login-url="${CP}/user/login.do">
        <img src="${CP}/resources/images/icons/12_mypage_inactive.png" alt="" aria-hidden="true">
        <span>마이페이지</span>
    </a>
</nav>
