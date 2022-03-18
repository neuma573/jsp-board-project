<%@ include file="/WEB-INF/view/jstl.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
</head>
<body>
  
  <h1>답글 작성하기</h1>
  <div style="border: 1px solid gray; width: 600px; padding: 5px; margin-top: 5px;
  display: inline-block">
<form action="/page/board/commentPost" method="post">
 <input TYPE="hidden" NAME="commentUpper" SIZE=10 value='<c:out value="${board.boardId}" />'>
 <input TYPE="hidden" NAME="commentUpper" SIZE=10 value='<c:out value="${list.commentParent}" />'>
 <div class="form-group">
     <table>
 </div>

         <textarea class="form-control" rows="5" cols="70" id="commentContent" name="commentContent"
             placeholder="댓글입력" required="required"></textarea>
</div>

<button type="submit">등록</button>



</form>
</div>
  <script>
  </script>
</body>
</html>
