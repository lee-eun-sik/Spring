<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="model.reservation.Reservation" %>
<%
    Reservation reservation = (Reservation) request.getAttribute("reservation");
%>

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
<link rel="stylesheet" href="/css/reservationUpdate.css?ver=1" >


</head>
<body>
	<h2>게시글 수정</h2>
	<div class="reservationupdate">	
		<input type="hidden" name="boardId" id="boardId" value="${reservation.boardId}">
		<label for="startDate">시작날짜:</label>
	    <input type="date" name="startDate" id="startDate" value="${formattedStartDate}" required>
	
	    <label for="endDate">종료날짜:</label>
	   <input type="date" name="endDate" id="endDate" value="${formattedEndDate}" required>
	   <%
    System.out.println("startDate 디버깅: " + reservation.getStartDate());
    System.out.println("endDate 디버깅: " + reservation.getEndDate());
%>
	   
	   
		<input type="hidden" name="reservationDate" id="reservationDate" value="${reservation.reservationDate}"><br/>
        <br/>
        <label for="address">주 소:</label> 
		<input type="text" name="address" id="sample6_address" value="${reservation.address}" placeholder="주소">
		<input type="button" onclick="sample6_execDaumPostcode()" value="주소찾기">
        <br/>
        <br/>        
        <label for="addressDetail">상세주소:</label> 
		<input type="text" name="addressDetail" id="addressDetail" value="${reservation.addressDetail}" placeholder="상세주소">
        <br/>
        <br/>
        <label for="variety">품 종:</label> 
        <select name="variety" id="variety" required>
		    <option value="강아지" <c:if test="${reservation.variety eq '강아지'}">selected</c:if>>강아지</option>
		    <option value="고양이" <c:if test="${reservation.variety eq '고양이'}">selected</c:if>>고양이</option>
		    <option value="조류" <c:if test="${reservation.variety eq '조류'}">selected</c:if>>조류</option>
		    <option value="파충류" <c:if test="${reservation.variety eq '파충류'}">selected</c:if>>파충류</option>
		</select><br/>
        <br/>
        <label for="petName">펫이름:</label> 
		<input type="text" name="petName" id="petName" value="${reservation.petName}" maxlength="100" placeholder="펫이름 입력" required><br/>
        <br/>
        <label for="phoneNumber">휴대번호:</label> 
		<input type="text" name="phoneNumber" id="phoneNumber" value="${reservation.phoneNumber}" maxlength="100" placeholder="휴대번호 입력" required><br/>
        <br/>

       <label for="sitter">펫시터:</label> 
		<select name="sitter" id="sitter" required>
		    <option value="">펫시터 선택</option>
		    <c:forEach var="sitter" items="${sitterList}">
		        <c:set var="isSelected" value="${reservation.sitter eq sitter.sitterName}" />
		        <option value="${sitter.sitterName}" <c:if test="${isSelected}">selected</c:if>>
		            ${sitter.sitterName} - ${sitter.content}
		        </option>
		    </c:forEach>
		</select>
	</div>

		<br/>

		        
       	<label for="price">가격:</label> 
       	<input type="hidden" name="price" id="price" value="${reservation.price}">
		<span  id="calculatedPrice">${reservation.price}원</span> <!-- 가격을 동적으로 표시 -->
		

        <br/>
		<label for="reply">문의사항 :</label> 
		<textarea rows="5" cols="40"  id="reply" name="reply" placeholder="문의사항 입력">${reservation.reply}</textarea>
		<br/>	
		<!-- 드래그 앤 드롭 파일 업로드 영역 -->
      <div id="dropZone">여기로 파일을 드래그하거나 클릭하여 선택하세요.</div>
       <input type="file" id="files" name="files" multiple style="display: none;"> <!-- 숨겨진 파일 선택 input -->
       <ul class="file-list" id="fileList"></ul> <!-- 업로드한 파일 목록 표시 -->				
		<input type="hidden" name="createId" id="createId" value="${sessionScope.user.userId}"/>
	


<div class="btcontainer">
    <button type="button" id="updateBtn" class="updateBtn">게시글 수정</button>
    <button type="button" id="deleteBtn" class="deleteBtn">게시글 삭제</button>
    </div>

	<script type="text/javascript">	
	$(document).ready(function() {
		let edit = editInit("reply");
		

		let uploadedFiles = setupFileUpload({
            dropZone: '#dropZone',
            fileInput: '#files',
            fileList: '#fileList',
            maxFileSize: 50 * 1024 * 1024
        });

		// 날짜가 변경될 때마다 자동으로 가격을 계산하는 이벤트 핸들러
        $("#startDate, #endDate").change(function() {
            let startDate = $("#startDate").val().trim();
            let endDate = $("#endDate").val().trim();

            if (startDate && endDate) {
                // 날짜 차이 계산
                let start = new Date(startDate);
                let end = new Date(endDate);
                let timeDiff = end - start;

                if (timeDiff < 0) {
                    alert("종료일이 시작일보다 이전일 수 없습니다.");
                    return;
                }

                let dayDiff = timeDiff / (1000 * 3600 * 24)+1; // 밀리초 -> 일
                $("#reservationDate").val(dayDiff);

                // 가격 계산
                let calculatedPrice = dayDiff * 10000; // 예시: 하루당 10,000원
                $("#calculatedPrice").text(calculatedPrice + "원"); // 계산된 가격을 표시

                // 계산된 가격을 hidden input에 설정
                $("#price").val(calculatedPrice); // price 필드에 계산된 가격 설정
            }
        });
		
			//삭제버튼누를시 실행될 함수
			 $("#deleteBtn").click(function(event) {

				   event.preventDefault(); // 기본 폼 제출 방지
				   
				   ajaxRequest(
							"/reservation/delete.do",
						   {boardId: $("#boardId").val(),
								updateId: $("#updateId").val()
                               },
						    function(response) {
								  if (response.success){
										  alert("예약 삭제 성공 !!! 목록으로 이동합니다.");
											  //window.location.href =  '/user/login.do';
										} else {
												 alert("예약 삭제 실패." + response.message);
									           	}
						             	}							
							);
			 });
	    
			//예약수정버튼누를시 실행될함수
	    $("#updateBtn").click(function(event) {
	        event.preventDefault(); // 기본 폼 제출 방지
	        let boardId = $("#boardId").val().trim();		
	        let startDate = $("#startDate").val().trim();
	        let endDate = $("#endDate").val().trim();
	        let reservationDate = $("#reservationDate").val().trim();
	        let address = $("#sample6_address").val().trim();
	        let variety = $("#variety").val().trim();
	        let petName = $("#petName").val().trim();
	        let phoneNumber = $("#phoneNumber").val().trim();
	        let sitter = $("#sitter").val().trim();
	        let price = parseInt($("#price").val().trim());
	        let reply = tinymce.get('reply').getContent({format: 'text'});	 // 내용 (텍스트 형식)
	        let addressDetail = $("#addressDetail").val().trim();
	        
	        console.log("Reply Value: " + reply);
	        
	        if (!startDate || !endDate) {
	            alert("시작일과 종료일을 모두 선택해주세요.");
	            return;
	        }

	        if (!validationUtil.isEmpty(address)) {
				alert("주소를 입력해주세요");
				$("#sample6_address").focus();
				return;
			}
	        
	        if (!validationUtil.isEmpty(addressDetail)) {
				alert("상세주소를 입력해주세요");
				$("#addressDetail").focus();
				return;
			}

			if (isNaN(price)) {
				alert("품종을 선택해주세요");
				$("#variety").focus();
				return;
			}

			if (!validationUtil.isEmpty(petName)) {
				alert("펫이름을 입력해주세요");
				$("#petName").focus();
				return;
			}
			
			if (!validationUtil.isEmpty(phoneNumber)) {
				alert("휴대번호를 입력해주세요");
				$("#phoneNumber").focus();
				return;
			}
			
			if (!validationUtil.isNumeric(phoneNumber)) {
				alert("휴대번호를 입력해주세요");
				$("#phoneNumber").focus();
				return;
			}
			

			if (!validationUtil.isEmpty(sitter)) {
				alert("펫시터를 입력해주세요");
				$("#sitter").focus();
				return;
			}
			
			if (!sitter) {
			    alert("펫시터를 선택해주세요");
			    $("#sitter").focus();
			    return;
			}
				
			let formData = new FormData();
			formData.append("boardId", boardId);
			formData.append("startDate", startDate);
			formData.append("endDate", endDate);
			formData.append("reservationDate", reservationDate);
			formData.append("address", address);
			formData.append("variety", variety);
			formData.append("petName", petName);
			formData.append("phoneNumber", phoneNumber);
			formData.append("sitter", sitter);
			formData.append("price", price);
			formData.append("reply",tinymce.get('reply').getContent());
			formData.append("createId", $("#createId").val().trim());
			formData.append("addressDetail", addressDetail);
			
			// 폼 데이터 전송 전 콘솔에 출력
		    console.log("FormData being sent:");
		    console.log("boardId: " + boardId);
		    console.log("startDate: " + startDate);
		    console.log("endDate: " + endDate);
		    console.log("reply: " + reply); // reply 값을 확인하기 위해 콘솔에 출력
				
				
		        
		        ajaxRequestFile(
		        		"/reservation/update.do",
		        		formData,
		        		function (response){
                                 if(response.success){
                                        alert("예약 수정 성공! 예약 목록으로 이동합니다");
                                  } else {
                                         alert("예약 수정 실패: "+ response.message);
                                      }
                                }
		        		);                          
				});
	    });
    </script>

</body>
</html>
