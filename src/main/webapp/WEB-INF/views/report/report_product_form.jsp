<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SPAM - 상품 신고하기</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/member.css">
    <style>
        /* 이진숙 팀원님의 메인 테마 적용 전, 임시로 정렬 및 가독성을 높이기 위한 내츄럴 CSS 최소화 */
        .report-container {
            max-width: 540px;
            margin: 40px auto;
            padding: 24px;
            background: #ffffff;
            border-radius: 8px;
            box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
        }
        .report-summary-box {
            background-color: #f8f9fa;
            border: 1px solid #e9ecef;
            border-radius: 6px;
            padding: 16px;
            margin-bottom: 24px;
        }
        .report-summary-box p {
            margin: 6px 0;
            font-size: 14px;
            color: #495057;
        }
        .report-summary-box strong {
            color: #212529;
        }
        .required-star {
            color: #dc3545;
            margin-left: 2px;
        }
        textarea.input {
            height: 140px;
            resize: none;
            padding: 12px;
            font-family: inherit;
        }
    </style>
</head>
<body class="auth-page">

    <main class="report-container">
        
        <header class="page-header" style="margin-bottom: 20px; border-bottom: none; padding-bottom: 0;">
            <div>
                <h1 class="page-title" style="font-size: 20px; color: #212529;">허위매물 및 상품 신고</h1>
                <p class="page-desc" style="margin-top: 4px;">해당 상품의 위반 사항을 선택하고 상세 사유를 적어주세요.</p>
            </div>
        </header>

        <div class="report-summary-box">
            <p><strong>신고 대상 상품 번호:</strong> <span><c:out value="${param.targetId}"/></span></p>
            <p><strong>피신고 회원 번호:</strong> <span><c:out value="${param.reportedUserNo}"/></span></p>
        </div>

        <form id="reportForm" action="${pageContext.request.contextPath}/report/doInsert.do" method="post" class="form-grid">
            
            <input type="hidden" name="targetId" value="${param.targetId}">
            <input type="hidden" name="reportedUserNo" value="${param.reportedUserNo}">
            <input type="hidden" name="reportType" value="${empty param.reportType ? 'PRODUCT' : param.reportType}">

			<div class="form-row">
				<label class="label" for="reportReasonSelect">신고 사유 선택<span
					class="required-star">*</span></label> <select class="select"
					id="reportReasonSelect" name="reason" required>
					<option value="">-- 상품 신고 사유를 선택하세요 --</option>
					<option value="판매금지 물품 등록">판매금지 물품 등록</option>
					<option value="전문 업자 의심">전문 업자 의심</option>
					<option value="허위성 매물">허위성 매물</option>
					<option value="카테고리 위반">카테고리 위반</option>
					<option value="음란물 및 유해콘텐츠 게시">음란물 및 유해콘텐츠 게시</option>
					<option value="광고 및 홍보 도배">광고 및 홍보 도배</option>
					<option value="불량 상품판매">불량 상품판매</option>
				</select>
			</div>

			<div class="form-row">
                <label class="label" for="reason">상세 내용 입력<span class="required-star">*</span></label>
                <textarea class="input" id="reason" name="reason" placeholder="신고 사유를 명확하게 작성해주시면 운영진 확인 후 빠르게 조치하겠습니다." required></textarea>
            </div>

            <div class="form-actions" style="margin-top: 24px; gap: 12px; display: flex;">
                <button type="button" class="btn outline" style="flex: 1;" onclick="history.back();">취소</button>
                <button type="submit" class="btn primary" style="flex: 2;">신고서 제출</button>
            </div>
            
        </form>
    </main>

    <script>
        // 셀렉트 박스 선택 시 textarea에 값을 자동으로 넣어주거나 제어하는 편의 로직
        function handleReasonChange(selectElement) {
            const textarea = document.getElementById('reason');
            const selectedValue = selectElement.value;
            
            if (selectedValue === "기타 (직접 입력)") {
                textarea.value = "";
                textarea.placeholder = "기타 신고 사유를 구체적으로 적어주세요.";
                textarea.focus();
            } else if (selectedValue !== "") {
                textarea.value = "[" + selectedValue + "] \n";
                textarea.placeholder = "추가적인 상세 내용이 있다면 기록해주세요.";
                textarea.focus();
            } else {
                textarea.value = "";
            }
        }

        // 폼 제출 전 간단한 Validation 체크
        document.getElementById('reportForm').addEventListener('submit', function(e) {
            const select = document.getElementById('reportReasonSelect');
            const textarea = document.getElementById('reason');
            
            if(!select.value) {
                alert('신고 사유를 선택해주세요.');
                select.focus();
                e.preventDefault();
                return;
            }
            
            if(!textarea.value.trim()) {
                alert('상세 내용을 입력해주세요.');
                textarea.focus();
                e.preventDefault();
                return;
            }
            
            if(!confirm('정말로 이 상품을 신고하시겠습니까?\n허위 신고일 경우 서비스 이용이 제한될 수 있습니다.')) {
                e.preventDefault();
            }
        });
    </script>
</body>
</html>