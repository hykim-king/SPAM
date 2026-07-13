<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${empty product ? '상품 상세' : product.productTitle}"/> | SPAM</title>
    <link rel="stylesheet" href="${CP}/resources/css/index.css">
    <link rel="stylesheet" href="${CP}/resources/css/product.css">
    <script defer src="${CP}/resources/js/index.js"></script>
    <script defer src="${CP}/resources/js/product.js"></script>
</head>
<body>
<div class="page-shell" id="top">
    <jsp:include page="../common/header.jsp" />
    <jsp:include page="../common/nav.jsp" />

    <main class="product-page product-detail-page">
        <c:choose>
            <c:when test="${empty product}">
                <div class="product-empty-state">
                    <strong>상품 정보를 찾을 수 없습니다.</strong>
                    <p>삭제되었거나 존재하지 않는 상품입니다.</p>
                    <a class="product-primary-button" href="${CP}/product/list.do">상품 목록으로</a>
                </div>
            </c:when>
            <c:otherwise>
                <nav class="product-breadcrumb" aria-label="현재 위치">
                    <a href="${CP}/main.do">홈</a><span>›</span>
                    <a href="${CP}/product/list.do">전체 상품</a><span>›</span>
                    <c:if test="${not empty product.largeName}"><span><c:out value="${product.largeName}"/></span></c:if>
                    <c:if test="${not empty product.middleName}"><span>›</span><span><c:out value="${product.middleName}"/></span></c:if>
                    <c:if test="${not empty product.smallName}"><span>›</span><span><c:out value="${product.smallName}"/></span></c:if>
                </nav>

                <section class="product-detail-card" aria-labelledby="productDetailTitle">
                    <div class="product-gallery">
                        <div class="product-gallery-main">
                            <c:choose>
                                <c:when test="${not empty product.imageList}">
                                    <img class="js-product-main-image" src="${CP}${product.imageList[0].filePath}"
                                         alt="<c:out value='${product.productTitle}'/> 대표 이미지">
                                </c:when>
                                <c:otherwise>
                                    <div class="product-image-placeholder"><span>등록된 이미지가 없습니다.</span></div>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <c:if test="${not empty product.imageList}">
                            <div class="product-gallery-thumbs" aria-label="상품 이미지 선택">
                                <c:forEach var="image" items="${product.imageList}" varStatus="status">
                                    <button type="button"
                                            class="product-gallery-thumb js-product-gallery-thumb ${status.first ? 'is-active' : ''}"
                                            data-image-src="${CP}${image.filePath}"
                                            data-image-alt="<c:out value='${image.originName}'/>"
                                            aria-pressed="${status.first ? 'true' : 'false'}">
                                        <img src="${CP}${image.filePath}" alt="<c:out value='${image.originName}'/>">
                                    </button>
                                </c:forEach>
                            </div>
                        </c:if>
                    </div>

                    <div class="product-detail-info">
                        <div class="product-detail-status-row">
                            <p class="product-detail-category">
                                <c:out value="${product.largeName}"/>
                                <c:if test="${not empty product.middleName}"> · <c:out value="${product.middleName}"/></c:if>
                                <c:if test="${not empty product.smallName}"> · <c:out value="${product.smallName}"/></c:if>
                            </p>
                            <c:choose>
                                <c:when test="${product.status eq '01'}"><span class="product-status-badge product-status-sale">판매중</span></c:when>
                                <c:when test="${product.status eq '02'}"><span class="product-status-badge product-status-reserved">예약중</span></c:when>
                                <c:when test="${product.status eq '03'}"><span class="product-status-badge product-status-sold">판매완료</span></c:when>
                                <c:otherwise><span class="product-status-badge product-status-sold">상태 미정</span></c:otherwise>
                            </c:choose>
                        </div>

                        <h1 class="product-detail-title" id="productDetailTitle"><c:out value="${product.productTitle}"/></h1>
                        <div class="product-detail-price"><fmt:formatNumber value="${product.price}" pattern="#,##0"/>원</div>

                        <dl class="product-detail-meta">
                            <div>
                                <dt>거래지역</dt>
                                <dd><c:out value="${empty product.location ? '지역 미입력' : product.location}"/></dd>
                            </div>
                            <div>
                                <dt>상품상태</dt>
                                <dd>
                                    <c:choose>
                                        <c:when test="${empty product.productCondition}">미입력</c:when>
                                        <c:otherwise><c:out value="${product.productCondition}" /></c:otherwise>
                                    </c:choose>
                                </dd>
                            </div>
                            <%-- 2026-07-13 [수정] 등록일은 한 줄 전체를 사용하고 조회수와 채팅수를 나란히 배치한다. --%>
                            <div class="product-detail-meta-wide">
                                <dt>등록일</dt>
                                <dd>
                                    <time class="js-product-date" data-product-date="${product.createDt}">
                                        <c:out value="${product.createDt}"/>
                                    </time>
                                </dd>
                            </div>
                            <div>
                                <dt>조회수</dt>
                                <dd><fmt:formatNumber value="${product.viewCount}" pattern="#,##0"/>회</dd>
                            </div>
                            <div>
                                <dt>채팅수</dt>
                                <dd><fmt:formatNumber value="${product.chatCnt}" pattern="#,##0"/>건</dd>
                            </div>
                        </dl>

                        <div class="product-seller-summary">
                            <div class="product-seller-main">
                                <span class="product-seller-avatar" aria-hidden="true">S</span>
                                <div>
                                    <strong><c:out value="${empty seller.nickname ? '판매자' : seller.nickname}" /></strong>
                                    <span><c:out value="${empty product.location ? '거래지역 미입력' : product.location}"/></span>
                                </div>
                            </div>
                            <a class="product-seller-link" href="${CP}/product/seller.do?userNum=${product.userNum}">판매자 보기</a>
                        </div>

                        <c:set var="isOwner" value="${not empty sessionScope.loginUser and sessionScope.loginUser.userNum == product.userNum}" />
                        <div class="product-detail-actions">
                            <c:choose>
                                <c:when test="${isOwner}">
                                    <%-- 2026-07-13 [수정] 상품 수정 이동 전에 공통 디자인 확인 팝업을 표시한다. --%>
                                    <button type="button" class="product-primary-button js-product-edit"
                                            data-update-url="${CP}/product/updateForm.do?productNo=${product.productNo}">상품 수정</button>
                                </c:when>
                                <c:otherwise>
                                    <button type="button" class="product-primary-button js-product-chat"
                                            data-login="${not empty sessionScope.loginUser}"
                                            data-login-url="${CP}/user/login.do"
                                            data-chat-url="${CP}/chat/enterRoom.do"
                                            data-chat-view-url="${CP}/chat/view.do"
                                            data-product-no="${product.productNo}"
                                            data-seller-no="${product.userNum}">채팅하기</button>
                                </c:otherwise>
                            </c:choose>
                            <a class="product-secondary-button" href="${CP}/product/list.do">목록으로</a>
                            <a class="product-secondary-button product-report-button"
                               href="${CP}/report/report_product_form.do?targetId=${product.productNo}&amp;reportedUserNo=${product.userNum}&amp;reportType=PRODUCT">신고하기</a>
                        </div>

                        <c:if test="${isOwner}">
                            <div class="product-owner-actions" aria-label="판매자 상품 관리">
                                <div class="product-owner-status-box">
                                    <%-- 2026-07-13 [수정] 거래 상태 변경도 공통 디자인 확인 팝업으로 처리한다. --%>
                                    <span class="product-owner-status-label">거래 상태 변경</span>
                                    <div class="product-status-toggle" role="group" aria-label="거래 상태 선택">
                                        <button type="button" class="js-product-status ${product.status eq '01' ? 'is-active' : ''}"
                                                aria-pressed="${product.status eq '01'}"
                                                data-status-url="${CP}/product/updateStatus.do" data-product-no="${product.productNo}"
                                                data-status="01" data-status-text="판매중">판매중</button>
                                        <button type="button" class="js-product-status ${product.status eq '02' ? 'is-active' : ''}"
                                                aria-pressed="${product.status eq '02'}"
                                                data-status-url="${CP}/product/updateStatus.do" data-product-no="${product.productNo}"
                                                data-status="02" data-status-text="예약중">예약중</button>
                                        <button type="button" class="js-product-status ${product.status eq '03' ? 'is-active' : ''}"
                                                aria-pressed="${product.status eq '03'}"
                                                data-status-url="${CP}/product/updateStatus.do" data-product-no="${product.productNo}"
                                                data-status="03" data-status-text="판매완료">판매완료</button>
                                    </div>
                                </div>
                                <button type="button" class="product-danger-button js-product-delete"
                                        data-product-no="${product.productNo}"
                                        data-delete-url="${CP}/product/doDelete.do" data-redirect-url="${CP}/product/list.do">상품 삭제</button>
                            </div>
                        </c:if>
                    </div>
                </section>

                <section class="product-detail-description" aria-labelledby="productDescriptionTitle">
                    <h2 id="productDescriptionTitle">상품 설명</h2>
                    <div class="product-detail-description-body"><c:out value="${product.productContent}"/></div>
                </section>
            </c:otherwise>
        </c:choose>
    </main>

    <jsp:include page="../common/footer.jsp" />
    <jsp:include page="../common/floatingBar.jsp" />
    <jsp:include page="../common/mobileBottomNav.jsp" />
</div>
</body>
</html>
