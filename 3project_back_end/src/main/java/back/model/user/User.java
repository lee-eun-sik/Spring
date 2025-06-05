package back.model.user;
import java.util.Date;

import back.model.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends Model{
	private String usersId;
	private String usersFileId;
	private String usersName;
	private String usersPassword;
	private String usersEmail;
	private String createDt;
	private String updateDt;
	private String createId;
	private String updateId;
	private String delYn;
	public String getUsersId() { return usersId; }
	public void setUsersId(String usersId) { this.usersId = usersId; }

	public String getUsersPassword() { return usersPassword; }
	public void setUsersPassword(String usersPassword) { this.usersPassword = usersPassword; }

	public String getUsersEmail() { return usersEmail; }
	public void setUsersEmail(String usersEmail) { this.usersEmail = usersEmail; }

	public String getUsersName() { return usersName; }
	public void setUsersName(String usersName) { this.usersName = usersName; }
}
