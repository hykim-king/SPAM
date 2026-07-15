<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원상세 | SPAM 관리자</title>
    <link rel="stylesheet" href="${CP}/resources/css/index.css?v=20260715">
    <link rel="stylesheet" href="${CP}/resources/css/member.css?v=20260715">
    <link rel="stylesheet" href="${CP}/resources/css/admin.css?v=20260715">
    <script defer src="${CP}/resources/js/index.js?v=20260715"></script>
    <script defer src="${CP}/resources/js/member.js?v=20260715"></script>
</head>
<body>
    <div class="page-shell" id="top">
        <jsp:include page="../../common/header.jsp" />
        <jsp:include page="../../common/nav.jsp" />

        <main class="member-page-shell">
        <header class="admin-page-header">
            <div class="admin-heading">
                <span class="admin-kicker">ADMIN</span>
                <h1>회원 상세</h1>
            </div>
            <nav class="admin-nav" aria-label="관리자 메뉴">
                <a class="admin-nav-link" href="${CP}/admin/user/list.do">회원 관리</a>
                <a class="admin-nav-link" href="${CP}/report/admin_doRetrieve.do">신고 센터</a>
                <a class="admin-nav-link" href="${CP}/transact/list.do">거래내역</a>
                <a class="admin-nav-link" href="${CP}/admin/transact/list.do">거래 목록</a>
            </nav>
        </header>

        <div class="grid-2">
            <section class="info-card">
                <h2 class="card-title">회원 정보</h2>
                <dl class="info-list">
                    <div class="info-item">
                        <dt class="info-label">회원번호</dt>
                        <dd class="info-value"><c:out value="${user.userNum}" /></dd>
                    </div>
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
                                <c:when test="${empty user.email}">-</c:when>
                                <c:otherwise><c:out value="${user.email}" /></c:otherwise>
                            </c:choose>
                        </dd>
                    </div>
                    <div class="info-item">
                        <dt class="info-label">생년월일</dt>
                        <dd class="info-value"><fmt:formatDate value="${user.birthDt}" pattern="yyyy-MM-dd" /></dd>
                    </div>
                    <div class="info-item">
                        <dt class="info-label">권한</dt>
                        <dd class="info-value">
                            <c:choose>
                                <c:when test="${user.userRole == '02'}"><span class="badge role-admin">관리자</span></c:when>
                                <c:otherwise><span class="badge role-user">일반회원</span></c:otherwise>
                            </c:choose>
                        </dd>
                    </div>
                    <div class="info-item">
                        <dt class="info-label">상태</dt>
                        <dd class="info-value">
                            <c:choose>
                                <c:when test="${user.userStatus == '01'}"><span class="badge status-normal">정상</span></c:when>
                                <c:when test="${user.userStatus == '02'}"><span class="badge status-withdrawn">탈퇴</span></c:when>
                                <c:when test="${user.userStatus == '03'}"><span class="badge status-dormant">휴면</span></c:when>
                                <c:when test="${user.userStatus == '04'}"><span class="badge status-blocked">정지</span></c:when>
                                <c:otherwise><span class="badge"><c:out value="${user.userStatus}" /></span></c:otherwise>
                            </c:choose>
                        </dd>
                    </div>
                    <div class="info-item">
                        <dt class="info-label">가입일</dt>
                        <dd class="info-value"><fmt:formatDate value="${user.createDt}" pattern="yyyy-MM-dd HH:mm:ss" /></dd>
                    </div>
                    <div class="info-item">
                        <dt class="info-label">수정일</dt>
                        <dd class="info-value"><fmt:formatDate value="${user.updateDt}" pattern="yyyy-MM-dd HH:mm:ss" /></dd>
                    </div>
                    <div class="info-item">
                        <dt class="info-label">탈퇴일</dt>
                        <dd class="info-value"><fmt:formatDate value="${user.withdrawDt}" pattern="yyyy-MM-dd HH:mm:ss" /></dd>
                    </div>
                </dl>
            </section>

            <div class="stack">
                <section class="panel">
                    <h2 class="panel-title">회원상태 변경</h2>
                    <form class="form-grid admin-change-form" action="${CP}/admin/user/statusUpdate.do" method="post" data-confirm-message="선택한 회원상태로 변경하시겠습니까?">
                        <input type="hidden" name="userNum" value="${user.userNum}">
                        <div class="form-row">
                            <label class="label" for="userStatus">상태</label>
                            <select class="select" id="userStatus" name="userStatus">
                                <option value="01" ${user.userStatus == '01' ? 'selected' : ''}>정상</option>
                                <option value="02" ${user.userStatus == '02' ? 'selected' : ''}>탈퇴</option>
                                <option value="03" ${user.userStatus == '03' ? 'selected' : ''}>휴면</option>
                                <option value="04" ${user.userStatus == '04' ? 'selected' : ''}>정지</option>
                            </select>
                        </div>
                        <button class="btn primary" type="submit">상태 변경</button>
                        <p class="admin-result-message"><c:out value="${statusMsg}" /></p>
                    </form>
                </section>

                <section class="panel">
                    <h2 class="panel-title">회원권한 변경</h2>
                    <form class="form-grid admin-change-form" action="${CP}/admin/user/roleUpdate.do" method="post" data-confirm-message="선택한 회원권한으로 변경하시겠습니까?">
                        <input type="hidden" name="userNum" value="${user.userNum}">
                        <div class="form-row">
                            <label class="label" for="userRole">권한</label>
                            <select class="select" id="userRole" name="userRole">
                                <option value="01" ${user.userRole == '01' ? 'selected' : ''}>일반회원</option>
                                <option value="02" ${user.userRole == '02' ? 'selected' : ''}>관리자</option>
                            </select>
                        </div>
                        <button class="btn primary" type="submit">권한 변경</button>
                        <p class="admin-result-message"><c:out value="${roleMsg}" /></p>
                    </form>
                </section>
            </div>
        </div>
        </main>

        <jsp:include page="../../common/footer.jsp" />
        <jsp:include page="../../common/floatingBar.jsp" />
        <jsp:include page="../../common/mobileBottomNav.jsp" />
    </div>
</body>
</html>
