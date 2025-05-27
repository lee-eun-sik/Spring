package back.mapper.user;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import back.model.user.User;
@Mapper
public interface UserMapper {

	User getUserById(String username);

	public int registerUser(User user);

	int checkUserIdDuplicate(String userId);
	
	List<User> findUsersByInfo(Map<String, Object> params);
	
	public User findUserByUserIdAndEmail(Map<String, Object> params);
	public int updatePassword(@Param("userId") String userId, @Param("encodedPassword") String encodedPassword);

	User findByUserId(String userId);
	
	public List<User> selectUsersByEmail(String email);
}
