<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head> 
    <meta charset="UTF-8">
    <title>게시판 작성</title>
    <script src="/js/jquery-3.7.1.min.js?ver=1"></script>
    <script src="/js/tinymce/tinymce.min.js?ver=1"></script>
    <script src="/js/edit.js?ver=1"></script>
	<script src="/js/common.js?ver=1.1"></script>
	<link rel="stylesheet" href="/css/create.css?ver=1" >
	<script>
	$(document).ready(function () {
		
		let edit = editInit("content");
		
		let uploadedFiles = setupFileUpload({
            dropZone: '#dropZone',
            fileInput: '#files',
            fileList: '#fileList',
            maxFileSize: 50 * 1024 * 1024 // 50MB 제한
        });
		
		$("#boardCreateForm").submit(function (event) {

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

			// $("#content").val(tinymce.get('content').getContent());
			
			let formData = new FormData();
			formData.append("title", title);
			formData.append("content", tinymce.get('content').getContent());
			formData.append("content", content);
			formData.append("createId", $("#createId").val().trim());
			formData.append("viewCount", 0);
			
			// 선택된 파일 추가
            for (let i = 0; i < uploadedFiles.length; i++) {
                formData.append("files", uploadedFiles[i]);
            }
			

			ajaxRequestFile(
						"/newsboard/newsboardcreate.do",
						formData,
						function (response) {
							if (response.success) {
								alert("게시글 작성 성공");
								window.location.href = "/newsboard/newsboardlist.do";
							} else {
								alert("게시글 작성 실패" + response.message);
							}
						}
				);
			
		});
	});
</script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"></jsp:include>
	<h2>게시판 작성</h2>
	<form method="post" id="boardCreateForm">
	
	  <label for="title">제목:</label>
	  <input type="text" id="title" name="title" maxlength="100" placeholder="제목 입력" required />
	  <br/>
	  <label for="content">내용:</label>
	  <textarea rows="5" cols="40" id="content" name="content" placeholder="내용 입력" ></textarea>
	  <br/>
	  
	  <!-- 드래그 앤 드롭 파일 업로드 영역 -->
      <div id="dropZone">여기로 파일을 드래그하거나 클릭하여 선택하세요.</div>
      <input type="file" id="files" name="files" multiple style="display: none;"> <!-- 숨겨진 파일 선택 input -->
      <ul class="file-list" id="fileList"></ul> <!-- 업로드한 파일 목록 표시 -->
	  
	  <input type="hidden" id="createId" name="createId"  value="${sessionScope.user.userId}" />
	  <input type="hidden" id="viewCount" name="viewCount"  value="0" />
	  <button type="submit" id="registerBtn">게시글 작성</button>
	</form>
</body>
</html>