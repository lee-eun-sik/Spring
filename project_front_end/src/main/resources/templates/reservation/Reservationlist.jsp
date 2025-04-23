<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>예약 신청 목록</title>
    <script src="/js/jquery-3.7.1.min.js"></script>
    <link rel="stylesheet" th:href="@{/css/reservation/list.css}">
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 30px;
        }

        th, td {
            border: 1px solid #ccc;
            padding: 12px;
            text-align: center;
        }

        button {
            margin: 2px;
        }

        .header-buttons {
            margin-top: 20px;
        }

        .header-buttons button {
            margin-right: 10px;
        }
    </style>
    <script>
        $(document).ready(function () {
            $("#background, #p, #p1, #join, #login").hide();

            $(".acceptBtn").click(function () {
                const id = $(this).data("id");
                $.ajax({
                    url: `/api/reservation/${id}/accept`,
                    type: "POST",
                    success: function () {
                        alert("예약을 수락했습니다.");
                        location.reload();
                    },
                    error: function () {
                        alert("예약 수락에 실패했습니다.");
                    }
                });
            });

            $(".rejectBtn").click(function () {
                const id = $(this).data("id");
                $.ajax({
                    url: `/api/reservation/${id}/reject`,
                    type: "POST",
                    success: function () {
                        alert("예약을 거절했습니다.");
                        location.reload();
                    },
                    error: function () {
                        alert("예약 거절에 실패했습니다.");
                    }
                });
            });
        });
    </script>
</head>
<body>

<div th:replace="~{fragments/header :: header}"></div>

<h2>신청된 예약목록</h2>

<div class="header-buttons">
    <button type="button" th:onclick="|location.href='@{/user/main}'|">마이페이지</button>
    <button type="button" th:onclick="|location.href='@{/reservation/list}'|">예약관리</button>
    <button type="button" th:onclick="|location.href='@{/member/list}'|">회원관리</button>
</div>

<table>
    <thead>
        <tr>
            <th>아이디</th>
            <th>날짜</th>
            <th>주소</th>
            <th>품종</th>
            <th>펫이름</th>
            <th>전화번호</th>
            <th>펫시터</th>
            <th>일급</th>
            <th>관리</th>
        </tr>
    </thead>
    <tbody>
        <tr th:each="reservation : ${reservationList}">
            <td th:text="${reservation.reservationId}"></td>
            <td th:text="${reservation.startDate} + ' ~ ' + ${reservation.endDate}"></td>
            <td th:text="${reservation.address}"></td>
            <td th:text="${reservation.variety}"></td>
            <td th:text="${reservation.petName}"></td>
            <td th:text="${reservation.phoneNumber}"></td>
            <td th:text="${reservation.sitterName}"></td>
            <td th:text="${reservation.price}"></td>
            <td>
                <button type="button" th:attr="data-id=${reservation.reservationId}" class="acceptBtn">수락</button>
                <button type="button" th:attr="data-id=${reservation.reservationId}" class="rejectBtn">거절</button>
            </td>
        </tr>
        <tr th:if="${#lists.isEmpty(reservationList)}">
            <td colspan="9" style="text-align:center;">검색 결과가 없습니다.</td>
        </tr>
    </tbody>
</table>

</body>
</html>