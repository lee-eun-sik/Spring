package back.controller.email;

import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import back.service.email.EmailService;
import back.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-code.do")
    public ResponseEntity<?> sendCode(@RequestBody Map<String, String> request) {
        String email = request.get("usersEmail");

        if (email == null || email.isEmpty()) {
            log.info("이메일 누락");
            return ResponseEntity.badRequest().body(ApiResponse.error("이메일이 누락되었습니다."));
        }

        // 이미 가입된 이메일인지 확인
        if (emailService.isEmailRegistered(email)) {
            return ResponseEntity.ok().body(ApiResponse.of("DUPLICATE_EMAIL", false));
        }

        String code = String.format("%06d", new Random().nextInt(999999));

        // DB나 메모리/캐시에 저장
        emailService.saveEmailCode(email, code);

        // 이메일 전송
        boolean result = emailService.sendVerificationCodeEmail(email, code);

        if (result) {
            return ResponseEntity.ok().body(ApiResponse.success());
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error("이메일 전송 실패"));
        }
    }

    @PostMapping("/verify-code.do")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("usersEmail"); // 수정된 키 (대소문자 정확히 일치)
        String code = request.get("code");

        if (email == null || code == null) {
            log.warn("verify-code 요청에서 email 또는 code가 null입니다. email={}, code={}", email, code);
            return ResponseEntity.badRequest().body(ApiResponse.error("이메일 또는 인증번호가 누락되었습니다."));
        }

        if (emailService.verifyEmailCode(email, code)) {
            return ResponseEntity.ok().body(ApiResponse.success());
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error("인증번호 불일치"));
        }
    }
}
