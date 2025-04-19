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
<script src="/js/common.js?ver=1.1"></script>
<link rel="stylesheet" href="/css/create.css?ver=1" >


</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"></jsp:include>
<h2>게시글 작성</h2>
	<form method="post" id="boardCreateForm">

		<label for="title">제 목:</label> 
		<input type="text" name="title" id="title" maxlength="100" placeholder="제목 입력"  required><br/>
        <br/>
		<label for="content">내용 :</label> 
		<textarea rows="5" cols="40"  id="content" name="content"></textarea>
		<br/>	
		<!-- 드래그 앤 드롭 파일 업로드 영역 -->
      <div id="dropZone">여기로 파일을 드래그하거나 클릭하여 선택하세요.</div>
       <input type="file" id="files" name="files" multiple style="display: none;"> <!-- 숨겨진 파일 선택 input -->
       <ul class="file-list" id="fileList"></ul> <!-- 업로드한 파일 목록 표시 -->				
		<input type="hidden" name="createId" id="createId" value=" ${sessionScope.user.userId}"/>
		<input type="hidden" name="viewCount" id="viewCount" value="0"/>
		
			<button type="submit" id="registerBtn">게시글 등록</button>
		
	</form>
	<script type="text/javascript">
		$(document).ready(function() {
			// TinyMCE 에디터 초기화
			let edit = editInit("content");
			
			let uploadedFiles = setupFileUpload({
                dropZone: '#dropZone',
                fileInput: '#files',
                fileList: '#fileList',
                maxFileSize: 50 * 1024 * 1024 // 50MB 제한
            });
	    
	    $("#boardCreateForm").submit(function(event) {
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
				
				//$("#content").val(tinymce.get('content').getContent()); // 에디터의 HTML 내용을 폼 데이터로 변환
	
				let formData = new FormData();
				
				formData.append("title",title);
				formData.append("content",tinymce.get('content').getContent());
				formData.append("createId",$("#createId").val().trim());
				formData.append("viewCount",0);
				
		        for (let i = 0; i < uploadedFiles.length; i++) {
                    formData.append("files", uploadedFiles[i]);
                }
	       
				ajaxRequestFile(
						"/freeBoard/create.do",
					   formData,
					    function(response) {
							  if (response.success){
									  alert("게시글 성공 !!! 게시판으로 이동합니다.");
									  window.location.href = "/freeBoard/list.do";
									} else {
											 alert("게시글 생성 실패." + response.message);
								           	}
					             	}
						
						);
                    
				});
	    });
    </script>


</body>
</html>
