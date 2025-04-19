<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head> 
    <meta charset="UTF-8">
    <title>게시글 수정</title>
    <script src="/js/jquery-3.7.1.min.js?ver=1"></script>
    <script src="/js/tinymce/tinymce.min.js?ver=1"></script>
    <script src="/js/edit.js?ver=1"></script>
	<script src="/js/common.js?ver=1.3"></script>
	<link rel="stylesheet" href="/css/update.css?ver=1" >
	<script>
	$(document).ready(function () {
		
		let edit = editInit("content");
		
		let fileObj = setupFileUploadUpdate({
            dropZone: '#dropZone',
            fileInput: '#files',
            newFileList: '#newFileList',
            existingFileList: '#existingFileList',
            remainingFileIds: '#remainingFileIds',
            maxFileSize: 10 * 1024 * 1024 // 10MB 제한
        });

		
		$("#updateBtn").click(function (event) {

			event.preventDefault();

			let title = $("#title").val().trim();
			let content = tinymce.get('content').getContent({format: 'text'});
			//let content = $("#content").val().trim();

			if (!validationUtil.isEmpty(title)) {
				alert("제목을 입력해주세요.");
				$("#title").focus();
				return;
			}
			if (!validationUtil.maxLength(title, 100)) {
				alert("제목은 최대 100자까지 입력할 수 있습니다.");
				$("#title").focus();
				return;
			}
			
			if (!validationUtil.isEmpty(content)) {
				alert("내용을 입력해주세요.");
				$("#content").focus();
				return;
			}
			if (!validationUtil.maxLength(content, 2000)) {
				alert("내용은 최대 2000자까지 입력할 수 있습니다.");
				$("#content").focus();
				return;
			}

			$("#content").val(tinymce.get('content').getContent());
			
			let formData = new FormData();
			formData.append("boardId", $("#boardId").val());
			formData.append("title", title);
			formData.append("content", tinymce.get('content').getContent());
			formData.append("updateId", $("#updateId").val().trim());
			formData.append("viewCount", $("#viewCount").val());
			
			let uploadedFiles = fileObj.getUploadedFiles();
			//선택된 파일 추가
			for (let i = 0; i < uploadedFiles.length; i++) {
				formData.append("files", uploadedFiles[i]);
			}
			
			fileObj.updateRemainingFileIds();
			formData.append("remainingFileIds", $('#remainingFileIds').val());
			
			ajaxRequestFile(
						"/newsboard/newsboardupdate.do",
						formData,
						function (response) {
							if (response.success) {
								alert("게시글 수정 성공");
								window.location.href = "/newsboard/newsboardview.do?id="+$("#boardId").val();
							} else {
								alert("게시글 수정 실패" + response.message);
							}
						}
			);
			
		});
	});
</script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"></jsp:include>
	<h2>게시판 수정</h2>
	<form method="post" id="boardUpdateForm">
	
	  <label for="title">제목:</label>
	  <input type="text" id="title" name="title" value="${board.title}" maxlength="100" placeholder="제목 입력" required />
	  <br/>
	  <label for="content">내용:</label>
	  <textarea rows="5" cols="40" id="content" name="content" placeholder="내용 입력" >${board.content}</textarea>
	  <br/>
	  <input type="hidden" id="remainingFileIds" name="remainingFileIds"  value="" />
	  
	  <input type="hidden" id="updateId" name="updateId"  value="${sessionScope.user.userId}" />
	  <input type="hidden" id="viewCount" name="viewCount"  value="${board.viewCount}" />
	  <input type="hidden" id="boardId" name="boardId"  value="${board.boardId}" />
	</form>
	
	<!-- 드래그 앤 드롭 파일 업로드 영역 -->
      <div id="dropZone">여기로 파일을 드래그하거나 클릭하여 선택하세요.</div>
      <input type="file" id="files" name="files" multiple style="display: none;"> <!-- 숨겨진 파일 선택 input -->
      <c:if test="${not empty board.postFiles}">
		<ul id="existingFileList">
			<c:forEach var="file" items="${board.postFiles}">
				<li>
					${file.fileName}
					<button name="removeBtn" data-file="${file.fileId}">제거</button>
					<a href="/newsboardfile/down.do?fileId=${file.fileId}">다운로드</a>
				</li>
			</c:forEach>
		</ul>
	</c:if>	
      <ul id="newFileList"></ul> <!-- 업로드한 파일 목록 표시 -->
	
	  <button type="submit" id="updateBtn">게시글 수정</button>
</body>
</html>