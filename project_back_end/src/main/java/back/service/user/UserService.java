package back.service.user;

import org.apache.ibatis.session.SqlSession;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Date;

import back.model.user.User;

public interface UserService {
    /**
     * 사용자 회원가입
     */
	public boolean registerUser(User user);
    
	public boolean validateUser(User user);
    
    public User getUserById(String userId);

    User login(String id, String pass);

    List<User>  findUsersByInfo(String email);
    
    
    boolean updatePassword(String userId, String encodedPassword);
    
    boolean resetPassword(String userId, String newPassword);

	User findUserByUserIdAndEmail(String userId, String email);
}    