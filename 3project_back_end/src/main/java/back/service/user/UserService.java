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
    
    List<User>  findUsersByInfo(String email);
    
    public boolean isUserIdDuplicate(String usersId);
    boolean updatePassword(String userId, String encodedPassword);
    
    boolean resetPassword(String userId, String newPassword);

	User findUserByUserIdAndEmail(String userId, String email);

	public boolean isUserEmailDuplicate(String email);

	public boolean isEmailRegistered(String email);

	public User findByEmail(String email);
}    