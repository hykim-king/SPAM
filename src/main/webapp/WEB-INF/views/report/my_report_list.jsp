<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SPAM - 나의 신고 내역</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/member.css">
    <style>
        * { box-sizing: border-box; }
        .report-list-container {
            max-width: 900px;
            margin: 40px auto;
            padding: 24px;
            background: #ffffff;
            border-radius: 8px;
            box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
        }
        .page-title {
            font-size: 22px;
            font-weight: bold;
            margin-bottom: 24px;
            color: #333;
        }
        /* 탭 스타일 */
        .tab-menu {
            display: flex;
            border-bottom: 2px solid #e9ecef;
            margin-bottom: 20px;
        }
        .tab-item {
            padding: 12px 24px;
            font-size: 16px;
            font-weight: 600;
            color: #666;
            cursor: pointer;
            border-bottom: 2px solid transparent;
            margin-bottom: -2px;
            transition: all 0.2s ease;
        }
        .tab-item.active {
            color: #ff4d4f; /* SPAM 프로젝트 포인트 컬러 */
            border-bottom: 2px solid #ff4d4f;
        }
        /* 테이블 스타일 */
        .report-table-panel {
            display: none;
        }
        .report-table-panel.active {
            display: block;
        }
        .report-table {
            width: 100%;
            border-collapse: collapse;
            text-align: left;
        }
        .report-table th, .report-table td {
            padding: 14px 12px;
            border-bottom: 1px solid #e9ecef;
            font-size: 14px;
        }
        .report-table th {
            background-color: #f8f9fa;
            color: #495057;
            font-weight: 600;
        }
        .report-table tbody tr {
            cursor: pointer;
            transition: background 0.2s;
        }
        .report-table tbody tr:hover {
            background-color: #fdf2f2; /* 마우스 호버 시 살짝 붉은 도는 효과 */
        }
        /* 상태 배지 스타일 */
        .status-badge {
            display: inline-block;
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: bold;
        }
        .status-01 { background-color: #e9ecef; color: #495057; } /* 접수완료/처리대기 */
        .status-02 { background-color: #e3faf2; color: #0ca678; } /* 처리완료 */
        .status-03 { background-color: #fff0f6; color: #d6336c; } /* 반려 (필요 시 유지) */
        
        .empty-msg {
            text-align: center;
            padding: 40px 0;
            color: #999;
            font-size: 15px;
        }
    </style>
</head>
<body>

<div class="report-list-container">
    <h1 class="page-title">🚨 나의 신고 내역 관리</h1>
    
    <div class="tab-menu">
        <div class="tab-item active" onclick="switchTab('my-reports')">내가 신고한 목록</div>
        <div class="tab-item" onclick="switchTab('received-reports')">내가 신고 당한 목록</div>
    </div>
    
    <%-- 섹션 1: 내가 신고한 목록 --%>
    <div id="my-reports" class="report-table-panel active">
        <table class="report-table">
            <thead>
                <tr>
                    <th style="width: 7%;">번호</th>
                    <th style="width: 12%;">신고유형</th>
                    <th style="width: 45%;">신고사유(상세)</th>
                    <th style="width: 21%;">신고일자</th>
                    <th style="width: 15%;">처리상태</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty myReportList}">
                        <c:forEach var="report" items="${myReportList}" varStatus="status">
                            <tr onclick="goDetail('${report.reportNo}')">
                                <td>${status.count}</td> <%-- 💡 reportStatus.count를 status.count로 수정 완료! --%>
                                <td><strong>${report.reportType}</strong></td>
                                <td>
    								<a href="${pageContext.request.contextPath}/report/doSelectOne.do?reportNo=${report.reportNo}" 
								       style="text-decoration: none; color: inherit; display: block;">
								        <c:choose>
								            <c:when test="${report.reason.length() > 30}">
								                <c:out value="${report.reason.substring(0, 30)}..."/>
								            </c:when>
								            <c:otherwise>
								                <c:out value="${report.reason}"/>
								            </c:otherwise>
								        </c:choose>
								    </a>
								</td>
                                <td>${report.createDt}</td>
                                <td>
                                    <%-- 💡 DB 코드 규칙이 '01', '02' 형태이므로 맞춰서 바인딩할 수 있도록 유연하게 설계 --%>
                                    <span class="status-badge status-${report.reportStatus}">
                                        <c:choose>
                                            <c:when test="${report.reportStatus == '01' || report.reportStatus == '0'}">처리대기</c:when>
                                            <c:when test="${report.reportStatus == '02' || report.reportStatus == '1'}">처리완료</c:when>
                                            <c:otherwise>반려</c:otherwise>
                                        </c:choose>
                                    </span>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="5" class="empty-msg">내가 접수한 신고 내역이 없습니다.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
    
    <%-- 섹션 2: 내가 신고 당한 목록 --%>
    <div id="received-reports" class="report-table-panel">
        <table class="report-table">
            <thead>
                <tr>
                    <th style="width: 7%;">번호</th>
                    <th style="width: 12%;">신고유형</th>
                    <th style="width: 60%;">신고사유(상세)</th>
                    <th style="width: 21%;">신고일자</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty receivedReportList}">
                        <c:forEach var="received" items="${receivedReportList}" varStatus="status">
                            <tr onclick="goDetail('${received.reportNo}')">
                                <td>${status.count}</td> 
                                <td><span style="color: #ff4d4f;">${received.reportType}</span></td>
                                <td><c:out value="${received.reason}"/></td>
                                <td>${received.createDt}</td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="4" class="empty-msg">피신고(신고 당한) 내역이 없습니다. 깨끗한 매너 유저이시군요! 🎉</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</div>

<script>
    // 1. 탭 전환 스크립트
    function switchTab(tabId) {
        // 모든 탭 아이템 active 제거
        document.querySelectorAll('.tab-menu .tab-item').forEach(tab => {
            tab.classList.remove('active');
        });
        // 모든 테이블 패널 숨기기
        document.querySelectorAll('.report-table-panel').forEach(panel => {
            panel.classList.remove('active');
        });
        
        // 클릭한 탭과 패널 활성화
        event.currentTarget.classList.add('active');
        document.getElementById(tabId).classList.add('active');
    }

    // 2. 상세 페이지 이동 스크립트
    function goDetail(reportNo) {
        if(!reportNo) return;
        // 상세조회 Get 매핑 주소로 번호를 파라미터로 들고 이동
        location.href = "${pageContext.request.contextPath}/report/doSelectOne.do?reportNo=" + reportNo;
    }
</script>

</body>
</html>