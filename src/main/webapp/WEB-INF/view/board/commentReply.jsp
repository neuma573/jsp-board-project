<%@ include file="/WEB-INF/view/jstl.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
</head>
<body>
  
  <h1>대댓글</h1>



    <div style="border: 1px solid gray; width: 600px; padding: 5px; margin-top: 5px;
        display: inline-block">
      <form action="/page/board/commentPost" method="post">
        <c:set var="plus" value="1" />
        <input TYPE="hidden" NAME="reBrdNo" SIZE=10 value='<c:out value="${bid}" />'>
        <input type="hidden" name="reParents"  value="<c:out value=" ${id}" />">
        <input type="hidden" name="reDepth"  value="<c:out value=" ${depth+plus}" />">
        <div class="form-group">
       </div>

               <textarea class="form-control" rows="5" cols="70" id="reContent" name="reContent"
                   placeholder="댓글입력" required="required"></textarea>
</div>

   <button type="submit">등록</button>


</div>
  <script>
  </script>
</body>
</html>
