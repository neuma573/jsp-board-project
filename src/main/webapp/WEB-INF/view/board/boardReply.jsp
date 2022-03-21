<%@ include file="/WEB-INF/view/jstl.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
<!-- include libraries(jQuery, bootstrap) -->
<link href="http://netdna.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.css" rel="stylesheet">
<script src="http://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.js"></script> 
<script src="http://netdna.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.js"></script> 
<!-- include summernote css/js-->
<link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.11/summernote-bs4.css" rel="stylesheet">
<script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.11/summernote-bs4.js"></script>
<!-- include summernote-ko-KR -->
<script src="/resources/js/summernote-ko-KR.js"></script>
<title>글쓰기</title>

<script>
  $(document).ready(function() {
      $('#summernote').summernote({
           placeholder: 'brdContent',
            minHeight: 370,
            maxHeight: null,
            focus: true, 
            lang : 'ko-KR'
      });
    });
  </script>

</head>
<body>
  
  <h1>게시판 글 작성하기</h1>
  <form action="/page/board/pageWrite" method="post" enctype="multipart/form-data">
    <c:set var="plus" value="1" />
    <input type="hidden" name="brdParents"  value="<c:out value=" ${id}" />">
    <input type="hidden" name="brdOrigin"  value="<c:out value=" ${origin}" />">
    <input type="hidden" name="brdDepth"  value="<c:out value=" ${depth+plus}" />">
    <div class="form-group">
      <table>
        <tr><td>
      <label for="title">제목</label></td>
    
      <td><input type="text" id="brdTitle"
        placeholder="제목 입력(4-100)" name="brdTitle"
        maxlength="100" required="required"
        pattern=".{4,100}" value="RE: ${board.brdTitle}"></td>
      </tr>
    </div>
    <tr><td>
    <div class="form-group">
   <label for="content">내용</label></td>
<td>
   <textarea rows="5" id="summernote"
    name="brdContent" placeholder="내용 작성"  required="required">RE: ${board.brdContent}</textarea>


 </div></td></tr>
</table>
 <input type="file" id="uploadFile" name="uploadFile"/>
<button type="submit">등록</button>
<a href="javascript:history.back()">돌아가기</button>
  </form>




    </div>
  <script>
  </script>
</body>
</html>
