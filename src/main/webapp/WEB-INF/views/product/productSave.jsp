<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>상품 등록 | SPAM</title>
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
                <h1>상품 등록</h1>
                <p>판매할 상품의 정보와 실제 이미지를 정확하게 입력해주세요.</p>
            </div>
        </header>

        <form class="product-form-card js-product-form" action="${CP}/product/doSave.do" method="post" enctype="multipart/form-data">
            <div class="product-form-card-header">
                <h2>상품 정보 입력</h2>
                <p><span class="product-required-mark">*</span> 표시는 필수 입력 항목입니다.</p>
            </div>

            <input type="hidden" name="sallerNo" value="${empty sessionScope.loginUser ? 0 : sessionScope.loginUser.userNum}">
            <input class="js-category-value" type="hidden" id="categoryNo" name="categoryNo" value="">

            <div class="product-form-grid">
                <div class="product-form-field is-full">
                    <label for="productTitle">상품명<span class="product-required-mark">*</span></label>
                    <input class="product-form-input" id="productTitle" type="text" name="productTitle" maxlength="100"
                           placeholder="상품명을 입력하세요" required>
                </div>

                <div class="product-form-field">
                    <label for="price">가격<span class="product-required-mark">*</span></label>
                    <input class="product-form-input" id="price" type="number" name="price" min="0" step="1"
                           inputmode="numeric" placeholder="0" required>
                    <p class="product-field-help">무료나눔은 0원으로 입력합니다.</p>
                </div>

                <div class="product-form-field">
                    <label for="location">거래지역<span class="product-required-mark">*</span></label>
                    <input class="product-form-input" id="location" type="text" name="location" maxlength="100"
                           placeholder="예: 서울 강남구" required>
                </div>

                <div class="product-form-field">
                    <label for="productCondition">상품 상태</label>
                    <select class="product-form-select" id="productCondition" name="productCondition">
                        <option value="">선택 안 함</option>
                        <option value="새상품">새상품</option>
                        <option value="사용감 적음">사용감 적음</option>
                        <option value="사용감 있음">사용감 있음</option>
                        <option value="수리 필요">수리 필요</option>
                    </select>
                </div>

                <div class="product-form-field">
                    <label>거래상태</label>
                    <div class="product-current-category"><strong>판매중</strong>&nbsp;상태로 등록됩니다.</div>
                    <input type="hidden" name="status" value="01">
                </div>

                <div class="product-form-field is-full">
                    <span class="product-form-label">카테고리<span class="product-required-mark">*</span></span>
                    <div class="product-category-selects">
                        <select class="product-form-select js-category-large" id="largeCategory"
                                data-child-url="${CP}/product/categoryChild.do" required>
                            <option value="">대분류 선택</option>
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
                </div>

                <div class="product-form-field is-full">
                    <span class="product-form-label">이미지 업로드</span>
                    <label class="product-upload-box" for="productImages">
                        <span class="product-upload-icon" aria-hidden="true">＋</span>
                        <strong>상품 사진을 선택하거나 이곳에 끌어다 놓으세요.</strong>
                        <span>최대 5장 · 첫 번째 이미지가 대표 이미지로 표시됩니다.</span>
                        <input class="js-product-file-input" id="productImages" type="file" name="files"
                               accept="image/*" multiple>
                    </label>
                    <div class="product-upload-preview js-product-upload-preview" aria-live="polite"></div>
                </div>

                <div class="product-form-field is-full">
                    <label for="productContent">상품 설명<span class="product-required-mark">*</span></label>
                    <textarea class="product-form-textarea" id="productContent" name="productContent" maxlength="2000"
                              placeholder="상품 상태, 사용 기간, 거래 조건 등을 자세히 작성해주세요." required></textarea>
                </div>
            </div>

            <div class="product-form-actions">
                <button class="product-primary-button" type="submit">등록</button>
                <a class="product-secondary-button" href="${CP}/product/list.do">취소</a>
            </div>
        </form>
    </main>

    <jsp:include page="../common/footer.jsp" />
    <jsp:include page="../common/floatingBar.jsp" />
    <jsp:include page="../common/mobileBottomNav.jsp" />
</div>
</body>
</html>
