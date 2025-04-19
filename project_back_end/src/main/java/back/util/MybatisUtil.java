package back.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MybatisUtil {
    private static SqlSessionFactory sqlSessionFactory;

    // 정적 초기화 블록에서 MyBatis 설정 파일을 읽고 SqlSessionFactory를 초기화
    static {
    	try {											 
    		InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
	        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
	    } catch (IOException e) {
	        throw new RuntimeException("MyBatis configuration file not found.", e);
	    }
    }
    /**
     * SqlSessionFactory를 반환하는 메서드
     * @return SqlSessionFactory 객체
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    /**
     * SqlSession을 반환하는 메서드
     * @return SqlSession 객체
     */
    public static SqlSession getSqlSession() {
        return sqlSessionFactory.openSession();
    }
}
