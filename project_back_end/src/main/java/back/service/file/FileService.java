package back.service.file;


import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;
import back.model.common.PostFile;

public interface FileService {
    
	
    public PostFile getFileByFileId(PostFile file);
    
    public HashMap insertBoardFiles(HttpServletRequest request);

	
    

}