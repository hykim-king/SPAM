<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>전체 상품 | SPAM</title>
    <link rel="stylesheet" href="${CP}/resources/css/index.css">
    <link rel="stylesheet" href="${CP}/resources/css/product.css">
    <script defer src="${CP}/resources/js/index.js"></script>
    <script defer src="${CP}/resources/js/product.js"></script>
</head>
<body>
<div class="page-shell" id="top">
    <jsp:include page="../common/header.jsp" />
    <jsp:include page="../common/nav.jsp" />

    <main class="product-page product-list-page">
        <header class="product-page-header">
            <div class="product-page-heading">
                <h1>전체 상품 목록</h1>
                <p>검색어와 카테고리, 거래 조건에 맞는 상품을 확인하세요.</p>
            </div>
            <a class="product-primary-button" href="${CP}/product/saveForm.do"
               data-spam-modal="${empty sessionScope.loginUser ? 'login' : ''}"
               data-login-url="${CP}/user/login.do">＋ 상품 등록</a>
        </header>

        <section class="product-filter-panel" aria-label="상품 검색 필터">
            <button type="button" class="product-filter-toggle js-product-filter-toggle" aria-expanded="false">
                <span>검색 및 필터</span><span aria-hidden="true">⌄</span>
            </button>

            <form class="product-filter-form" action="${CP}/product/list.do" method="get">
                <input type="hidden" name="categoryNo" value="${search.categoryNo}">
                <div class="product-filter-field">
                    <label for="productSearchWord">검색어</label>
                    <input class="product-filter-input" id="productSearchWord" type="search" name="searchWord"
                           value="<c:out value='${search.searchWord}'/>" placeholder="상품명, 카테고리, 지역 검색">
                </div>
                <div class="product-filter-field">
                    <label for="productRegion">지역</label>
                    <input class="product-filter-input" id="productRegion" type="text" name="location"
                           value="<c:out value='${search.location}'/>" placeholder="예: 서울 강남구">
                </div>
                <div class="product-filter-field">
                    <label for="productPriceRange">가격대</label>
                    <select class="product-filter-select" id="productPriceRange" name="priceRange">
                        <option value="">전체 가격</option>
                        <option value="under30000" ${search.priceRange eq 'under30000' ? 'selected' : ''}>3만원 이하</option>
                        <option value="30000to100000" ${search.priceRange eq '30000to100000' ? 'selected' : ''}>3만원~10만원</option>
                        <option value="100000to300000" ${search.priceRange eq '100000to300000' ? 'selected' : ''}>10만원~30만원</option>
                        <option value="over300000" ${search.priceRange eq 'over300000' ? 'selected' : ''}>30만원 이상</option>
                    </select>
                </div>
                <div class="product-filter-field">
                    <label for="productTradeStatus">거래상태</label>
                    <select class="product-filter-select" id="productTradeStatus" name="status">
                        <option value="">전체 상태</option>
                        <option value="01" ${search.status eq '01' ? 'selected' : ''}>판매중</option>
                        <option value="02" ${search.status eq '02' ? 'selected' : ''}>예약중</option>
                        <option value="03" ${search.status eq '03' ? 'selected' : ''}>판매완료</option>
                    </select>
                </div>
                <button class="product-filter-submit" type="submit">검색</button>
            </form>
        </section>

        <div class="product-list-layout">
            <aside class="product-category-sidebar" aria-label="상품 카테고리 필터">
                <h2>카테고리</h2>
                <nav class="product-category-list">
                    <a class="${empty search.categoryNo ? 'is-active' : ''}" href="${CP}/product/list.do">전체</a>
                    <c:forEach var="category" items="${categoryList}">
                        <c:url var="categoryUrl" value="/product/list.do">
                            <c:param name="categoryNo" value="${category.categoryNo}" />
                        </c:url>
                        <a class="${search.categoryNo == category.categoryNo ? 'is-active' : ''}" href="${categoryUrl}">
                            <c:out value="${category.categoryName}" />
                        </a>
                    </c:forEach>
                </nav>
            </aside>

            <section class="product-results" aria-labelledby="productResultTitle">
                <div class="product-results-toolbar">
                    <p class="product-results-count" id="productResultTitle">
                        전체 <strong><fmt:formatNumber value="${totalCnt}" pattern="#,##0" /></strong>개 상품
                        <c:if test="${not empty search.searchWord}">
                            · ‘<c:out value="${search.searchWord}"/>’ 검색 결과
                        </c:if>
                    </p>

                    <form class="product-sort-form" action="${CP}/product/list.do" method="get">
                        <input type="hidden" name="searchWord" value="<c:out value='${search.searchWord}'/>">
                        <input type="hidden" name="categoryNo" value="${search.categoryNo}">
                        <input type="hidden" name="location" value="<c:out value='${search.location}'/>">
                        <input type="hidden" name="priceRange" value="<c:out value='${search.priceRange}'/>">
                        <input type="hidden" name="status" value="<c:out value='${search.status}'/>">
                        <label for="productSort">정렬</label>
                        <select class="js-product-sort" id="productSort" name="sort">
                            <option value="latest" ${empty search.sort or search.sort eq 'latest' ? 'selected' : ''}>최신순</option>
                            <option value="popular" ${search.sort eq 'popular' ? 'selected' : ''}>인기순</option>
                            <option value="recommend" ${search.sort eq 'recommend' ? 'selected' : ''}>추천순</option>
                            <option value="priceLow" ${search.sort eq 'priceLow' ? 'selected' : ''}>낮은 가격순</option>
                            <option value="priceHigh" ${search.sort eq 'priceHigh' ? 'selected' : ''}>높은 가격순</option>
                            <option value="view" ${search.sort eq 'view' ? 'selected' : ''}>조회수순</option>
                        </select>
                    </form>
                </div>

                <c:choose>
                    <c:when test="${empty list}">
                        <div class="product-empty-state">
                            <strong>조건에 맞는 상품이 없습니다.</strong>
                            <p>검색 조건을 바꾸거나 첫 번째 상품을 등록해보세요.</p>
                            <a class="product-primary-button" href="${CP}/product/saveForm.do"
                               data-spam-modal="${empty sessionScope.loginUser ? 'login' : ''}"
                               data-login-url="${CP}/user/login.do">상품 등록하기</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="product-card-grid">
                            <c:forEach var="product" items="${list}">
                                <article class="product-list-card">
                                    <a class="product-list-card-link" href="${CP}/product/view.do?productNo=${product.productNo}">
                                        <div class="product-list-thumb">
                                            <c:choose>
                                                <c:when test="${not empty product.thumbnailPath}">
                                                    <img src="${CP}${product.thumbnailPath}" alt="<c:out value='${product.productTitle}'/> 상품 이미지">
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="product-image-placeholder"><span>상품 이미지 준비중</span></div>
                                                </c:otherwise>
                                            </c:choose>

                                            <c:choose>
                                                <c:when test="${product.status eq '01'}"><span class="product-status-badge product-status-sale">판매중</span></c:when>
                                                <c:when test="${product.status eq '02'}"><span class="product-status-badge product-status-reserved">예약중</span></c:when>
                                                <c:otherwise><span class="product-status-badge product-status-sold">판매완료</span></c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="product-list-info">
                                            <strong class="product-list-title"><c:out value="${product.productTitle}"/></strong>
                                            <span class="product-list-price"><fmt:formatNumber value="${product.price}" pattern="#,##0"/>원</span>
                                            <span class="product-list-meta">
                                                <c:out value="${empty product.location ? '지역 미입력' : product.location}" />
                                                · <time class="js-product-date" data-product-date="${product.createDt}"><c:out value="${product.createDt}" /></time>
                                            </span>
                                        </div>
                                    </a>
                                </article>
                            </c:forEach>
                        </div>

                        <c:if test="${totalPage gt 1}">
                            <nav class="product-pagination" aria-label="상품 목록 페이지">
                                <c:choose>
                                    <c:when test="${search.pageNo gt 1}">
                                        <c:url var="prevUrl" value="/product/list.do">
                                            <c:param name="pageNo" value="${search.pageNo - 1}" />
                                            <c:param name="searchWord" value="${search.searchWord}" />
                                            <c:param name="categoryNo" value="${search.categoryNo}" />
                                            <c:param name="location" value="${search.location}" />
                                            <c:param name="priceRange" value="${search.priceRange}" />
                                            <c:param name="status" value="${search.status}" />
                                            <c:param name="sort" value="${search.sort}" />
                                        </c:url>
                                        <a href="${prevUrl}" aria-label="이전 페이지">‹</a>
                                    </c:when>
                                    <c:otherwise><span aria-hidden="true">‹</span></c:otherwise>
                                </c:choose>

                                <c:forEach var="page" begin="${startPage}" end="${endPage}">
                                    <c:url var="pageUrl" value="/product/list.do">
                                        <c:param name="pageNo" value="${page}" />
                                        <c:param name="searchWord" value="${search.searchWord}" />
                                        <c:param name="categoryNo" value="${search.categoryNo}" />
                                        <c:param name="location" value="${search.location}" />
                                        <c:param name="priceRange" value="${search.priceRange}" />
                                        <c:param name="status" value="${search.status}" />
                                        <c:param name="sort" value="${search.sort}" />
                                    </c:url>
                                    <c:choose>
                                        <c:when test="${search.pageNo == page}">
                                            <a class="is-current" href="${pageUrl}" aria-current="page">${page}</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageUrl}">${page}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>

                                <c:choose>
                                    <c:when test="${search.pageNo lt totalPage}">
                                        <c:url var="nextUrl" value="/product/list.do">
                                            <c:param name="pageNo" value="${search.pageNo + 1}" />
                                            <c:param name="searchWord" value="${search.searchWord}" />
                                            <c:param name="categoryNo" value="${search.categoryNo}" />
                                            <c:param name="location" value="${search.location}" />
                                            <c:param name="priceRange" value="${search.priceRange}" />
                                            <c:param name="status" value="${search.status}" />
                                            <c:param name="sort" value="${search.sort}" />
                                        </c:url>
                                        <a href="${nextUrl}" aria-label="다음 페이지">›</a>
                                    </c:when>
                                    <c:otherwise><span aria-hidden="true">›</span></c:otherwise>
                                </c:choose>
                            </nav>
                        </c:if>
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
