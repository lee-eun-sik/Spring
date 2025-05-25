package back.controller.auth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import back.controller.find.FindController;
import back.model.user.NaverUserInfo;
import back.model.user.User;
import back.service.auth.KakaoOAuthService;
import back.service.auth.NaverOAuthService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private NaverOAuthService naverOAuthService;
    @Autowired
    private KakaoOAuthService kakaoOAuthService;
    @PostMapping("/naver.do")
    public ResponseEntity<?> naverLogin(@RequestBody Map<String, String> body) {
        String accessToken = body.get("accessToken");
        System.out.println("Received AccessToken: " + accessToken);
        if (accessToken == null || accessToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Access token is missing");
        }

        try {
            // 1. 액세스 토큰으로 사용자 정보 조회
            NaverUserInfo userInfo = naverOAuthService.getUserInfo(accessToken);

            // 2. 사용자 존재 여부 확인 및 저장 또는 로그인 처리
            User user = naverOAuthService.processUser(userInfo);

            // 3. JWT 발급 (선택)
            String jwt = naverOAuthService.createJwtToken(user);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "token", jwt,
                "user", user
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("네이버 로그인 실패: " + e.getMessage());
        }
    }
    
    

    @PostMapping("/kakao.do")
    public ResponseEntity<?> kakaoLogin(@RequestBody Map<String, String> body) {
        String accessToken = body.get("accessToken");
        if (accessToken == null || accessToken.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Access token is missing"));
        }

        try {
            User kakaoUser = kakaoOAuthService.getUserInfo(accessToken);
            User user = kakaoOAuthService.processUser(kakaoUser);
            String jwt = kakaoOAuthService.createJwtToken(user);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "token", jwt,
                "user", user
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "카카오 로그인 실패: " + e.getMessage()));
        }
    }
}
