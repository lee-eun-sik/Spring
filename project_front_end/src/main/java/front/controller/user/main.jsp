<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<style>

.MainLogo {
    width: 100% !important;
    height: 800px !important;  /* 화면의 전체 높이에 맞게 설정 (100vh는 viewport의 100%) */
    overflow: hidden;  /* To prevent any overflow issues if the image is larger */
}

.logopic {
    width: 100% !important;  /* Ensures the image takes up the full width */
    height: 100% !important; /* Maintains the aspect ratio */
    object-fit: cover !important;
}
</style>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"></jsp:include>
<script>
				$(document).ready(function() {
					//로그인 폼에 섬밋이벤트시 작동
					$("#logout").submit(function(event) {
						event.preventDefault(); // 기본 폼 제출 방지
						$.ajax({
							url : '/user/logout.do', // 로그아웃 url
							type : 'POST',
							data : $(this).serialize(),
							dataType : 'json',
							success:  function(response) {
								//응답 처리
								if (response.success) {
									alert("로그아웃 완료했습니다.");
									window.location.href = "/user/login.do";
								} else {
									alert("오류");
								}
							},
							error : function() {
								alert("통신 실패");
							}
						
						})
						
						
					})
				})
			</script>

<br/>

<div class="MainLogo">
	<div class="headers">
		<a href="/user/main.do"><img src="/images/mainlogo.png" alt="로고" class="logopic"></a>
	</div>
</div>

</body>
</html>