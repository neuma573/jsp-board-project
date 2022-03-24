<%@ include file="/WEB-INF/view/jstl.jsp" %>
    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="widtr=device-widtr, initial-scale=1" />
        </head>
        <style>
            h2 { text-align: center;}
          table { width: 100%;}
          textarea { width: 100%;}
             #outter {
                display: block;
                width: 30%; 
                margin: auto;
            }
        </style>
        
        <body>


            <h1>게시판 게시글 페이지</h1>


            <!-- 게시글 본문영역 -->


            <table style="width: 900px;" border="1">
                <tr>
                    <td>게시글ID</td>
                    <td>
                        <c:out value="${board.brdNo}" />
                    </td>
                </tr>
                <tr>
                    <td>작성일</td>
                    <td>
                        <c:out value="${board.brdRegDt}" />
                    </td>
                </tr>
                <tr>
                    <td>작성자</td>
                    <td>
                        <c:out value="${board.brdWriter}" />
                    </td>
                </tr>
                <tr>
                    <td>조회수</td>
                    <td>
                        <c:out value="${board.brdHit}" />
                    </td>
                <tr>
                    <td>제목</td>
                    <td>
                        <c:out value="${board.brdTitle}"  />
                    </td>
                </tr>

                <tr>
                    <td>내용</td>
                    <td>
                        <script type="text/javascript">
                            var test = '<c:out value="${board.brdContent}" escapeXml="false" />';

                            let replaced_str = test.replace(/&lt;/g, '<');
                            replaced_str = replaced_str.replace(/&gt;/g, '>');
                            document.write(replaced_str);
                        </script>

                    </td>
                </tr>
                <tr>
                    <td>첨부파일</td>
                    <td>
                        <c:if test="${!empty listview.fileRandomNo}">
                        <form action ="/page/board/fileDownload" method="post">
                            파일이름: <c:out value="${listview.fileRealName}"/>  크기:<c:out value="${listview.fileSize}"/> byte
                            <input name="fileRandomNo" type="hidden" value="${listview.fileRandomNo}" >
                            <input name="brdNo" type="hidden" value="${board.brdNo}" >
                            <input type="submit" value="버튼"  >
                        </form>



                        </c:if>						 
<br/>

                
                
                </td>
                </tr>
               


            </table>
            <table style="width: 900px;">
       <th>
            <form action="/page/board/boardWrite" method="post">
                <input type="hidden" value="modify" name="action" />
                <input type="hidden" value="${board.brdNo}" name="id" />
                <input type="submit" value="수정" />
            </form></th>
        <th>
            <form action="/page/board/boardWrite" method="post">
                <input type="hidden" value="reply" name="action" />
                <input type="hidden" value="${board.brdNo}" name="id" />
                <input type="hidden" value="${board.brdOrigin}" name="origin" />
                <input type="submit" value="답글" />
            </form>
        </th>
        <th>
            <button id="delete" onclick="location.href='boardDelete?id=${board.brdNo}'">삭제</button>
        </th>
        <th>
            <a href="/page/board">돌아가기</button></a>
        </th>
            </table>
            <hr style="width: 900px; float:left;" />
            <!-- 게시글 본문 끝 -->
<br>

            <!-- 댓글 리스트 영역 -->
            <c:forEach var="li" items="${list}" varStatus="status">
                <div style="border: 1px solid gray; width: 900px; padding: 5px; margin-top: 5px;
                              margin-left: <c:out value=" ${20*li.reDepth}" />px; display: inline-block">
                    <c:out value="${li.reWriter}" />
                    <c:out value="${li.reRegDt}" />
                    <a id="delete" href='commentDelete?id=${li.reNo}&bid=${board.brdNo}'>삭제</a>
                    <a id="reply" href='/page/board/commentReply?id=${li.reNo}&bid=${board.brdNo}'>답글</a>
                    <br />
                    <c:out value="${li.reContent}" escapeXml="false"/>
                
                
                </div>


                <br />
             </c:forEach>
                

                

                
                

            <!-- 댓글 리스트 끝 -->



            <!-- 댓글 입력 영역 -->
            <div style="border: 1px solid gray; width: 900px; padding: 5px; margin-top: 5px;
                     display: inline-block">
                <form action="/page/board/commentPost" method="post">
                    <input TYPE="hidden" NAME="reBrdNo" SIZE=10 value='<c:out value="${board.brdNo}" />'>
                    <div class="form-group">
                    </div>

                            <textarea class="form-control" rows="5" cols="70" id="reContent" name="reContent"
                                placeholder="댓글입력" required="required"></textarea>
                                <button type="submit">등록</button>
                            </form>
                            </div>





            
            </div>
            <!-- 댓글 입력 영역 끝 -->




         
        </body>

        </html>