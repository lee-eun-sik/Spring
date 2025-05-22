package back.mapper.user;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import back.model.user.User;
@Mapper
public interface UserMapper {

	User getUserById(String username);

	void registerUser(User user);

	int checkUserIdDuplicate(String userId);
	
	List<User> findUsersByInfo(Map<String, Object> params);
	
	User findUserForPwReset(String userId, String username, String phonenumber, String birthDate, String email);
	int updatePassword(String userId, String encodedPassword);
}
