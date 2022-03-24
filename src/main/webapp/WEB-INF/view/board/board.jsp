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

        <h1>게시판 메인 : 접속유저 : ${User}</h1>
        <table  width="770">
          <tr>
          <th>
        <form action="/page/board/setPageCnt" method="post">
        <input id="displayRowCount"
        placeholder="1~100" name="displayRowCount"
        input type='number' min='1' max='100' required="required">
            <button type="submit">게시글 표시</button>
        </form></th>
        <th>
        <form action="/page/board/setPagingMethod" method="post">
              <button type="submit">최신순으로 표시 토글</button>
        </form>
        </th>
        <th>
            <form action="/page/board/selectOrigin" method="post">
                  <button type="submit">최상위 부모 게시글 그룹만 표시 토글</button>
            </form>
        </th>
        <th>
          <form action="/page/board/selectRe" method="post">
                <button type="submit">댓글 그룹만 표시 토글</button>
          </form>
      </th></tr>
    <tr>
      <td>게시물 ${pageVO.displayRowCount} 개 표시</td>
      <td>${pageVO.pagingByNew}</td>
      <td>${pageVO.selectOrigin}</td>
      <td>${pageVO.selectRe}</td>
  </tr>
</table>
          <table border="2" width="770">
            <tr>
              <th width="60">번호</th>
              <th width="260">제목</th>
              <th width="110">작성자</th>
              <th width="160">작성일</th>
              <th width="60">조회수</th>
              <th width="60">댓글수</th>
            </tr>
            <c:forEach items="${list}" var="li" varStatus="status">
              <tr align="center" height="30">
                <td>
                  ${pageVO.displayRowCount*(pageVO.page-1)+status.count}
                </td>
                <c:url value="/page/board/boardView" var="url">
                  <c:param name="id" value="${li.brdNo}" />
                  </c:url>
                <td style="text-align: left;"> <a href="${url}"><c:set var="re" value="└─" /><c:forEach var="i" begin="1" end="${li.brdDepth}" step="1"><c:out value="${re}"></c:out></c:forEach>${li.brdTitle}</a></td>
                <td>${li.brdWriter}</td>
                <td>${li.brdRegDt}</td>
                <td>${li.brdHit}</td>
                <td>${li.brdReCnt}</td>
              <tr>
            </c:forEach> <!-- Paging 처리 -->
            <tr>
              <td colspan="7" align="center" height="40">

                <c:if test="${pageVO.totPage>1}">
                  <div class="paging">
                    <c:if test="${pageVO.hasPrev}">
                      <c:url var="pageLinkPrev" value="board">
                      <c:param name="page" value="${pageVO.pageStart-1}" />
                    </c:url>
                    <a href="${pageLinkPrev}">[이전으로]</a>
                    </c:if>
                      <c:forEach var="i" begin="${pageVO.pageStart}" end="${pageVO.pageEnd}" step="1">
                          <c:url var="pageLink" value="board">
                          <c:param name="page" value="${i}" />
                          </c:url>
                              <a href="${pageLink}"><c:out value="${i}"/></a>

                      </c:forEach>
                      <c:if test="${pageVO.hasNext}">
                        <c:url var="pageLinkNext" value="board">
                        <c:param name="page" value="${pageVO.pageEnd+1}" />
                      </c:url>
                      <a href="${pageLinkNext}">[다음으로]</a>
                      </c:if> 
                  </div>
                  </c:if>
 
              </td>
            </tr>
          </table> 
          

        
       



        <a href="/page/board/boardWrite">글쓰기</a>
        <!-- <a href="/page/board/api">API 호출</a> -->
        <form action="/page/board/searchResult" method="post">
          <div>
            <select name="type" id="type" required="required">
              <option value="title">제목으로 검색</option>
              <option value="content">내용으로 검색</option>
             </select>
              <input id="searchKeyword"
              placeholder="검색키워드입력" name="searchKeyword"
              maxlength="10" required="required"
              pattern=".{1,10}">
              <button type="submit">검색</button>
          </div>
        </form>   
          
        <script>
        </script>
      </body>

      </html>