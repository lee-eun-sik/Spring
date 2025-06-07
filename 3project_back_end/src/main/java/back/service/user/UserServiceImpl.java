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
	         log.debug("입력된 비밀번호: {}", user.getUsersPassword());

	         // 비밀번호 암호화
	         String usersPassword = user.getUsersPassword();
	         user.setUsersPassword(passwordEncoder.encode(usersPassword));

	         // 생성 일시 설정
	         user.setCreateDt(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));

	         int result = userMapper.registerUser(user);
	         log.debug("registerUser 결과: {}", result);  // 몇 건이 insert 되었는지 출력
	         
	         return result > 0;
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
	     try {
	         return userMapper.selectUsersByEmail(email);
	     } catch (Exception e) {
	         log.error("이메일로 사용자 목록 조회 중 오류", e);
	         throw new HException("사용자 조회 실패", e);
	     }
	 }

	 @Override
	 public User findUserByUserIdAndEmail(String usersId, String usersEmail) {
	     try {
	         Map<String, Object> params = new HashMap<>();
	         params.put("usersId", usersId);
	         params.put("usersEmail", usersEmail);
	         return userMapper.findUserByUserIdAndEmail(params);
	     } catch (Exception e) {
	         log.error("아이디와 이메일로 사용자 조회 중 오류", e);
	         throw new HException("사용자 조회 실패", e);
	     }
	 }
	
	 @Override
	 public boolean updatePassword(String usersId, String encodedPassword) {
	     try {
	         return userMapper.updatePassword(usersId, encodedPassword) > 0;
	     } catch (Exception e) {
	         log.error("비밀번호 업데이트 중 오류", e);
	         throw new HException("비밀번호 업데이트 실패", e);
	     }
	 }
	
	 @Override
	 public boolean resetPassword(String usersId, String newPassword) {
	     try {
	         User user = userMapper.findByUserId(usersId);
	         if (user == null) return false;

	         String encodedPassword = passwordEncoder.encode(newPassword);
	         return userMapper.updatePassword(usersId, encodedPassword) > 0;
	     } catch (Exception e) {
	         log.error("비밀번호 재설정 중 오류", e);
	         throw new HException("비밀번호 재설정 실패", e);
	     }
	 }
	
	
	@Override
	public boolean isEmailRegistered(String email) {
		try {
		    int count = userMapper.isEmailRegistered(email);
		    return count > 0;
		} catch (Exception e) {
	    	log.error("이메일 등록 여부 중 확인 실패했습니다.");
	    	throw new HException("이메일 등록 여부 확인 실패");
	    }
	}
	@Override
	public User findByEmail(String email) {
	    try {
	        List<User> users = userMapper.selectUsersByEmail(email);
	        return users != null && !users.isEmpty() ? users.get(0) : null;
	    } catch (Exception e) {
	        log.error("이메일로 사용자 조회 중 오류", e);
	        throw new HException("이메일 조회 실패", e);
	    }
	}

}