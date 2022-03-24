<%@ include file="/WEB-INF/view/jstl.jsp" %>
  <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>




	<script>
		var err = '<c:out value="${err}" />';
		alert(err);
		history.back(-1);
	</script>	

