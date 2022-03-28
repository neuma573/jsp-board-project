<%@ include file="/WEB-INF/view/jstl.jsp" %>
  <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
      <!DOCTYPE html>
      <html>

      <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
      </head>

      <body>

        <h1>${title}</h1>
        <!-- public class Board {
          private int boardId; // [PK] BOARD_ID
          private String boardUser; // BOARD_USER
          private String boardName; // BOARD_NM
          private String boardCont; // BOARD_CONTENT
          private String boardRegDt; // BOARD_REG_DT
          private String boatdDelDt; // BOARD_DEL_DT
          private String boardModDt; // BOARD_MOD_DT
          private int boardView; // BOARD_VIEW
          private int boardComment; // BOARD_COMMENT
          private int boardLikes; // BOARD_LIKES -->






          <table border="2" width="770">
            <tr>
              <th width="60">번호</th>
              <th width="260">제목</th>
              <th width="110">작성자</th>
              <th width="160">작성일</th>
              <th width="60">조회수</th>
              <th width="60">댓글수</th>
            </tr>
            <c:forEach items="${list}" var="li">
              <tr align="center" height="30">
                <td>${li.brdNo}</td>
                <c:url value="/page/board/boardView" var="url">
                  <c:param name="id" value="${li.brdNo}" />
                  </c:url>
                <td><a href="${url}">${li.brdTitle}</a></td>
                <td>${li.brdWriter}</td>
                <td>${li.brdRegDt}</td>
                <td>${li.brdHit}</td>
                <td>${li.brdReCnt}</td>
              <tr>
            </c:forEach> <!-- Paging 처리 -->
          </table> 
          

        
       



        <a href="/page/board">전체 목록으로 가기</a>
    
      
          
        <script>
        </script>
      </body>

      </html>