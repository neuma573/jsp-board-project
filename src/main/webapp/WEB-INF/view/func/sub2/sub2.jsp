<%@ include file="/WEB-INF/view/jstl.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<h2>sub 2 page</h2>
ROLE_MENU_MAP 테이블에서 ROLE_ADMIN만 접근 가능도록 설정한 페이지<br/>
<br/>
UriSecurityAspect가 활성화된 상태에서 비권한자가 접근하면 UnknownRole 예외가 발생한다.
