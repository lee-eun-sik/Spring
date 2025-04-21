package back.mapper.user;

import org.apache.ibatis.annotations.Mapper;

import back.model.user.User;
@Mapper
public interface UserMapper {

	User getUserById(String username);

	void registerUser(User user);

	int checkUserIdDuplicate(String userId);

}
