$(document).bind("ajaxError", function(event, jqxhr) {
	if (jqxhr.status == 418) {
		alert('세션이 만료되었습니다.\n로그인 화면으로 이동합니다.');
		window.top.location.href = '/login';
	}
});
