<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty CP}">
    <c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
</c:if>

<!-- 공통 Footer -->
<footer class="site-footer">
    <div class="footer-inner">
        <div class="footer-brand">
            <strong>SPAM</strong>
            <p>SPAM은 필요한 사람에게 물건을 연결하는 중고거래 플랫폼입니다.</p>
            <div class="footer-social" aria-label="SNS 링크 준비 영역">
                <span aria-label="instagram">◎</span>
                <span aria-label="facebook">●</span>
                <span aria-label="youtube">▶</span>
            </div>
        </div>

        <div class="footer-column">
            <a class="footer-heading" href="${CP}/service/info.do">서비스 안내</a>
            <a href="${CP}/service/info.do?tab=notice">공지사항</a>
            <a href="${CP}/service/info.do?tab=terms">이용약관</a>
            <a href="${CP}/service/info.do?tab=faq">FAQ</a>
            <a href="${CP}/service/info.do?tab=safe">안전거래 가이드</a>
        </div>

        <div class="footer-column">
            <strong>고객센터</strong>
            <c:choose>
        		<c:when test="${sessionScope.loginUser.userRole == '02'}">
            		<a href="${CP}/report/admin_doRetrieve.do">신고 문의</a>
        		</c:when>
        		<c:otherwise>
            		<a href="${CP}/report/myReportList.do">신고 문의</a>
        		</c:otherwise>
    		</c:choose>
            <a href="${CP}/service/info.do?tab=safe">이용 정책 안내</a>
            <a href="${CP}/user/update.do#withdrawStartArea"
               data-spam-modal="${empty sessionScope.loginUser ? 'login' : ''}"
               data-login-url="${CP}/user/login.do">회원 탈퇴</a>
            <span class="footer-tel">문의: spam.team@example.com</span>
        </div>

        <div class="footer-column">
            <strong>회사 정보</strong>
            <span>TEAM SPAM</span>
            <%-- 2026-07-13 [수정] 회사 영문 표기를 서비스 슬로건과 통일한다. --%>
            <span>SELL PRODUCT AND MEETING</span>
            <span>중고거래 플랫폼 팀 프로젝트</span>
        </div>
    </div>

    <div class="footer-bottom">
        <span>© 2026 TEAM SPAM. All rights reserved.</span>
        <div>
            <a href="${CP}/service/info.do?tab=terms">이용약관</a>
            <button type="button" class="footer-policy-button js-privacy-open">개인정보처리방침</button>
            <c:choose>
                <c:when test="${empty sessionScope.loginUser}">
                    <a class="admin-link" href="${CP}/admin/user/list.do"
                       data-spam-modal="login" data-login-url="${CP}/user/login.do">관리자</a>
                </c:when>
                <c:when test="${sessionScope.loginUser.userRole == '02'}">
                    <a class="admin-link is-admin-login" href="${CP}/admin/user/list.do">관리자</a>
                </c:when>
                <c:otherwise>
                    <a class="admin-link" href="${CP}/admin/user/list.do" data-spam-modal="forbidden">관리자</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="privacy-modal" id="privacyPolicyModal" aria-hidden="true">
        <div class="privacy-modal-backdrop js-privacy-close" tabindex="-1"></div>
        <section class="privacy-modal-card" role="dialog" aria-modal="true" aria-labelledby="privacyPolicyTitle">
            <div class="privacy-modal-header">
                <div>
                    <span class="modal-kicker">SPAM Policy</span>
                    <h2 id="privacyPolicyTitle">개인정보처리방침</h2>
                </div>
                <button type="button" class="privacy-modal-close js-privacy-close" aria-label="개인정보처리방침 팝업 닫기">×</button>
            </div>

            <div class="privacy-modal-body">
                <p>TEAM SPAM은 회원가입, 로그인, 상품거래, 채팅, 신고 처리를 위해 필요한 최소한의 개인정보만 수집·이용합니다.</p>

                <h3>1. 수집 항목</h3>
                <%-- 2026-07-13 [수정] 닉네임 미입력 시 이름으로 자동 설정되는 정책 반영 --%>
                <p>필수 항목은 아이디, 비밀번호, 이름, 전화번호입니다. 닉네임은 미입력 시 이름과 동일하게 설정되며, 이메일과 생년월일은 선택 입력 항목입니다.</p>

                <h3>2. 이용 목적</h3>
                <p>회원 식별, 서비스 이용 관리, 거래 관련 안내, 부정 이용 방지, 신고 처리, 서비스 개선 목적으로 이용합니다.</p>

                <h3>3. 보유 기간</h3>
                <p>회원 탈퇴 시 개인정보는 지체 없이 파기하는 것을 원칙으로 합니다. 단, 분쟁 처리와 법령상 보관이 필요한 정보는 필요한 기간 동안 분리 보관할 수 있습니다.</p>

                <h3>4. 제3자 제공</h3>
                <p>SPAM은 이용자의 동의 없이 개인정보를 외부에 제공하지 않습니다. 다만 법령에 따른 요청이 있는 경우 예외적으로 제공될 수 있습니다.</p>

                <h3>5. 이용자 권리</h3>
                <p>회원은 마이페이지에서 자신의 정보를 확인·수정할 수 있으며, 회원탈퇴 메뉴를 통해 서비스 이용 해지를 요청할 수 있습니다.</p>

                <p class="privacy-note">이 내용은 팀 프로젝트용 샘플 문안입니다. 실제 서비스 배포 시 법무 검토와 운영 정책 확정이 필요합니다.</p>
            </div>
        </section>
    </div>

</footer>

<jsp:include page="commonModal.jsp" />
