<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko"> 
<head>
<meta charset="UTF-8">
<title>예약신청 상세</title>
<script src="/js/jquery-3.7.1.min.js"></script>
<script src="/js/common.js"></script>
<link rel="stylesheet" href="/css/view2.css?ver=1.2" >
<jsp:include page="/WEB-INF/jsp/common/header.jsp"></jsp:include>

</head>
<body>
	<div class="contentbox">
	<h2>예약내용 상세보기</h2>
	<form id="reservationForm">
			<label for="createId">작성자:</label> ${reservation.createId} <br /> 
			<label for="createDt">예약신청일:</label> ${reservation.createDt} <br /> 
			<label for="petName">펫네임:</label> ${reservation.petName} <br />
			<label for="variety">품종:</label> ${reservation.variety} <br />
			<label for="phoneNumber">휴대폰번호:</label> ${reservation.phoneNumber} <br />
			<label for="reservationDate">신청기간:</label> ${reservation.reservationDate}일 <br />
			<label for="price">가격:</label> ${reservation.price}원 <br /> 
			<label for="address">주소:</label> ${reservation.address},${reservation.addressDetail} <br /> 
			<label for="reply">문의사항:</label>${reservation.reply} <br />
	</form>
	</div>
	<c:if test="${not empty reservation.postFiles}">
		<ul>
			<c:forEach var="file" items="${reservation.postFiles}">
				<li>${file.fileName} <a
					href="/file/down.do?fileId=${file.fileId}">다운로드</a>
				</li>
			</c:forEach>
		</ul>
	</c:if>
	
	
 
</body>
</html>