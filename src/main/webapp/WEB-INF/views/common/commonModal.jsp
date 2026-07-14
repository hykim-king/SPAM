<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty CP}">
    <c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
</c:if>

<%-- 2026-07-13 [추가] 로그인 필요·권한 없음·서비스 준비중 공통 안내 모달 --%>
<div class="spam-confirm-modal spam-notice-modal" id="spamNoticeModal" aria-hidden="true"
     data-login-url="${CP}/user/login.do">
    <div class="spam-confirm-backdrop js-spam-notice-close" tabindex="-1"></div>
    <section class="spam-confirm-card" role="dialog" aria-modal="true" aria-labelledby="spamNoticeTitle"
             aria-describedby="spamNoticeMessage">
        <div class="spam-confirm-icon" aria-hidden="true">!</div>
        <h2 id="spamNoticeTitle">안내</h2>
        <p class="spam-confirm-message" id="spamNoticeMessage">안내 내용을 확인해주세요.</p>
        <div class="spam-confirm-actions">
            <button type="button" class="spam-confirm-cancel js-spam-notice-close">취소</button>
            <button type="button" class="spam-confirm-ok js-spam-notice-ok">확인</button>
        </div>
    </section>
</div>
