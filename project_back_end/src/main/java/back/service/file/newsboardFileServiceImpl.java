package back.service.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import back.model.NewBoard.NewBoard;
import back.model.common.PostFile;
import back.util.FileUploadUtil;
import back.util.MybatisUtil;
@Service
public class newsboardFileServiceImpl implements newsboardFileService {

	@Override
	public List<NewBoard> getAllNotices() {
	    SqlSession sqlSession = MybatisUtil.getSqlSessionFactory().openSession();
	    try {
	        return sqlSession.selectList("back.mapper.NewBoardMapper.getAllNotices");
	    } finally {
	        sqlSession.close();
	    }
	}

	

	@Override
	public ResponseEntity<?> uploadImage(PostFile postFile) {
	    try {
	        // Get the file from PostFile object
	        MultipartFile file = postFile.getFile();
	        
	        // Save the file using your custom file upload utility
	        String fileName = FileUploadUtil.saveFile(file); // Assuming saveFile accepts a MultipartFile
	        return ResponseEntity.ok("File uploaded successfully: " + fileName);
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
	    }
	}



	@Override
	public void createNotice(NewBoard newBoard) {
	    // Save the new board (notice) in the database
	    SqlSession sqlSession = MybatisUtil.getSqlSessionFactory().openSession();
	    try {
	        // Insert the new board record into the database
	        sqlSession.insert("back.mapper.NewBoardMapper.insertNewBoard", newBoard);
	        
	        // If there are files to upload, associate them with the notice
	        if (newBoard.getFiles() != null) {
	            for (PostFile postFile : newBoard.getFiles()) {
	                // Save the file record into the database
	                sqlSession.insert("back.mapper.NewBoardMapper.insertPostFile", postFile);
	            }
	        }
	        sqlSession.commit();
	    } finally {
	        sqlSession.close();
	    }
	}
	
	
    


}