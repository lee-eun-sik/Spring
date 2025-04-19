<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="/js/jquery-3.7.1.min.js"></script>

<style> 

    .container2 {
        width: 1050px;
	    margin: 100px auto 70px auto; /* 위 80px, 좌우 자동, 아래 80px */
	    background-color: pink;
	    padding: 20px;
	    border-radius: 10px;
	    text-align: center;
	    box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.1); /* 살짝 그림자 효과로 띄운 느낌 */
    }
    .sitter-box2 {
	    background-color: white;
	    border: 2px solid #aaa;
	    padding: 15px;
	    margin-bottom: 15px;
	    border-radius: 15px;
	    display: flex;
	    flex-wrap: nowrap; /* 줄바꿈 방지 */
	    justify-content: flex-start;
	    align-items: center;
	    overflow-x: auto; /* 내용이 길면 가로 스크롤 */
	    white-space: nowrap; /* 줄바꿈 방지 */
	    gap: 40px; /* 이름과 내용 사이 넓게 띄움 */
	}
    .sitter-label {
        font-weight: bold;
    }
    
    .sitter-line {
        margin-bottom: 10px; /* 줄 간 여백 */
    }
    .sitter-name {
        font-weight: bold;
	    min-width: 150px;
	    flex-shrink: 0;
    }
    
	.sitter-content {
	    flex-shrink: 0;
	}
</style>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"></jsp:include>
<script type="text/javascript">

$(document).ready(function() {
			$("#background").hide(); // 헤더 숨기기
			$("#p").hide(); // 단락 숨기기
			$("#p1").hide(); //단락 숨기기
			
			
			
		});
	</script>	

<div class="container2">
    <h2>펫 시터 소개</h2>

    <c:forEach var="petSitter" items="${petSitterList}">
	    <div class="sitter-box2">
	        <span class="sitter-name">${petSitter.sitterName}</span>
	        <span class="sitter-content">${petSitter.content}</span>
	    </div>
	</c:forEach>

</div>



</body>
</html>