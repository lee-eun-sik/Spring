<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html> 
<head>
<meta charset="UTF-8">
<title>게시글 작성</title>

<script src="/js/jquery-3.7.1.min.js?ver=1"></script>
<script src="/js/tinymce/tinymce.min.js?ver=1"></script>
<script src="/js/edit.js?ver=1"></script>
<script src="/js/common.js?ver=1"></script>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/freeBoard/freeUpdate.css">


</head>
<body>
	<h2>게시글 수정</h2>
	<form method="post" id="freeBoardUpdateForm">

		<label for="title">제 목:</label>
		 <input type="text" name="title" id="title" value=" ${freeBoard.title}" maxlength="100" placeholder="제목 입력" required><br /> <br /> 
		<label for="content">내용 :</label>
		<textarea rows="5" cols="40" id="content" name="content"> ${freeBoard.content}</textarea><br /> 
		<input type="hidden" id="remainingFileIds" name="remainingFileIds" value=""> 
		<input type="hidden" 	name="updateId" id="updateId" value=" ${sessionScope.user.userId}" />
		<input type="hidden" name="viewCount" id="viewCount" 	value="${freeBoard.viewCount}" />
		<input type="hidden" name="boardId"  id="boardId"  value="${freeBoard.boardId}" />
	</form>

	<c:if test="${not empty freeBoard.postFiles}">
		<ul id="existingFileList">
			<c:forEach var="file" items="${freeBoard.postFiles}">
				<li>${file.fileName}
					<button name="removeBtn" data-file="${file.fileId}">제거</button>
					<a href="/file/down.do?fileId=${file.fileId}">다운로드</a>
				</li>
			</c:forEach>
		</ul>
	</c:if>

	<div id="dropZone">여기로 파일을 드래그하거나 클릭하여 선택하세요.</div>
	<input type="file" id="files" name="files" multiple
		style="display: none;">

	<ul id="newFileList"></ul>

	<div class="btcontainer">
	<button type="button" id="updateBtn" class="updateBtn">게시글 수정</button>
	</div>
	<a href="/user/login.do">로그인 페이지로 이동</a>

	<script type="text/javascript">	
	
		$(document).ready(function() {
			// TinyMCE 에디터 초기화
			let edit = editInit("content");
			
			let fileObj = setupFileUploadUpdate({
                dropZone: '#dropZone',
                fileInput: '#files',
                newFileList: '#newFileList',
                existingFileList: '#existingFileList',
                remainingFileIds: '#remainingFileIds',
                maxFileSize: 10 * 1024 * 1024 // 10MB 제한
            });
			
			 
	    
	    $("#updateBtn").click(function(event) {
	        event.preventDefault(); // 기본 폼 제출 방지
					
	        let title = $("#title").val().trim();
	        let content = tinymce.get('content').getContent({format: 'text'});	 // 내용 (텍스트 형식) 
			//let content = $("#content").val().trim();
				if (!validationUtil.isEmpty(title)) {
					alert("제목을 입력해주세요");
					$("#title").focus();
				　return;
					}
				if (!validationUtil.maxLength(title, 100)) {
					alert("제목은 최대 100자 까지 입력 할 수 있습니다");
					$("#title").focus();
					return;
				}
				if (!validationUtil.isEmpty(content)) {
					alert("내용을 입력해주세요");
					$("#content").focus();
					return;
				}
				if (!validationUtil.maxLength(content, 2000)) {
					alert("내용은 최대 2000자 까지 입력 할 수 있습니다");
					$("#content").focus();
					return;
				}	
				
         	let formData = new FormData();
         	    formData.append("boardId", $("#boardId").val());
				formData.append("title",title);
				formData.append("content",tinymce.get('content').getContent());
				formData.append("updateId",$("#updateId").val().trim());
				formData.append("viewCount",0);
				
				let uploadedFiles = fileObj.getUploadedFiles();
				
		        for (let i = 0; i < uploadedFiles.length; i++) {
                    formData.append("files", uploadedFiles[i]);
                }
		        
		        fileObj.updateRemainingFileIds();
		        formData.append("remainingFileIds", $('#remainingFileIds').val());
		        
		        ajaxRequestFile(
		        		"/freeBoard/update.do",
		        		formData,
		        		function (response){
                                 if(response.success){
                                        alert("게시글 수정 성공! 게시판 목록으로 이동합니다");
                                        window.location.href = "/freeBoard/view.do?id="+$("#boardId").val();
                                  } else {
                                         alert("게시글 수정 실패: "+ response.message);
                                      }
                                }
		        		);                          
				});
	    });
    </script>

</body>
</html>
