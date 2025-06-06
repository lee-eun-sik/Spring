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
	    		String password = user.getPassword();
	    		user.setPassword(password != null ? passwordEncoder.encode(password) : null);
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
			 User dbUser = userMapper.getUserById(user.getUserId());
			 if (dbUser == null) return false;
			
			 String encryptedPassword = passwordEncoder.encode(user.getPassword());
			 return passwordEncoder.matches(dbUser.getPassword(), encryptedPassword);
		 } catch(Exception e) {
			 log.error("로그인 검증 중 오류", e);
			 throw new HException("로그인 검증 실패", e);
		 }
	 }

	@Override
	public User login(String id, String pass) {
	    try (SqlSession session = MybatisUtil.getSqlSessionFactory().openSession()) {
	        UserMapper mapper = session.getMapper(UserMapper.class);
	        User user = mapper.getUserById(id); // id == userId

	        if (user != null) {
	            String encryptedPass = SHA256Util.encrypt(pass); // 입력한 비밀번호를 해싱
	            if (user.getPassword().equals(encryptedPass)) {
	                return user;
	            }
	        }
	        return null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	@Override
	public User getUserById(String userId) {
	    try (SqlSession session = MybatisUtil.getSqlSessionFactory().openSession()) {
	        UserMapper mapper = session.getMapper(UserMapper.class);
	        User user = mapper.getUserById(userId);
	        log.info("조회된 사용자 정보: {}", user);  // 디버깅용 로그 추가
	        return user;
	    } catch (Exception e) {
	        log.error("DB 조회 오류: {}", e.getMessage());
	        return null;
	    }
	}

	public List<User> findUsersByInfo(String email) {
	    try (SqlSession session = MybatisUtil.getSqlSessionFactory().openSession()) {
	        UserMapper mapper = session.getMapper(UserMapper.class);
	        Map<String, Object> params = new HashMap<>();
	       
	        params.put("email", email);
	        return mapper.findUsersByInfo(params);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Collections.emptyList(); // 결과가 없거나 오류 시 빈 리스트 반환
	    }
	}

	@Override
	public User findUserByUserIdAndEmail(String userId, String email) {
	    try (SqlSession session = MybatisUtil.getSqlSessionFactory().openSession()) {
	        UserMapper mapper = session.getMapper(UserMapper.class);
	        Map<String, Object> params = new HashMap<>();
	        params.put("userId", userId);
	        params.put("email", email);
	        return mapper.findUserByUserIdAndEmail(params);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
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
}