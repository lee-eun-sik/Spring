package back.service.auth;

import org.springframework.stereotype.Service;

import back.model.user.NaverUserInfo;
import back.model.user.User;


public interface NaverOAuthService {
	NaverUserInfo getUserInfo(String accessToken);
    User processUser(NaverUserInfo userInfo);
    String createJwtToken(User user);
}
