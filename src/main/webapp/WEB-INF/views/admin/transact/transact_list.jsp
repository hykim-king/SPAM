<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>전체 상품 현황 | SPAM 관리자</title>
    <link rel="stylesheet" href="${CP}/resources/css/index.css?v=20260715">
    <link rel="stylesheet" href="${CP}/resources/css/member.css?v=20260715">
    <link rel="stylesheet" href="${CP}/resources/css/admin.css?v=20260715">
    <script defer src="${CP}/resources/js/index.js?v=20260715"></script>
</head>
<body>
<div class="page-shell" id="top">
    <jsp:include page="../../common/header.jsp" />
    <jsp:include page="../../common/nav.jsp" />

    <main class="member-page-shell">
        <header class="admin-page-header">
            <div class="admin-heading">
                <span class="admin-kicker">ADMIN</span>
                <h1>전체 상품 현황</h1>
            </div>
            <nav class="admin-nav" aria-label="관리자 메뉴">
                <a class="admin-nav-link" href="${CP}/admin/user/list.do">회원 관리</a>
                <a class="admin-nav-link" href="${CP}/report/admin_doRetrieve.do">신고 센터</a>
                <span class="admin-nav-link is-active" aria-current="page">전체 상품</span>
            </nav>
        </header>

        <section class="panel stack" aria-label="관리자 전체 상품 목록">
            <div class="admin-list-meta">
                <strong>총 상품 수: ${totalCount}</strong>
                <span>현재 페이지: ${paging.pageNo}<c:if test="${totalPage gt 0}"> / ${totalPage}</c:if></span>
            </div>

            <nav class="admin-product-filters" aria-label="상품 상태 필터">
                <a class="admin-filter-link ${empty currentStatus ? 'is-active' : ''}" href="${CP}/admin/transact/list.do">전체</a>
                <a class="admin-filter-link ${currentStatus eq '01' ? 'is-active' : ''}" href="${CP}/admin/transact/list.do?status=01">판매중</a>
                <a class="admin-filter-link ${currentStatus eq '02' ? 'is-active' : ''}" href="${CP}/admin/transact/list.do?status=02">거래완료</a>
                <a class="admin-filter-link ${currentStatus eq '03' ? 'is-active' : ''}" href="${CP}/admin/transact/list.do?status=03">기타</a>
            </nav>

            <div class="table-wrap">
                <table class="data-table">
                    <thead>
                    <tr>
                        <th>상품명</th>
                        <th>판매자 아이디</th>
                        <th>가격</th>
                        <th>상태</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty list}">
                            <tr><td class="empty-row" colspan="4">등록된 상품이 없습니다.</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="vo" items="${list}">
                                <tr>
                                    <td class="report-cell-left">
                                        <button class="admin-product-title-link" type="button"
                                                data-product-no="${vo.productNo}"
                                                onclick="showAdminProductDetail(this.getAttribute('data-product-no'))">
                                            <c:out value="${vo.productTitle}" />
                                        </button>
                                    </td>
                                    <td><c:out value="${vo.userId}" /></td>
                                    <td><fmt:formatNumber value="${vo.price}" pattern="#,###" />원</td>
                                    <td>${vo.status == '01' ? '판매중' : (vo.status == '02' ? '거래완료' : '기타')}</td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>

            <c:if test="${totalPage gt 1}">
                <nav class="pagination admin-pagination" aria-label="전체 상품 페이지 이동">
                    <div class="admin-pagination-side admin-pagination-prev">
                        <c:if test="${paging.pageNo gt 1}">
                            <a class="btn outline" href="${CP}/admin/transact/list.do?pageNo=${paging.pageNo - 1}&amp;status=${currentStatus}">이전</a>
                        </c:if>
                    </div>
                    <strong class="admin-pagination-current">${paging.pageNo} / ${totalPage}</strong>
                    <div class="admin-pagination-side admin-pagination-next">
                        <c:if test="${paging.pageNo lt totalPage}">
                            <a class="btn outline" href="${CP}/admin/transact/list.do?pageNo=${paging.pageNo + 1}&amp;status=${currentStatus}">다음</a>
                        </c:if>
                    </div>
                </nav>
            </c:if>
        </section>
    </main>

    <div class="admin-detail-modal" id="adminProductDetailModal" aria-hidden="true">
        <section class="admin-detail-modal-card" role="dialog" aria-modal="true" aria-labelledby="adminProductDetailTitle">
            <h2 id="adminProductDetailTitle">거래 상세</h2>
            <dl class="admin-detail-list">
                <div><dt>상품명</dt><dd id="mProductTitle">-</dd></div>
                <div><dt>상태</dt><dd id="mStatus">-</dd></div>
                <div><dt>금액</dt><dd id="mAmount">-</dd></div>
                <div><dt>등록일</dt><dd id="mDate">-</dd></div>
                <div><dt>바로가기</dt><dd><a id="mProductLink" class="admin-product-title-link" href="#" target="_blank" rel="noopener">상품 상세 페이지 이동</a></dd></div>
            </dl>
            <div class="admin-modal-actions">
                <button class="btn outline" type="button" onclick="closeAdminProductDetail()">닫기</button>
            </div>
        </section>
    </div>

    <jsp:include page="../../common/footer.jsp" />
    <jsp:include page="../../common/floatingBar.jsp" />
    <jsp:include page="../../common/mobileBottomNav.jsp" />
</div>

<script>
function showAdminProductDetail(productNo) {
    fetch('${CP}/transact/getDetail.do?productNo=' + encodeURIComponent(productNo), {
        headers: { 'Accept': 'application/json' }
    })
        .then(function (response) {
            if (!response.ok) throw new Error('상품 상세 조회 실패');
            return response.json();
        })
        .then(function (data) {
            document.getElementById('mProductTitle').textContent = data.productName || '-';
            document.getElementById('mStatus').textContent = data.txStatus === '01' ? '판매중' : '거래완료';
            document.getElementById('mAmount').textContent = Number(data.amount || 0).toLocaleString('ko-KR') + '원';
            var dateValue = data.createDt;
            var parsedDate = dateValue ? new Date(dateValue) : null;
            document.getElementById('mDate').textContent = parsedDate && !Number.isNaN(parsedDate.getTime())
                ? parsedDate.toLocaleString('ko-KR')
                : (dateValue || '정보 없음');
            document.getElementById('mProductLink').href = '${CP}/product/view.do?productNo=' + encodeURIComponent(productNo);
            var modal = document.getElementById('adminProductDetailModal');
            modal.classList.add('is-open');
            modal.setAttribute('aria-hidden', 'false');
        })
        .catch(function () {
            alert('정보를 불러오지 못했습니다.');
        });
}

function closeAdminProductDetail() {
    var modal = document.getElementById('adminProductDetailModal');
    modal.classList.remove('is-open');
    modal.setAttribute('aria-hidden', 'true');
}

document.addEventListener('keydown', function (event) {
    if (event.key === 'Escape') closeAdminProductDetail();
});

document.getElementById('adminProductDetailModal').addEventListener('click', function (event) {
    if (event.target === this) closeAdminProductDetail();
});
</script>
</body>
</html>
