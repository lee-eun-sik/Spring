package back.controller.find;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import back.service.user.UserService;
import back.util.ApiResponse;

import java.util.List;
import java.util.Map;
@RequiredArgsConstructor  // Lombok으로 생성자 자동 주입
@RestController
@RequestMapping("/api/find")

@Slf4j
public class FindController {

	 private final UserService userService;
	 private final JavaMailSender mailSender;

    // 이메일로 사용자 ID 찾기 후 메일 전송
    @PostMapping("/sendUserIdsByEmail.do")
    public ResponseEntity<?> sendUserIdsByEmail(@RequestParam("email") String email) {
        log.info("이메일로 사용자 ID 찾기 요청: {}", email);

        List<String> userIds = userService.getUserIdsByEmail(email);

        if (userIds == null || userIds.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(false, "해당 이메일로 등록된 사용자 ID가 없습니다.", null));
        }

        String subject = "요청하신 사용자 ID 목록입니다.";
        StringBuilder content = new StringBuilder();
        content.append("입력하신 이메일 [").append(email).append("] 로 등록된 사용자 ID는 다음과 같습니다:\n\n");

        for (String userId : userIds) {
            content.append("- ").append(userId).append("\n");
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject(subject);
            message.setText(content.toString());

            mailSender.send(message);
            log.info("이메일 전송 성공: {}", email);
            return ResponseEntity.ok(new ApiResponse<>(true, "이메일이 성공적으로 발송되었습니다.", null));
        } catch (Exception e) {
            log.error("이메일 전송 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "이메일 발송 중 오류가 발생했습니다.", null));
        }
    }

    // 이메일로 아이디 찾기 (응답에 아이디 목록 포함)
    @PostMapping("/find-id-by-email.do")
    public ResponseEntity<?> findUserIdByEmail(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        log.info("아이디 찾기 요청 - 이메일: {}", email);

        List<String> userIds = userService.getUserIdsByEmail(email);

        if (userIds == null || userIds.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(false, "해당 이메일로 등록된 아이디가 없습니다.", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "아이디 찾기 성공", userIds));
    }
}
