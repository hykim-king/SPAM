<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // 2026-07-13 [수정] 루트(/ehr) 최초 접속 시 메인화면으로 이동한다.
    response.sendRedirect(request.getContextPath() + "/main.do");
%>
