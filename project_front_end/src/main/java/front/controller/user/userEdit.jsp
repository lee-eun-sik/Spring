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
    <script>
    $("#userEditBtn").click(function (event) {

		event.preventDefault();

		let title = $("#title").val().trim();
		let content = tinymce.get('content').getContent({format: 'text'});
		//let content = $("#content").val().trim();

		

		$("#content").val(tinymce.get('content').getContent());
		
		let formData = new FormData();
		formData.append("boardId", $("#userId").val());
		formData.append("title", title);
		formData.append("content", tinymce.get('content').getContent());
		formData.append("updateId", $("#updateId").val().trim());
		formData.append("viewCount", 0);
		
		let uploadedFiles = fileObj.getUploadedFiles();
		//선택된 파일 추가
		for (let i = 0; i < uploadedFiles.length; i++) {
			formData.append("files", uploadedFiles[i]);
		}
		
		fileObj.updateRemainingFileIds();
		formData.append("remainingFileIds", $('#remainingFileIds').val());
		
		ajaxRequestFile(
					"/board/update.do",
					formData,
					function (response) {
						if (response.success) {
							alert("게시글 수정 성공");
							window.location.href = "/board/list.do";
						} else {
							alert("게시글 수정 실패" + response.message);
						}
					}
		);
		
	});
    </script>
</head>
<body>
	<h2>회원정보</h2>
	<form id="userForm">
	  <label for="userId">아이디:</label>
	  <input type="text" id="userId" name="userId" maxlength="20" value="${sessionScope.user.userId}" readonly>
	  <br/>
	  <label for="username">이름:</label>
	  <input type="text" id="username" name="username" maxlength="20" value="${sessionScope.user.username}" required>
	  <br/>
	  <label for="password">비밀번호:</label>
	  <input type="text" id="password" name="password" maxlength="20" value="${sessionScope.user.password}" required>
	  <br/>
	  <label for="email">이메일:</label>
	  <input type="email" id="email" name="email" maxlength="20" value="${sessionScope.user.email}" required>
	  <br/>
	  <button type="submit" id="userEditBtn">회원정보 수정하기</button>
	</form>
</body>
</html>