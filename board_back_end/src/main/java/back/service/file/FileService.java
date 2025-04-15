package back.service.file;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import back.model.common.PostFile;
import jakarta.servlet.http.HttpServletRequest;


public interface FileService {
    public PostFile getFileByBoardIdAndFileId(PostFile  file);
    
	Map<String, Object> insertBoardFiles(PostFile file);
    
}
