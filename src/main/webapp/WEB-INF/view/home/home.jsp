<%@ include file="/WEB-INF/view/jstl.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<h2>It works!</h2>

<div>
  Spring security 설정으로 URL 단위로 접근제어를 할 수 있다.<br/><br/>
  <a href="/view/home/adminOnly">adminOnly page (spring security에서 설정)</a><br/>
  <a href="#" onclick="api()">adminOnly API (spring security에서 설정)</a><br/>
</div>

<sec:authorize access="isAuthenticated()">
<p>
  인증된 사용자만 볼 수 있는 페이지.<br/>
  <font color="blue">
    <sec:authorize access="hasRole('ROLE_USER')">
      ROLE_USER
    </sec:authorize>
    <sec:authorize access="hasRole('ROLE_ADMIN')">
      + ROLE_ADMIN
    </sec:authorize>
    권한으로 로그인 했습니다.
  </font>
</p>
</sec:authorize>

<table border="1">
  <tr>
    <th>Tag</th>
    <th>Value</th>
  </tr>
  <tr>
    <td>&lt;sec:authentication property='name' /&gt;</td>
    <td><sec:authentication property="name" /></td>
  </tr>
  <tr>
    <td>&lt;sec:authentication property='principal' /&gt;</td>
    <td><sec:authentication property="principal" var="var_principal" scope="page" />${var_principal}</td>
  </tr>
  <tr>
    <td>&lt;sec:authentication property='principal.username' /&gt;</td>
    <td><sec:authentication property="principal.username" /></td>
  </tr>
  <tr>
    <td>&lt;sec:authentication property='principal.enabled' /&gt;</td>
    <td><sec:authentication property="principal.enabled" /></td>
  </tr>
  <tr>
    <td>&lt;sec:authentication property='principal.accountNonLocked' /&gt;</td>
    <td><sec:authentication property="principal.accountNonLocked" /></td>
  </tr>
  <tr>
    <td>&lt;sec:authentication property='principal.authorities[0].authority' /&gt;</td>
    <td><sec:authentication property="principal.authorities[0].authority" /></td>
  </tr>
  <tr>
    <td>&lt;c:forEach items="\${var_principal.authorities}" /&gt;</td>
    <td>
      <c:forEach var="auth" items="${var_principal.authorities}" varStatus="status">
        <c:out value="${status.count}:${auth.authority}"/><br/>
      </c:forEach>
    </td>
  </tr>
  <tr style="background-color: pink; text-align: center; font-weight: bold;">
    <td colspan="2">다음은 Custom User 객체에 추가된 멤버 변수</td>
  </tr>
  <tr>
    <td>&lt;sec:authentication property='principal.userNm' /&gt;</td>
    <td><sec:authentication property="principal.userNm" /></td>
  </tr>
  <tr>
    <td>&lt;sec:authentication property='principal.loginFailCnt' /&gt;</td>
    <td><sec:authentication property="principal.loginFailCnt" /></td>
  </tr>
</table>

<script>
	function api() {
		$.getJSON('/api/home/adminOnly', function(rsp) {
			alert(JSON.stringify(rsp));
		}).fail(function(e) {
			alert(JSON.stringify(e.responseJSON));
		});
	}
</script>
