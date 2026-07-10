<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
                <p>검색어와 카테고리, 거래 조건에 맞는 상품을 한곳에서 확인하세요.</p>
            </div>
            <a class="product-primary-button" href="${CP}/product/saveForm.do">＋ 상품 등록</a>
        </header>

        <section class="product-filter-panel" aria-label="상품 검색 필터">
            <button type="button" class="product-filter-toggle js-product-filter-toggle" aria-expanded="false">
                <span>검색 및 필터</span>
                <span aria-hidden="true">⌄</span>
            </button>

            <form class="product-filter-form" action="${CP}/product/list.do" method="get">
                <div class="product-filter-field">
                    <label for="productSearchWord">검색어</label>
                    <input class="product-filter-input" id="productSearchWord" type="search" name="searchWord"
                           value="<c:out value='${param.searchWord}'/>" placeholder="상품명, 카테고리, 지역 검색">
                </div>
                <div class="product-filter-field">
                    <label for="productRegion">지역</label>
                    <input class="product-filter-input" id="productRegion" type="text" name="location"
                           value="<c:out value='${param.location}'/>" placeholder="예: 서울 강남구">
                </div>
                <div class="product-filter-field">
                    <label for="productPriceRange">가격대</label>
                    <select class="product-filter-select" id="productPriceRange" name="priceRange">
                        <option value="">전체 가격</option>
                        <option value="under30000" ${param.priceRange eq 'under30000' ? 'selected' : ''}>3만원 이하</option>
                        <option value="30000to100000" ${param.priceRange eq '30000to100000' ? 'selected' : ''}>3만원~10만원</option>
                        <option value="100000to300000" ${param.priceRange eq '100000to300000' ? 'selected' : ''}>10만원~30만원</option>
                        <option value="over300000" ${param.priceRange eq 'over300000' ? 'selected' : ''}>30만원 이상</option>
                    </select>
                </div>
                <div class="product-filter-field">
                    <label for="productTradeStatus">거래상태</label>
                    <select class="product-filter-select" id="productTradeStatus" name="status">
                        <option value="">전체 상태</option>
                        <option value="01" ${param.status eq '01' ? 'selected' : ''}>판매중</option>
                        <option value="02" ${param.status eq '02' ? 'selected' : ''}>예약중</option>
                        <option value="03" ${param.status eq '03' ? 'selected' : ''}>거래완료</option>
                    </select>
                </div>
                <button class="product-filter-submit" type="submit">검색</button>
            </form>
        </section>

        <div class="product-list-layout">
            <aside class="product-category-sidebar" aria-label="상품 카테고리 필터">
                <h2>카테고리</h2>
                <nav class="product-category-list">
                    <a class="${empty param.category ? 'is-active' : ''}" href="${CP}/product/list.do">전체</a>
                    <a class="${param.category eq 'digital' ? 'is-active' : ''}" href="${CP}/product/list.do?category=digital">디지털/가전</a>
                    <a class="${param.category eq 'fashion' ? 'is-active' : ''}" href="${CP}/product/list.do?category=fashion">패션/잡화</a>
                    <a class="${param.category eq 'beauty' ? 'is-active' : ''}" href="${CP}/product/list.do?category=beauty">뷰티/미용</a>
                    <a class="${param.category eq 'living' ? 'is-active' : ''}" href="${CP}/product/list.do?category=living">생활/가구</a>
                    <a class="${param.category eq 'baby' ? 'is-active' : ''}" href="${CP}/product/list.do?category=baby">유아동</a>
                    <a class="${param.category eq 'sports' ? 'is-active' : ''}" href="${CP}/product/list.do?category=sports">스포츠/레저</a>
                    <a class="${param.category eq 'book' ? 'is-active' : ''}" href="${CP}/product/list.do?category=book">도서/티켓</a>
                    <a class="${param.category eq 'etc' ? 'is-active' : ''}" href="${CP}/product/list.do?category=etc">기타</a>
                </nav>
            </aside>

            <section class="product-results" aria-labelledby="productResultTitle">
                <div class="product-results-toolbar">
                    <p class="product-results-count" id="productResultTitle">
                        전체 <strong>${fn:length(list)}</strong>개 상품
                        <c:if test="${not empty param.searchWord}">
                            · ‘<c:out value="${param.searchWord}"/>’ 검색 결과
                        </c:if>
                    </p>

                    <form class="product-sort-form" action="${CP}/product/list.do" method="get">
                        <input type="hidden" name="searchWord" value="<c:out value='${param.searchWord}'/>">
                        <input type="hidden" name="category" value="<c:out value='${param.category}'/>">
                        <input type="hidden" name="location" value="<c:out value='${param.location}'/>">
                        <input type="hidden" name="priceRange" value="<c:out value='${param.priceRange}'/>">
                        <input type="hidden" name="status" value="<c:out value='${param.status}'/>">
                        <label for="productSort">정렬</label>
                        <select class="js-product-sort" id="productSort" name="sort">
                            <option value="latest" ${empty param.sort or param.sort eq 'latest' ? 'selected' : ''}>최신순</option>
                            <option value="priceLow" ${param.sort eq 'priceLow' ? 'selected' : ''}>낮은 가격순</option>
                            <option value="priceHigh" ${param.sort eq 'priceHigh' ? 'selected' : ''}>높은 가격순</option>
                            <option value="view" ${param.sort eq 'view' ? 'selected' : ''}>조회수순</option>
                        </select>
                    </form>
                </div>

                <c:choose>
                    <c:when test="${empty list}">
                        <div class="product-empty-state">
                            <strong>등록된 상품이 없습니다.</strong>
                            <p>검색 조건을 바꾸거나 첫 번째 상품을 등록해보세요.</p>
                            <a class="product-primary-button" href="${CP}/product/saveForm.do">상품 등록하기</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="product-card-grid">
                            <c:forEach var="product" items="${list}">
                                <article class="product-list-card">
                                    <a class="product-list-card-link" href="${CP}/product/view.do?productNo=${product.productNo}&amp;sallerNo=0">
                                        <div class="product-list-thumb">
                                            <c:choose>
                                                <c:when test="${not empty product.imageList}">
                                                    <img src="${CP}${product.imageList[0].filePath}" alt="<c:out value='${product.productTitle}'/> 상품 이미지">
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="product-image-placeholder"><span>상품 이미지 준비중</span></div>
                                                </c:otherwise>
                                            </c:choose>

                                            <c:choose>
                                                <c:when test="${product.status eq '01'}"><span class="product-status-badge product-status-sale">판매중</span></c:when>
                                                <c:when test="${product.status eq '02'}"><span class="product-status-badge product-status-reserved">예약중</span></c:when>
                                                <c:when test="${product.status eq '03'}"><span class="product-status-badge product-status-sold">거래완료</span></c:when>
                                            </c:choose>
                                        </div>
                                        <div class="product-list-info">
                                            <strong class="product-list-title"><c:out value="${product.productTitle}"/></strong>
                                            <span class="product-list-price"><fmt:formatNumber value="${product.price}" pattern="#,##0"/>원</span>
                                            <span class="product-list-meta">
                                                <c:choose>
                                                    <c:when test="${empty product.location}">지역 미입력</c:when>
                                                    <c:otherwise><c:out value="${product.location}"/></c:otherwise>
                                                </c:choose>
                                                · <c:out value="${product.createDt}"/>
                                            </span>
                                        </div>
                                    </a>
                                </article>
                            </c:forEach>
                        </div>

                        <nav class="product-pagination" aria-label="상품 목록 페이지">
                            <span aria-hidden="true">‹</span>
                            <span class="is-current" aria-current="page">1</span>
                            <span aria-hidden="true">›</span>
                        </nav>
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
