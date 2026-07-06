<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SPAM 관리자 회원상세</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/member.css">
    <script defer src="${pageContext.request.contextPath}/resources/js/member.js"></script>
</head>
<body>
    <main class="page-shell">
        <header class="page-header">
            <div>
                <a class="brand" href="${pageContext.request.contextPath}/admin/user/list.do" aria-label="SPAM 관리자 회원목록">
                    <span class="brand-mark">SP</span>
                    <span>SPAM Admin</span>
                </a>
                <h1 class="page-title">관리자 회원상세</h1>
                <p class="page-desc">회원 기본 정보 확인과 상태/권한 변경을 처리합니다.</p>
            </div>
            <nav class="header-actions">
                <a class="btn outline" href="${pageContext.request.contextPath}/admin/user/list.do">목록</a>
                <a class="btn" href="${pageContext.request.contextPath}/user/logout.do">로그아웃</a>
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
                    <form class="form-grid" action="${pageContext.request.contextPath}/admin/user/statusUpdate.do" method="post">
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
                    </form>
                </section>

                <section class="panel">
                    <h2 class="panel-title">회원권한 변경</h2>
                    <form class="form-grid" action="${pageContext.request.contextPath}/admin/user/roleUpdate.do" method="post">
                        <input type="hidden" name="userNum" value="${user.userNum}">
                        <div class="form-row">
                            <label class="label" for="userRole">권한</label>
                            <select class="select" id="userRole" name="userRole">
                                <option value="01" ${user.userRole == '01' ? 'selected' : ''}>일반회원</option>
                                <option value="02" ${user.userRole == '02' ? 'selected' : ''}>관리자</option>
                            </select>
                        </div>
                        <button class="btn primary" type="submit">권한 변경</button>
                    </form>
                </section>
            </div>
        </div>
    </main>
</body>
</html>
