<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script src="/js/jquery-3.7.1.min.js?ver=1"></script>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 100%;
        }
        /* 헤더 스타일 */
        .header {
        	height: 80px; /* 고정 높이 설정 */
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 15px 30px;
            background-color: #fff;
        }
        .headerlogo {
        	position: relavtive;
            height: 150px;
            width: 150px;
            margin-left: -50px;
            top: 20px;
        }
        .menu {
            display: flex;
            gap: 15px;

        }
        .headerBtn {
       		all: unset; /* 기본 스타일 제거 후 재설정 */
            padding: 8px 40px;
            border: 1px solid #FFD166;
            background: #FFD166;
            border-radius: 20px;
            cursor: pointer;
            color: black;
    		text-align: center;
        }
        .headerBtn:hover {
            background: white;
        }
        /* 드롭다운 스타일 */
	    .dropdown {
	        position: relative;
	    }
	
	    .dropdown-content {
	        display: none;
	        position: absolute;
	        top: 100%;
	        left: 0;
	        overflow: hidden;
	        width: 130px;
	    }
	    
	    .dropdown-content button {
	        width: 100%; /* 버튼이 부모 요소에 맞게 100% 너비 차지 */
	        padding: 8px 20px; /* 패딩 설정 */
	        border: 1px solid #FFD166;
	        background: #FFD166;
	        border-radius: 20px;
	        cursor: pointer;
	        white-space: nowrap; /* 줄바꿈 방지 */
	        text-align: center; /* 텍스트 중앙 정렬 */
	        font-size: 11px; /* 글씨 크기 수정 */
    	}
        
        .auth {
            display: flex;
            gap: 10px;
        }
        .auth button {
            padding: 8px 12px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
        }
        .login, .register {
            background: #6D9886;
            color: white;
        }
        
        .userId {
		    text-decoration: none;
		    color: #3b7bbb;
		    font-weight: bold;
		}
		
		.userId:hover {
		    text-decoration: underline;
		}
		

        
        .main-content {
            width: 100%;
            max-width: 1000px;
            height: 700px;
            background: #ddd;
            margin: 50px auto;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .placeholder {
            width: 200px;
            height: 150px;
            background: #fffae0;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 14px;
            color: black;
            padding: 10px;
        }

        /* 배너 스타일 */
        .banner {
            position: relative;
            width: 100%;
            height: 700px; /* 원하는 높이 설정 */
            background: url('./images/banner.png') no-repeat center/cover; /* 경로 수정 */
            display: flex;
            align-items: center;
            justify-content: center;
            text-align: center;
            color: white;
        }
        .banner h1 {
            position: absolute;
            top: 70%; 
            left: 5%;
            font-size: 36px;
            margin: 0;
            text-shadow: 2px 2px 5px rgba(0, 0, 0, 0.7);
        }
        .banner p {
            position: absolute;
            top: 75%; 
            left: 5%;
            font-size: 25px;
            text-shadow: 2px 2px 5px rgba(0, 0, 0, 0.7);
        }
    </style>

    <div class="container">
        <!-- 헤더 -->
        <div class="header">
            <a href="/user/main.do"><img src="/images/pet.png" alt="로고" class="headerlogo"></a>
            
            
            <div class="menu">
                <button class="headerBtn" onclick="showAlertAndRedirect('/newsboard/newsboardlist.do')">공지사항</button>
			    <button class="headerBtn" onclick="showAlertAndRedirect('/petsitter/PetSitterlist.do')">펫시터</button>
			    <button class="headerBtn" onclick="showAlertAndRedirect('/reservation/create.do')">예약하기</button>
			    <button class="headerBtn" onclick="showAlertAndRedirect('/reviewboard/reviewlist.do')">리뷰목록</button>
			    
		        <div class="dropdown">
                    <button id="communityBtn" class="headerBtn">커뮤니티</button>
                    <div class="dropdown-content">
                       <button onclick="showAlertAndRedirect('/freeBoard/list.do')">자유게시판</button>
           			   <button onclick="showAlertAndRedirect('/pet/petPictureList.do')">펫사진공유게시판</button>
                    </div>
                </div>
            </div>

		
            <c:choose>
			    <c:when test="${empty sessionScope.user.userId}">
			        <div class="auth">
			            <button class="login" onclick="location.href='/user/login.do'">로그인</button>
			            <button class="register" onclick="location.href='/user/join.do'">회원가입</button>
			        </div>
			    </c:when>
			    <c:otherwise>
			        <div class="user-greeting">
					    <a href="/user/userInfo.do" class="userId">${sessionScope.user.userId}</a>님 안녕하세요.
					</div>
			    </c:otherwise>
			</c:choose>
        </div>
    </div>
    
    <script>
    
    // 메뉴클릭시
    function showAlertAndRedirect(url) {
        <%-- 세션에 로그인 정보가 있는지 확인 --%>
        <c:choose>
            <c:when test="${empty sessionScope.user.userId}">
                alert('로그인을 해주세요.');
                location.href = '/user/login.do';
            </c:when>
            <c:otherwise>
                location.href = url;
            </c:otherwise>
        </c:choose>
    }
    
    
    // 커뮤니티 버튼 클릭 시 드롭다운 메뉴 표시
    $("#communityBtn").click(function(event) {
        event.preventDefault(); // 링크 이동 방지
        $(".dropdown-content").toggle(); // 드롭다운 표시/숨김
    });

    // 메뉴 외부 클릭 시 드롭다운 닫기
    $(document).click(function(event) {
        if (!$(event.target).closest('.dropdown').length) {
            $(".dropdown-content").hide();
        }
    });
    </script>

