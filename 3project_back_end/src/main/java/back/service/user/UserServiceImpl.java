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
	         

	         // 비밀번호 암호화
	         String usersPassword = user.getUsersPassword();
	         user.setUsersPassword(passwordEncoder.encode(usersPassword));

	         // 생성 일시 설정
	         user.setCreateDt(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));

	         // 회원 등록
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
	public User findUserByUserIdAndEmail(String usersId, String usersEmail) {
	    Map<String, Object> params = new HashMap<>();
	    params.put("usersId", usersId);
	    params.put("usersEmail", usersEmail);
	    return userMapper.findUserByUserIdAndEmail(params);
	}
	
	@Override
	public boolean updatePassword(String usersId, String encodedPassword) {
	    return userMapper.updatePassword(usersId, encodedPassword) > 0;
	}
	
	@Override
	public boolean resetPassword(String usersId, String newPassword) {
	    User user = userMapper.findByUserId(usersId);
	    if (user == null) return false;

	    String encodedPassword = passwordEncoder.encode(newPassword);
	    return userMapper.updatePassword(usersId, encodedPassword) > 0;
	}
	
	
	@Override
	public boolean isEmailAlreadyRegistered(String email) {
	    return userMapper.isEmailRegistered(email); // UserService에 이메일 중복 체크 메서드 필요
	}
	@Override
	public boolean isEmailRegistered(String email) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public User findByEmail(String email) {
	    List<User> users = userMapper.selectUsersByEmail(email);
	    return users != null && !users.isEmpty() ? users.get(0) : null;
	}

}