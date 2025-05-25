package back.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import back.mapper.auth.AuthMapper;
import back.model.user.NaverUserInfo;
import back.model.user.NaverUserResponse;
import back.model.user.User;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NaverOAuthServiceImpl implements NaverOAuthService {

    @Autowired
    private AuthMapper authMapper;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Override
    public NaverUserInfo getUserInfo(String accessToken) {
        String url = "https://openapi.naver.com/v1/nid/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<NaverUserResponse> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                NaverUserResponse.class
            );

            NaverUserResponse userResponse = responseEntity.getBody();
            if (userResponse == null || userResponse.getResponse() == null) {
                throw new RuntimeException("네이버 사용자 정보를 불러오지 못했습니다.");
            }

            return userResponse.getResponse();
        } catch (Exception e) {
            log.error("네이버 사용자 정보 요청 실패", e);
            throw new RuntimeException("네이버 API 호출 중 오류가 발생했습니다.");
        }
    }

    @Override
    public User processUser(NaverUserInfo userInfo) {
        User user = authMapper.findByNaverId(userInfo.getId());
        if (user == null) {
            user = new User();
            user.setNaverId(userInfo.getId());
            user.setUsername(userInfo.getName());
            user.setEmail(userInfo.getEmail());
            authMapper.insertNaverUser(user);
        }
        return user;
    }

    @Override
    public String createJwtToken(User user) {
        return jwtTokenProvider.createToken(user.getUsername(), "ROLE_USER");
    }
}