<%@ include file="/WEB-INF/view/jstl.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>

<%=new Date()%>
CopyLeft fearndare.
<a href="/view/home">home</a>
<a href="#" onclick="logout()">logout</a>

<script>
	function logout() {
		$.getJSON('/api/auth/logout', function(rsp, txtsts, jqXHR) {
			window.location.href = '/login';
		});
	}
</script>
