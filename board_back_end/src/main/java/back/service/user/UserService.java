package back.service.user;

import java.util.Date;

import back.model.user.User;

public interface UserService {
    /**
     * 사용자 회원가입
     */
    boolean registerUser(User user);
    
    boolean validateUser(User user);
    
    public User getUserById(String userId);
    
  

	boolean updateUser(User user);

	boolean deleteUser(User user);

	
}
