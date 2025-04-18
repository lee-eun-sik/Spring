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
<script src="/js/jquery-3.7.1.min.js"></script>
<script src="/js/common.js?ver=1"></script>
<link rel="stylesheet" href="/css/join.css?ver=1" >
<script type="text/javascript">

	$(document).ready(function () {
		$("#joinForm").submit(function (event) {
			event.preventDefault();
			let userId = $("#userId").val().trim();
			let username = $("#username").val().trim();
			
			let password = $("#password").val().trim();
			let password_confirm = $("#password_confirm").val().trim();
			let gender = $("#input[name='gender']:checked").val();
			
			let email = $("#email").val().trim();
			
			let phone = $("#phonenumber").val();
			
			let birthdate = $("#birthdate").val().trim();
			
			
			if (!validationUtil.isEmpty(userId)) {
				alert("아이디를 입력해주세요.");
				$("#userId").focus();
				return;
			}
			if (!validationUtil.maxLength(userId, 20)) {
				alert("아이디는 최대 20자까지 입력할 수 있습니다.");
				$("#userId").focus();
				return;
			}
			
			if (!validationUtil.isEmpty(username)) {
				alert("이름을 입력해주세요.");
				$("#username").focus();
				return;
			}
			if (!validationUtil.maxLength(username, 15)) {
				alert("이름은 최대 15자까지 입력할 수 있습니다.");
				$("#username").focus();
				return;
			}
			
			if (!validationUtil.isEmpty(password)) {
				alert("비밀번호를 입력해주세요.");
				$("#password").focus();
				return;
			}
			if (!validationUtil.maxLength(password, 20)) {
				alert("비밀번호는 최대 20자까지 입력할 수 있습니다.");
				$("#password").focus();
				return;
			}
			
			if (!validationUtil.isEmpty(email)) {
				alert("이메일을 입력해주세요.");
				$("#email").focus();
				return;
			}
			if (!validationUtil.maxLength(email, 100)) {
				alert("이메일은 최대 100자까지 입력할 수 있습니다.");
				$("#email").focus();
				return;
			}
			
			//이메일 형식 체크
			if (!validationUtil.isEmail(email)) {
				alert("올바른 이메일 주소를 입력해주세요.");
				$("#email").focus();
				return;
			}
			//모든 검증 통과 후 AJAX 요청
			
				ajaxRequest(
						"/user/register.do",
						$("#joinForm").serialize(),
						function (response) {
							console.log(response);
							if (response.success) {
								alert("회원가입 성공하셨습니다. "
										+"로그인 페이지로 이동합니다.");
								window.location.href = "/user/login.do";
							} else {
								alert("회원가입 실패하셨습니다.");
							}
						}
				);
			
		});
		
		
		 $('#registerCancel').click(function() {
	            // 이동할 URL을 설정
	            window.location.href = "/user/login.do";  // 원하는 URL로 수정
	        });
	});
</script>
</head>
<body>
			<div class="findid">
                <div>
                    <a href="./main.do" >
                    <img src="../images/pet.png" class="logo"></a>
                </div>
                <div class="h3">회원가입</div>
                <form id="joinForm">
	                <div class="form">
	                	<label for="userId">*아이디</label>
						<input type="text" id="userId" name="userId" maxlength="20"  required/> 

						<label for="password">*비밀번호</label>
						<input type="password" id="password" name="password" maxlength="20"required/>

						<label for="password_confirm">*비밀번호확인</label>
						<input type="password" id="password_confirm" name="password_confirm" maxlength="20" required/>
	
						<label for="username">*이름</label>
						<input type="text" id="username" name="username" maxlength="15" required/>

						<div class="gender-group">
						    <label>성별</label>
						    <input type="radio" name="gender" value="male">남자
						    <input type="radio" name="gender" value="female">여자
						</div>
						<label for="phone">*전화번호</label>
						<input type="tel" name="phonenumber"  required>
				
						<label for="email">*이메일</label>
						<input type="email" id="email" name="email" maxlength="100"required/>
				
						<label for="birthdate">*생년월일</label>
						<input type="date" id="birthdate" name="birthdate" required/><br>
					</div>
					<div class="button-container">
						<button type="submit" id="joinBtn">가입하기</button>
						<button type="button" id="registerCancel">가입취소</button>
					</div>
                </form>
            </div>




</body>
</html>