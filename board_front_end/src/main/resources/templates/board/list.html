<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 목록</title>
	<script src = "/js/jquery_3.7.1.min.js"></script>
	<script src = "/js/common.js"></script>
</head>
<body>
	<h2>게시판 목록</h2>
	<form id="searchForm">
		<input type="text" name="searchText" id="searchText" value="${param.searchText}"placeholder="검색">
		<input type="date" name="searchStartDate" id="searchStartDate" value="${param.searchStartDate}">
		~ <input type="date" name="searchEndDate" id="searchEndDate" value="${param.searchEndDate}">
		<button type="button" id="searchBtn" onclick="searchBoard()">검색</button>
	</form>
	<table>
		<thead>
			<tr>
				<th>번호</th>
				<th>제목</th>
				<th>작성자</th>
				<th>조회수</th>
				<th>작성일</th>
				<th>상세보기</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="board" items="${boardList}">
			 <tr>
			 	<td>${board.rn}</td>
			 	<td>${board.title}</td>
			 	<td>${board.createId}</td>
			 	<td>${board.viewCount}</td>
			 	<td>${board.createDt}</td>
			 	<td><a href="view.do?id=${board.boardId}">보기</a></td>
			 </tr>
			</c:forEach> 	
		</tbody>		
	</table>
	<ul>
		<c:if test="${currentPage > 1}">
				<a href="list.do?page=${currentPage - 1}&size=${size}" > &laquo;</a>
		</c:if>
		<c:forEach begin="1" end="${totalPages}" var="i">
				<a href="list.do?page=${i}&size=${size}"
				style="<c:if test='${i == currentPage}'>font-weight: bold;</c:if>">
				${i}
				</a>
		</c:forEach>	
		<c:if test="${currentPage < totalPages}">
					<a href="list.do?page=${currentPage + 1}&size=${size}">&raquo;</a>
		</c:if>							
	</ul>
	<a href="/user/main.do">메인 페이지로 이동</a>
</body>
</html>