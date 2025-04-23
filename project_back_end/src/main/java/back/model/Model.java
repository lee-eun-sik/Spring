package back.model;

import java.util.List;

import back.model.petSitter.PetSitter;
import lombok.Data;

@Data // 겟터 세터없음
public class Model {
    private String createId;   // 생성자 ID
    private String updateId;   // 수정자 ID
    private String createDt;   // 생성일
    private String updateDt;   // 수정일
	public void addAttribute(String string, String string2) {
		// TODO Auto-generated method stub
		
	}
	public void addAttribute(String string, List<PetSitter> petSitterList) {
		// TODO Auto-generated method stub
		
	}
}
