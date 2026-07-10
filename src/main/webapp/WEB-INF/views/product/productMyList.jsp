<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>내가 등록한 상품 | SPAM</title>
    <link rel="stylesheet" href="${CP}/resources/css/index.css">
    <link rel="stylesheet" href="${CP}/resources/css/product.css">
    <script defer src="${CP}/resources/js/index.js"></script>
    <script defer src="${CP}/resources/js/product.js"></script>
</head>
<body>
<div class="page-shell" id="top">
    <jsp:include page="../common/header.jsp" />
    <jsp:include page="../common/nav.jsp" />

    <main class="product-page product-my-list-page">
        <header class="product-page-header">
            <div class="product-page-heading">
                <h1>내가 등록한 상품</h1>
                <p>등록한 상품의 거래상태를 확인하고 수정하거나 삭제할 수 있습니다.</p>
            </div>
            <a class="product-primary-button" href="${CP}/product/saveForm.do">＋ 상품 등록</a>
        </header>

        <nav class="product-status-tabs" aria-label="상품 상태별 보기">
            <a class="${empty param.status ? 'is-active' : ''}" href="${CP}/product/myList.do">전체</a>
            <a class="${param.status eq '01' ? 'is-active' : ''}" href="${CP}/product/myList.do?status=01">판매중</a>
            <a class="${param.status eq '02' ? 'is-active' : ''}" href="${CP}/product/myList.do?status=02">예약중</a>
            <a class="${param.status eq '03' ? 'is-active' : ''}" href="${CP}/product/myList.do?status=03">판매완료</a>
        </nav>

        <c:choose>
            <c:when test="${empty list}">
                <div class="product-empty-state">
                    <strong>등록한 상품이 없습니다.</strong>
                    <p>판매할 상품을 등록하면 이곳에서 관리할 수 있습니다.</p>
                    <a class="product-primary-button" href="${CP}/product/saveForm.do">상품 등록하기</a>
                </div>
            </c:when>
            <c:otherwise>
                <section class="product-management-card" aria-label="내 상품 관리 목록">
                    <table class="product-management-table">
                        <colgroup>
                            <col style="width:44%">
                            <col style="width:17%">
                            <col style="width:14%">
                            <col style="width:25%">
                        </colgroup>
                        <thead>
                        <tr>
                            <th scope="col">상품</th>
                            <th scope="col">가격</th>
                            <th scope="col">상태</th>
                            <th scope="col">관리</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="product" items="${list}">
                            <tr>
                                <td>
                                    <a class="product-management-item" href="${CP}/product/view.do?productNo=${product.productNo}&amp;sallerNo=${product.sallerNo}">
                                        <span class="product-management-thumb">
                                            <c:choose>
                                                <c:when test="${not empty product.imageList}">
                                                    <img src="${CP}${product.imageList[0].filePath}" alt="<c:out value='${product.productTitle}'/>">
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="product-image-placeholder" aria-label="상품 이미지 없음"></span>
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                        <span class="product-management-title"><c:out value="${product.productTitle}"/></span>
                                    </a>
                                </td>
                                <td><fmt:formatNumber value="${product.price}" pattern="#,##0"/>원</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${product.status eq '01'}"><span class="product-status-badge product-status-sale">판매중</span></c:when>
                                        <c:when test="${product.status eq '02'}"><span class="product-status-badge product-status-reserved">예약중</span></c:when>
                                        <c:otherwise><span class="product-status-badge product-status-sold">판매완료</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <div class="product-table-actions">
                                        <a class="product-table-button" href="${CP}/product/updateForm.do?productNo=${product.productNo}&amp;sallerNo=${product.sallerNo}">수정</a>
                                        <button type="button" class="product-table-button is-delete js-product-delete"
                                                data-product-no="${product.productNo}" data-seller-no="${product.sallerNo}"
                                                data-delete-url="${CP}/product/doDelete.do" data-redirect-url="${CP}/product/myList.do">삭제</button>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
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
