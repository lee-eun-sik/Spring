package back.controller.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import back.model.common.CustomUserDetails;
import back.model.user.User;
import back.service.user.UserService;
import back.util.ApiResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
  
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @PostMapping("/register.do")
	public ResponseEntity<?> register(@RequestBody User user) {
		log.info("회원가입 요청: {}", user.getUserId());
		
		user.setCreateId("SYSTEM");
		boolean success = userService.registerUser(user);
		
		return ResponseEntity.ok(new ApiResponse<>(success, success ? "회원가입 성공" : "회원가입 실패", null));
	}
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
	        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
	        return ResponseEntity.ok(new ApiResponse<>(true, "로그인 성공", userDetails.getUser()));
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