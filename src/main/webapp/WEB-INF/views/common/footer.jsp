<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- [추가] 공통 Footer -->
<footer class="site-footer">
    <div class="footer-inner">
        <div class="footer-brand">
            <strong>SPAM</strong>
            <p>SPAM은 누구나 안전하고 편리하게 중고거래할 수 있는 공간을 제공합니다.</p>
            <div class="footer-social">
                <span aria-label="instagram">◎</span>
                <span aria-label="facebook">●</span>
                <span aria-label="youtube">▶</span>
            </div>
        </div>

        <div class="footer-column">
            <strong>서비스 안내</strong>
            <a href="${CP}/guide/safe.do">이용약관</a>
            <a href="${CP}/guide/safe.do">안전거래 가이드</a>
            <a href="${CP}/guide/safe.do">FAQ</a>
            <a href="${CP}/guide/safe.do">공지사항</a>
        </div>

        <div class="footer-column">
            <strong>고객센터</strong>
            <a href="${CP}/report/view.do">신고문의</a>
            <a href="${CP}/guide/safe.do">자주 묻는 질문</a>
            <a href="${CP}/guide/safe.do">이용 정책 안내</a>
            <span class="footer-tel">☎ 1600-1234</span>
        </div>

        <div class="footer-column">
            <strong>회사 정보</strong>
            <span>TEAM SPAM</span>
            <span>중고거래 플랫폼 프로젝트</span>
            <span>이메일 spam.team@gmail.com</span>
        </div>
    </div>

    <div class="footer-bottom">
        <span>© 2024 TEAM SPAM. All rights reserved.</span>
        <div>
            <a href="${CP}/guide/safe.do">이용약관</a>
            <a href="${CP}/guide/safe.do">개인정보처리방침</a>
            <!-- [추가] 관리자 링크는 일반 사용자에게 크게 노출하지 않기 위해 footer 우측 하단에 작게 배치 -->
            <a class="admin-link" href="${CP}/admin/user/list.do">관리자</a>
        </div>
    </div>
</footer>
