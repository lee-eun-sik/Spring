package back.service.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		return ResponseEntity.ok().body(Map.of("url", "/upload/path/" + postFile.getFile().getOriginalFilename()));
	}



	@Override
	public void createNotice(NewBoard newBoard) {
	    // Save the new board (notice) in the database
	    SqlSession sqlSession = MybatisUtil.getSqlSessionFactory().openSession();
	    try {
	        // Insert the new board record into the database
	        sqlSession.insert("back.mapper.NewBoardMapper.insertComment", newBoard);
	        
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