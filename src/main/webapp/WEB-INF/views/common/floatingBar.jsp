<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty CP}">
    <c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
</c:if>

<!-- PC 전용 플로팅바 -->
<aside class="floating-bar" aria-label="빠른 실행 메뉴">
    <a href="${CP}/product/saveForm.do" class="floating-item">
        <img src="${CP}/resources/images/icons/13_floating_sell.png" alt="" aria-hidden="true">
        <span>판매</span>
    </a>
    <a href="${CP}/chat/view.do" class="floating-item">
        <img src="${CP}/resources/images/icons/13_floating_chat.png" alt="" aria-hidden="true">
        <span>채팅</span>
    </a>
    <a href="#top" class="floating-item js-scroll-top">
        <img src="${CP}/resources/images/icons/13_floating_top.png" alt="" aria-hidden="true">
        <span>TOP</span>
    </a>
</aside>
