<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>

<html lang="ko">
<head> 
    <meta charset="UTF-8">
    <title>게시판 목록</title>
   	<script src="/js/jquery-3.7.1.min.js?ver=1"></script>
	<script src="/js/common.js?ver=1.1"></script>	
	<link rel="stylesheet" href="/css/list.css?ver=1">
	
</head>
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
				

				window.location.href="/reviewboard/reviewlist.do?"
									+"searchText="+searchText+"&"
									+"searchColumn="+searchColumn+"&"
									+"page="+page+"&"
									+"size=${size}";
			} else {
				let searchText = '${board.searchText}';
				let searchColumn = '${board.searchColumn}';
				window.location.href="/reviewboard/reviewlist.do?"
									+"searchText="+searchText+"&"
									+"searchColumn="+searchColumn+"&"
									+"page="+page+"&"
									+"size=${size}";
				}
			}	
	</script>
<body>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"></jsp:include>
	<h2>펫시터 후기 리스트</h2>
	<button class="review-create-btn">
		<a href="/reviewboard/reviewcreate.do">후기 작성</a>
	</button>
	<div class="review-container">
		<div class="review-box">
	            <div class="review-count">
	                <h3>전체후기(${totalReviewCount})</h3>
	            </div>
	            
	            <div class="review-stats">
	                <h3>평점 상세</h3>
	                <c:forEach var="entry" items="${ratingStats}">
					    <p>${entry.key}점 : ${entry.value}개</p>
					</c:forEach>
	                
	        	</div>
		</div>
        <!-- 시터별 후기 선택 -->
        <%-- <select class="dropdown">
            <option>시터별 후기</option>
            <c:forEach var="sitter" items="${sitterList}">
                <option value="${sitter.id}">${sitter.name}</option>
            </c:forEach>
        </select> --%>
    </div>
    <br/>
	
	<table>
		<thead>
			<tr>
				<th>번호</th>
				<th>펫시터</th>
				<th>작성자</th>
				<th>제목</th>
				<th>조회수</th>
				<th>작성일</th>
				<th>상세보기</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="board" items="${boardList}">
				<tr>
					<td>${board.rn}</td>
					<td>${board.sitter}</td>
					<td>${board.createId}</td>
					<td>${board.title}</td>
					<td>${board.viewCount}</td>
					<td>${board.createDt}</td>
					<td><a href="reviewview.do?id=${board.boardId}">보기</a></td>
				</tr>
			</c:forEach>	
		</tbody>	
	</table>
	<ul>
		<c:if test="${currentPage > 1}">		
				<button type="button" onclick="search(${currentPage - 1},false)" >&raquo;</button>
		</c:if>
		<c:forEach begin="1" end="${totalPages}" var="i">
			    <button type="button" onclick="search(${i}, false)"
			       <c:if test="${i == currentPage}">style="font-weight: bold;"</c:if>> 
			       ${i} 
			    </button>
		</c:forEach>
		
		<c:if test="${currentPage < totalPages}">
				<button type="button" onclick="search(${currentPage + 1},false)" >&raquo;</button>
		</c:if>		
	</ul>
	<div class="searchBox">
		<select name="searchColumn" id="searchColumn">
		    <option value="TITLE" selected>제목</option>
		    <option value="CREATE_ID">작성자</option>
		</select>
	
	    <input class="search" type="text" id="searchText" name="searchText" value="${reservation}">
	    <button type="button" id="searchBtn" class="searchBtn">검색</button>
    </div>
</body>
</html>
