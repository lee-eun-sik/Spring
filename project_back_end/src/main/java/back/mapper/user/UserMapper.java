package back.mapper.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import back.model.user.User;
@Mapper
public interface UserMapper {

	User getUserById(String username);

	void registerUser(User user);

	int checkUserIdDuplicate(String userId);
	
	List<String> getUserIdsByEmail(String email);
}
