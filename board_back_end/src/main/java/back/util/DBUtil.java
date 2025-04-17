package back.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe"; // DB URL
    private static final String USER = "c##leeeunsik"; // DB 사용자명
    private static final String PASSWORD = "1234"; // DB 비밀번호

    public static Connection getConnection() throws SQLException {
    	try {
            Class.forName("oracle.jdbc.driver.OracleDriver"); // 드라이버 로드
        } catch (ClassNotFoundException e) {
            throw new SQLException("Oracle JDBC Driver not found.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
