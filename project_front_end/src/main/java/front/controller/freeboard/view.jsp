<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko"> 
<head>
<meta charset="UTF-8">
<title>게시판 상세</title>
<script src="/js/jquery-3.7.1.min.js"></script>
<script src="/js/common.js"></script>
<link rel="stylesheet" href="/css/view.css?ver=1" >
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"></jsp:include>
	<h2>게시글</h2>
	<form id="boardForm">
	<input type="hidden" id="boardId" name="boardId" value="${freeBoard.boardId}"/>
	<input type="hidden" id="createId" name="createId" value="${freeBoard.createId}"/>
	<input type="hidden" id="viewCount" name="viewCount" value="${freeBoard.viewCount}"/>
		<label for="createId">작성자 : ${freeBoard.createId}</label>
		<br/>
		
		<label for="createDt">작성일 : ${freeBoard.createDt}</label>
		<br/>
		
		<label for="title">제목 : ${freeBoard.title}</label>
		<br/>
		<label for="content">내용 : </label>
		<br/> ${freeBoard.content}
	</form>
	
	
	<c:if test="${not empty freeBoard.postFiles}">
		<ul>
			<c:forEach var="file" items="${freeBoard.postFiles}">
				<li>${file.fileName} <a
					href="/file/down.do?fileId=${file.fileId}">다운로드</a>
				</li>
			</c:forEach>
		</ul>
	</c:if>
	
	<button type="button" id="updateBtn" style="display:none;">게시글 수정</button>
	<button type="button" id="deleteBtn" style="display:none;">게시글 삭제</button>
	
	<c:if test="${not empty sessionScope.user.userId}">
	 <h4>댓글 작성</h4><br/>
	 <textarea id="commentContent" rows="4"  placeholder="댓글을 입력하세요...."></textarea>
	<button type="button" id="commentCreateBtn" onclick="addComment()">댓글 작성</button>
	</c:if>
	
	<h4>댓글 목록</h4>
	<c:if test="${empty freeBoard.comments}">
	    <p>댓글 목록이 없습니다.</p>
	</c:if>
	<c:if test="${not empty freeBoard.comments}">
		<div id="commentBox">
			<c:set var="comments" value="${freeBoard.comments}" scope="request" />
			<jsp:include page="commentItem.jsp">
				<jsp:param name="commentId" value="0"/>
			</jsp:include>
		</div>
	</c:if>
	 
	 <button><a href="/freeBoard/list.do">목록 페이지로 이동</a></button>
	 	
	<script>
	
	$(document).ready(function() {
		
		// 작성자 ID와 로그인된 사용자가 일치하면 수정, 삭제 버튼 보이기
	    if ($("#createId").val() == '${sessionScope.user.userId}') {
	        $("#updateBtn").show();
	        $("#deleteBtn").show();
	    }
		
		
		ajaxRequest(
				"/freeBoard/freeBoardviewCount.do",
				{
				 boardId : $("#boardId").val().trim(),
				 viewCount : $("#viewCount").val().trim()
				},
				function (response) {
					if (response.success) {

					} else {

					}
				}
		);
		
		//게시글 수정
		$("#updateBtn").click( function(event) {
			
			if($("#createId").val() == '${sessionScope.user.userId}'){
				window.location.href = "/freeBoard/update.do?id=${freeBoard.boardId}";
			} else {
				alert("아이디가 일치하지않음.")
			}
			
		});
		
		
		//게시글 삭제
		$("#deleteBtn").click( function(event) {
			
			if($("#createId").val() == '${sessionScope.user.userId}'){
				
				if (confirm('이 게시물을 삭제 하시겠습니까?')) {
					ajaxRequest(
							"/freeBoard/delete.do",
							{
							 boardId : $("#boardId").val(),

							 updateId : $("#updateId").val()
							},
							function (response) {
								if (response.success) {
									alert("게시글 삭제 성공");
									window.location.href = "/freeBoard/list.do";
								} else {
									alert("게시글 삭제 실패" + response.message);
								}
							}
					);
				}
				
			} else {
				alert("아이디가 일치하지않음.")
			}
			
		});
	});
	
	
	//댓글작성
	function addComment(parentId) {
		let content = parentId && parentId != 0 ?
				$('#replyContent_' + parentId).val().trim()
			:	$('#commentContent').val().trim();

		if (!content) {
			alert("댓글 내용을 입력해 주세요.");
			return;
		}
		
		if (content.length > 500) {
			alert("댓글은 500자 이하로 작성해 주세요.");
			return;
		}
		
		let boardId = ${freeBoard.boardId};
		let createId = '${sessionScope.user.userId}';
		
		ajaxRequest(
			'/freeBoard/freeComment/create.do',
			{
				content: content,
				boardId: boardId,
				createId: createId,
				parentCommentId: parentId || 0
			},
			function(response) {
				if (response.success) {
					alert(response.message);
					location.reload();
				} else {
					alert(response.message);
				}
			}
		);		
	}
	
	//댓글 수정 폼 토글
	function toggleEditComment(commentId) {
		
		let editForm = $('#editForm_' + commentId);
		let content = $('#commentContent_' + commentId).text().trim();
		
		// editForm이 존재하는지 확인
	    if (editForm.length > 0) {
	        // 폼이 보일 때는 숨기고, 보이지 않을 때는 나타나게 하기
	        if (editForm.is(":visible")) {
	            editForm.fadeOut();  // 부드럽게 숨기기
	        } else {
	            editForm.fadeIn();   // 부드럽게 나타내기
	            $('#editContent_' + commentId).val(content);  // 댓글 내용을 수정 폼에 넣기
	        }
	    }
	}
	
	//댓글수정
	function editComment(commentId) {
		let content = $('#editContent_' + commentId).val().trim();
		
		if (!content) {
			alert("댓글 내용을 입력해 주세요.");
			return;
		}
		
		if (content.length > 500) {
			alert("댓글은 500자 이하로 작성해 주세요.");
			return;
		}
		
		let boardId = ${freeBoard.boardId};
		let updateId = '${sessionScope.user.userId}';
		
		ajaxRequest(
			'/freeBoard/freeComment/update.do',
			{
				commentId: commentId,
				content: content,
				boardId: boardId,
				updateId: updateId
			},
			function(response) {
				if (response.success) {
					alert(response.message);
					location.reload();
				} else {
					alert(response.message);
				}
			}
		);
	}
	
	//댓글 삭제
	function deleteComment(commentId) {
		if (confirm('이 댓글을 삭제 하시겠습니까?')) {
			let updateId = '${sessionScope.user.userId}';
			ajaxRequest(
					'/freeBoard/freeComment/delete.do',
					{
						commentId: commentId,
						updateId: updateId
					},
					function(response) {
						if (response.success) {
							alert(response.message);
							location.reload();
						} else {
							alert(response.message);
						}
					}
			);
		}
	}
	
	//대댓글 폼 토글
	function toggleReplyComment (commentId) {
		
		let replyForm = $('#replyForm_' + commentId);
		
		if (replyForm.is(":visible")) {
			replyForm.fadeOut();
		} else {
			replyForm.fadeIn();
		}
	}
			
	
	
	</script>

</body>
</html>