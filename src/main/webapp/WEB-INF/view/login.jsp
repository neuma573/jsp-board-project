<%@ include file="/WEB-INF/view/jstl.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
</head>
<body>
  ROLE_ADMIIN + ROLE_USER 권한 계정: foo/foopwd<br/>
  ROLE_USER 권한 계정: bar/barpwd<br/>
  <br/>
  도메인 사용하여 로그인하려면 SecurityConfig에서 활성화 시켜야 함.<br/>
  ROLE_ADMIIN 권한 도메인 소속 계정: hoo/hoopwd/acorp<br/>
  <br/>
  <div>
    USER_ROLE_MAP 테이블에 등록되지 않은 계정은 ROLE_USER 권한으로 로그인 됨.
  </div>
  <br/>
  
  <form id="frm" action="/login" method="post" autocomplete="off">
    <table border="1">
      <tr>
        <td>ID</td>
        <td><input type="text" name="userId" value="bar"/></td>
      </tr>
      <tr>
        <td>PWD</td>
        <td><input type="password" name="userPwd" value="barpwd"/></td>
      </tr>
      <tr>
        <td>DOMAIN</td>
        <td><input type="text" name="userDomain" value="acorp"/></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td>
          <input type="submit" value="form login" />
          <input type="button" value="ajax login" onclick="ajaxlogin(event)" />
          <input type="button" value="json login" onclick="jsonlogin()" /><br/>
          <input type="button" value="cors ajax login" onclick="corslogin()" />
          <input type="button" value="cors use resource" onclick="corsresource()" />
        </td>
      </tr>
      <c:if test="${not empty param.error}">
        <tr>
          <td colspan="2">Login failed : ${SPRING_SECURITY_LAST_EXCEPTION.message}.</td>
        </tr>
      </c:if>
    </table>
  </form>
  
  <p></p>
  <div>
    <a href="/view/public/home/pub1">pub page1</a>
    <a href="/view/public/home/pub2">pub page2</a>
    <a href="#" onclick="api('/api/public/home/pub')">pub api</a>
    <a href="#" onclick="api('/api/home/adminOnly')">auth api</a>
  </div>

  <script src="/static/js/jquery-3.6.0.min.js"></script>
  <script>
  	function ajaxlogin(evt) {
  		evt.preventDefault();

		$.post('/api/auth/login', $('#frm').serialize() , function(rsp) {
			if (rsp.success) {
				window.location.href = rsp.data;
				return false;
			}
			
			var msg = '';
			switch(rsp.code) {
			case 'CE_ID_PWD': // ID/PWD 불일치
				msg = '로그인 실패하였습니다. (5회 실패 시 계정 잠김)'; 
				break;
			case 'SS_USER_STOP': // 중지 계정
			case 'SS_USER_LOCK': // 잠김 계정
				msg = '계정 상태가 중지 또는 차단된 계정입니다.\n관리자에게 문의하세요.';
				break;
			case 'SS_PWD_EXPIRED': // 비밀번호 만료
				msg = '비밀번호를 변경하신지 90일이 지났습니다.\n비밀번호 변경 화면으로 이동합니다.';
				break;
			case 'SS_INIT_PWD': // 최초 비밀번호 변경 필요
				msg = '새로운 비밀번호로 변경이 필요합니다.\n비밀번호 변경 화면으로 이동합니다.';
				break;
			case 'SS_NOT_ALLOWED_IP': // ID/PWD 불일치 횟수초과로 계정 잠김
				msg = '허용되지 않은 접속IP입니다.\n관리자에게 문의하세요.';
				break;
			case 'CE_TO_LOCK': // ID/PWD 불일치 횟수초과로 계정 잠김
				msg = '5회 이상 로그인 실패하여 해당 아이디에 대한 계정이 잠금처리되었습니다.\n관리자에게 문의하세요.';
				break;
			default:
				msg = '인증에 실패했습니다.';
			}
			alert(msg);
		});
	}
  	
  	function jsonlogin() {
  		// json body로 ajax 로그인 요청하여 jwt 토큰을 cookie로 받는 경우
  		// 서버에 jwt 패키지 설정 필요
  	    $.ajax({
  	        url: "/api/auth/login",
  	        method: "POST",
  	        contentType: "application/json; charset=utf-8",
  	        processData: false, // data를 query string으로 변환하지 않는다
  	        data: JSON.stringify(form2json($('#frm'))) // json body 요청
  	    }).done(function (response, status, xhr) {
  	    	console.log('header.payload:', getCookie('X-Access-Token'));
  	    	console.log('signature:', getCookie('X-Access-Token-Signature'));
  	    });
  	}

  	function getCookie(name) {
  	  var value = "; " + document.cookie;
  	  var parts = value.split("; " + name + "=");
  	  if (parts.length == 2) return parts.pop().split(";").shift();
  	}

	function api(url) {
		$.getJSON(url, function(rsp) {
			alert(JSON.stringify(rsp));
		}).fail(function(e) {
			alert(JSON.stringify(e.responseJSON));
		});
	}

	//------------------------ CORS API 요청 ------------------------//
  	function form2json($form) {
  	    var data = $form.serializeArray();
  	    var json = {};

  	    $.map(data, function(n, i) {
  	        json[n['name']] = n['value'];
  	    });
  	    return json;
  	}
	
	var accessToken = null;
  	
  	function corslogin() {
  		// 다른 도메인으로 json body로 ajax 로그인 요청하여 jwt 토큰을 header로 받는 경우
  	    $.ajax({
  	        url: "http://localhost:9001/api/auth/login", // 다른 도메인으로 login 요청
  	        method: "POST",
  	        contentType: "application/json; charset=utf-8",
  	        processData: false, // data를 query string으로 변환하지 않는다
  	        data: JSON.stringify(form2json($('#frm'))) // json body 요청
  	    }).done(function (response, status, xhr) {
  	    	console.log('access:', xhr.getResponseHeader('X-Access-Token'));
  	    	console.log('refresh:', xhr.getResponseHeader('X-Refresh-Token'));
  	    	accessToken = xhr.getResponseHeader('X-Access-Token')
  	    });
  	}
  	
  	function corsresource() {
  		$.ajax({
  		  type: 'GET',
  		  url: 'http://localhost:9001/api/home/adminOnly', // 다른 도메인으로 resource 요청
  		  dataType: 'json',
  		  headers: {
  		    'Authorization': 'Bearer ' + accessToken
  		  }
  		}).done(function (response, status, xhr) {
  			alert(JSON.stringify(response));
  	    }).fail(function(e) {
			alert(JSON.stringify(e.responseJSON));
		});
  	}
  </script>
</body>
</html>
