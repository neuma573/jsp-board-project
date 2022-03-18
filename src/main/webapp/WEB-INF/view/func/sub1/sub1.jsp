<%@ include file="/WEB-INF/view/jstl.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style type="text/css">
#drop_zone {
  border: 2px solid blue;
  width:  400px;
  height: 200px;
  margin-top: 20px;
}
</style>


<h2>sub 1 page</h2>
ROLE_MENU_MAP 테이블에서 ROLE_ADMIN, ROLE_USER 모두 접근 가능도록 설정한 페이지<br/>
<br/>
UriSecurityAspect가 활성화된 상태에서 비권한자가 접근하면 UnknownRole 예외가 발생한다.
<br/><hr/><br/>

<a href="#" onclick="fileDownload(1)">다운로드 PPT</a>
<a href="#" onclick="fileDownload(2)">다운로드 TXT</a><br/>

<br/>
<form id="uploadForm" action="/api/upload" method="post" enctype="multipart/form-data">
  uploader : <input type="text" name="uploader" value="foo"/><br/>
  upfile : <input type="file" name="upfile"/><br/>
  <input id="upload" type="button" value="upload form submit"/>
</form>

<div id="drop_zone" ondrop="dropHandler(event);" ondragover="dragOverHandler(event);">
  <p>Drag one or more files to this Drop Zone ...</p>
  <p id="upmsg"></p>
</div>


<script src="/static/js/jquery.form-4.3.0.min.js"></script>
<script>
	function fileDownload(fileId) {
		var fileName = null
		if (fileId == 1) {
			fileName = '샘플 PowerPoint 문서.pptx'
		} else if (fileId == 2) {
			fileName = '샘플 Text 문서.txt'
		}

		window.location.href = '/api/download?name=' + encodeURIComponent(fileName, 'UTF-8')
	}

	$('#upload').click(function() {
		var options = {
			dataType : 'json', 
			success : function(responseText, statusText, xhr, $form) {
				console.log(responseText);
			}
		};
		$('#uploadForm').ajaxForm(options).submit();
	});

	function dropHandler(ev) {
		// Prevent default behavior (Prevent file from being opened)
		ev.preventDefault();
		
		var formData = new FormData();

		if (ev.dataTransfer.items) {
			// Use DataTransferItemList interface to access the file(s) - chrome
			for (var i = 0; i < ev.dataTransfer.items.length; i++) {
				// If dropped items aren't files, reject them
				if (ev.dataTransfer.items[i].kind === 'file') {
					var file = ev.dataTransfer.items[i].getAsFile();
					formData.append('upfiles', file, file.name);
				}
			}
		} else {
			// Use DataTransfer interface to access the file(s) - IE
			for (var i = 0; i < ev.dataTransfer.files.length; i++) {
				var file = ev.dataTransfer.files[i];
				formData.append('upfiles', file, file.name);
			}
		}
		
		upload(formData);
	}

	function dragOverHandler(ev) {
		// Prevent default behavior (Prevent file from being opened)
		ev.preventDefault();
		$('#upmsg').html('');
	}

	function upload(formData) {
		$.ajax({
			url : '/api/upload',
			data : formData,
			type : 'post',
			contentType : false,
			processData : false,
			success : function(rsp) {
				console.log(rsp)
				$('#upmsg').html('업로드 성공');
			},
			error: function(err) {
				console.log(err)
				$('#upmsg').html('업로드 실패');
			}
		});
	}
</script>
