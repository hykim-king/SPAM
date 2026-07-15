<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>마이페이지 | SPAM</title>
    <link rel="stylesheet" href="${CP}/resources/css/index.css?v=20260714">
    <link rel="stylesheet" href="${CP}/resources/css/member.css?v=20260714">
    <link rel="stylesheet" href="${CP}/resources/css/product.css?v=20260714">
    <script defer src="${CP}/resources/js/index.js?v=20260714"></script>
    <script defer src="${CP}/resources/js/member.js"></script>
</head>
<body>
    <div class="page-shell" id="top">
        <jsp:include page="../common/header.jsp" />
        <jsp:include page="../common/nav.jsp" />

        <main class="member-page-shell mypage-page">
            <header class="page-header mypage-page-header">
                <div>
                    <h1 class="page-title">마이페이지</h1>
                </div>
                <nav class="header-actions mypage-header-actions" aria-label="마이페이지 바로가기">
                    <a class="btn primary" href="${CP}/product/saveForm.do">상품 등록</a>
                    <a class="btn js-confirm-logout" href="${CP}/user/logout.do">로그아웃</a>
                </nav>
            </header>

            <c:if test="${not empty msg}">
                <p class="alert success"><c:out value="${msg}" /></p>
            </c:if>

            <%-- 2026-07-13 [수정] 참고 화면과 동일하게 왼쪽 내 정보, 오른쪽 상품 카드 목록으로 배치한다. --%>
            <div class="mypage-dashboard">
                <div class="mypage-profile-column">
                    <aside class="info-card mypage-info-card" aria-labelledby="myInfoTitle">
                        <h2 class="card-title" id="myInfoTitle">내 정보</h2>

                        <%-- 2026-07-13 [추가] 판매자 프로필과 같은 원형 프로필 영역을 표시한다. --%>
                        <div class="mypage-profile-summary">
                            <span class="mypage-profile-avatar" aria-hidden="true">S</span>
                            <strong class="mypage-profile-nickname">
                                <c:choose>
                                    <c:when test="${not empty user.nickname}"><c:out value="${user.nickname}" /></c:when>
                                    <c:otherwise><c:out value="${user.userName}" /></c:otherwise>
                                </c:choose>
                            </strong>
                        </div>

                        <dl class="info-list mypage-info-list">
                            <div class="info-item">
                                <dt class="info-label">아이디</dt>
                                <dd class="info-value"><c:out value="${user.userId}" /></dd>
                            </div>
                            <div class="info-item">
                                <dt class="info-label">이름</dt>
                                <dd class="info-value"><c:out value="${user.userName}" /></dd>
                            </div>
                            <div class="info-item">
                                <dt class="info-label">닉네임</dt>
                                <dd class="info-value"><c:out value="${user.nickname}" /></dd>
                            </div>
                            <div class="info-item">
                                <dt class="info-label">전화번호</dt>
                                <dd class="info-value"><c:out value="${user.phoneNum}" /></dd>
                            </div>
                            <div class="info-item">
                                <dt class="info-label">이메일</dt>
                                <dd class="info-value">
                                    <c:choose>
                                        <c:when test="${empty user.email}">미입력</c:when>
                                        <c:otherwise><c:out value="${user.email}" /></c:otherwise>
                                    </c:choose>
                                </dd>
                            </div>
                            <div class="info-item">
                                <dt class="info-label">가입일</dt>
                                <dd class="info-value">
                                    <c:choose>
                                        <c:when test="${not empty user.createDt}"><fmt:formatDate value="${user.createDt}" pattern="yyyy.MM.dd" /></c:when>
                                        <c:otherwise>-</c:otherwise>
                                    </c:choose>
                                </dd>
                            </div>
                        </dl>
                    </aside>

                    <%-- 2026-07-14 [수정] 내 정보 카드 아래 왼쪽 신고 조회, 오른쪽 회원정보 수정 배치. --%>
                    <div class="mypage-profile-actions">
                        <a class="btn outline" href="${CP}/report/myReportList.do">신고 조회</a>
                        <a class="btn outline" href="${CP}/user/update.do">회원정보 수정</a>
                    </div>
                </div>

                <section class="mypage-products-area" aria-label="내 상품 목록">
                    <%-- 2026-07-13 [수정] 상태 필터를 상품 영역 오른쪽 상단에 배치한다. --%>
                    <nav class="product-status-tabs mypage-status-tabs" aria-label="내 상품 상태별 보기">
                        <a class="${search.status eq 'ACTIVE' ? 'is-active' : ''}" href="${CP}/user/mypage.do">전체</a>
                        <a class="${search.status eq '01' ? 'is-active' : ''}" href="${CP}/user/mypage.do?status=01">판매중</a>
                        <a class="${search.status eq '02' ? 'is-active' : ''}" href="${CP}/user/mypage.do?status=02">예약중</a>
                        <a class="${search.status eq '03' ? 'is-active' : ''}" href="${CP}/user/mypage.do?status=03">판매완료</a>
                    </nav>

                    <c:choose>
                        <c:when test="${empty list}">
                            <div class="product-empty-state mypage-empty-state">
                                <strong>해당 상태의 상품이 없습니다.</strong>
                                <p>판매할 상품을 등록하면 이곳에서 확인할 수 있습니다.</p>
                                <a class="product-primary-button" href="${CP}/product/saveForm.do">상품 등록하기</a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <%-- 2026-07-13 [수정] 판매자 프로필과 같은 3열 상품 카드만 표시한다. --%>
                            <div class="product-card-grid mypage-product-grid">
                                <c:forEach var="product" items="${list}">
                                    <article class="product-list-card">
                                        <a class="product-list-card-link" href="${CP}/product/view.do?productNo=${product.productNo}">
                                            <div class="product-list-thumb">
                                                <c:choose>
                                                    <c:when test="${not empty product.thumbnailPath}">
                                                        <img src="${CP}${product.thumbnailPath}" alt="<c:out value='${product.productTitle}' />">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="product-image-placeholder"><span>상품 이미지 준비중</span></div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="product-list-info">
                                                <strong class="product-list-title"><c:out value="${product.productTitle}" /></strong>
                                                <span class="product-list-price"><fmt:formatNumber value="${product.price}" pattern="#,##0" />원</span>
                                                <span class="product-list-meta">
                                                    <c:out value="${empty product.location ? '지역 미입력' : product.location}" />
                                                    · <c:out value="${product.createDt}" />
                                                </span>
                                            </div>
                                        </a>
                                    </article>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </section>
            </div>
        </main>

        <jsp:include page="../common/footer.jsp" />
        <jsp:include page="../common/floatingBar.jsp" />
        <jsp:include page="../common/mobileBottomNav.jsp" />
    </div>
</body>
</html>
