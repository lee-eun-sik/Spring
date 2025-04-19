<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<link rel="stylesheet" href="/css/login.css?ver=1" >
</head>


<body>

	<div class="loginform">
	    <div>
	       <a href="./Main.html" >
	       <img src="../images/pet.png" class="logo"></a>
	    </div>
	    
		<form method="post" id="LoginForm">
			<label for="useid">아이디 :</label> 
			   <input type="text" name="id" id="useid"><br /> 
			<label for="usepwd">비밀번호 :</label> 
			   <input type="password" name="pass" id="usepwd"><br />
	
			<div class="button-group">
				<input type="submit" value="로그인" class="bt1"> 
			</div>
		</form>
		
		</br>
		
		<div class = "alink">
			&nbsp;
			<a href='/user/join.do'>회원가입</a>&nbsp;&nbsp;
			<a href='/user/findid.do'>아이디찾기</a>&nbsp;&nbsp;
			<a href='/user/findpsw.do'>비밀번호찾기</a>
		</div>
	</div>


<script>
		$(document).ready(function() {
			//로그인 폼에 섬밋이벤트시 작동
			$("#LoginForm").submit(function(event) {
				event.preventDefault(); // 기본 폼 제출 방지
				
				$.ajax({
					url : '/user/loginCheck.do', // 로그인 요청 url
					type : 'POST',
					data : $(this).serialize(),
					dataType : 'json',
					success:  function(response) {
						//응답 처리
						if (response.success) {
							alert("로그인에 성공하셨습니다.");
							window.location.href = "/user/main.do";
						} else {
							alert("로그인에 실패하셨습니다.");
						}
					},
					error : function() {
						alert("통신 실패");
					}
				
				})
				
				
			})
		})
	</script>

</body>
</html>