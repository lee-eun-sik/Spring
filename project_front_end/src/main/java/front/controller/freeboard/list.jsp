<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>자유게시판</title>
<script src="/js/jquery-3.7.1.min.js"></script>
<script src="/js/common.js"></script>
<link rel="stylesheet" href="/css/list.css?ver=1">
<script>
		$(document).ready(function () {
		
			let searchColumn = "${board.searchColumn}";  // 서버에서 전달된 값 (예: 'TITLE' or 'CREATE_ID')
		
		    if (searchColumn === "" || searchColumn === "TITLE") {
		        $("#searchColumn").val("TITLE");  // 기본값으로 '제목' 설정
		    } else if (searchColumn === "CREATE_ID") {
		        $("#searchColumn").val("CREATE_ID");  // '작성자' 선택
		    }
			
			$("#searchBtn").click(function() {
				search(1, true);
			});
			$("#viewcategory").change(function() {
		        let selectedValue = $(this).val();
		        sortByCategory(1,selectedValue);
		    });
		});


		function search(page,checkNow) {
			if(checkNow) {
				let searchText = $("#searchText").val();
				let searchColumn = $("#searchColumn").val();
				

				window.location.href="/freeBoard/list.do?"
									+"searchText="+searchText+"&"
									+"searchColumn="+searchColumn+"&"
									+"page="+page+"&"
									+"size=${size}";
			} else {
				let searchText = '${board.searchText}';
				let searchColumn = '${board.searchColumn}';
				window.location.href="/freeBoard/list.do?"
									+"searchText="+searchText+"&"
									+"searchColumn="+searchColumn+"&"
									+"page="+page+"&"
									+"size=${size}";
				}
			}	

    </script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"></jsp:include>
	<h2>자유게시판</h2>
	<button><a href="/freeBoard/create.do">게시글 작성</a></button>
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
		<tbody >
		<!-- 게시글 목록을 반복 출력 -->
			<c:forEach var="freeBoard"  items="${boardList}">
				<tr>
				<!-- 각각의 게시글 정보를 출력 -->
					<td>${freeBoard.rn}</td> <!-- 게시글의 순번 (DB에서 ROW_NUMBER()로 생성된 값) -->
					<td>${freeBoard.createId}</td> <!-- 게시글 작성자 ID -->
					<td>${freeBoard.title}</td> <!-- 게시글 제목 -->
					<td>${freeBoard.viewCount}</td> <!-- 조회수 -->
					<td>${freeBoard.createDt}</td> <!-- 작성 날짜 -->
					<!-- 상세보기 링크 (게시글 ID를 URL 파라미터로 전달) -->
					<td><a href="view.do?id=${freeBoard.boardId}">보기</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	
	<!-- 페이지 네비게이션 (페이징 기능) -->
	<ul>
		<c:if test="${currentPage > 1}">		
				<button type="button" onclick="search(${currentPage - 1},false)" >&raquo;</button>
		</c:if>
		<c:forEach begin="1" end="${totalPage}" var="i">
			    <button type="button" onclick="search(${i}, false)"
			       <c:if test="${i == currentPage}">style="font-weight: bold;"</c:if>> 
			       ${i} 
			    </button>
		</c:forEach>
		
		<c:if test="${currentPage < totalPage}">
				<button type="button" onclick="search(${currentPage + 1},false)" >&raquo;</button>
		</c:if>		
	</ul>
	
	<div class="search-container">
		<select name="searchColumn" id="searchColumn">
		    <option value="TITLE" selected>제목</option>
		    <option value="CREATE_ID">작성자</option>
		</select>
	
	    <input class="search" type="text" id="searchText" name="searchText" value="${reservation}" placeholder="입력해주세요">
	    <button type="button" id="searchBtn" class="searchBtn">검색</button>
    </div>

</body>
</html>