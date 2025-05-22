package back.controller.find;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import back.model.user.User;
import back.service.user.UserService;
import back.util.ApiResponse;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/find")
@Slf4j
@RequiredArgsConstructor
public class FindController {

    private final UserService userService;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/findId.do")
    public ResponseEntity<?> findUserIdByInfo(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String phonenumber = body.get("phonenumber");
        String birthDate = body.get("birthdate");  // 통일: "birthdate"
        String email = body.get("email");

        log.info("사용자 정보로 ID 찾기 요청: {}, {}, {}, {}", username, phonenumber, birthDate, email);

        List<User> users = userService.findUsersByInfo(username, phonenumber, birthDate, email);
        if (users.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(false, "일치하는 사용자 정보를 찾을 수 없습니다.", null));
        }

        List<Map<String, String>> userInfoList = users.stream()
            .map(user -> Map.of(
                "userId", user.getUserId(),
                "createDt", user.getCreateDt()
            ))
            .toList();

        return ResponseEntity.ok(new ApiResponse<>(true, "일치하는 사용자 정보를 찾았습니다.", Map.of("list", userInfoList)));
    }

    @PostMapping("/findPw.do")
    public ResponseEntity<?> findUserPwByInfo(@RequestBody Map<String, String> body) {
        String userId = body.get("userId");
        String username = body.get("username");
        String phonenumber = body.get("phonenumber");
        String birthDate = body.get("birthDate");  // 통일: "birthdate"
        String email = body.get("email");

        log.info("비밀번호 찾기 요청: {}, {}, {}, {}, {}", userId, username, phonenumber, birthDate, email);

        User user = userService.findUserForPwReset(userId, username, phonenumber, birthDate, email);
        if (user == null) {
            return ResponseEntity.ok(new ApiResponse<>(false, "일치하는 사용자 정보를 찾을 수 없습니다.", null));
        }

        String tempPassword = generateSecureTempPassword(10);
        String encodedPassword = passwordEncoder.encode(tempPassword);
        log.info("임시비밀번호 생성: {}", tempPassword);

        boolean updateResult = userService.updatePassword(user.getUserId(), encodedPassword);
        if (!updateResult) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "비밀번호 변경 실패", null));
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("임시 비밀번호 안내");
            message.setText("요청하신 임시 비밀번호는 다음과 같습니다:\n" + tempPassword + "\n로그인 후 반드시 비밀번호를 변경해주세요.");
            mailSender.send(message);
        } catch (Exception e) {
            log.error("임시 비밀번호 이메일 전송 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "이메일 전송 실패", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "임시 비밀번호가 이메일로 발송되었습니다.", null));
    }

    private String generateSecureTempPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
