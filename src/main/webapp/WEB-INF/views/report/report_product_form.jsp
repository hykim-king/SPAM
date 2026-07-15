<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>허위 매물 및 상품 신고 | SPAM</title>
    <link rel="stylesheet" href="${CP}/resources/css/index.css?v=20260715">
    <link rel="stylesheet" href="${CP}/resources/css/report.css?v=20260715">
    <script defer src="${CP}/resources/js/index.js?v=20260715"></script>
    <script defer src="${CP}/resources/js/report.js?v=20260715"></script>
</head>
<body>
<div class="page-shell" id="top">
    <jsp:include page="../common/header.jsp" />
    <jsp:include page="../common/nav.jsp" />

    <main class="report-page report-form-page">
        <header class="report-page-header">
            <div class="report-header-copy">
                <h1>허위 매물 및 상품 신고</h1>
                <p>상품의 위반 사항을 선택하고 확인 가능한 내용을 작성해주세요.</p>
            </div>
            <button class="report-button js-history-back" type="button">이전</button>
        </header>

        <%-- 2026-07-14 [수정] 중복 reason 파라미터를 제거하고 신고 작성 UI를 공통화한다. --%>
        <section class="report-form-card">
            <div class="report-summary" aria-label="신고 대상 정보">
                <p><strong>상품 번호</strong><span><c:out value="${empty param.targetId ? '-' : param.targetId}" /></span></p>
                <p><strong>피신고 회원 번호</strong><span><c:out value="${empty param.reportedUserNo ? '-' : param.reportedUserNo}" /></span></p>
            </div>

            <form class="js-report-form" action="${CP}/report/doInsert.do" method="post"
                  data-confirm-message="정말로 이 상품을 신고하시겠습니까?&#10;허위 신고일 경우 서비스 이용이 제한될 수 있습니다.">
                <input type="hidden" name="targetId" value="<c:out value='${param.targetId}' />">
                <input type="hidden" name="reportedUserNo" value="<c:out value='${param.reportedUserNo}' />">
                <input type="hidden" name="reportType" value="PRODUCT">

                <div class="report-field">
                    <label class="report-label" for="reportReasonSelect">신고 사유<span class="report-required">*</span></label>
                    <select class="report-select js-report-reason-select" id="reportReasonSelect" required>
                        <option value="">상품 신고 사유를 선택하세요</option>
                        <option value="상품 미배송">상품 미배송</option>
                        <option value="판매금지 물품 등록">판매금지 물품 등록</option>
                        <option value="전문 업자 의심">전문 업자 의심</option>
                        <option value="허위성 매물">허위성 매물</option>
                        <option value="카테고리 위반">카테고리 위반</option>
                        <option value="음란물 및 유해콘텐츠 게시">음란물 및 유해콘텐츠 게시</option>
                        <option value="광고 및 홍보 도배">광고 및 홍보 도배</option>
                        <option value="불량 상품판매">불량 상품판매</option>
                        <option value="기타 (직접 입력)">기타 (직접 입력)</option>
                    </select>
                </div>

                <div class="report-field">
                    <label class="report-label" for="reason">상세 내용<span class="report-required">*</span></label>
                    <div class="report-textarea-wrap">
                        <textarea class="report-textarea js-report-reason-text" id="reason" name="reason" maxlength="1000"
                                  placeholder="신고 사유를 명확하게 작성해주시면 확인 후 조치하겠습니다." required></textarea>
                        <span class="report-char-count"><span class="js-report-char-count">0</span>/1000</span>
                    </div>
                </div>

                <div class="report-form-actions">
                    <button class="report-button js-report-cancel" type="button">취소</button>
                    <button class="report-button is-primary js-report-submit" type="submit" disabled>신고서 제출</button>
                </div>
            </form>
        </section>
    </main>

    <jsp:include page="../common/footer.jsp" />
    <jsp:include page="../common/floatingBar.jsp" />
    <jsp:include page="../common/mobileBottomNav.jsp" />
</div>
</body>
</html>
