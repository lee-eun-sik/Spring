<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>펫사진 공유게시판</title>
  <script src="/js/jquery-3.7.1.min.js"></script>
<script src="/js/common.js"></script>


<link rel="preconnect" href="https://fonts.gstatic.com">
<link href="https://fonts.googleapis.com/css2?family=Do+Hyeon&family=Nanum+Myeongjo&family=Stylish&display=swap" rel="stylesheet">
</head>
<script>
$(document).ready(function () {
	
	$("#searchBtn").click(function () {
		search(1,true);
	});
});

function search(page,checkNow) {
	if(checkNow) {
		let searchText = $("#searchText").val();

		window.location.href="/pet/petPictureList.do?"
							+"searchText="+searchText+"&"
							+"page="+page+"&"
							+"size=${size}";
	} else {
		let searchText = '${board.searchText}';
		window.location.href="/pet/petPictureList.do?"
							+"searchText="+searchText+"&"
							+"page="+page+"&"
							+"size=${size}";
		}
	}	
</script>

<jsp:include page="/WEB-INF/jsp/common/header.jsp"></jsp:include>
<link rel="stylesheet" href="/css/petPictureList.css">
<body>
  <h2>펫사진 공유게시판</h2>
  
  <a href="/pet/petPictureCreate.do" class="createbtn">게시글 작성</a>

  <div class="gallery-container">
  <c:forEach var="post" items="${postList}">
    <div class="photo-box">
      <c:if test="${not empty post.imagePath}">

        <img src="${pageContext.request.contextPath}/images/${post.imagePath}" alt="펫사진"/>
      </c:if>
      <div class="overlay">
        <div>${post.petName}</div>
        <br/>
        <br/>
        <div>${post.createId}</div>
        <br/>
        <br/>
        <div>${post.content}</div>
      </div>
    </div>
  </c:forEach>
</div>



	<div class="search-container">
	  <label for="searchText"></label>
	  <input type="text" id="searchText" name="searchText" value="${board.searchText}" placeholder="아이디를 검색하세요">	
	  <button type="button" id="searchBtn" class="icon-button">🔍</button>
	</div>
	
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


</body>
</html>

