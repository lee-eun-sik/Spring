package back.service.user;

import org.apache.ibatis.session.SqlSession;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Date;
import org.apache.ibatis.session.SqlSessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import back.exception.HException;
import back.mapper.user.UserMapper;
import back.model.user.User;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class UserServiceImpl implements UserService {
	@Autowired
    private UserMapper userMapper;
	 @Autowired
	 private PasswordEncoder passwordEncoder;
	 @Override
	    public boolean registerUser(User user) {
	    	try {
	    		String password = user.getUsersPassword();
	    		user.setUsersPassword(password != null ? passwordEncoder.encode(password) : null);
	    		return userMapper.registerUser(user) > 0;
	    	} catch (Exception e) {
	    		log.error("회원가입 중 오류", e);
	    		throw new HException("회원가입 실패", e);
	    	}
	    }
	 @Override
	 public boolean validateUser(User user) {
		 // TODO Auto-generated method stub
		 try {
			 User dbUser = userMapper.getUserById(user.getUsersId());
			 if (dbUser == null) return false;
			
			 String encryptedPassword = passwordEncoder.encode(user.getUsersPassword());
			 return passwordEncoder.matches(dbUser.getUsersPassword(), encryptedPassword);
		 } catch(Exception e) {
			 log.error("로그인 검증 중 오류", e);
			 throw new HException("로그인 검증 실패", e);
		 }
	 }
	 
	 @Override
	public boolean isUserIdDuplicate(String usersId) {
		try {
			int count = userMapper.checkUserIdDuplicate(usersId);
			return count > 0; // 이미 DB에 존재하면 true, 존재하지 않으면 false
		} catch (Exception e) {
			log.error("아이디 중복 체크 중 오류", e);
			throw new HException("아이디 중복 체크 실패", e);
		}
	}
	
	@Override
    public List<User> findUsersByInfo(String email) {
        return userMapper.selectUsersByEmail(email);
    }

	@Override
	public User findUserByUserIdAndEmail(String userId, String email) {
	    Map<String, Object> params = new HashMap<>();
	    params.put("userId", userId);
	    params.put("email", email);
	    return userMapper.findUserByUserIdAndEmail(params);
	}
	
	@Override
	public boolean updatePassword(String userId, String encodedPassword) {
	    return userMapper.updatePassword(userId, encodedPassword) > 0;
	}
	
	@Override
	public boolean resetPassword(String userId, String newPassword) {
	    User user = userMapper.findByUserId(userId);
	    if (user == null) return false;

	    String encodedPassword = passwordEncoder.encode(newPassword);
	    return userMapper.updatePassword(userId, encodedPassword) > 0;
	}
	@Override
	public boolean isUserEmailDuplicate(String email) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
    public boolean isEmailRegistered(String email) {
        return userMapper.countByEmail(email) > 0;
    }
	
	@Override
    public User findByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

}