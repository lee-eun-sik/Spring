package front.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class UserController {
	/**
	 * 로그인 화면 이동
	 */
	@GetMapping("/user/login.do")
	public String loginPage() {
		return "user/login"; // login.html로 이동
	}
	
	/**
	 * 회원가입 화면 이동
	 */
	@GetMapping("/user/join.do")
	public String joinPage() {
		return "user/join"; // join.html로 이동
	}
	/**
	 * 사용자 정보 화면 이동
	 */
	@GetMapping("/user/userInfo.do")
	public String userInfoPage() {
		return "user/userInfo"; // userInfo.html로 이동
	}
	/**
	 * 수정 화면 이동
	 */
	@GetMapping("/user/update.do")
	public String updatePage() {
		return "user/update"; // update.html로 이동
	}
	
}
