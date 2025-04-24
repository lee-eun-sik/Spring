package back.model.petSitter;

import java.util.List;

import back.model.board.Comment;
import back.model.Model;
import back.model.NewBoard.NewBoard;
import back.model.common.PostFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PetSitter extends Model {
	
	
	private String sitterName;
	private String content;
	
	
	@Override
	public String toString() {
	    return "PetSitter [sitterName=" + sitterName + ", content=" + content +"]";
	}
	
	public String getSitterName() {
		return sitterName;
	}


	public void setSitterName(String sitterName) {
		this.sitterName = sitterName;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}

	
}

	
	


	
