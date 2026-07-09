<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="tab" value="${empty activeTab ? 'notice' : activeTab}" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="SPAM 서비스 안내">
    <title>서비스 안내 | SPAM</title>
    <link rel="stylesheet" href="${CP}/resources/css/index.css">
    <script defer src="${CP}/resources/js/index.js"></script>
</head>
<body>
    <div class="page-shell" id="top">
        <jsp:include page="../common/header.jsp" />
        <jsp:include page="../common/nav.jsp" />

        <main class="main-content service-page">
            <section class="service-hero">
                <h1>서비스 안내</h1>
                <p>서비스 이용 안내와 안전거래 가이드를 확인할 수 있습니다.</p>
            </section>

            <nav class="service-tabs" aria-label="서비스 안내 탭">
                <a class="${tab == 'notice' ? 'active' : ''}" href="${CP}/service/info.do?tab=notice">공지사항</a>
                <a class="${tab == 'terms' ? 'active' : ''}" href="${CP}/service/info.do?tab=terms">이용약관</a>
                <a class="${tab == 'faq' ? 'active' : ''}" href="${CP}/service/info.do?tab=faq">FAQ</a>
                <a class="${tab == 'safe' ? 'active' : ''}" href="${CP}/service/info.do?tab=safe">안전거래 가이드</a>
            </nav>

            <section class="service-panel ${tab == 'notice' ? 'active' : ''}" id="noticePanel">
                <div class="service-title-row">
                    <div>
                        <span class="service-label">Notice</span>
                        <h2>공지사항</h2>
                    </div>
                    <span class="service-count">2건</span>
                </div>

                <article class="notice-card">
                    <div class="notice-meta">
                        <span>안내</span>
                        <time datetime="2026-07-08">2026.07.08</time>
                    </div>
                    <h3>SPAM 서비스 이용 안내</h3>
                    <p>SPAM은 회원 간 중고 물품 거래를 돕기 위한 팀 프로젝트 플랫폼입니다. 상품 등록, 상품 조회, 채팅, 거래내역, 신고 기능을 순차적으로 연결하고 있습니다.</p>
                </article>

                <article class="notice-card">
                    <div class="notice-meta">
                        <span>안전거래</span>
                        <time datetime="2026-07-08">2026.07.08</time>
                    </div>
                    <h3>거래 전 확인해야 할 기본 수칙</h3>
                    <p>거래 전 상품 사진, 설명, 거래 장소, 결제 방식을 충분히 확인하세요. 의심스러운 거래나 부적절한 게시글은 신고 문의를 통해 접수할 수 있습니다.</p>
                </article>
            </section>

            <section class="service-panel ${tab == 'terms' ? 'active' : ''}" id="termsPanel">
                <div class="service-title-row">
                    <div>
                        <span class="service-label">Terms</span>
                        <h2>이용약관</h2>
                    </div>
                </div>

                <div class="terms-box">
                    <h3>제1조 목적</h3>
                    <p>이 약관은 TEAM SPAM이 제공하는 중고거래 플랫폼 서비스의 이용 조건, 절차, 회원과 서비스 운영자의 권리·의무를 정하는 것을 목적으로 합니다.</p>

                    <h3>제2조 회원가입 및 계정 관리</h3>
                    <p>회원은 정확한 정보를 입력하여 가입해야 하며, 아이디와 비밀번호 관리 책임은 회원 본인에게 있습니다. 타인의 정보를 도용하거나 허위 정보를 입력할 수 없습니다.</p>

                    <h3>제3조 상품 등록과 거래</h3>
                    <p>회원은 실제 거래 가능한 상품만 등록해야 합니다. 허위 매물, 금지 물품, 타인의 권리를 침해하는 게시글은 사전 안내 없이 제한될 수 있습니다.</p>

                    <h3>제4조 채팅 및 신고</h3>
                    <p>회원은 거래 목적에 맞게 채팅을 이용해야 하며, 욕설·스팸·사기 의심 행위가 확인되면 신고 처리와 이용 제한이 이루어질 수 있습니다.</p>

                    <h3>제5조 서비스 제한</h3>
                    <p>운영자는 안정적인 서비스 제공과 이용자 보호를 위해 비정상적인 접근, 반복 신고, 부정 거래가 확인된 계정의 이용을 제한할 수 있습니다.</p>

                    <p class="terms-note">본 약관은 학원 팀 프로젝트용 샘플 문안이며, 실제 상용 서비스 약관으로 사용하려면 법률 검토가 필요합니다.</p>
                </div>
            </section>

            <section class="service-panel ${tab == 'faq' ? 'active' : ''}" id="faqPanel">
                <div class="service-title-row">
                    <div>
                        <span class="service-label">FAQ</span>
                        <h2>자주 묻는 질문</h2>
                    </div>
                    <span class="service-count">4건</span>
                </div>

                <div class="faq-list">
                    <details class="faq-item">
                        <summary>회원가입할 때 이메일은 꼭 입력해야 하나요?</summary>
                        <p>아니요. 현재 회원관리 기준에서는 이메일은 선택 입력 항목입니다. 아이디, 비밀번호, 이름, 전화번호는 필수로 입력해야 합니다.</p>
                    </details>
                    <details class="faq-item">
                        <summary>상품 찜이나 후기는 사용할 수 있나요?</summary>
                        <p>현재 팀 프로젝트 범위에서는 상품 찜과 후기는 제외되어 있습니다. 우선 상품 조회, 채팅, 거래내역, 신고 흐름을 중심으로 구현합니다.</p>
                    </details>
                    <details class="faq-item">
                        <summary>거래 중 문제가 생기면 어떻게 하나요?</summary>
                        <p>신고 문의 메뉴에서 문제 내용을 접수할 수 있습니다. 상품 정보, 상대방 닉네임, 채팅 내용 등 확인 가능한 정보를 함께 남기면 처리에 도움이 됩니다.</p>
                    </details>
                    <details class="faq-item">
                        <summary>회원 탈퇴는 어디서 하나요?</summary>
                        <p>마이페이지의 회원정보 수정 화면에서 회원탈퇴 영역을 열고 비밀번호 확인 후 탈퇴할 수 있습니다.</p>
                    </details>
                </div>
            </section>

            <section class="service-panel ${tab == 'safe' ? 'active' : ''}" id="safePanel">
                <div class="service-title-row">
                    <div>
                        <span class="service-label">Safe Trade</span>
                        <h2>안전거래 가이드</h2>
                    </div>
                </div>

                <h3 class="safe-section-title">판매자 안전거래 가이드</h3>
                <div class="safe-grid safe-grid-seller">
                    <article>
                        <span>01</span>
                        <h3>상품 상태 정확히 작성</h3>
                        <p>하자, 사용 기간, 구성품 여부를 숨기지 말고 상품 설명에 정확히 남깁니다.</p>
                    </article>
                    <article>
                        <span>02</span>
                        <h3>실제 사진 등록</h3>
                        <p>인터넷 이미지보다 직접 촬영한 사진을 사용하고, 주요 하자는 확대 사진으로 안내합니다.</p>
                    </article>
                    <article>
                        <span>03</span>
                        <h3>거래 조건 명확히 안내</h3>
                        <p>가격, 거래 장소, 거래 가능 시간, 환불 가능 여부를 채팅에 남겨 분쟁을 줄입니다.</p>
                    </article>
                    <article>
                        <span>04</span>
                        <h3>외부 거래 유도 금지</h3>
                        <p>외부 링크나 별도 연락처로 거래를 유도하지 않고 SPAM 채팅 기록을 남깁니다.</p>
                    </article>
                </div>

                <h3 class="safe-section-title">구매자 안전거래 가이드</h3>
                <div class="safe-grid">
                    <article>
                        <span>01</span>
                        <h3>상품 정보 확인</h3>
                        <p>사진이 실제 상품과 일치하는지, 하자·구성품·사용 기간이 명확히 적혀 있는지 확인합니다.</p>
                    </article>
                    <article>
                        <span>02</span>
                        <h3>채팅 기록 보관</h3>
                        <p>가격, 거래 장소, 약속 시간은 채팅으로 남겨두면 분쟁 발생 시 확인 자료로 사용할 수 있습니다.</p>
                    </article>
                    <article>
                        <span>03</span>
                        <h3>안전한 장소 선택</h3>
                        <p>직거래는 사람이 많은 장소나 CCTV가 있는 곳에서 진행하고, 늦은 시간 단독 거래는 피합니다.</p>
                    </article>
                    <article>
                        <span>04</span>
                        <h3>의심 거래 신고</h3>
                        <p>선입금만 요구하거나 외부 링크 이동을 유도하는 경우 거래를 중단하고 신고 문의를 이용합니다.</p>
                    </article>
                </div>

                <div class="safe-callout">
                    <strong>SPAM 안전거래 원칙</strong>
                    <p>상품 상태를 충분히 확인하고, 거래 조건을 명확히 남기고, 문제가 생기면 신고센터를 통해 기록을 남기는 것이 핵심입니다.</p>
                </div>
            </section>
        </main>

        <jsp:include page="../common/footer.jsp" />
        <jsp:include page="../common/floatingBar.jsp" />
        <jsp:include page="../common/mobileBottomNav.jsp" />
    </div>
</body>
</html>
