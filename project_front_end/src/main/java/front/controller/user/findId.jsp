<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>아이디 찾기</title>
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

    <script>
        $(function () {
            $("#birthdate").datepicker({
                dateFormat: 'mm/dd/yy',
                changeMonth: true,
                changeYear: true,
                yearRange: "1900:2025"
            });
        });
    </script>
</head>
<body>

<div class="container">
    <h1>아이디 찾기</h1>
    <form id="findIdForm">
        <table>
            <tr>
                <td>*이름</td>
                <td><input type="text" name="name" placeholder="홍길동" required /></td>
            </tr>
            <tr>
                <td>*전화번호</td>
                <td><input type="tel" name="phonenumber" placeholder="010-1234-5678" required></td>
            </tr>
            <tr>
                <td>*생년월일</td>
                <td><input type="date" id="birthdate" name="birthdate" placeholder="MM/DD/YYYY" required></td>
            </tr>
        </table>

        <div class="button-group">
            <button type="button" id="findID">아이디 찾기</button>
            <button type="reset">초기화</button>
        </div>
    </form>
</div>

<script>
    $(document).ready(function () {
        $("#findID").click(function () {
            const name = $("input[name='name']").val();
            const phone = $("input[name='phonenumber']").val();
            const birthdate = $("#birthdate").val();

            if (!name || !phone || !birthdate) {
                alert("모든 필수 정보를 입력해주세요.");
                return;
            }
            ajaxRequest(
            		 "/user/findId.do",
					$("#findIdForm").serialize(),
					function (response) {
            			 if (response.success) {
                            
                               const userId = response.userId;
                               alert("입력하신 정보로 등록된 아이디는 다음과 같습니다:\n" + userId);
                            
                         } else {
                             alert("해당 정보로 등록된 아이디를 찾을 수 없습니다.");
                         }
					}
			);
           
        });
    });
</script>

</body>
</html>