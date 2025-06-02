package back.model.user;

import java.util.Date;

import back.model.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자 자동 생성
@EqualsAndHashCode(callSuper = true)
public class User extends Model {
	private String userId;     // 사용자 ID (Primary Key)
    private String username;   // 사용자 이름
    private String password;   // 비밀번호 (암호화 저장됨)
    private String email;      // 이메일
    //검색 필터
    private String startDate;
	private String endDate;
	private String searchText;
	private String delYn;
	 //페이징 정렬
	private int rn;
	private int startRow;
	private int endRow;
	private int page = 1; // 기본 페이지 1
	private int size = 10; // 기본 페이지 크기 10
	private int totalCount;
	private int totalPages;

	private String sortField = "CREATE_DT";
    private String sortOrder = "DESC"; //정렬
	public void setUsersPassword(Object object) {
		// TODO Auto-generated method stub
		
	}
	public String getUsersPassword() {
		// TODO Auto-generated method stub
		return null;
	}
}