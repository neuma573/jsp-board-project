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
           placeholder: '내용작성',
            minHeight: 370,
            maxHeight: null,
            focus: true, 
            lang : 'ko-KR'
      });
    });
  </script>


<body>
  <c:if test="${empty action}">
    <c:set var="action" value="write" />
  </c:if>
  <script>

    console.log("action : "+ "${action}");
  </script>
  <h1>${title}</h1>






  <form action="/page/board/boardWriteRegist" name="write" method="post" enctype="multipart/form-data">
    <c:if test="${action eq 'reply'}" >
    <c:set var="plus" value="1" />
    
    <input type="hidden" name="brdParents"  value="<c:out value=" ${id}" />">
    <input type="hidden" name="brdOrigin"  value="<c:out value=" ${origin}" />">
    <input type="hidden" name="brdDepth"  value="<c:out value=" ${depth+plus}" />">
  </c:if>
    <c:if test="${action eq 'modify'}" >
      <table>
        <th>
            <div><label>글번호</label>
                <input name="brdNo" readonly="readonly" value="<c:out value="${board.brdNo}" />">
           </div>
        </th>
        <th>
           <div><label>작성자</label>
                <input name="brdWriter" readonly="readonly" value="<c:out value="${board.brdWriter}"/>">
           </div>
        </th>
        </table>


    </c:if>
    
    <div class="form-group">
      <table>
        <tr><td>
      <label for="title">제목</label></td>

      <td><input type="text" id="brdTitle"
       placeholder="제목 입력(1-100)" name="brdTitle"
       maxlength="100" required="required"
       pattern=".{1,100}" value="<c:if test="${action eq 'reply'}" >RE:</c:if>${board.brdTitle}"></td>
      </tr>
    </div>
    <tr><td>
    <div class="form-group">
   <label for="content">내용</label></td>
<td>
   <textarea rows="5" id="summernote"
    name="brdContent" placeholder="내용 작성" required="required"><c:if test="${action eq 'reply'}" >RE:</c:if>${board.brdContent}</textarea>


 </div></td></tr>
 <c:if test="${action eq 'modify'}" >
 <tr><td>첨부파일 이름 : </td><td>${file.fileRealName} <a href="/page/board/fileDelete?id=${file.fileNo}&bid=${board.brdNo}">삭제</a></td></tr>
</c:if>
</table>
 <input type="file" id="uploadFile" name="uploadFile"/>
 <input type="hidden" name="action" value="${action}" />
<input type="submit" value="등록" />
<a href="javascript:history.back()">돌아가기</a>
  </form>







    </div>
  <script>
  </script>
</body>
</html>
