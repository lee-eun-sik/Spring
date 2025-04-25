package back.service.file;



import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import back.model.NewBoard.NewBoard;
import back.model.common.PostFile;
@Service
public interface newsboardFileService {
    
	
    
    
    

	public List<NewBoard> getAllNotices();

	

	public ResponseEntity<?> uploadImage(PostFile postFile);



	public void createNotice(NewBoard newBoard);
    

}