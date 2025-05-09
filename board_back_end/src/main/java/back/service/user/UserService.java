package back.service.user;

import java.util.Date;
import java.util.List;

import back.model.user.User;

public interface UserService {
    /**
     * 사용자 회원가입
     */
    boolean registerUser(User user);
    
    boolean validateUser(User user);
    //서비스 붙여야 스프링이 관리
    public User getUserById(String userId);
    
  

	boolean updateUser(User user);

	boolean deleteUser(User user);
	
	public List<User> getUserList(User user);
	
	public boolean userM(User user);
	
	public boolean isUserIdDuplicate(String userId);
}
