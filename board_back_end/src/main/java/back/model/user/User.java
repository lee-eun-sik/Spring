package back.model.user;

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
    private String userId;    // 사용자 ID (Primary Key)
    private String username;  // 사용자 이름
    private String password;  // 비밀번호 (암호화 저장됨)
    private String email;     // 이메일
}