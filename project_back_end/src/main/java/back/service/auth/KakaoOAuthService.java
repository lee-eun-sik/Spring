package back.service.auth;

import back.model.user.User;

public interface KakaoOAuthService {
	User getUserInfo(String accessToken) throws Exception;
    User processUser(User kakaoUser) throws Exception;
    String createJwtToken(User user) throws Exception;
}
