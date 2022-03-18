<%@ include file="/WEB-INF/view/jstl.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<h2>func page</h2>

<div>
  MENU, ROLE_MENU_MAP 테이블에 등록한 메뉴를 left.jsp 파일에서 표시.<br/>
  UriSecurityAspect를 활성화해서 메뉴 단위로 접근제어를 할 수 있다.<br/>
  <br/>
  @AccessMapping 설정으로 API 접근제어.<br/>
  UriSecurityAspect가 활성화된 상태에서 비권한자가 호출하면 401 응답을 받는다.<br/>
  <br/>
  <a href="#" onclick="api(1)">ROLE_ADMIN만 호출 가능한 API</a><br/>
  <a href="#" onclick="api(2)">ROLE_UESER도 호출 가능한 API</a><br/>
  <a href="#" onclick="api(3)">권한 체크 안하는 API</a><br/>
  <a href="#" onclick="api(0)">퍼블릭 API</a><br/>
</div>
<br/>
API response:
<div id="out"></div>

<script>
	function api(num) {
		var url = num == 0 ? '/api/public/home/pub' : '/api/func/sub2/api' + num
		$.getJSON(url, function(rsp) {
			$('#out').html(rsp.data);
		}).fail(function(e) {
			$('#out').html('Error: ' + e.responseJSON.error);
		});
	}
</script>
