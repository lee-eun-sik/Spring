package back.service.user;

import org.apache.ibatis.session.SqlSession;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.user.UserDAO;
import model.user.User;
import util.MybatisUtil;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private UserDAO userDAO;

    private SqlSessionFactory sqlSessionFactory; // MyBatis SQL 세션 팩토리
    
    /**
     * UserServiceImpl 생성자
     */
    public UserServiceImpl() {
        this.userDAO = new UserDAO();
        try {
            sqlSessionFactory = MybatisUtil.getSqlSessionFactory(); // SQL 세션 팩토리 초기화
        } catch (Exception e) {
            logger.error("Mybatis 오류", e); // 오류 발생 시 로그 출력
        }
    }

    /**
     * 사용자 회원가입 서비스
     * @param userId 사용자 ID
     * @param username 사용자 이름
     * @param password 비밀번호 (SHA-256 암호화 적용)
     * @param email 이메일
     * @return 성공 여부
     */
    
	public boolean validateUser(User user) {
		SqlSession session = sqlSessionFactory.openSession();
		boolean result = false; 
		try {
			  User selectUser = userDAO.getUserById(session, user.getUserId()); // 사용자 정보 조회
			  // 사용자 정보가 없으면 false 반환
			  if (user == null) {
				  return false; // 사용자 ID가 존재하지 않을 경우
			  }
			  // 입력된 비밀번호와 DB에 저장된 비밀번호 비교
			  result = user.getPassword().equals(selectUser.getPassword()); // 비밀번호 비교
			  session.commit();
		  
		} catch (Exception e) {
			e.printStackTrace();
		    session.rollback();
		}
		return result;
	}

	@Override
    public boolean registerUser(User user) {
        SqlSession session = sqlSessionFactory.openSession();
        boolean result = false; 
        try {
            // DAO를 통해 회원가입 진행
            result = userDAO.registerUser(session, user);
            session.commit(); // 트랜잭션 커밋
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        }
        return result;
    }
	
	public User getUserById(String userId) {
		SqlSession session = sqlSessionFactory.openSession();
		User selectUser = userDAO.getUserById(session, userId);
		return selectUser;
	}
	
	
    


}