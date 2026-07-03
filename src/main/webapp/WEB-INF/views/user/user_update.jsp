<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>회원정보 수정</title>

    <script>
        /*
            회원탈퇴 영역 제어 JavaScript입니다.

            현재 방식:
            1. 처음에는 [회원탈퇴] 버튼만 보입니다.
            2. 버튼을 누르면 비밀번호 확인 영역이 나타납니다.
            3. [탈퇴 진행]을 누르면 비밀번호 입력 여부 확인 후 confirm 창을 띄웁니다.
            4. 사용자가 confirm에서 확인을 누르면 서버로 POST 요청을 보냅니다.
        */

        function showWithdrawArea() {
            /*
                withdrawStartArea:
                - 처음에 보이는 [회원탈퇴] 버튼 영역입니다.
                - 탈퇴 확인 영역을 보여줄 때는 이 영역을 숨깁니다.
            */
            document.getElementById('withdrawStartArea').style.display = 'none';

            /*
                withdrawConfirmArea:
                - 비밀번호 확인 입력칸과 [탈퇴 진행], [취소] 버튼이 있는 영역입니다.
                - 처음에는 숨겨져 있다가 회원탈퇴 버튼을 누르면 보입니다.
            */
            document.getElementById('withdrawConfirmArea').style.display = 'block';

            /*
                사용자가 바로 비밀번호를 입력할 수 있도록 커서를 이동합니다.
            */
            document.getElementById('withdrawPassword').focus();
        }

        function hideWithdrawArea() {
            /*
                사용자가 탈퇴를 취소하면 비밀번호 확인 영역을 다시 숨깁니다.
            */
            document.getElementById('withdrawConfirmArea').style.display = 'none';

            /*
                처음에 보였던 [회원탈퇴] 버튼 영역을 다시 보여줍니다.
            */
            document.getElementById('withdrawStartArea').style.display = 'block';

            /*
                입력했던 비밀번호는 화면에 남기지 않도록 지웁니다.
            */
            document.getElementById('withdrawPassword').value = '';
        }

        function confirmWithdraw() {
            /*
                탈퇴 확인용 비밀번호 입력값을 가져옵니다.
            */
            var password = document.getElementById('withdrawPassword').value;

            /*
                비밀번호를 입력하지 않았으면 서버로 보내지 않습니다.
            */
            if (password == null || password.trim() === '') {
                alert('회원탈퇴를 진행하려면 비밀번호를 입력하세요.');
                document.getElementById('withdrawPassword').focus();
                return false;
            }

            /*
                마지막 확인 창입니다.
                true이면 form submit이 진행되고,
                false이면 submit이 취소됩니다.
            */
            return confirm('정말 탈퇴하시겠습니까?\n탈퇴 후 계정 상태가 탈퇴로 변경되며 로그인이 제한됩니다.');
        }
    </script>
</head>
<body>
<h2>회원정보 수정</h2>

<!-- Controller가 전달한 안내/오류 메시지를 표시 -->
<c:if test="${not empty msg}">
    <p style="color:red;">${msg}</p>
</c:if>

<!-- Date 값을 yyyy-MM-dd 문자열로 변환 -->
<fmt:formatDate value="${user.birthDt}" pattern="yyyy-MM-dd" var="birthDtText" />

<!--
    회원 기본정보 수정 form

    - 수정 가능 항목: 이름, 닉네임, 전화번호, 이메일, 생년월일
    - 수정 불가 항목: 아이디, 권한, 상태
-->
<form action="${pageContext.request.contextPath}/user/update.do" method="post">
    <!--
        hidden userNum은 화면에서 보내지만, Controller에서 세션 userNum으로 다시 덮어씀
        ㄴ 사용자가 개발자도구로 userNum을 바꾸는 조작을 막기 위해
    -->
    <input type="hidden" name="userNum" value="${user.userNum}">

    <table border="1">
        <tr>
            <th>아이디</th>
            <td>
                ${user.userId}
                <span style="color:gray;">변경 불가</span>
            </td>
        </tr>
        <tr>
            <th>이름 *</th>
            <td>
                <input type="text" name="userName" value="${user.userName}" maxlength="7" required>
            </td>
        </tr>
        <tr>
            <th>닉네임</th>
            <td>
                <input type="text" name="nickname" value="${user.nickname}" maxlength="30">
            </td>
        </tr>
        <tr>
            <th>전화번호 *</th>
            <td>
                <input type="text" name="phoneNum" value="${user.phoneNum}" maxlength="13" placeholder="010-0000-0000" required>
            </td>
        </tr>
        <tr>
            <th>이메일</th>
            <td>
                <input type="email" name="email" value="${user.email}" maxlength="100" placeholder="example@test.com">
            </td>
        </tr>
        <tr>
            <th>생년월일</th>
            <td>
                <input type="date" name="birthDt" value="${birthDtText}">
            </td>
        </tr>
    </table>

    <button type="submit">수정</button>
    <a href="${pageContext.request.contextPath}/user/mypage.do">취소</a>
</form>

<hr>

<h3>비밀번호 변경</h3>

<!--
    비밀번호 변경 form

    - currentPassword: 현재 비밀번호 확인
	- newPassword: 새로 저장할 비밀번호
-->
<form action="${pageContext.request.contextPath}/user/password.do" method="post">
    현재 비밀번호
    <input type="password" name="currentPassword" autocomplete="current-password" required>

    새 비밀번호
    <input type="password" name="newPassword" autocomplete="new-password" required>

    <button type="submit">비밀번호 변경</button>
</form>

<hr>

<h3>회원탈퇴</h3>

<div id="withdrawStartArea" style="display:${withdrawError ? 'none' : 'block'};">
    <button type="button" onclick="showWithdrawArea();">회원탈퇴</button>
</div>

<div id="withdrawConfirmArea" style="display:${withdrawError ? 'block' : 'none'};">
    <form action="${pageContext.request.contextPath}/user/withdraw.do" method="post" onsubmit="return confirmWithdraw();">
        비밀번호 확인
        <input type="password" id="withdrawPassword" name="password" autocomplete="current-password" required>

        <button type="submit">탈퇴 진행</button>
        <button type="button" onclick="hideWithdrawArea();">취소</button>
    </form>
</div>

</body>
</html>
