package back.service.auth;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import back.mapper.auth.AuthMapper;
import back.model.user.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoOAuthServiceImpl implements KakaoOAuthService {

    private final AuthMapper authMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate; // 주입받은 Bean
 // KakaoOAuthServiceImpl.java
    @Override
    public User getUserInfo(String accessToken) throws Exception {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response.getBody());

        String kakaoId = node.get("id").asText();
        String nickname = node.path("properties").path("nickname").asText();
        String email = node.path("kakao_account").path("email").asText(null);

        User user = new User();
        user.setKakaoId(kakaoId);
        user.setUsername(nickname);
        user.setEmail(email);

        return user;
    }

    @Override
    public User processUser(User kakaoUser) {
        User user = authMapper.findByKakaoId(kakaoUser.getKakaoId());
        if (user == null) {
            authMapper.insertKakaoUser(kakaoUser);
            user = kakaoUser;
        }
        return user;
    }

    @Override
    public String createJwtToken(User user) {
        // JWT 토큰 생성 로직 (user.getUsername(), user.getRole() 사용)
        return jwtTokenProvider.createToken(user.getUsername(), user.getRole());
    }
}
