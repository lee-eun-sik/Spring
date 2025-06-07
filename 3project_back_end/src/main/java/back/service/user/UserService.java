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
    
    public List<User>  findUsersByInfo(String email);
    
    public boolean isUserIdDuplicate(String usersId);
    public boolean updatePassword(String userId, String encodedPassword);
    
    public boolean resetPassword(String userId, String newPassword);

	public User findUserByUserIdAndEmail(String userId, String email);

	public boolean isEmailRegistered(String email);

	

	public User findByEmail(String email);
}    