<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>관리자 전체 조회</title>
    <style>
        * { box-sizing: border-box; }
        body, html { margin: 0; padding: 0; height: 100%; font-family: 'Noto Sans KR', sans-serif; background-color: #f4f5f7; }
        .admin-wrap { display: flex; min-height: 100vh; }
        .sidebar { width: 220px; background-color: #ffffff; border-right: 1px solid #e2e8f0; flex-shrink: 0; }
        .sidebar h2 { padding: 30px 20px; font-size: 18px; text-align: center; margin: 0; color: #1e293b; font-weight: 800; }
        .main-content { flex-grow: 1; padding: 40px; }
        
        /* 버튼 스타일 */
        .filter-btn {
            padding: 6px 16px;
            margin-right: 8px;
            text-decoration: none;
            color: #475569;
            background: #ffffff;
            border: 1px solid #cbd5e1;
            border-radius: 20px;
            font-size: 13px;
            transition: all 0.2s;
        }
        .filter-btn:hover { background: #f1f5f9; border-color: #94a3b8; }

        /* 테이블 스타일 */
        .styled-table { width: 100%; border-collapse: collapse; background: white; border-radius: 8px; overflow: hidden; }
        .styled-table th, .styled-table td { padding: 15px; border-bottom: 1px solid #e2e8f0; text-align: center; }
        .styled-table th { background-color: #f8fafc; color: #334155; }
    </style>
</head>
<body>

<div class="admin-wrap">
    <div class="sidebar">
        <h2>관리자 페이지</h2>
    </div>
    <div class="main-content">
        <h2 class="title">전체 상품 현황</h2>
        
        <div style="margin-bottom: 25px; padding-bottom: 15px; border-bottom: 1px solid #e2e8f0;">
            <a href="${pageContext.request.contextPath}/transact/list.do" class="filter-btn">전체</a>
            <a href="${pageContext.request.contextPath}/transact/list.do?status=01" class="filter-btn">판매중</a>
            <a href="${pageContext.request.contextPath}/transact/list.do?status=02" class="filter-btn">거래완료</a>
            <a href="${pageContext.request.contextPath}/transact/list.do?status=03" class="filter-btn">기타</a>
        </div>

        <div class="table-container">
            <table class="styled-table">
                <thead>
                    <tr>
                        <th style="width: 40%;">상품명</th>
                        <th style="width: 20%;">판매자 아이디</th>
                        <th style="width: 20%;">가격</th>
                        <th style="width: 10%;">상태</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="vo" items="${list}">
                        <tr>
                            <td style="text-align: left; padding-left: 20px;">${vo.productTitle}</td>
                            <td>${vo.sellerId}</td>
                            <td><fmt:formatNumber value="${vo.price}" pattern="#,###" />원</td>
                            <td>
                                <c:choose>
                                    <c:when test="${vo.status == '01'}">판매중</c:when>
                                    <c:when test="${vo.status == '02'}">거래완료</c:when>
                                    <c:otherwise>기타</c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

</body>
</html>