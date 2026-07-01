<%@page import="com.pcwk.ehr.user.domain.UserVO"%>
<%@page import="java.util.List"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원관리</title>

<!-- JavaScript Code -->
<script>
	//body안에 있는 모든 내용이 수행되면 실행
	document.addEventListener('DOMContentLoaded',function(){
		console.log('DOMContentLoaded ^^');
		
		const moveToRegButton = document.getElementById("moveToRegBtn");
		console.log(moveToRegButton); 
		
		//등록버튼 클릭: Event감지
		moveToRegButton.addEventListener('click',function(){
			console.log('moveToRegButton click ');
			
			if(confirm('등록 화면으로 이동 하시겠습니까?') === false){
				return;
			}
			
			//화면이동
			window.location.href = "/ehr/user/user_reg.do";
			
		});
	});     

</script>
<!-- JavaScript Code -->
</head>
<body>
   <h2>회원목록</h2>
   <div>
   	<input type="button" value="등록" id="moveToRegBtn">
   </div>
   <table border="1" >
   	<tr>
   		<th>NO</th>
		<th>사용자ID</th>
		<th>이름</th>
		<th>로그인</th>
		<th>추천</th>
		<th>이메일</th>
		<th>등급</th>
		<th>등록일</th>
   	</tr>
        <c:forEach var="vo" items="${list}">
		   	<tr>
		   		<td>${vo.getNo() }</td>
				<td>${vo.userId }</td>
				<td>${vo.name }</td>
				<td>${vo.login }</td>
				<td>${vo.recommend }</td>
				<td>${vo.email }</td>
				<td>${vo.grade }</td>
				<td>${vo.regDt }</td>
		   	</tr>
	   	</c:forEach>  
   	
   </table>
</body>
</html>