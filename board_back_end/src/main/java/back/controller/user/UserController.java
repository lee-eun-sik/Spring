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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import back.model.common.CustomUserDetails;
import back.model.user.User;
import back.service.user.UserService;
import back.service.user.UserServiceImpl;
import back.util.ApiResponse;
import back.util.SecurityUtil;




@Slf4j
@RestController // <- 이것도 추가해줘야 Rest API 컨트롤러가 작동함
@RequestMapping("/api/user")
public class UserController extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1441950303124081693L;

	@Autowired //의존성 주입 스프링 보안을 관리하는 매니저
    private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	@PostMapping("/view.do")// user정보를 꺼내온다.
	public ResponseEntity<?> view() {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		
		SecurityUtil.checkAuthorization(userDetails, userDetails.getUser().getUserId());
		User user = userService.getUserById(userDetails.getUser().getUserId());
		
		return ResponseEntity.ok(new ApiResponse<>(true, "조회 성공", user));
	}
	
	@PostMapping("/register.do")
	public ResponseEntity<?> register(@RequestBody User user) {
		log.info("회원가입 요청: {}", user.getUserId());
		
		user.setCreateId("SYSTEM");
		boolean success = userService.registerUser(user);
		
		return ResponseEntity.ok(new ApiResponse<>(success, success ? "회원가입 성공" : "회원가입 실패", null));
	}
	/**
	 * 회원 정보 수정 요청을 처리하는 컨트롤러 메서드
	 * 
	 * @param user 수정할 회원의 정보 (JSON으로 전달됨)
	 * @return 수정 성공 여부 및 메시지를 담은 ApiResponse 객체를 ResponseEntity 형태로 반환
	 */
	@PostMapping("/update.do")
	public ResponseEntity<?> update(@RequestBody User user) {
		// security에서 관리하는 유저정보
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		// 수정요청 로그 출력(로그에 수정 대상 사용자 ID 출력)
		log.info("회원정보 수정 요청: {}", user.getUserId());
		// 사용자 권한 확인: 로그인한 사용자와 수정 대상 사용자가 동일한지 검사
		SecurityUtil.checkAuthorization(userDetails, user.getUserId());
		// 수정 수행자의 ID를 현재 로그인한 사용자로 설정 (추적 목적)
		user.setUpdateId(userDetails.getUsername());
		 // 회원 정보 수정 처리 (UserService를 통해 DB 업데이트 수행)
		boolean success = userService.updateUser(user);
		// 클라이언트에게 JSON 형태로 결과 응답
	    // ApiResponse는 공통 응답 형식 (성공 여부, 메시지, 데이터)
		//HTTP 200 응답과 함께 결과 반환
		return ResponseEntity.ok(new ApiResponse<>(success, success ? "수정 성공" : "수정 실패", null));
	}
	/**
	 * 회원 탈퇴 요청을 처리하는 컨트롤러 메서드
	 * 
	 * @param user    삭제할 회원의 정보 (userId 등) - JSON으로 요청받음
	 * @param session 현재 사용자의 세션
	 * @return 삭제 성공 여부 및 메시지를 담은 ApiResponse 객체를 ResponseEntity 형태로 반환
	 */
	//응답 객체, 스프링 부트에서 제공. 응답 바디의 타입을 특정하지 않고, 어떤 타입이든 허용하겠다는 의미
	@PostMapping("/delete.do")
	public ResponseEntity<?> delete(@RequestBody User user, HttpSession session) {
		// 현재 로그인된 사용자의 정보를 가져옴 (Spring Security에서 관리)
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();//현재 로그인한 사용자의 정보를 가져올 때 사용하는 메소드입니다.
		//SecurityContextHolder.getContext()	현재 **보안 컨텍스트(SecurityContext)**를 가져옵니다. 보안 컨텍스트는 현재 로그인 세션의 인증 정보를 담고 있어요.
		//.getAuthentication()	현재 사용자의 인증(Authentication) 객체를 가져옵니다. 로그인한 사용자 정보, 권한, 인증 상태 등을 포함합니다.
		//.getPrincipal()	인증 객체에서 **실제 로그인한 사용자 정보(Principal)**를 가져옵니다. 이 정보는 보통 UserDetails 구현체입니다.
		//(CustomUserDetails)	내가 만든 사용자 클래스(CustomUserDetails)로 타입 변환해서 사용합니다.
		//userDetails	로그인한 사용자의 정보를 담는 변수입니다. 이 변수로 아이디, 권한 등 다양한 정보 접근 가능
		// 삭제 요청 로그 출력
		log.info("회원탈퇴 요청: {}", user.getUserId());
		// 사용자 권한 확인: 로그인한 사용자와 요청한 탈퇴 대상 사용자가 동일한지 검사
		SecurityUtil.checkAuthorization(userDetails, user.getUserId());
		// 업데이트 수행자의 ID로 로그인된 사용자의 ID를 설정
		user.setUpdateId(userDetails.getUsername());
		 // 실제 회원 삭제 로직 호출 (userService에서 처리)
		boolean success = userService.deleteUser(user);
		// 삭제가 성공한 경우, 세션과 Spring Security 인증 정보 초기화
		if (success) {
			session.invalidate();// 세션 무효화 (로그아웃 처리)
			SecurityContextHolder.clearContext();// SecurityContext 초기화 getContext를 했기 때문이다.
		}
		// 클라이언트에게 결과를 JSON 형태로 응답
	    // ApiResponse는 일반화된 응답 객체: (성공 여부, 메시지, 반환 데이터)
		return ResponseEntity.ok(new ApiResponse<>(success, success ? "삭제 성공" : "삭제 실패", null));
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
