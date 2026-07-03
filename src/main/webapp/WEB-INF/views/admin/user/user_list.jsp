<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>관리자 회원목록</title>
</head>
<body>
<h2>관리자 회원목록</h2>

<!--
    관리자 회원 검색 form입니다.

    GET 방식으로 검색하는 이유:
    - 검색 조건이 URL에 남아서 새로고침/공유가 쉽습니다.
    - 단순 조회는 POST보다 GET이 자연스럽습니다.
-->
<form action="${pageContext.request.contextPath}/admin/user/list.do" method="get">
    <select name="searchDiv">
        <option value="" ${empty searchDTO.searchDiv ? 'selected' : ''}>전체</option>
        <option value="userId" ${searchDTO.searchDiv == 'userId' ? 'selected' : ''}>아이디</option>
        <option value="userName" ${searchDTO.searchDiv == 'userName' ? 'selected' : ''}>이름</option>
        <option value="nickname" ${searchDTO.searchDiv == 'nickname' ? 'selected' : ''}>닉네임</option>
        <option value="phoneNum" ${searchDTO.searchDiv == 'phoneNum' ? 'selected' : ''}>전화번호</option>
        <option value="email" ${searchDTO.searchDiv == 'email' ? 'selected' : ''}>이메일</option>
        <option value="userStatus" ${searchDTO.searchDiv == 'userStatus' ? 'selected' : ''}>상태</option>
    </select>

    <input type="text" name="searchWord" value="${searchDTO.searchWord}">

    <select name="pageSize">
        <option value="10" ${searchDTO.pageSize == 10 ? 'selected' : ''}>10개</option>
        <option value="20" ${searchDTO.pageSize == 20 ? 'selected' : ''}>20개</option>
        <option value="50" ${searchDTO.pageSize == 50 ? 'selected' : ''}>50개</option>
    </select>

    <button type="submit">검색</button>
</form>

<p>총 회원 수: ${totalCount}</p>

<table border="1">
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
        <!--
            userList는 AdminUserController에서 model.addAttribute("userList", userList)로 넘긴 값입니다.
        -->
        <c:forEach var="user" items="${userList}">
            <tr>
                <td>${user.userNum}</td>
                <td>${user.userId}</td>
                <td>${user.userName}</td>
                <td>${user.nickname}</td>
                <td>${user.phoneNum}</td>
                <td>${user.email}</td>

                <!--
                    DB에는 01/02 권한 공통코드로 저장하지만 화면에는 한글로 보여줍니다.
                -->
                <td>
                    <c:choose>
                        <c:when test="${user.userRole == '02'}">관리자</c:when>
                        <c:otherwise>일반회원</c:otherwise>
                    </c:choose>
                </td>

                <!--
                    DB에는 01/02/03/04 상태 공통코드로 저장하지만 화면에는 한글로 보여줍니다.
                -->
                <td>
                    <c:choose>
                        <c:when test="${user.userStatus == '01'}">정상</c:when>
                        <c:when test="${user.userStatus == '02'}">탈퇴</c:when>
                        <c:when test="${user.userStatus == '03'}">휴먼</c:when>
                        <c:when test="${user.userStatus == '04'}">정지</c:when>
                        <c:otherwise>${user.userStatus}</c:otherwise>
                    </c:choose>
                </td>

                <td><fmt:formatDate value="${user.createDt}" pattern="yyyy-MM-dd" /></td>
                <td>
                    <a href="${pageContext.request.contextPath}/admin/user/detail.do?userNum=${user.userNum}">상세</a>
                </td>
            </tr>
        </c:forEach>

        <!-- 회원목록이 비어 있을 때 표시합니다. -->
        <c:if test="${empty userList}">
            <tr>
                <td colspan="10">조회된 회원이 없습니다.</td>
            </tr>
        </c:if>
    </tbody>
</table>

<p>현재 페이지: ${searchDTO.pageNo}</p>
</body>
</html>
