<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>

    <script>
        /* 회원가입 submit 전에 브라우저에서 1차 검증을 수행 */
        function validateJoinForm() {
            /* 비밀번호 입력값을 가져옴 */
            var password = document.getElementById('password').value;

            /* 비밀번호 확인 입력값을 가져옴 */
            var passwordConfirm = document.getElementById('passwordConfirm').value;

            /* 비밀번호와 비밀번호 확인이 다르면 회원가입 요청을 보내지 않음 */
            if (password !== passwordConfirm) {
                alert('비밀번호와 비밀번호 확인 값이 다릅니다.');
                document.getElementById('passwordConfirm').focus();
                return false;
            }

            /* true를 반환하면 form 정상 제출 */
            return true;
        }
    </script>
</head>
<body>
<h2>회원가입</h2>

<!--
    Controller에서 model.addAttribute("msg", "...")로 넘긴 안내/오류 메시지를 출력
    예:
    - 이미 사용 중인 아이디입니다.
    - 비밀번호와 비밀번호 확인 값이 다릅니다.
-->
<c:if test="${not empty msg}">
    <p style="color:red;">${msg}</p>
</c:if>

<!--
    회원가입 form

    action:
    - submit 시 /user/join.do로 POST 요청 전송

    onsubmit:
    - submit 직전에 validateJoinForm() 실행 
    - validateJoinForm()이 false를 반환하면 submit 중단
-->
<form action="${pageContext.request.contextPath}/user/join.do" method="post" onsubmit="return validateJoinForm();">
    <table border="1">
        <tr>
            <th>아이디 *</th>
            <td>
                <!-- name="userId"는 UserVO.setUserId()와 연결 -->
                <input type="text" name="userId" value="${user.userId}" maxlength="30" required>
            </td>
        </tr>
        <tr>
            <th>비밀번호 *</th>
            <td>
                <!-- name="password"는 UserVO.setPassword()와 연결 -->
                <input type="password" id="password" name="password" maxlength="100" required>
            </td>
        </tr>
        <tr>
            <th>비밀번호 확인 *</th>
            <td>
                <!-- passwordConfirm은 DB 컬럼이 아니라 확인용 파라미터/ Controller에서 @RequestParam으로 받음 -->
                <input type="password" id="passwordConfirm" name="passwordConfirm" maxlength="100" required>
            </td>
        </tr>
        <tr>
            <th>이름 *</th>
            <td>
                <!-- name="userName"은 UserVO.setUserName()과 연결 -->
                <input type="text" name="userName" value="${user.userName}" maxlength="7" required>
            </td>
        </tr>
        <tr>
            <th>닉네임</th>
            <td>
                <!-- 닉네임을 비워두면 Service에서 이름과 같은 값으로 저장합 -->
                <input type="text" name="nickname" value="${user.nickname}" maxlength="30">
            </td>
        </tr>
        <tr>
            <th>전화번호 *</th>
            <td>
                <!-- 전화번호는 01 정상/03 휴먼/04 정지 회원 기준 중복 금지 -->
                <input type="text" name="phoneNum" value="${user.phoneNum}" maxlength="13" placeholder="010-0000-0000" required>
            </td>
        </tr>
        <tr>
            <th>이메일</th>
            <td>
                <!-- 이메일은 선택값, 입력한 경우 01 정상/03 휴먼/04 정지 회원 기준 중복 금지 -->
                <input type="email" name="email" value="${user.email}" maxlength="100" placeholder="example@test.com">
            </td>
        </tr>
        <tr>
            <th>생년월일</th>
            <td>
                <!-- yyyy-MM-dd 형식으로 서버에 전달, UserVO.birthDt의 @DateTimeFormat과 연결 -->
                <input type="date" name="birthDt">
            </td>
        </tr>
    </table>

    <button type="submit">회원가입</button>
    <a href="${pageContext.request.contextPath}/user/login.do">로그인</a>
</form>
</body>
</html>
