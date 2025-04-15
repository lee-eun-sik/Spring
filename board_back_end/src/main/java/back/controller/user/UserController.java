package back.controller.user;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import back.model.user.User;
import back.service.user.UserService;
import back.service.user.UserServiceImpl;
import back.util.ApiResponse;



@Data
@Slf4j
@RestController // <- 이것도 추가해줘야 Rest API 컨트롤러가 작동함
public class UserController extends HttpServlet {
	@Autowired
    private AuthenticationManager authenticationManager;
	@PostMapping("/login.do")
	public ResponseEntity<?> login(@RequestBody User user, HttpServletRequest request) {
	    log.info("로그인 시도: {}", user.getUserId());
	    try {
	        Authentication auth = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPassword())
	        );
	
	        SecurityContextHolder.getContext().setAuthentication(auth);
	
	        HttpSession session = request.getSession(true);
	        session.setAttribute(
	            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
	            SecurityContextHolder.getContext()
	        );
	        log.info("세션 ID: {}", session.getId());
	
	        return ResponseEntity.ok(new ApiResponse<>(true, "로그인 성공", null));
	    } catch (AuthenticationException e) {
	        log.warn("로그인 실패: {}", user.getUserId());
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	            .body(new ApiResponse<>(false, "아이디 또는 비밀번호가 일치하지 않습니다.", null));
	    }
	}
	
	@PostMapping("/logout.do")
	public ResponseEntity<?> logout(HttpServletRequest request) {
	    log.info("로그아웃 요청");
	
	    request.getSession().invalidate();
	    SecurityContextHolder.clearContext();
	
	    return ResponseEntity.ok(new ApiResponse<>(true, "로그아웃 완료", null));
	}
}
