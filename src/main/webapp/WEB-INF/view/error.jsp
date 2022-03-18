<%@ page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ko">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<style TYPE="text/css">
		table, th, td, pre {border: 1px solid #d7d7d7; border-collapse:collapse; font-size:12px;}
	</style>
</head>
<body>
<h2><%= request.getAttribute("javax.servlet.error.status_code") %> Error</h2>
<h3><%= request.getAttribute("org.springframework.boot.web.servlet.error.ErrorAttributes.error") %></h3>
<p/>
<%
	try {
		out.println("<pre>");
		// The Servlet spec guarantees this attribute will be available
		Throwable exception = (Throwable) request.getAttribute("javax.servlet.error.exception");

		if (exception != null) {
			if (exception instanceof ServletException) {
				// It's a ServletException: we should extract the root cause
				ServletException sex = (ServletException) exception;
				Throwable rootCause = sex.getRootCause();
				if (rootCause == null)
					rootCause = sex;
				out.println("** Root cause is: " + rootCause.getMessage());
				rootCause.printStackTrace(new java.io.PrintWriter(out));
			}
			else {
				// It's not a ServletException, so we'll just show it
				exception.printStackTrace(new java.io.PrintWriter(out));
			}
		}
		else {
			out.println("No available error information");
		}
		out.println("</pre>");
		out.println("<hr/>");
		

		// display request headers
		out.println("<h4>Request Headers:</h4>");
		out.println("<table>");
		Enumeration<String> henm = request.getHeaderNames();
		while (henm.hasMoreElements()) {
			String key = henm.nextElement();
			out.println("<tr><td>" + key + "</td><td>" + request.getHeader(key) + "</td></tr>");
		}
		out.println("</table>");
		out.println("<hr/>");
		

		// display request parameters
		out.println("<h4>Request Parameters:</h4>");
		out.println("<table>");
		Enumeration<String> penm = request.getParameterNames();
		while (penm.hasMoreElements()) {
			String key = penm.nextElement();
			out.println("<tr><td>" + key + "</td><td>" + request.getParameter(key) + "</td></tr>");
		}
		out.println("</table>");
		out.println("<hr/>");


		// Display cookies
		out.println("<h4>Cookies:</h4>");
		out.println("<pre>");
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				out.println(cookies[i].getName() + "=[" + cookies[i].getValue() + "]");
			}
		}
		out.println("</pre>");
		out.println("<hr/>");
		

		// display request attributes
		out.println("<h4>Request Attributes:</h4>");
		out.println("<table>");
		Enumeration<String> enm = request.getAttributeNames();
		while (enm.hasMoreElements()) {
			String key = enm.nextElement();
			out.println("<tr><td>" + key + "</td><td>" + request.getAttribute(key) + "</td></tr>");
		}
		out.println("</table>");
		out.println("<hr/>");
	}
	catch (Exception ex) {
		ex.printStackTrace(new java.io.PrintWriter(out));
	}
%>

<p/>
<br/>

</body>
</html>