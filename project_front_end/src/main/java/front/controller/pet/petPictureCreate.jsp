<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>펫 사진 공유게시판 글쓰기</title>
	<script src="/js/jquery-3.7.1.min.js?ver=1"></script>
	<script src="/js/tinymce/tinymce.min.js?ver=1"></script>
	<script src="/js/edit.js?ver=1"></script>
	<script src="/js/common.js?ver=1.1"></script>
	
	<link rel="preconnect" href="https://fonts.gstatic.com">
	<link href="https://fonts.googleapis.com/css2?family=Do+Hyeon&family=Nanum+Myeongjo&family=Stylish&display=swap" rel="stylesheet">
	<link rel="stylesheet" href="/css/petPictureCreate.css">
	<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("/user/login.do");
        return;
    }
	%>
	
	<script>
        $(document).ready(function () {
        	tinymce.init({
    	        selector: '#content',
    	        height: 300,
    	        menubar: false,
    	        plugins: 'lists image',
    	        toolbar: 'undo redo | formatselect | bold italic | alignleft aligncenter alignright | bullist numlist | image'
    	    });

        	let uploadedFiles = [];

    	    function handleFiles(files) {
    	        for (let file of files) {
    	            if (!file.type.startsWith("image/")) {
    	                alert("이미지 파일만 업로드 가능합니다.");
    	                continue;
    	            }

    	            if (file.size > 50 * 1024 * 1024) {
    	                alert("파일 크기는 50MB를 초과할 수 없습니다.");
    	                continue;
    	            }

    	            uploadedFiles.push(file);
    	            previewImage(file);
    	        }
    	    }

    	    function previewImage(file) {
    	        let reader = new FileReader();
    	        reader.onload = function (e) {
    	            let img = $("<img>").attr("src", e.target.result).addClass("preview-img");
    	            let removeBtn = $("<button>").text("X").addClass("remove-btn");

    	            let container = $("<div>").addClass("preview-item").append(img, removeBtn);
    	            $("#previewContainer").append(container);

    	            removeBtn.on("click", function () {
    	                let index = uploadedFiles.indexOf(file);
    	                if (index > -1) {
    	                    uploadedFiles.splice(index, 1);
    	                }
    	                container.remove();
    	            });
    	        };
    	        reader.readAsDataURL(file);
    	    }

    	    $("#files").on("change", function (event) {
    	        handleFiles(event.target.files);
    	    });

    	    $("#dropZone").on("dragover", function (event) {
    	        event.preventDefault();
    	        event.stopPropagation();
    	        $(this).addClass("dragover");
    	    });

    	    $("#dropZone").on("dragleave", function () {
    	        $(this).removeClass("dragover");
    	    });

    	    $("#dropZone").on("click", function () {
    	    	$("#files").click();
    	    });
			
    	 	// ⭐ 폼 제출 처리 ⭐
            $("#boardCreateForm").submit(function (event) {
                event.preventDefault();
                
                let petName = $("#petName").val().trim();
                let content = tinymce.get('content').getContent({format: 'text'}); 

                if(!validationUtil.isEmpty(petName)) {
                	alert("펫 이름을 입력해주세요.");
                	$("#petName").focus();
                	return;
                }
                if(!validationUtil.maxLength(petName,50)) {
                	alert("펫 이름은 최대 50자입니다.");
                	$("#petName").focus();
                	return;
                }
                if(!validationUtil.isEmpty(content)) {
                	alert("설명을 입력해주세요.");
                	$("#content").focus();
                	return;
                }
                if(!validationUtil.maxLength(content,2000)) {
                	alert("설명은 최대 2000자입니다.");
                	$("#content").focus();
                	return;
                }

                let formData = new FormData();
                formData.append("petName", petName);
              	formData.append("content", content);
                formData.append("createId", $("#createId").val().trim());
              
                
             	// 선택된 파일 추가
                for (let i = 0; i < uploadedFiles.length; i++) {
                    formData.append("files", uploadedFiles[i]);
                }
                
             	// AJAX 요청
                $.ajax({
    	            url: "/pet/petPictureCreate.do",
    	            type: "POST",
    	            data: formData,
    	            processData: false,
    	            contentType: false,
    	            success: function (response) {
    	            	console.log(response);
    	                if (response.success) {
    	                    alert("게시글 생성 성공! 게시판 목록으로 이동합니다.");
    	                    window.location.href = "/pet/petPictureList.do";
    	                } else {
    	                    alert("게시글 생성 실패: " + response.message);
    	                }
    	            },
    	            error: function () {
    	                alert("서버 오류가 발생했습니다.");
                        }    
                    }
                );
            });
        });
    </script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"></jsp:include>

<h2>펫 사진 공유 글쓰기</h2>
<form id="boardCreateForm">
	<label for="petName">펫 이름:</label> 
	<input type="text" id="petName" name="petName" maxlength="100" placeholder="펫 이름을 입력해주세요" required /> <br />
	<label for="content">설명 :</label>
	<textarea rows="5" cols="40" id="content" name="content" placeholder="펫을 자랑해주세요"></textarea> <br />
	
	<!-- 미리보기 및 업로드 영역 -->
	<div id="previewContainer"></div>
  	<div id="dropZone">여기로 파일을 드래그하거나 클릭하여 선택하세요.</div>
  	<input type="file" id="files" name="files" multiple style="display: none;" />
  	
	<input type="hidden" id="createId" name="createId" value="${sessionScope.user.userId}" />
	<button type="submit" id="registerBtn">게시글 등록</button>
</form>
</body>
</html>
