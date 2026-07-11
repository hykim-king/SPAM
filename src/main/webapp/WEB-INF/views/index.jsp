<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="SPAM 중고거래 플랫폼 메인화면">
    <title>SPAM | 중고거래 플랫폼</title>

    <!-- 메인화면 전용 CSS -->
    <link rel="stylesheet" href="${CP}/resources/css/index.css">

    <!-- 메인화면 전용 JS -->
    <script defer src="${CP}/resources/js/index.js"></script>
</head>
<body>
    <!--
        [화면 구성]
        header.jsp          : 로고/검색/회원 메뉴
        nav.jsp             : 카테고리 내비게이션
        main                : 배너, 퀵 메뉴, 상품 섹션, 안전거래 가이드
        footer.jsp          : 서비스 정보 + 개인정보처리방침 팝업 + 관리자 링크
        floatingBar.jsp     : PC 우측 고정 메뉴
        mobileBottomNav.jsp : 모바일 하단 고정 메뉴
    -->

    <div class="page-shell" id="top">
        <jsp:include page="common/header.jsp" />
        <jsp:include page="common/nav.jsp" />

        <main class="main-content">
            <!-- 메인 히어로 배너 -->
            <section class="hero-section" aria-label="SPAM 메인 배너">
                <button type="button" class="hero-arrow hero-arrow-left" aria-label="이전 배너">‹</button>

                <div class="hero-stage">
                    <!--
                        [수정] 메인 배너 1
                        - 기존 단일 배너를 슬라이드 구조로 변경
                        - 상품관리 모듈 연결 전까지 정적 배너로 사용
                    -->
                    <article class="hero-banner hero-banner-main is-active" data-hero-index="0">
                        <div class="hero-copy">
                            <p class="hero-kicker">중고거래 플랫폼 SPAM</p>
                            <h1>필요한 사람에게,<br>당신의 물건을 연결하세요</h1>
                            <p class="hero-desc">쓰지 않는 물건은 새 주인에게, 필요한 물건은 합리적인 가격으로 만나보세요.</p>

                            <div class="hero-actions">
                                <a href="${CP}/product/list.do" class="btn btn-dark">상품 둘러보기</a>
                                <a href="${CP}/product/saveForm.do" class="btn btn-light">판매하기</a>
                            </div>
                        </div>
                    </article>

                    <!--
                        [추가] 안전거래 가이드 배너
                        - 클릭 시 서비스 안내의 안전거래 가이드 탭으로 이동
                    -->
                    <article class="hero-banner hero-banner-safe" data-hero-index="1">
                        <a class="hero-safe-link" href="${CP}/service/info.do?tab=safe" aria-label="안전거래 가이드로 이동">
                            <div class="hero-copy">
                                <p class="hero-kicker">SPAM SAFE GUIDE</p>
                                <h1>처음 거래해도<br>안전하게 확인하세요</h1>
                                <p class="hero-desc">판매자와 구매자가 거래 전에 꼭 확인해야 할 안전거래 가이드를 정리했습니다.</p>

                                <div class="hero-actions">
                                    <span class="btn btn-dark">안전거래 가이드</span>
                                    <span class="btn btn-light">신고센터</span>
                                </div>
                            </div>
                        </a>
                    </article>

                    <div class="hero-dots" aria-label="배너 페이지 표시">
                        <button type="button" class="active" aria-label="1번 배너"></button>
                        <button type="button" aria-label="2번 배너"></button>
                    </div>
                </div>

                <button type="button" class="hero-arrow hero-arrow-right" aria-label="다음 배너">›</button>
            </section>

            <!-- 빠른 이동 아이콘 메뉴 -->
            <section class="quick-menu" aria-label="주요 기능 바로가기">
                <a href="${CP}/product/list.do" class="quick-card">
                    <img src="${CP}/resources/images/icons/01_all_products.png" alt="" aria-hidden="true">
                    <span>전체 상품</span>
                </a>
                <a href="${CP}/product/popular.do" class="quick-card">
                    <img src="${CP}/resources/images/icons/02_popular_products.png" alt="" aria-hidden="true">
                    <span>인기 상품</span>
                </a>
                <a href="${CP}/product/recommend.do" class="quick-card">
                    <img src="${CP}/resources/images/icons/03_todays_recommendation.png" alt="" aria-hidden="true">
                    <span>오늘의 추천</span>
                </a>
                <a href="${CP}/product/latest.do" class="quick-card">
                    <img src="${CP}/resources/images/icons/04_latest_registration.png" alt="" aria-hidden="true">
                    <span>최근 등록</span>
                </a>
                <a href="${CP}/chat/view.do" class="quick-card">
                    <img src="${CP}/resources/images/icons/05_chat.png" alt="" aria-hidden="true">
                    <span>채팅하기</span>
                </a>
                <a href="${CP}/transact/list.do" class="quick-card">
                    <img src="${CP}/resources/images/icons/06_transaction_history.png" alt="" aria-hidden="true">
                    <span>거래내역</span>
                </a>
                <a href="${CP}/report/doRetrieve.do" class="quick-card">
                    <img src="${CP}/resources/images/icons/07_report_center.png" alt="" aria-hidden="true">
                    <span>신고센터</span>
                </a>
            </section>

            <!-- 상품 섹션 3개 -->
            <section class="product-dashboard" aria-label="상품 모아보기">
                <article class="product-section-card" id="popularProducts">
                    <div class="section-title-row">
                        <h2><span class="title-icon">🔥</span> 인기 상품 Top</h2>
                        <a href="${CP}/product/popular.do">더보기</a>
                    </div>

                    <div class="product-grid compact-grid">
                        <c:forEach var="product" items="${popularProductList}">
                            <jsp:include page="common/productCard.jsp">
                                <jsp:param name="productNo" value="${product.productNo}" />
                                <jsp:param name="productTitle" value="${product.productTitle}" />
                                <jsp:param name="price" value="${product.price}" />
                                <jsp:param name="location" value="${product.location}" />
                                <jsp:param name="createDt" value="${product.createDt}" />
                                <jsp:param name="thumbnailPath" value="${product.thumbnailPath}" />
                            </jsp:include>
                        </c:forEach>
                    </div>
                </article>

                <article class="product-section-card" id="recommendProducts">
                    <div class="section-title-row">
                        <h2><span class="title-icon">✨</span> 오늘의 추천</h2>
                        <a href="${CP}/product/recommend.do">더보기</a>
                    </div>

                    <div class="product-grid compact-grid">
                        <c:forEach var="product" items="${recommendedProductList}">
                            <jsp:include page="common/productCard.jsp">
                                <jsp:param name="productNo" value="${product.productNo}" />
                                <jsp:param name="productTitle" value="${product.productTitle}" />
                                <jsp:param name="price" value="${product.price}" />
                                <jsp:param name="location" value="${product.location}" />
                                <jsp:param name="createDt" value="${product.createDt}" />
                                <jsp:param name="thumbnailPath" value="${product.thumbnailPath}" />
                            </jsp:include>
                        </c:forEach>
                    </div>
                </article>

                <article class="product-section-card" id="latestProducts">
                    <div class="section-title-row">
                        <h2><span class="title-icon">🛍️</span> 최신 등록 상품</h2>
                        <a href="${CP}/product/latest.do">더보기</a>
                    </div>

                    <div class="product-grid compact-grid">
                        <c:forEach var="product" items="${latestProductList}">
                            <jsp:include page="common/productCard.jsp">
                                <jsp:param name="productNo" value="${product.productNo}" />
                                <jsp:param name="productTitle" value="${product.productTitle}" />
                                <jsp:param name="price" value="${product.price}" />
                                <jsp:param name="location" value="${product.location}" />
                                <jsp:param name="createDt" value="${product.createDt}" />
                                <jsp:param name="thumbnailPath" value="${product.thumbnailPath}" />
                            </jsp:include>
                        </c:forEach>
                    </div>
                </article>
            </section>

            <!-- 안전거래 가이드 -->
            <section class="guide-section" aria-label="안전거래 가이드">
                <div class="guide-title-area">
                    <h2>안전한 거래를 위한 SPAM 가이드</h2>
                    <p>처음 이용하는 분들을 위한 필수 안내입니다.</p>
                </div>

                <div class="guide-list">
                    <article class="guide-item">
                        <img src="${CP}/resources/images/icons/08_guide_safe_trade.png" alt="" aria-hidden="true">
                        <div>
                            <h3>직거래 약속 확인</h3>
                            <p>약속 장소와 시간을 서로 다시 확인하세요.</p>
                        </div>
                    </article>
                    <article class="guide-item">
                        <img src="${CP}/resources/images/icons/08_guide_verified_user.png" alt="" aria-hidden="true">
                        <div>
                            <h3>판매자 정보 확인</h3>
                            <p>상품 정보와 판매자 정보를 꼼꼼히 확인하세요.</p>
                        </div>
                    </article>
                    <article class="guide-item">
                        <img src="${CP}/resources/images/icons/08_guide_report_alert.png" alt="" aria-hidden="true">
                        <div>
                            <h3>문제 발생 시 신고</h3>
                            <p>문제가 생기면 신고센터를 이용하세요.</p>
                        </div>
                    </article>
                </div>

                <a href="${CP}/service/info.do?tab=safe" class="guide-more">자세히 보기</a>
            </section>
        </main>

        <jsp:include page="common/footer.jsp" />
        <jsp:include page="common/floatingBar.jsp" />
        <jsp:include page="common/mobileBottomNav.jsp" />
    </div>
</body>
</html>
