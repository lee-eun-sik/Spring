package controller.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONArray;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.user.User;
import service.user.UserService;
import service.user.UserServiceImpl;


@WebServlet("/user/*")
public class UserController extends HttpServlet {

private static final long serialVersionUID = 7949105235983859619L;
private static final Logger logger = LogManager.getLogger(UserController.class); 
private UserService userService;
public UserController() {
        super();
        userService = new UserServiceImpl();
    }
/**
 * get 화면 이동용 및 조회용
 */
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  logger.info("UserController doGet"); 
      String path = request.getRequestURI();
      logger.info("UserController doGet path" + path); 
      
      if ("/user/login.do".equals(path)) {
          // 로그인 JSP로 포워딩
            request.getRequestDispatcher("/WEB-INF/jsp/user/login.jsp").forward(request, response);
      } else if ("/user/join.do".equals(path)) {
          // 회원가입 JSP로 포워딩
          request.getRequestDispatcher("/WEB-INF/jsp/user/join.jsp").forward(request, response);
      } else if ("/user/main.do".equals(path)) {
          // 메인 JSP로 포워딩
          request.getRequestDispatcher("/WEB-INF/jsp/user/main.jsp").forward(request, response);
      } else if ("/user/main2.do".equals(path)) {
          // 메인 JSP로 포워딩
          request.getRequestDispatcher("/WEB-INF/jsp/user/main2.jsp").forward(request, response);
      } else if ("/user/userInfo.do".equals(path)) {
          // 회원정보 JSP로 포워딩
          request.getRequestDispatcher("/WEB-INF/jsp/user/userInfo.jsp").forward(request, response);
      } else if ("/user/userEdit.do".equals(path)) {
          // 회원정보 JSP로 포워딩
          request.getRequestDispatcher("/WEB-INF/jsp/user/userEdit.jsp").forward(request, response);
      }
}

/**
 * POST ajax 로직 처리용 
 */
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	logger.info("UserController doPost");
    String path = request.getRequestURI();
    response.setContentType("application/json; charset=UTF-8"); // 응답 타입 설정
    PrintWriter out = response.getWriter(); // PrintWriter 객체 생성
    JSONObject jsonResponse = new JSONObject(); // JSON 응답 객체 생성
    try {
        logger.info("UserController doPost path:" + path);
        //유저 가입시 처리
        if ("/user/register.do".equals(path)) { 
            // User 객체 생성
            User user = new User();
            user.setUserId(request.getParameter("userId"));
            user.setUsername(request.getParameter("username"));
            user.setPassword(request.getParameter("password"));
            user.setGender(request.getParameter("gender"));
            user.setEmail(request.getParameter("email"));
            user.setPhonenumber(request.getParameter("phonenumber"));
            user.setBirthdate(request.getParameter("birthdate"));
            user.setCreateId("SYSTEM");

            jsonResponse.put("success", userService.registerUser(user));
        } else if ("/user/loginCheck.do".equals(path)) { 

            User user = new User();
            user.setUserId(request.getParameter("id"));
            user.setPassword(request.getParameter("pass")); 

            boolean loginCheck = userService.validateUser(user);

            //로그인 체크
            if(loginCheck){
                //성공시
                HttpSession session = request.getSession();
                User selectUser = userService.getUserById(user.getUserId());

                session.setAttribute("user", selectUser);

                jsonResponse.put("success", true); // 성공 여부
            } else {
                //실패시
                jsonResponse.put("success", false); // 성공 여부
            }

        } else if ("/user/logout.do".equals(path)) {
        	String id = request.getParameter("id");
        	
        	HttpSession session = request.getSession();
        	User user = (User) session.getAttribute("user");
        	
        	User selectUser = userService.getUserById(user.getUserId());
        	
        	if(selectUser != null) {
        		//세션 삭제
        		session.invalidate();
        		jsonResponse.put("success", true);
        	} else {
        		jsonResponse.put("success", false);
        	}
        }
    } catch (Exception e) {
        jsonResponse.put("success", false); // 오류 발생 시
        jsonResponse.put("message", "서버 오류 발생"); // 오류 메시지
        logger.error("Error in UserController doPost", e); // 오류 로그 추가
    }

    logger.info("jsonResponse.toString() : ", jsonResponse.toString()); 
    // JSON 응답 출력
    out.print(jsonResponse.toString());
    out.flush();
}

}