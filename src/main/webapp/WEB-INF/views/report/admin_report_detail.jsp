<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 - 신고 상세 내역</title>
<style>
    :root {
        --primary-color: #4e73df;
        --success-color: #1cc88a;
        --info-color: #36b9cc;
        --warning-color: #f6c23e;
        --danger-color: #e74a3b;
        --bg-color: #f8f9fc;
        --card-border: #e3e6f0;
        --text-main: #4e5565;
        --text-heading: #2e3440;
    }

    body {
        background-color: var(--bg-color);
        font-family: 'Noto Sans KR', sans-serif;
        color: var(--text-main);
        margin: 0;
        padding: 40px 20px;
    }

    .container {
        max-width: 1100px;
        margin: 0 auto;
    }

    /* 페이지 타이틀 */
    h1 {
        font-size: 28px;
        color: var(--text-heading);
        margin-bottom: 30px;
        font-weight: 700;
        border-left: 5px solid var(--primary-color);
        padding-left: 15px;
    }

    /* 2단 분할 레이아웃 메인 그리드 */
    .detail-grid {
        display: grid;
        grid-template-columns: 1.2fr 0.8fr;
        gap: 25px;
        align-items: start;
    }

    @media (max-width: 900px) {
        .detail-grid {
            grid-template-columns: 1fr;
        }
    }

    /* 세련된 카드 스타일 공통 */
    .card {
        background: #ffffff;
        border: 1px solid var(--card-border);
        border-radius: 12px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.03);
        padding: 25px;
        margin-bottom: 25px;
    }

    .card-title {
        font-size: 18px;
        font-weight: 600;
        color: var(--text-heading);
        margin-top: 0;
        margin-bottom: 20px;
        padding-bottom: 10px;
        border-bottom: 2px solid #eaecf1;
    }

    /* 정돈된 테이블 스타일 */
    .info-table {
        width: 100%;
        border-collapse: collapse;
    }

    .info-table th, .info-table td {
        padding: 14px 12px;
        border-bottom: 1px solid #f1f3f7;
        font-size: 15px;
        text-align: left;
    }

    .info-table th {
        width: 130px;
        color: #858796;
        font-weight: 500;
        background-color: #fafbfc;
    }

    .info-table td {
        color: var(--text-heading);
    }

    /* 내용 텍스트 박스 */
    .reason-box {
        background-color: #fdfefe;
        border: 1px solid #e1e4e8;
        border-radius: 6px;
        padding: 12px;
        min-height: 100px;
        white-space: pre-wrap;
        word-break: break-all;
        line-height: 1.6;
    }

    /* 상태 관리 배지 */
    .badge {
        display: inline-block;
        padding: 5px 12px;
        font-size: 12px;
        font-weight: 600;
        border-radius: 30px;
    }
    .badge-waiting { background-color: #f1f3f5; color: #6c757d; }
    .badge-processing { background-color: #e0f2fe; color: #0369a1; }
    .badge-completed { background-color: #dcfce7; color: #15803d; }

    /* 폼 요소 스타일링 */
    .form-group {
        margin-bottom: 20px;
    }

    .form-group label {
        display: block;
        font-size: 14px;
        font-weight: 500;
        margin-bottom: 8px;
        color: #6e707e;
    }

    .form-control {
        width: 100%;
        padding: 10px 12px;
        font-size: 14px;
        border: 1px solid #d1d3e2;
        border-radius: 6px;
        box-sizing: border-box;
        color: var(--text-heading);
        transition: border-color 0.15s ease-in-out;
    }

    .form-control:focus {
        outline: none;
        border-color: var(--primary-color);
        box-shadow: 0 0 0 3px rgba(78, 115, 223, 0.15);
    }

    select.form-control {
        height: 42px;
    }

    textarea.form-control {
        resize: vertical;
        min-height: 120px;
    }

    /* 하단 버튼 인터페이스 */
    .btn-area {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-top: 20px;
    }

    .btn {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        padding: 11px 22px;
        font-size: 15px;
        font-weight: 500;
        border-radius: 6px;
        cursor: pointer;
        transition: all 0.2s ease;
        text-decoration: none;
        border: none;
    }

    .btn-primary {
        background-color: var(--primary-color);
        color: white;
        width: 100%; /* 우측 카드 안 버튼은 꽉 차게 */
    }

    .btn-primary:hover {
        background-color: #2e59d9;
    }

    .btn-secondary {
        background-color: #eaecf1;
        color: #4d5156;
    }

    .btn-secondary:hover {
        background-color: #dcdfe6;
    }
</style>
</head>
<body>

<div class="container">
    <h1>신고 상세 내역 조회</h1>

    <div class="detail-grid">
        <div class="card">
            <h2 class="card-title">🚨 신고 접수 정보</h2>
            
            <table class="info-table">
                <tr>
                    <th>신고 번호</th>
                    <td><strong>${outVO.reportNo}</strong></td>
                </tr>
                <tr>
                    <th>신고 유형</th>
                    <td>
                        <c:choose>
                            <c:when test="${outVO.reportType == 'USER'}">👤 사용자 비매너 신고</c:when>
                            <c:when test="${outVO.reportType == 'PRODUCT'}">📦 상품 위반 신고</c:when>
                            <c:otherwise>${outVO.reportType}</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <th>신고자 닉네임</th>
    				<%-- 💡 outVO.reporterNickname이 비어있으면 번호를, 있으면 닉네임을 출력하도록 변경 --%>
				    <td>${empty outVO.reporterNickname ? outVO.reporterNo : outVO.reporterNickname}</td>
                </tr>
                <tr>
    				<th>피신고자 닉네임</th>
    				<td>
	        			<c:choose>
            			<%-- 💡 숫자가 아닌 닉네임 변수(reportedNickname)가 비어있는지 확인하고 출력하도록 변경 --%>
	            			<c:when test="${empty outVO.reportedNickname}">-</c:when>
            				<c:otherwise>${outVO.reportedNickname}</c:otherwise>
        				</c:choose>
    				</td>
				</tr>
                <tr>
                    <th>대상 ID (PK)</th>
                    <td>${outVO.targetId}</td>
                </tr>
                <tr>
                    <th>신고 일시</th>
                    <td>${outVO.createDt}</td>
                </tr>
                <tr>
                    <th>신고 사유(상세)</th>
                    <td>
                        <div class="reason-box">${outVO.reason}</div>
                    </td>
                </tr>
            </table>
        </div>

        <div>
            <div class="card">
                <h2 class="card-title">⚙️ 처리 상태 관리</h2>
                
                <form action="${pageContext.request.contextPath}/report/doUpdateStatus.do" method="post">
                    <input type="hidden" name="reportNo" value="${outVO.reportNo}">
                    
                    <div class="form-group">
                        <label>현재 진행 상태</label>
                        <div style="margin-bottom: 15px;">
                            <c:choose>
                                <c:when test="${outVO.reportStatus == '01'}"><span class="badge badge-waiting">처리 대기</span></c:when>
                                <c:when test="${outVO.reportStatus == '02'}"><span class="badge badge-processing">접수 / 진행중</span></c:when>
                                <c:when test="${outVO.reportStatus == '03'}"><span class="badge badge-completed">처리 완료</span></c:when>
                                <c:otherwise><span class="badge badge-waiting">${outVO.reportStatus}</span></c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="reportStatus">상태 변경하기</label>
                        <select name="reportStatus" id="reportStatus" class="form-control">
                            <option value="01" ${outVO.reportStatus == '01' ? 'selected' : ''}>처리 대기 (01)</option>
                            <option value="02" ${outVO.reportStatus == '02' ? 'selected' : ''}>접수 / 진행중 (02)</option>
                            <option value="03" ${outVO.reportStatus == '03' ? 'selected' : ''}>처리 완료 (03)</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>최종 처리 관리자 번호</label>
                        <input type="text" class="form-control" value="${empty outVO.adminNo ? '미처리' : outVO.adminNo}" disabled>
                    </div>

                    <div class="form-group">
                        <label>최종 처리 일시</label>
                        <input type="text" class="form-control" value="${empty outVO.processDt ? '미처리' : outVO.processDt}" disabled>
                    </div>

                    <button type="submit" class="btn btn-primary" style="margin-top: 10px;">💾 처리 상태 저장</button>
                </form>
            </div>
            
            <div class="btn-area">
                <a href="${pageContext.request.contextPath}/report/admin_doRetrieve.do" class="btn btn-secondary">📋 전체 신고 목록으로</a>
            </div>
        </div>
    </div>
</div>

</body>
</html>