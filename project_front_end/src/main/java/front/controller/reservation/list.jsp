<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>예약신청목록</title>
<script src="/js/jquery-3.7.1.min.js"></script>
<script src="/js/common.js"></script>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"></jsp:include>
<link rel="stylesheet" href="/css/reservationList.css" >
<script>


			    function url(page, viewcategory){
			    	let searchText = $("#searchText").val();
			    	let searchColumn = $("#searchColumn").val();
					
					
		        	window.location.href = "/reservation/list.do?"+
		        	"searchText=" + searchText + "&" +
		        	"searchColumn=" + searchColumn + "&" +
		            "viewcategory=" + viewcategory + "&" +
		            "page=" + page + "&" +
		            "size=${size}";
					if(viewcategory==null){
						window.location.href = "/reservation/list.do?"+
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
			    function sortByCategory(page,viewcategory) {
			        if (viewcategory === "추천순") {
			        	url(1,viewcategory);
			        } else if (viewcategory === "최신순") {
			        	url(1,viewcategory);
			        } else if (viewcategory === "낮은 가격순") {
			        	url(1,viewcategory);
			        } else if (viewcategory === "높은 가격순") {
			        	url(1,viewcategory);
			        }
			    }
			    
		    	$(document).ready(function() {
		    		$("#searchBtn").click(function() {
		    			searchname(1, true);
		    		});
		    		$("#viewcategory").change(function() {
		    	        let selectedValue = $(this).val();
		    	        sortByCategory(1,selectedValue);
		    	    });
		    		
		    	
			  		
			  	
			  		
		    	});
		    	

	
		 function accept(boardId, acceptYN) {

			   ajaxRequest(
						"/reservation/accept.do",
					   {boardId: boardId,
							updateId: "${sessionScope.user.userId}",
							accept:acceptYN,
							reason:$("#reason_"+boardId).val()
                            },
					    function(response) {
						  if (response.success) {
							  if(acceptYN == "Y") {
								  alert(" 예약 수락되었습니다.");
							  } else {
								  alert(" 예약 거절되었습니다.");
							  }
								
							  $("#searchBtn").click();
								  //window.location.href =  '/user/login.do';
							} else {
									 alert("예약거절 실패." + response.message);
						           	}
			             	}							
						);
		 }
    </script>
</head>
<body>
	<h2>예약신청 목록</h2>
	<table border="1">
		<thead>
			<tr>
				<th class="num">순번</th>
				<th class="num">아이디</th>
				<th>날짜</th>
				<th>주소</th>
				<th class="num">품종</th>
				<th class="num">펫이름</th>
				<th>전화번호</th>
				<th class="num">펫시터</th>
				<th class="num2">가격</th>
				
				<th>상세</th>
				<th>수락,거절</th>
				<th>예약상태</th>
				<th>사유</th>
				
			</tr>
		</thead>
		<tbody >
		<!-- 게시글 목록을 반복 출력 -->
			<c:forEach var="reservation"  items="${reservationList}">
				<tr>
				<!-- 각각의 게시글 정보를 출력 -->
					<td>${reservation.rn}</td>
					<td>${reservation.createId}</td> <!-- 게시글의 순번 (DB에서 ROW_NUMBER()로 생성된 값) -->
					<td>${fn:substring(reservation.startDate, 0, 10)}</br>
					 &nbsp;&nbsp;&nbsp;~ ${fn:substring(reservation.endDate, 0, 10)}</td> <!-- 게시글 제목 -->
					<td>${reservation.address}</td> <!-- 게시글 작성자 ID -->
					<td>${reservation.variety}</td> <!-- 조회수 -->
					<td>${reservation.petName}</td> <!-- 작성 날짜 -->
					<td>${reservation.phoneNumber}</td>
					<td>${reservation.sitter}</td>
					<td>${reservation.price}원</td>
					<!-- 상세보기 링크 (게시글 ID를 URL 파라미터로 전달) -->
					<td><a class="bt1" href="view.do?id=${reservation.boardId}">보기</a></td>
					<td class="num3"><button type="button" id="acceptBtn" class="searchBtn" onclick="accept('${reservation.boardId}', 'Y')">수락</button>
					&nbsp;&nbsp;&nbsp;<button type="button" id="refusalBtn" class="searchBtn" onclick="accept('${reservation.boardId}', 'N')">거절</button></td>
					<td>
					<c:if test='${reservation.accept  == "N"}'>
				 	거절
					</c:if>
					<c:if test='${reservation.accept  == "Y"}'>
				 	수락
					</c:if>
					<c:if test='${reservation.accept  == ""}'>
				 	선택
					</c:if>
					</td>
					<td><input type="text" id="reason_${reservation.boardId}" name="reason_${reservation.boardId}" value="${reservation.reason}"></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<!-- 페이지 네비게이션 (페이징 기능) -->
	<ul>
	<!-- 이전 페이지로 이동 버튼 -->
		<c:if test=" ${currentPage  > 1}">
		<!-- 현재 페이지가 1보다 클 때만 표시 -->
			<a class="paginationbt" href="list.do?page=${currentPage-1}&size=${size}">&laquo;</a>
			<!-- '&laquo;'는 왼쪽 화살표 기호로 이전 페이지 표시 -->
		</c:if>
		<!-- 페이지 번호 목록 출력 -->
		<c:forEach begin="1" end="${totalPage}" var="i">
		<!-- 현재 페이지와 동일하면 굵은 글씨로 표시 -->
			<a class="paginationbt"  href="list.do?page=${i}&size=${size}"   			
				<c:if test="${i == currentPage }"> style="font-weight : bold;"</c:if>
				>${i}</a>				
		</c:forEach>
		<!-- 다음 페이지로 이동 버튼 -->
		<c:if test="${currentPage < totalpages }">
		<!-- 현재 페이지가 마지막 페이지보다 작을 때만 표시 -->
			<a class="paginationbt"  href="list.do?page=${currentPage+1}&size=${size}">&raquo;</a>
			<!-- '&raquo;'는 오른쪽 화살표 기호로 다음 페이지 표시 -->
		</c:if>
	</ul>
	
	<div class="search-container">
		<select name="searchColumn" id="searchColumn">
		    <option value="VARIETY">품종</option>
		    <option value="SITTER">펫시터</option>
		    <option value="CREATE_ID">작성자</option>
		</select>	
	<input class="search" type="text" id="searchText" name="searchText" value="${reservation}">
	<button type="button" id="searchBtn" class="icon-button">
	🔍
	</button>
	</div>
</body>
</html>