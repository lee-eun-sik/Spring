<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head> 
    <meta charset="UTF-8">
    <title>회원정보</title>
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <link rel="stylesheet" href="/css/userInfo.css?ver=1">
</head>

<script>


</script>
<body>
	
	<div class="findid">
	<form id="joinForm">
	<div>
         <a href="./main.do" >
         <img src="../images/pet.png" class="logo"></a>
   	</div>
        <h3 class="h3">회원정보</h3>
            <div>
                <label for="userId">아이디:</label>
                <span class="value">${sessionScope.user.userId}</span>
            </div>
            <br/>
            <div>
                <label for="password">비밀번호:</label>
                <span class="value">${sessionScope.user.password}</span>
            </div>
            <br/>
            <div>
                <label for="username">이름:</label>
                <span class="value">${sessionScope.user.username}</span>
            </div>
            <br/>
            <div>
                <label for="password">성별:</label>
                <span class="value">${user.gender}</span>
            </div>
            <br/>
            <div>
                <label for="password">전화번호:</label>
                <span class="value">${user.phonenumber}</span>
            </div>
            <br/>
            <div>
                <label for="email">이메일:</label>
                <span class="value">${user.email}</span>
            </div>
            <br/>

        </form>
        <div class="button-container">
            <a href="/user/main.do">
                <button type="button">메인 페이지로 이동</button>
            </a>
            <a href="/reservation/list.do">
                <button type="button">예약관리</button>
            </a>
            	
        </div>
    </div>
</body>
</html>
