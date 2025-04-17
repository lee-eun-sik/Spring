package back.service.file;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import back.exception.HException;
import back.mapper.file.FileMapper;
import back.model.common.PostFile;

import back.util.FileUploadUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class FileServiceImpl implements FileService {// 보안때문, 인터페이스 호출, 스프링때문에 생긴이유
	@Autowired
	private FileMapper fileMapper;

    
    
    /**
     * boardServiceImpl 생성자
     */
    
    
	@Override
	public PostFile getFileByBoardIdAndFileId(PostFile file) {
		return fileMapper.getFileByBoardIdAndFileId(file);
	}
	@Override
	@Transactional
	public Map<String, Object> insertBoardFiles(PostFile file) { //오브젝트가 안에 들어감. 꺼낼때 강제로 형변환, 안에 들어갈 것을 정함 그것이 제네릭
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			int boardId = file.getBoardId();
			String userId = file.getCreateId();
			String bashPath = file.getBasePath();
			
			List<MultipartFile> files = file.getFiles();
			
			if (files == null || files.isEmpty()) {
				resultMap.put("result", false);
				resultMap.put("message", "파일이 존재하지 않습니다.");
				return resultMap;
			}
				
			List<PostFile> uploadedFiles = FileUploadUtil.uploadFiles(files, bashPath, boardId, userId);
			
			for (PostFile postFile : uploadedFiles) {
				fileMapper.insertFile(postFile);
			}
			
			resultMap.put("result", true);
			if(uploadedFiles != null && uploadedFiles.size() > 0) {
				resultMap.put("fileId", uploadedFiles.get(0).getFileId());
			}
			return resultMap;
		} catch (Exception e) {
			log.error("파일 업로드 중 오류", e);
			throw new HException("파일 업로드 실패", e);
		}
	}
}
