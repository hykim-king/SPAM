<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!--
    [추가] PC 전용 플로팅바
    [동작] 화면 우측에 고정. 모바일에서는 CSS로 숨기고 mobileBottomNav.jsp를 사용한다.
-->
<aside class="floating-bar" aria-label="빠른 실행 메뉴">
    <a href="${CP}/product/reg.do" class="floating-item">
        <img src="${CP}/resources/images/icons/13_floating_sell.png" alt="" aria-hidden="true">
        <span>판매하기</span>
    </a>
    <a href="${CP}/chat/view.do" class="floating-item">
        <img src="${CP}/resources/images/icons/13_floating_chat.png" alt="" aria-hidden="true">
        <span>채팅</span>
    </a>
    <a href="${CP}/product/latest.do" class="floating-item">
        <img src="${CP}/resources/images/icons/13_floating_recent_view.png" alt="" aria-hidden="true">
        <span>최근 본 상품</span>
    </a>
    <a href="#top" class="floating-item js-scroll-top">
        <img src="${CP}/resources/images/icons/13_floating_top.png" alt="" aria-hidden="true">
        <span>TOP</span>
    </a>
</aside>
