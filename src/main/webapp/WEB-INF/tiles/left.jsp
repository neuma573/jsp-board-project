  <%@ include file="/WEB-INF/view/jstl.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

현재 메뉴의 최상위 부모 ID: ${activeMenuId}<br/>
menu-mapper.xml에서 권한 없는 메뉴 표시여부를 정할 수 있음.<br/>

<ul>
  <c:forEach var="m1" items="${rootMenu.children}">
    <spr:eval var="size1" expression="m1.children.size()"/>
    <c:if test="${size1 == 0}">
      <li><a href="${m1.url}">${m1.menuNm}</a></li>
    </c:if>
    <c:if test="${size1 > 0}">
      <li><a href="${m1.url}">${m1.menuNm}</a></li>
      <ul>
        <c:forEach var="m2" items="${m1.children}">
          <spr:eval var="size2" expression="m2.children.size()"/>
          <c:if test="${size2 == 0}">
            <li><a href="${m2.url}">${m2.menuNm}</a></li>
          </c:if>
          <c:if test="${size2 > 0}">
            <li><a href="${m2.url}">${m2.menuNm}</a></li>
          </c:if>
        </c:forEach>
      </ul>
    </c:if>
  </c:forEach>
</ul>