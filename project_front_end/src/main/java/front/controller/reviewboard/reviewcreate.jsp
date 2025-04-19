<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>펫시터후기작성</title>
	<script src="/js/jquery-3.7.1.min.js?ver=1"></script>
	<script src="/js/tinymce/tinymce.min.js?ver=1"></script>
	<script src="/js/edit.js?ver=1"></script>
	<script src="/js/common.js?ver=1.1"></script>
	
	<link rel="stylesheet" href="/css/petPictureCreate2.css">
	
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

	    // ⭐ 파일 선택 또는 드래그 앤 드롭 시 실행 ⭐
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

	    // ⭐ 미리보기 이미지 생성 함수 ⭐
	    function previewImage(file) {
	        let reader = new FileReader();
	        reader.onload = function (e) {
	            let img = $("<img>").attr("src", e.target.result).addClass("preview-img");
	            let removeBtn = $("<button>").text("X").addClass("remove-btn");

	            let container = $("<div>").addClass("preview-item").append(img, removeBtn);
	            $("#previewContainer").append(container);

	            // 삭제 버튼 클릭 이벤트
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

	    // ⭐ 파일 입력(input type="file") 변경 시 파일 처리 ⭐
	    $("#files").on("change", function (event) {
	        handleFiles(event.target.files);
	    });

	    // ⭐ 드래그 앤 드롭 기능 구현 ⭐
	    $("#dropZone").on("dragover", function (event) {
	        event.preventDefault();
	        event.stopPropagation();
	        $(this).addClass("dragover");
	    });

	    $("#dropZone").on("dragleave", function () {
	        $(this).removeClass("dragover");
	    });

	    $("#dropZone").on("click", function (event) {
	    	$("#files").click();  // 파일 선택 창을 여는 역할
	    });

	    // ⭐ 별점 선택 처리 ⭐
	    $(".star").on("click", function () {
	        let rating = $(this).data("value");
	        $("#ratingValue").text(rating);

	        $(".star").each(function () {
	            let value = $(this).data("value");
	            $(this).toggleClass("selected", value <= rating);
	        });
	    });

	    $(".star").on("mouseenter", function () {
	        let rating = $(this).data("value");
	        $(".star").each(function () {
	            let value = $(this).data("value");
	            $(this).toggleClass("selected", value <= rating);
	        });
	    });

	    $(".star").on("mouseleave", function () {
	        let selectedRating = $("#ratingValue").text();
	        $(".star").each(function () {
	            let value = $(this).data("value");
	            $(this).toggleClass("selected", value <= selectedRating);
	        });
	    });

	    // ⭐ 폼 제출 처리 ⭐
	    $("#boardCreateForm").submit(function (event) {
	        event.preventDefault();

	        let title = $("#title").val().trim();
	        let sitter = $("#sitter").val();
	        let content = tinymce.get('content').getContent({ format: 'text' });
			let count=0;
			$(".star.selected").each(function (){
				count++;
			});
			
			let rating = count;

	        if (!title) {
	            alert("후기 제목을 입력해주세요.");
	            $("#title").focus();
	            return;
	        }
	        if (title.length > 50) {
	            alert("후기 제목은 최대 50자입니다.");
	            $("#title").focus();
	            return;
	        }
	        if (!content) {
	            alert("후기 내용을 입력해주세요.");
	            $("#content").focus();
	            return;
	        }
	        if (content.length > 2000) {
	            alert("후기 내용은 최대 2000자입니다.");
	            $("#content").focus();
	            return;
	        }

	        let formData = new FormData();
	        formData.append("title", title);
	        formData.append("content", content);
	        formData.append("createId", $("#createId").val().trim());
	        formData.append("rating", rating);
	        formData.append("sitter", sitter);
	        // 선택된 파일 추가
	        for (let file of uploadedFiles) {
	            formData.append("files", file);
	        }
	        // AJAX 요청
	        $.ajax({
	            url: "/reviewboard/reviewcreate.do",
	            type: "POST",
	            data: formData,
	            processData: false,
	            contentType: false,
	            success: function (response) {
	            	console.log(response);
	                if (response.success) {
	                    alert("게시글 생성 성공! 게시판 목록으로 이동합니다.");
	                    window.location.href = "/reviewboard/reviewlist.do";
	                } else {
	                    alert("게시글 생성 실패: " + response.message);
	                }
	            },
	            error: function () {
	                alert("서버 오류가 발생했습니다.");
	            }
	        });
	    });
	});

    </script>
    
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"></jsp:include>
	<h2>펫시터 후기 작성</h2>
	<form id="boardCreateForm">
	
	<label for="sitter">펫시터:</label>        
		<select name="sitter" id="sitter" required>
		    <option value="">펫시터 선택</option>
		    <c:forEach var="sitter" items="${sitterList}">
		        <option value="${sitter.sitterName}">${sitter.sitterName} - ${sitter.content}</option>
		    </c:forEach>
		</select>
		
		<div id="starRating">
		    <span class="star" data-value="1">&#9733;</span>
		    <span class="star" data-value="2">&#9733;</span>
		    <span class="star" data-value="3">&#9733;</span>
		    <span class="star" data-value="4">&#9733;</span>
		    <span class="star" data-value="5">&#9733;</span>
		</div>
		<p>평점: <span id="ratingValue">0</span></p>
		<label for="title">제목 :</label> 
		<input type="text" id="title" name="title" maxlength="100" placeholder="후기 제목을 입력해주세요" required /> <br />
		<label for="content">후기 :</label>
		<textarea rows="5" cols="40" id="content" name="content" placeholder="후기 내용을 입력해주세요"> </textarea> <br />
		
		<!--   드래그 앤 드롭 파일 업로드 영역 -->
		<div id="previewContainer"></div>
      	<div id="dropZone">여기로 파일을 드래그하거나 클릭하여 선택하세요.</div>
	    <input type="file" id="files" name="files" multiple style="display: none;" />
		
		<input type="hidden" id="createId" name="createId" value="${sessionScope.user.userId}"/>
		<button type="submit" id="registerBtn">후기 작성</button>
	</form>
	
</body>
</html>
