<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SPAM 관리자 회원목록</title>
    <link rel="stylesheet" href="${CP}/resources/css/index.css">
    <link rel="stylesheet" href="${CP}/resources/css/member.css">
    <script defer src="${CP}/resources/js/index.js"></script>
    <script defer src="${CP}/resources/js/member.js"></script>
</head>
<body>
    <div class="page-shell" id="top">
        <jsp:include page="../../common/header.jsp" />
        <jsp:include page="../../common/nav.jsp" />

        <main class="member-page-shell">
        <header class="page-header">
            <div>
                <a class="brand" href="${CP}/admin/user/list.do" aria-label="SPAM 관리자 회원목록">
                    <span class="brand-mark">SP</span>
                    <span>SPAM Admin</span>
                </a>
                <h1 class="page-title">관리자 회원목록</h1>
                <p class="page-desc">회원 검색, 페이징 조회, 상세 화면 이동을 처리합니다.</p>
            </div>
            <nav class="header-actions">
                <a class="btn outline" href="${CP}/admin/user/list.do">전체 목록</a>
                <a class="btn" href="${CP}/user/logout.do">로그아웃</a>
            </nav>
        </header>

        <section class="panel stack">
            <form class="search-box" action="${CP}/admin/user/list.do" method="get">
                <input type="hidden" name="pageNo" value="1">

                <select class="select" id="searchDiv" name="searchDiv" aria-label="검색 조건">
                    <option value="" ${empty searchDTO.searchDiv ? 'selected' : ''}>전체</option>
                    <option value="userId" ${searchDTO.searchDiv == 'userId' ? 'selected' : ''}>아이디</option>
                    <option value="userName" ${searchDTO.searchDiv == 'userName' ? 'selected' : ''}>이름</option>
                    <option value="nickname" ${searchDTO.searchDiv == 'nickname' ? 'selected' : ''}>닉네임</option>
                    <option value="phoneNum" ${searchDTO.searchDiv == 'phoneNum' ? 'selected' : ''}>전화번호</option>
                    <option value="email" ${searchDTO.searchDiv == 'email' ? 'selected' : ''}>이메일</option>
                    <option value="userStatus" ${searchDTO.searchDiv == 'userStatus' ? 'selected' : ''}>상태</option>
                </select>

                <input class="input" id="searchWord" type="text" name="searchWord" value="<c:out value='${searchDTO.searchWord}'/>" placeholder="검색어를 입력하세요.">

                <select class="select" name="pageSize" aria-label="페이지 크기">
                    <option value="10" ${searchDTO.pageSize == 10 ? 'selected' : ''}>10개</option>
                    <option value="20" ${searchDTO.pageSize == 20 ? 'selected' : ''}>20개</option>
                    <option value="50" ${searchDTO.pageSize == 50 ? 'selected' : ''}>50개</option>
                </select>

                <button class="btn primary" type="submit">검색</button>
            </form>

            <div class="meta-bar">
                <strong>총 회원 수: ${totalCount}</strong>
                <span>현재 페이지: ${searchDTO.pageNo}</span>
            </div>

            <div class="table-wrap">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>회원번호</th>
                            <th>아이디</th>
                            <th>이름</th>
                            <th>닉네임</th>
                            <th>전화번호</th>
                            <th>이메일</th>
                            <th>권한</th>
                            <th>상태</th>
                            <th>가입일</th>
                            <th>상세</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="user" items="${userList}">
                            <tr>
                                <td><c:out value="${user.userNum}" /></td>
                                <td><c:out value="${user.userId}" /></td>
                                <td><c:out value="${user.userName}" /></td>
                                <td><c:out value="${user.nickname}" /></td>
                                <td><c:out value="${user.phoneNum}" /></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${empty user.email}">-</c:when>
                                        <c:otherwise><c:out value="${user.email}" /></c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${user.userRole == '02'}"><span class="badge role-admin">관리자</span></c:when>
                                        <c:otherwise><span class="badge role-user">일반회원</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${user.userStatus == '01'}"><span class="badge status-normal">정상</span></c:when>
                                        <c:when test="${user.userStatus == '02'}"><span class="badge status-withdrawn">탈퇴</span></c:when>
                                        <c:when test="${user.userStatus == '03'}"><span class="badge status-dormant">휴면</span></c:when>
                                        <c:when test="${user.userStatus == '04'}"><span class="badge status-blocked">정지</span></c:when>
                                        <c:otherwise><span class="badge"><c:out value="${user.userStatus}" /></span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td><fmt:formatDate value="${user.createDt}" pattern="yyyy-MM-dd" /></td>
                                <td>
                                    <a class="btn outline" href="${CP}/admin/user/detail.do?userNum=${user.userNum}">상세</a>
                                </td>
                            </tr>
                        </c:forEach>

                        <c:if test="${empty userList}">
                            <tr>
                                <td class="empty-row" colspan="10">조회된 회원이 없습니다.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <div class="pagination">
                <c:if test="${searchDTO.pageNo > 1}">
                    <c:url var="prevUrl" value="/admin/user/list.do">
                        <c:param name="pageNo" value="${searchDTO.pageNo - 1}" />
                        <c:param name="pageSize" value="${searchDTO.pageSize}" />
                        <c:param name="searchDiv" value="${searchDTO.searchDiv}" />
                        <c:param name="searchWord" value="${searchDTO.searchWord}" />
                    </c:url>
                    <a class="btn outline" href="${prevUrl}">이전</a>
                </c:if>

                <c:if test="${searchDTO.pageNo * searchDTO.pageSize < totalCount}">
                    <c:url var="nextUrl" value="/admin/user/list.do">
                        <c:param name="pageNo" value="${searchDTO.pageNo + 1}" />
                        <c:param name="pageSize" value="${searchDTO.pageSize}" />
                        <c:param name="searchDiv" value="${searchDTO.searchDiv}" />
                        <c:param name="searchWord" value="${searchDTO.searchWord}" />
                    </c:url>
                    <a class="btn outline" href="${nextUrl}">다음</a>
                </c:if>
            </div>
        </section>
        </main>

        <jsp:include page="../../common/footer.jsp" />
        <jsp:include page="../../common/floatingBar.jsp" />
        <jsp:include page="../../common/mobileBottomNav.jsp" />
    </div>
</body>
</html>
