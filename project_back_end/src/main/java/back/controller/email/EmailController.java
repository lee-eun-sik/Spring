package back.controller.email;

import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import back.service.email.EmailService;
import back.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/email")
public class EmailController {
	
	
	@Autowired
	private EmailService emailService;
	
	// 이메일 인증번호 발송
	@PostMapping("/send-code.do")
	public ApiResponse<?> sendCode(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		if (email == null || !email.contains("@")) {
			return new ApiResponse<>(false, "유효한 이메일이 아닙니다.", null);
		}
		
		String code = String.format("%06d", new Random().nextInt(999999));
		log.info("이메일 인증번호 발송: {} -> {}", email, code);
		
		// 이메일 발송 생략 (예: JavaMailSender 등 사용 가능)
		
		emailService.saveVerificationCode(email, code);
		return new ApiResponse<>(true, "인증번호가 발송되었습니다.", null);
	}
	
	// 인증번호 검증
	@PostMapping("/verify-code.do") 
	public ApiResponse<?> verifyCode(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		String code = request.get("code");
		
		if (email == null || code == null) {
			return new ApiResponse<>(false, "요청값이 잘못되었습니다.", null);
		}
		
		boolean success = emailService.verifyCode(email, code);
		if (success) {
			return new ApiResponse<>(true, "인증 성공", null);
		} else {
			return new ApiResponse<>(false, "인증 실패 (코드 불일치 또는 만료)", null);
		}
	}
}
