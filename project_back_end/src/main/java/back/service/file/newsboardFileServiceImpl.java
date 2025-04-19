package service.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.file.newsboardFileDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import model.common.PostFile;
import util.FileUploadUtil;
import util.MybatisUtil;

public class newsboardFileServiceImpl implements newsboardFileService {
    private static final Logger logger = LogManager.getLogger(newsboardFileServiceImpl.class);
    private newsboardFileDAO newsboardFileDAO;

    private SqlSessionFactory sqlSessionFactory; // MyBatis SQL 세션 팩토리
    
    /**
     * FileServiceImpl 생성자
     */
    public newsboardFileServiceImpl() {
        this.newsboardFileDAO = new newsboardFileDAO();
        try {
            sqlSessionFactory = MybatisUtil.getSqlSessionFactory(); // SQL 세션 팩토리 초기화
        } catch (Exception e) {
            logger.error("Mybatis 오류", e); // 오류 발생 시 로그 출력
        }
    }

	@Override
	public PostFile getFileByFileId(PostFile file) {
		SqlSession session = sqlSessionFactory.openSession();
		PostFile selectPostFile = newsboardFileDAO.getFileByFileId(session, file);
		return selectPostFile;
	}

	@Override
	public HashMap insertBoardFiles(HttpServletRequest request) {
		SqlSession session = sqlSessionFactory.openSession();
		HashMap resultMap = new HashMap();
		
		try {
			//파일 업로드 처리
			int boardId = Integer.parseInt(request.getParameter("boardId"));
			String userId = request.getParameter("userId");
			String basePath = request.getParameter("basePath");
			
			//파일 업로드 파트 필터링
			List<Part> fileParts = new ArrayList();
			for (Part part : request.getParts()) {
				if ("files".equals(part.getName()) && part.getSize() > 0) {
					fileParts.add(part);
				}
			}
			
			//업로드된 파일들을 처리하여 PostFile 객체 리스트 반환
			List<PostFile> fileList = FileUploadUtil.uploadFiles(fileParts, basePath, boardId, userId);
					
			for (PostFile PostFile : fileList) {
				newsboardFileDAO.insertBoardFile(session, PostFile);
			}
			
			if (fileList.size() > 0) {
				resultMap.put("fileId", String.valueOf(fileList.get(0).getFileId()));
			}
			resultMap.put("result", true);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
			resultMap.put("result", false);
		}
		return resultMap;
	}


}