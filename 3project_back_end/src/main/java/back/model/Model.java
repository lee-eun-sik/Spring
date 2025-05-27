package back.model;

import java.util.List;


import lombok.Data;

@Data // 겟터 세터없음
public class Model {
    private String createId;   // 생성자 ID
    private String updateId;   // 수정자 ID
    private String createDt;   // 생성일
    private String updateDt;   // 수정일
	
	
}
