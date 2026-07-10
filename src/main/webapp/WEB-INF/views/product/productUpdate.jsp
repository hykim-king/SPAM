<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>상품 수정 | SPAM</title>
    <link rel="stylesheet" href="${CP}/resources/css/index.css">
    <link rel="stylesheet" href="${CP}/resources/css/product.css">
    <script defer src="${CP}/resources/js/index.js"></script>
    <script defer src="${CP}/resources/js/product.js"></script>
</head>
<body>
<div class="page-shell" id="top">
    <jsp:include page="../common/header.jsp" />
    <jsp:include page="../common/nav.jsp" />

    <main class="product-page product-form-page">
        <header class="product-page-header">
            <div class="product-page-heading">
                <h1>상품 수정</h1>
                <p>기존 상품 정보와 거래상태를 수정합니다.</p>
            </div>
        </header>

        <c:choose>
            <c:when test="${empty product}">
                <div class="product-empty-state">
                    <strong>수정할 상품 정보를 찾을 수 없습니다.</strong>
                    <a class="product-primary-button" href="${CP}/product/list.do">상품 목록으로</a>
                </div>
            </c:when>
            <c:otherwise>
                <form class="product-form-card js-product-form" action="${CP}/product/doUpdate.do" method="post">
                    <div class="product-form-card-header">
                        <h2>기존 상품 정보 수정</h2>
                        <p>변경할 항목을 입력한 뒤 수정 저장을 눌러주세요.</p>
                    </div>

                    <input type="hidden" name="productNo" value="${product.productNo}">
                    <input type="hidden" name="sallerNo" value="${product.sallerNo}">
                    <input class="js-category-value" type="hidden" id="categoryNo" name="categoryNo"
                           value="${product.categoryNo}" data-original-value="${product.categoryNo}">

                    <div class="product-form-grid">
                        <div class="product-form-field is-full">
                            <label for="productTitle">상품명<span class="product-required-mark">*</span></label>
                            <input class="product-form-input" id="productTitle" type="text" name="productTitle" maxlength="100"
                                   value="<c:out value='${product.productTitle}'/>" required>
                        </div>

                        <div class="product-form-field">
                            <label for="price">가격<span class="product-required-mark">*</span></label>
                            <input class="product-form-input" id="price" type="number" name="price" min="0" step="1"
                                   inputmode="numeric" value="${product.price}" required>
                        </div>

                        <div class="product-form-field">
                            <label for="location">거래지역<span class="product-required-mark">*</span></label>
                            <input class="product-form-input" id="location" type="text" name="location" maxlength="100"
                                   value="<c:out value='${product.location}'/>" required>
                        </div>

                        <div class="product-form-field">
                            <label for="productCondition">상품 상태</label>
                            <select class="product-form-select" id="productCondition" name="productCondition">
                                <option value="">선택 안 함</option>
                                <option value="새상품" ${product.productCondition eq '새상품' ? 'selected' : ''}>새상품</option>
                                <option value="사용감 적음" ${product.productCondition eq '사용감 적음' ? 'selected' : ''}>사용감 적음</option>
                                <option value="사용감 있음" ${product.productCondition eq '사용감 있음' ? 'selected' : ''}>사용감 있음</option>
                                <option value="수리 필요" ${product.productCondition eq '수리 필요' ? 'selected' : ''}>수리 필요</option>
                            </select>
                        </div>

                        <div class="product-form-field">
                            <label for="status">거래상태<span class="product-required-mark">*</span></label>
                            <select class="product-form-select" id="status" name="status" required>
                                <option value="01" ${product.status eq '01' ? 'selected' : ''}>판매중</option>
                                <option value="02" ${product.status eq '02' ? 'selected' : ''}>예약중</option>
                                <option value="03" ${product.status eq '03' ? 'selected' : ''}>판매완료</option>
                            </select>
                        </div>

                        <div class="product-form-field is-full">
                            <span class="product-form-label">카테고리</span>
                            <div class="product-current-category">
                                현재 카테고리:
                                <strong>&nbsp;<c:out value="${product.largeName}"/>
                                    <c:if test="${not empty product.middleName}"> &gt; <c:out value="${product.middleName}"/></c:if>
                                    <c:if test="${not empty product.smallName}"> &gt; <c:out value="${product.smallName}"/></c:if>
                                </strong>
                            </div>
                            <div class="product-category-selects">
                                <select class="product-form-select js-category-large" id="largeCategory"
                                        data-child-url="${CP}/product/categoryChild.do">
                                    <option value="">새 대분류 선택</option>
                                    <c:forEach var="category" items="${categoryList}">
                                        <option value="${category.categoryNo}"><c:out value="${category.categoryName}"/></option>
                                    </c:forEach>
                                </select>
                                <select class="product-form-select js-category-middle" id="middleCategory" disabled>
                                    <option value="">중분류 선택</option>
                                </select>
                                <select class="product-form-select js-category-small" id="smallCategory" disabled>
                                    <option value="">소분류 선택</option>
                                </select>
                            </div>
                            <p class="product-field-help">카테고리를 변경하지 않으면 기존 카테고리가 유지됩니다.</p>
                        </div>

                        <div class="product-form-field is-full">
                            <span class="product-form-label">이미지 변경</span>
                            <c:if test="${not empty product.imageList}">
                                <div class="product-upload-preview">
                                    <c:forEach var="image" items="${product.imageList}">
                                        <figure>
                                            <img src="${CP}${image.filePath}" alt="<c:out value='${image.originName}'/>">
                                            <figcaption><c:out value="${image.originName}"/></figcaption>
                                        </figure>
                                    </c:forEach>
                                </div>
                            </c:if>
                            <label class="product-upload-box" for="productImages">
                                <span class="product-upload-icon" aria-hidden="true">＋</span>
                                <strong>변경할 이미지 미리보기</strong>
                                <span>현재 단계에서는 선택한 이미지 미리보기만 제공됩니다.</span>
                                <input class="js-product-file-input" id="productImages" type="file" accept="image/*" multiple>
                            </label>
                            <div class="product-upload-preview js-product-upload-preview" aria-live="polite"></div>
                        </div>

                        <div class="product-form-field is-full">
                            <label for="productContent">상품 설명<span class="product-required-mark">*</span></label>
                            <textarea class="product-form-textarea" id="productContent" name="productContent" maxlength="2000" required><c:out value="${product.productContent}"/></textarea>
                        </div>
                    </div>

                    <div class="product-form-actions">
                        <button class="product-primary-button" type="submit">수정 저장</button>
                        <a class="product-secondary-button" href="${CP}/product/view.do?productNo=${product.productNo}&amp;sallerNo=${product.sallerNo}">취소</a>
                    </div>
                </form>
            </c:otherwise>
        </c:choose>
    </main>

    <jsp:include page="../common/footer.jsp" />
    <jsp:include page="../common/floatingBar.jsp" />
    <jsp:include page="../common/mobileBottomNav.jsp" />
</div>
</body>
</html>
