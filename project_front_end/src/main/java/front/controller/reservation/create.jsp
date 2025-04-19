<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>예약신청 작성</title>

<script src="/js/jquery-3.7.1.min.js?ver=1"></script>
<script src="/js/tinymce/tinymce.min.js?ver=1"></script>
<script src="/js/edit.js?ver=1"></script>
<script src="/js/common.js?ver=1.1"></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"></jsp:include>
<link rel="stylesheet" href="/css/reservationCreate.css?ver=1" >
</head>
<body>
<h2>예약신청 작성</h2>
	<form method="post" id="reservationCreateForm">

		<label for="startDate">시작날짜:</label>
	    <input type="date" name="startDate" id="startDate" placeholder="시작날 선택" required>
	
	    <label for="endDate">종료날짜:</label>
	    <input type="date" name="endDate" id="endDate" placeholder="종료날 선택" required>
		
		<input type="hidden" name="reservationDate" id="reservationDate"><br/>
        <br/>
        <label for="address">주 소:</label> 
		<input type="text" name="address" id="sample6_address" placeholder="주소">
		<input type="button" onclick="sample6_execDaumPostcode()" value="주소찾기">
        <br/>
        <br/>        
        <label for="addressDetail">상세주소:</label> 
		<input type="text" name="addressDetail" id="addressDetail" placeholder="상세주소">
        <br/>
        <br/>
        <label for="variety">품 종:</label> 
        <select name="variety" id="variety" required>
		    <option value="강아지">강아지</option>
		    <option value="고양이">고양이</option>
		    <option value="조류">조류</option>
		    <option value="파충류">파충류</option>
		</select><br/>
        <br/>
        <label for="petName">펫이름:</label> 
		<input type="text" name="petName" id="petName" maxlength="100" placeholder="펫이름 입력" required><br/>
        <br/>
        <label for="phoneNumber">휴대번호:</label> 
		<input type="text" name="phoneNumber" id="phoneNumber" maxlength="100" placeholder="휴대번호 입력" required><br/>
        <br/>
        <label for="sitter">펫시터:</label>        
		<select name="sitter" id="sitter" required>
		    <option value="">펫시터 선택</option>
		    <c:forEach var="sitter" items="${sitterList}">
		        <option value="${sitter.sitterName}">${sitter.sitterName} - ${sitter.content}</option>
		    </c:forEach>
		</select>
		<br/>
		        
       	<label for="price">가격:</label> 
       	<input type="hidden" name="price" id="price">
		<span  id="calculatedPrice">0원</span> <!-- 가격을 동적으로 표시 -->
		

        <br/>
		<label for="reply">문의사항 :</label> 
		<textarea rows="5" cols="40"  id="reply" name="reply" placeholder="문의사항 입력"></textarea>
		<br/>	
		<!-- 드래그 앤 드롭 파일 업로드 영역 -->
      <div id="dropZone">여기로 파일을 드래그하거나 클릭하여 선택하세요.</div>
       <input type="file" id="files" name="files" multiple style="display: none;"> <!-- 숨겨진 파일 선택 input -->
       <ul class="file-list" id="fileList"></ul> <!-- 업로드한 파일 목록 표시 -->				
		<input type="hidden" name="createId" id="createId" value="${sessionScope.user.userId}"/>
		<div class="button-group">
			<button type="submit" id="registerBtn">게시글 등록</button>
		</div>
	</form>

<script>
    function sample6_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                var addr = ''; 
                if (data.userSelectedType === 'R') {
                    addr = data.roadAddress;
                } else {
                    addr = data.jibunAddress;
                }
                document.getElementById("sample6_address").value = addr;
            }
        }).open();
    }
</script>

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

		
	    $("#reservationCreateForm").submit(function(event) {
	        event.preventDefault(); 
			
	        let startDate = $("#startDate").val().trim();
	        let endDate = $("#endDate").val().trim();
	        let reservationDate = $("#reservationDate").val().trim();
	        let address = $("#sample6_address").val().trim();
	        let variety = $("#variety").val().trim();
	        let petName = $("#petName").val().trim();
	        let phoneNumber = $("#phoneNumber").val().trim();
	        let sitter = $("#sitter").val().trim();
	        let price = parseInt($("#price").val().trim());
	        let reply = $("#reply").val().trim();
	        let addressDetail = $("#addressDetail").val().trim();

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
			formData.append("startDate", startDate);
			formData.append("endDate", endDate);
			formData.append("reservationDate", reservationDate);
			formData.append("address", address);
			formData.append("variety", variety);
			formData.append("petName", petName);
			formData.append("phoneNumber", phoneNumber);
			formData.append("sitter", sitter);
			formData.append("price", price);
			formData.append("reply", reply);
			formData.append("createId", $("#createId").val().trim());
			formData.append("addressDetail", addressDetail);

			for (let i = 0; i < uploadedFiles.length; i++) {
				formData.append("files", uploadedFiles[i]);
			}

			ajaxRequestFile("/reservation/create.do", formData, function(response) {
				if (response.success) {
					alert("게시글 성공! 게시판으로 이동합니다.");
					//window.location.href = "/newsboard/newsboardview.do?";
				} else {
					alert("게시글 생성 실패: " + response.message);
				}
			});
	    });
	});
</script>

</body>
</html>
