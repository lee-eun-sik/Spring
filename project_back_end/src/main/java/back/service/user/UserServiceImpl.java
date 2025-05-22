package back.service.user;

import org.apache.ibatis.session.SqlSession;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Date;
import org.apache.ibatis.session.SqlSessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import back.mapper.user.UserMapper;
import back.model.user.User;
import back.util.MybatisUtil;
import back.util.SHA256Util;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class UserServiceImpl implements UserService {
	@Autowired
    private UserMapper userMapper;

	@Override
	public boolean registerUser(User user) {
	    try (SqlSession session = MybatisUtil.getSqlSessionFactory().openSession()) {
	        UserMapper mapper = session.getMapper(UserMapper.class);

	        // 사용자 ID 중복 체크
	        int count = mapper.checkUserIdDuplicate(user.getUserId());
	        if (count > 0) {
	            return false; // 이미 존재하는 ID
	        }

	       
	        user.setCreateId(user.getUserId()); // 기본 작성자 ID로 userId 저장
	        user.setRole("USER"); // 기본 권한 설정

	        mapper.registerUser(user);
	        session.commit();
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	@Override
	public boolean validateUser(User user) {
	    try (SqlSession session = MybatisUtil.getSqlSessionFactory().openSession()) {
	        UserMapper mapper = session.getMapper(UserMapper.class);
	        int count = mapper.checkUserIdDuplicate(user.getUserId());
	        return count == 0; // 중복 없으면 유효
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
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

	public List<User> findUsersByInfo(String username, String phonenumber, String birthDate, String email) {
	    try (SqlSession session = MybatisUtil.getSqlSessionFactory().openSession()) {
	        UserMapper mapper = session.getMapper(UserMapper.class);
	        Map<String, Object> params = new HashMap<>();
	        params.put("name", username);
	        params.put("phone", phonenumber);
	        params.put("birthDate", birthDate);
	        params.put("email", email);
	        return mapper.findUsersByInfo(params);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Collections.emptyList(); // 결과가 없거나 오류 시 빈 리스트 반환
	    }
	}

	
	@Override
	public User findUserForPwReset(String userId, String username, String phonenumber, String birthDate, String email) {
	    return userMapper.findUserForPwReset(userId, username, phonenumber, birthDate, email);
	}

	@Override
	public boolean updatePassword(String userId, String encodedPassword) {
	    return userMapper.updatePassword(userId, encodedPassword) > 0;
	}
	
   
}