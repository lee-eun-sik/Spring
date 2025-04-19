<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head> 
    <meta charset="UTF-8">
    <title>공지사항</title>
    <script src="/js/jquery-3.7.1.min.js"></script>
	<script src="/js/common.js"></script>
	<link rel="stylesheet" href="/css/list.css?ver=1">
</head>
<script>
		$(document).ready(function () {

			$("#searchBtn").click(function() {
    			searchname(1, true);
    		});
    		$("#viewcategory").change(function() {
    	        let selectedValue = $(this).val();
    	        sortByCategory(1,selectedValue);
    	    });
		});
		
		function url(page, viewcategory){
	    	let searchText = $("#searchText").val();
	    	let searchColumn = $("#searchColumn").val();
			
			
        	window.location.href = "/freeBoard/list.do?"+
        	"searchText=" + searchText + "&" +
        	 "searchColumn=" + searchColumn + "&" +
            "viewcategory=" + viewcategory + "&" +
            "page=" + page + "&" +
            "size=${size}";
            
			if(viewcategory==null){
				window.location.href = "/freeBoard/list.do?"+
				"searchText=" +searchText+"&"+
				"searchColumn=" +searchColumn+"&"+
				"page=" +page+"&"+
				"size=${size}";
			}
	    }
	    function searchname(page, checkNow) {
			if(checkNow) {
				url(1,null)
										
			} else {
				url(1,null)
			}
		}
		
	</script>
<body>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"></jsp:include>
	<h2>공지사항</h2>
	<div class="button-wrapper">
	  <a href="/newsboard/newsboardcreate.do" class="write-btn">게시글 작성</a>
	</div>
	<table>
		<thead>
			<tr>
				<th>번호</th>
				<th>작성자</th>
				<th>제목</th>
				<th>조회수</th>
				<th>작성일</th>
				<th>상세보기</th>
			</tr>
		</thead>
		<tbody>
		<c:choose>
			<c:when test="${not empty boardList}">
				<c:forEach var="board" items="${boardList}">
					<tr>
						<td>${board.rn}</td>
						<td>${board.createId}</td>
						<td>${board.title}</td>
						<td>${board.viewCount}</td>
						<td>${board.createDt}</td>
						<td><a href="newsboardview.do?id=${board.boardId}">보기</a></td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td colspan="6" style="text-align:center; font-size:17px;">게시물이 없습니다.</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
	</table>

<!-- 페이지 네비게이션 -->
<ul>
  <!-- 이전 페이지 -->
  <c:if test="${currentPage > 1}">
    <li>
      <a href="newboardlist.do?page=${currentPage - 1}&size=${size}">&laquo;</a>
    </li>
  </c:if>

  <!-- 페이지 번호 -->
  <c:forEach begin="1" end="${totalPages}" var="i">
    <li>
      <a href="newboardlist.do?page=${i}&size=${size}"
         <c:if test="${i == currentPage}">style="font-weight: bold;"</c:if>>
        ${i}
      </a>
    </li>
  </c:forEach>

  <!-- 다음 페이지 -->
  <c:if test="${currentPage < totalPages}">
    <li>
      <a href="newboardlist.do?page=${currentPage + 1}&size=${size}">&raquo;</a>
    </li>
  </c:if>
</ul>

	<!-- 검색 영역 -->
<div class="search-container">
  <select name="searchColumn" id="searchColumn">
    <option value="TITLE">제목</option>
    <option value="CREATE_ID">작성자</option>
  </select>
  <input type="text" id="searchText" name="searchText" value="${reservation}" placeholder="입력해주세요">
  <button type="button" id="searchBtn" class="icon-button">
    🔍
  </button>
</div>
</body>
</html>