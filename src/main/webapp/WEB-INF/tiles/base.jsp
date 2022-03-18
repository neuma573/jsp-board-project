<%@ include file="/WEB-INF/view/jstl.jsp" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <tiles:getAsString name="title" />
  <link rel="icon" href="/favicon.ico" type="image/x-icon" />
  <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
  <link  rel="stylesheet" href="/static/css/default.css"/>
</head>
<body>
  <script src="/static/js/jquery-3.6.0.min.js"></script>
  <script src="/static/js/app-utils.js"></script>
  <script src="/static/js/app-setting.js"></script>

  <header>Header</header>
  <div id="main">
    <article><tiles:insertAttribute name="body" /></article>
    <nav><tiles:insertAttribute name="left" /></nav>
  </div>
  <footer><tiles:insertAttribute name="footer" /></footer>
</body>
</html>
