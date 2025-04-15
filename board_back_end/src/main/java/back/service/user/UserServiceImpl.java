package back.service.user;

import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import back.exception.HException;
import back.mapper.user.UserMapper;
import back.model.user.User;
import back.util.MybatisUtil;
import back.util.SHA256Util;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
@Data
@Slf4j
public class UserServiceImpl implements UserService {// 보안때문, 인터페이스 호출, 스프링때문에 생긴이유
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    /**
     * UserServiceImpl 생성자
     */
   

    /**
     * 사용자 회원가입 서비스
     * @param userId 사용자 ID
     * @param username 사용자 이름
     * @param password 비밀번호 (SHA-256 암호화 적용)
     * @param email 이메일
     * @return 성공 여부
     */
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
	 public User getUserById(String userId) {
		 try {
			 return userMapper.getUserById(userId);
		 } catch (Exception e) {
			 log.error("사용자 조회 중 오류", e);
			 throw new HException("사용자 조회 실패", e);
		 }
	 }

	 
	@Override
	@Transactional
	public boolean updateUser(User user) {
		try {
			String password = user.getPassword();
			user.setPassword(password != null ? passwordEncoder.encode(password) : null);
			return userMapper.updateUser(user) > 0;
		} catch(Exception e) {
			log.error("사용자 수정 중 오류", e);
			throw new HException("사용자 수정 실패", e);
		}
	}
	
	@Override
	@Transactional
	public boolean deleteUser(User user) {
		// TODO Auto-generated method stub
		try {
			String password = user.getPassword();
			user.setPassword(password != null ? passwordEncoder.encode(password) : null);
			return userMapper.deleteUser(user) > 0;
		} catch(Exception e) {
			log.error("사용자 수정 중 오류", e);
			throw new HException("사용자 수정 실패", e);
		}
	}

	
	
}
