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
	
	User findUserForPwReset(@Param("userId") String userId,
            @Param("username") String username,
            @Param("phonenumber") String phonenumber,
            @Param("birthDate") String birthDate,
            @Param("email") String email);
	int updatePassword(@Param("userId") String userId, @Param("encodedPassword") String encodedPassword);
}
