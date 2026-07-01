<%@page import="com.pcwk.ehr.user.domain.UserVO"%>
<%@page import="java.util.List"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    

    
<%
	List<UserVO>  list=(List<UserVO>)request.getAttribute("list");
	
	for(UserVO vo :list){
		//out.print(vo+"<br/>");
	}
%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원관리</title>
</head>
<body>
   <h2>회원목록</h2>
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
   	<% for(UserVO vo :list){ %>
	   	<tr>
	   		<td><%=vo.getNo() %></td>
			<td><%=vo.getUserId() %></td>
			<td><%=vo.getName() %></td>
			<td><%=vo.getLogin() %></td>
			<td><%=vo.getRecommend() %></td>
			<td><%=vo.getEmail() %></td>
			<td><%=vo.getGrade() %></td>
			<td><%=vo.getRegDt() %></td>
	   	</tr>   	
   	<% } %>
   	
   </table>
</body>
</html>