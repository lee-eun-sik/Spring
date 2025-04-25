package back.service.file;


import java.util.HashMap;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import back.model.common.PostFile;
@Service
public interface FileService {
    
	
    public PostFile getFileByFileId(PostFile file);

	HashMap insertBoardFiles(HttpServletRequest request);
    

	
    

}