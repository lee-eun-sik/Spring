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
import back.model.board.Board;
import back.model.common.PostFile;
import back.util.FileUploadUtil;

@Service
public class newsboardFileServiceImpl implements newsboardFileService {

	
	

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
	        sqlSession.insert("back.mapper.NewBoardMapper.create", newBoard);
	        
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
	

	@Override
	public List<NewBoard> getNoticeList(int page, int pageSize) {
	    int startRow = (page - 1) * pageSize + 1;  // Calculate start row based on page number and page size
	    int endRow = startRow + pageSize - 1;  // Calculate end row

	    SqlSession sqlSession = MybatisUtil.getSqlSessionFactory().openSession();
	    try {
	        return sqlSession.selectList("back.mapper.NewBoardMapper.getBoardList", Map.of("startRow", startRow, "endRow", endRow));
	    } finally {
	        sqlSession.close();
	    }
	}



	@Override
	public int getNoticeCount() {
	    SqlSession sqlSession = MybatisUtil.getSqlSessionFactory().openSession();
	    try {
	        return sqlSession.selectOne("back.mapper.NewBoardMapper.getTotalBoardCount");
	    } finally {
	        sqlSession.close();
	    }
	}



	@Override
	public List<NewBoard> getNoticeList() {
		// TODO Auto-generated method stub
		return null;
	}



	





}