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

	void registerUser(User user);

	int checkUserIdDuplicate(String userId);
	
	List<User> findUsersByInfo(Map<String, Object> params);
	
	User findUserByUserIdAndEmail(Map<String, Object> params);
	int updatePassword(@Param("userId") String userId, @Param("encodedPassword") String encodedPassword);

	User findByUserId(String userId);
}
