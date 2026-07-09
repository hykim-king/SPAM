<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SPAM - 사용자 신고하기</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/member.css">
    <style>
    	* {
    		box-sizing: border-box;
    	}
        /* 메인 테마 적용 전, 임시로 정렬 및 가독성을 높이기 위한 내츄럴 CSS 최소화 */
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
        .form-row {
        	position:relative;
        }
        .textarea-wrapper {
    		width: 100%;
    		position: relative;
        }
        textarea.input {	
    		width: 100%;
            height: 140px;
            padding: 12px 12px 30px 12px; /* 카운터 글씨와 입력 텍스트가 겹치지 않도록 아래쪽 여백 확보 */
            resize: none;
            font-family: inherit;
    		font-size: 14px;
    		border: 1px solid #ccc;
    		border-radius: 4px;
        }
        .counter-container {
 			position: absolute;
    		bottom: -12px;
    		right: 0px;
		    font-size: 12px;
    		color: #666666;
            pointer-events: none; /* 카운터 영역이 마우스 클릭을 방해하지 않도록 설정 */
		}
		
		/* 버튼 비활성화 상태일 때의 스타일 정의 */
		.btn.primary:disabled {
			background-color: #cccccc !important;
			border-color: #cccccc !important;
			color: #ffffff !important;
			cursor: not-allowed !important;
		}
    </style>
</head>
<body class="auth-page">

    <main class="report-container">
        
        <header class="page-header" style="margin-bottom: 20px; border-bottom: none; padding-bottom: 0;">
            <div>
                <h1 class="page-title" style="font-size: 20px; color: #212529;">사용자 비매너 및 이용 위반 신고</h1>
                <p class="page-desc" style="margin-top: 4px;">해당 회원의 위반 사항을 선택하고 상세 사유를 적어주세요.</p>
            </div>
        </header>

        <div class="report-summary-box">
            <p><strong>피신고 회원 번호:</strong> <span><c:out value="${param.reportedUserNo}"/></span></p>
            <c:if test="${not empty param.targetId}">
                <p><strong>연관 상품 번호:</strong> <span><c:out value="${param.targetId}"/></span></p>
            </c:if>
        </div>

        <form id="reportForm" action="${pageContext.request.contextPath}/report/doInsert.do" method="post" class="form-grid">
            
            <input type="hidden" name="targetId" value="${param.targetId}">
            <input type="hidden" name="reportedUserNo" value="${param.reportedUserNo}">
            <input type="hidden" name="reportType" value="${empty param.reportType ? 'USER' : param.reportType}">

			<div class="form-row">
				<label class="label" for="reportReasonSelect">신고 사유 선택<span class="required-star">*</span></label> 
				<select class="select" id="reportReasonSelect" name="reason" onchange="handleReasonChange(this)" required>
					<option value="">-- 유저 신고 사유를 선택하세요 --</option>
                    <option value="상품 미배송">상품 미배송</option>
					<option value="외부 결제 유도">외부 결제 유도</option>
					<option value="노쇼">노쇼</option>
					<option value="거래파기">거래파기</option>
					<option value="욕설 및 언어폭력">욕설 및 언어폭력</option>
					<option value="개인정보 침해 및 유출">개인정보 침해 및 유출</option>
					<option value="계정도용">계정도용</option>
                    <option value="기타 (직접 입력)">기타 (직접 입력)</option>
				</select>
			</div>

			<div class="form-row">
                <label class="label" for="reason">상세 내용 입력<span class="required-star">*</span></label>
                <div class="textarea-wrapper">
                    <textarea class="input" id="reason" name="reason" placeholder="신고 사유를 명확하게 작성해주시면 운영진 확인 후 빠르게 조치하겠습니다." maxlength="1000" required></textarea>
               	</div>
               	<div class="counter-container">
            		<span id="charCount">0</span>/1000
		        </div>
            </div>

            <div class="form-actions" style="margin-top: 24px; gap: 12px; display: flex;">
                <button type="button" class="btn outline" style="flex: 1;" onclick="history.back();">취소</button>
                <button type="submit" id="submitBtn" class="btn primary" style="flex: 2;" disabled>신고서 제출</button>
            </div>
            
        </form>
    </main>

    <script>
		// 버튼 활성화/비활성화 상태를 실시간으로 체크하는 함수
		function checkSubmitButtonStatus() {
		    const select = document.getElementById('reportReasonSelect');
		    const textarea = document.getElementById('reason');
		    const submitBtn = document.getElementById('submitBtn');
		    
		    if (!submitBtn) return;
		    
		    const selectValue = select ? select.value : "";
		    const textLength = textarea ? textarea.value.trim().length : 0; 
		    const rawLength = textarea ? textarea.value.length : 0;
		    
		    if (selectValue === "" || textLength === 0 || rawLength >= 1000) {
		        submitBtn.disabled = true;
		    } else {
		        submitBtn.disabled = false;
		    }
		}

        // 셀렉트 박스 선택 시 textarea에 값을 자동으로 넣어주거나 제어하는 편의 로직
        function handleReasonChange(selectElement) {
            const textarea = document.getElementById('reason');
            const selectedValue = selectElement.value;
            const charCount = document.getElementById('charCount');
            
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
            
            if (charCount) {
                charCount.textContent = textarea.value.length;
            }
            
            checkSubmitButtonStatus();
        }
        
        document.addEventListener("DOMContentLoaded", function() {
            const textarea = document.getElementById('reason');
            const charCount = document.getElementById('charCount');
            const select = document.getElementById('reportReasonSelect');
            const form = document.getElementById('reportForm');

            if (textarea && charCount) {
                textarea.addEventListener('input', function() {
                    const currentLength = textarea.value.length;
                    
                    charCount.textContent = currentLength;
                    
                    if (currentLength >= 900) {
                        charCount.style.color = 'red';
                        charCount.style.fontWeight = 'bold';
                    } else {
                        charCount.style.color = '#666';
                        charCount.style.fontWeight = 'normal';
                    }
                    
                    checkSubmitButtonStatus();
                });
            }

            if (form) {
                form.addEventListener('submit', function(e) {
                    if(document.getElementById('submitBtn').disabled) {
                        e.preventDefault();
                        return;
                    }
                    
                    // 💡 컨펌 창 메시지도 '회원' 단위에 맞게 수정
                    if(!confirm('정말로 이 회원을 신고하시겠습니까?\n허위 신고일 경우 서비스 이용이 제한될 수 있습니다.')) {
                        e.preventDefault();
                    }
                });
            }
            
            checkSubmitButtonStatus();
        });
    </script>
</body>
</html>