<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<script src="/js/jquery-3.7.1.min.js"></script>
<script src="/js/tinymce/tinymce.min.js"></script>
<script src="/js/edit.js?ver=1"></script>
<script src="/js/common.js?ver=1.2"></script>
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
        	
        	$("#deleteBtn").click(function(event){
        		ajaxRequest(
        				"/board/delete.do",
        				{boardId:$("#boardId").val(),
        				 updateId:$("#updateId").val()
        	            },
        	            function(response){
        	            	if(response.success){
        	            		alert("게시글 삭제 성공! 게시판 목록으로 이동합니다.");
        	            		//window.location.href = "/board/list.do";
        	            	} else{
        	            		alert("게시글 삭제 실패 : "+response.message);
        	            	}
        	             }
                      );
                   });
            $("#updateBtn").click(function (event) {
                event.preventDefault(); // 기본 제출 방지
                
                let title = $("#title").val().trim();
                let content = tinymce.get('content').getContent({format: 'text'}); 
                //let content = $("#content").val().trim();

                
                if(!validationUtil.isEmpty(title)){
                	alert("제목을 입력해주세요.")
                	$("#title").focus();
                	return;
                }
                
                if(!validationUtil.maxLength(title, 100)){
                	alert("제목은 최대 100자까지 입력할 수 있습니다")
                	$("#title").focus();
                	return;
                }
                
                if(!validationUtil.isEmpty(content)){
                	alert("내용을 입력해주세요")
                	$("#content").focus();
                	return;
                }
                
                if(!validationUtil.maxLength(content, 2000)){
                	alert("내용은 최대 2000자까지 입력할 수 있습니다")
                	$("#content").focus();
                	return;
                }
                
               let formData = new FormData();
               formData.append("boardId", $("#boardId").val());
               formData.append("title", title);
               formData.append("content", tinymce.get('content').getContent());
               formData.append("updateId", $("#updateId").val().trim());
               formData.append("viewCount", 0);
              
               let uploadedFiles = fileObj.getUploadedFiles();
               // 선택된 파일 추가
               for (let i = 0; i < uploadedFiles.length; i++) {
            	   formData.append("files", uploadedFiles[i]);
               }
               fileObj.updateRemainingFileIds(); // 함수실행
               formData.append("remainingFileIds", $('#remainingFileIds').val()); // 셋팅하고 넘어감
               
           	   ajaxRequestFile(
           		       "/board/update.do",
           		       formData,
           		       function (response){
           		    	   if(response.success){
           		    		   alert("게시글 수정 성공! 게시판 목록으로 이동합니다.");
           		    				   //window.location.href = "/board/list.do";
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
	<h2>게시글 수정</h2>
	<form id="boardUpdateForm">
		<label for="title">제목:</label> 
		<input type="text" id="title" name="title" value="${board.title}" maxlength="100" placeholder="제목 입력" required /> <br />
		<br>
		<label for="content">내용:</label> 
		<textarea rows="5" cols="40" id="content" name="content">${board.content}</textarea>
		<br/>
		<input type="hidden" id="remainingFileIds" name="remainingFileIds" value="">
        <input type="hidden" id="updateId" name="updateId" value="${sessionScope.user.userId}"/>
        <input type="hidden" id="viewCount" name="viewCount" value="${board.viewCount}"/>
        <input type="hidden" id="boardId" name="boardId" value="${board.boardId}"/>
		
		
	</form>
	<!-- 이미 업로드된 파일 목록 -->
	<c:if test="${not empty board.postFiles}">
		<ul id="existingFileList">
			<c:forEach var="file" items="${board.postFiles}">
				<li>
					${file.fileName}
					<button name="removeBtn" data-file="${file.fileId}">제거</button>
					<a href="/file/down.do?fileId=${file.fileId}">다운로드</a>
				</li>
			</c:forEach>		
		</ul>
	</c:if>	
	<!-- 드래그 앤 드롭 파일 업로드 영역 -->
    <div id="dropZone">여기로 파일을 드래그하거나 클릭하여 선택하세요.</div>
    <input type="file" id="files" name="files" multiple style="display: none;"> <!-- 숨겨진 파일 선택 input -->
    <ul type="square" id="newFileList"></ul> <!-- 업로드한 파일 목록 표시 -->
	
	<button type="button" id="updateBtn">게시글 수정</button>
	<button type="button" id="deleteBtn">게시글 삭제</button>
	<a href="/user/login.do">로그인 페이지로 이동</a>
</body>
</html>

