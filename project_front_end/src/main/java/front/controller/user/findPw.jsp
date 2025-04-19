<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>비밀번호 찾기</title>
<script src="/js/jquery-3.7.1.min.js"></script>
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
<link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
<script type="text/javascript">
		$(document).ready(function () {
			$("#background").hide();
			$("#p").hide();
			$("#p1").hide();
		});
		
</script>
<style>
    body {
        font-family: 'Noto Sans KR', sans-serif;
        background-color: #f0f0f0;
    }

    .container {
        width: 400px;
        margin: 80px auto;
        padding: 30px 40px;
        background-color: pink;
        border-radius: 12px;
        box-shadow: 0 4px 10px rgba(0,0,0,0.2);
        color: black;
    }

    h1 {
        text-align: center;
        margin-bottom: 30px;
    }

    table {
        width: 100%;
    }

    td {
        padding: 10px 0;
    }

    input[type="text"],
    input[type="tel"] {
        width: 100%;
        padding: 8px 10px;
        border-radius: 6px;
        border: none;
        font-size: 14px;
    }

    #birthdate {
        width: 100%;
    }

    .button-group {
        margin-top: 20px;
        display: flex;
        justify-content: space-between;
    }

    button {
        width: 48%;
        padding: 10px;
        background-color: white;
        color: black;
        border: none;
        border-radius: 8px;
        font-size: 15px;
        cursor: pointer;
        transition: background-color 0.3s;
    }

    button:hover {
        background-color: green;
    }
</style>
</head>
<body>


<div class="container">
    <h1>비밀번호 찾기</h1>
    <form method="POST" id="findPwForm">
        <table>
            <tr>
                <td>*아이디</td>
                <td><input type="text" name="userId" placeholder="abc123" required /></td>
            </tr>
            <tr>
                <td>*이름</td>
                <td><input type="text" name="username" placeholder="홍길동" required /></td>
            </tr>
            <tr>
                <td>*전화번호</td>
                <td><input type="tel" name="phonenumber" placeholder="010-0000-0000" required /></td>
            </tr>
            <tr>
                <td>*생년월일</td>
                <td><input type="text" id="birthdate" name="birthdate" placeholder="MM/DD/YYYY" required /></td>
            </tr>
        </table>

        <div class="button-group">
            <button type="button" id="findPW">비밀번호 찾기</button>
            <button type="reset">초기화</button>
        </div>
    </form>
</div>

<script>
$("#findPW").click(function () {
    const name = $("input[name='username']").val();
    const userId = $("input[name='userId']").val();
    const phone = $("input[name='phonenumber']").val();
    const birthdate = $("#birthdate").val();

    $.ajax({
        url: "/find/FindPw.do",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            name: name,
            userId: userId,
            phone: phone,
            birthdate: birthdate
        }),
        success: function (response) {
            if (response.success) {
                alert("비밀번호는: " + response.password); // 실제 서비스에서는 임시 비밀번호 발급 또는 이메일 발송 권장
            } else {
                alert("일치하는 정보가 없습니다.");
            }
        },
        error: function () {
            alert("서버 오류 발생");
        }
    });
    
    
});
</script>

</body>
</html>