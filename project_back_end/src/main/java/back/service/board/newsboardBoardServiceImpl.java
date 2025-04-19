package service.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.board.newsboardBoardDAO;
import dao.file.newsboardFileDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import model.board.Board;
import model.board.Comment;
import model.common.PostFile;
import model.user.User;
import util.FileUploadUtil;
import util.MybatisUtil;

public class newsboardBoardServiceImpl implements newsboardBoardService {
    private static final Logger logger = LogManager.getLogger(newsboardBoardServiceImpl.class);
    private newsboardBoardDAO newsboardBoardDAO;
    private newsboardFileDAO newsboardFileDAO;

    private SqlSessionFactory sqlSessionFactory; // MyBatis SQL 세션 팩토리
    
    /**
     * UserServiceImpl 생성자
     */
    public newsboardBoardServiceImpl() {
        this.newsboardBoardDAO = new newsboardBoardDAO();
        this.newsboardFileDAO = new newsboardFileDAO();
        try {
            sqlSessionFactory = MybatisUtil.getSqlSessionFactory(); // SQL 세션 팩토리 초기화
        } catch (Exception e) {
            logger.error("Mybatis 오류", e); // 오류 발생 시 로그 출력
        }
    }
    

	public Board getBoardById(String boardId) {
		SqlSession session = sqlSessionFactory.openSession();
		Board selectBoard = newsboardBoardDAO.getBoardById(session, boardId);
		//파일 목록 조회
		selectBoard.setPostFiles(newsboardFileDAO.getFilesByBoardId(session, boardId));
		
		//댓글 목록 조회
		selectBoard.setComments(newsboardBoardDAO.getCommentList(session, boardId));
				
		return selectBoard;
	}
	
	public boolean createBoard(Board board, HttpServletRequest request) {
        SqlSession session = sqlSessionFactory.openSession();
        boolean result = false; 
        try {
            result = newsboardBoardDAO.createBoard(session, board);
			
			//파일 업로드 파트 필터링
			List<Part> fileParts = new ArrayList();
			for (Part part : request.getParts()) {
				if ("files".equals(part.getName()) && part.getSize() > 0) {
					fileParts.add(part);
				}
			}
			
			//업로드된 파일들을 처리하여 PostFile 객체 리스트 반환
			List<PostFile> fileList = FileUploadUtil.uploadFiles(fileParts, "board",
						Integer.parseInt(board.getBoardId()), board.getCreateId());
					
			for (PostFile PostFile : fileList) {
				newsboardFileDAO.insertBoardFile(session, PostFile);
			}
            
            session.commit(); // 트랜잭션 커밋
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        }
        return result;
    }
	
	public boolean updateBoard(Board board, HttpServletRequest request) {
        SqlSession session = sqlSessionFactory.openSession();
        boolean result = false; 
        try {
            result = newsboardBoardDAO.updateBoard(session, board);
            if (result) {
            	
            	String postFilesParam = request.getParameter("remainingFileIds");  // 기존 파일 목록 (쉼표로 구분된 문자열)
            	
            	List<String> postFiles = new ArrayList<String>();
            	if (postFilesParam != null &&!postFilesParam.trim().isEmpty()) {
            		postFiles = Arrays.asList(postFilesParam.split(",")); //쉼표로 구분된 파일명 리스트
            	}
            	
            	//기존 파일 조회
            	List<PostFile> existingFiles = newsboardFileDAO.getFilesByBoardId(session, board.getBoardId());
            	
            	//기존 파일 리스트가 있을때
            	if (existingFiles != null && existingFiles.size() > 0) {
            		boolean fileExists = false;
	            	for (PostFile existingFile : existingFiles) {
	            		fileExists = false;
	            		//새로 넘어온 파일목록에 기존 파일이 포함되어 있는지 체크
	            		for (String fileId : postFiles) {
	            			if (existingFile.getFileId() == Integer.parseInt(fileId)) {
	            				fileExists = true;
	            				break;
	            			}
	            		}
	            		
	            		//넘어온 파일 목록에 없으면 삭제
	            		if (!fileExists) {
	            			existingFile.setUpdateId(board.getUpdateId());
	            			boolean deleteSuccess = newsboardFileDAO.deleteFile(session, existingFile);
	            			if (!deleteSuccess) {
	            				session.rollback(); // 파일 삭제 실패시 출력
	            				return false;
	            			}
	            		}
	            	}
            	}
            	
            	
            	
            	//새로운 파일 업로드
    			List<Part> fileParts = new ArrayList();
    			for (Part part : request.getParts()) {
    				if ("files".equals(part.getName()) && part.getSize() > 0) {
    					fileParts.add(part);
    				}
    			}
    			
    			//업로드된 파일들을 처리하여 PostFile 객체 리스트 반환
    			List<PostFile> fileList = FileUploadUtil.uploadFiles(fileParts, "board",
    						Integer.parseInt(board.getBoardId()), board.getUpdateId());
    					
    			for (PostFile PostFile : fileList) {
    				newsboardFileDAO.insertBoardFile(session, PostFile);
    			}
            	
            }
            
            
            session.commit(); // 트랜잭션 커밋
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        }
        return result;
    }
	
	public boolean deleteBoard(Board board) {
        SqlSession session = sqlSessionFactory.openSession();
        boolean result = false; 
        try {
            result = newsboardBoardDAO.deleteBoard(session, board);
            session.commit(); // 트랜잭션 커밋
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        }
        return result;
    }


	@Override
	public List getBoardList(Board board) {
		SqlSession session = sqlSessionFactory.openSession();
		
		int page = board.getPage();
		int size = board.getSize();
		
		int totalCount = newsboardBoardDAO.getTotalBoardCount(session, board);
		int totalPages = (int) Math.ceil((double) totalCount / size);
		
		int startRow = (page - 1) * size + 1;
		int endRow = page *size;
		
		board.setTotalCount(totalCount);
		board.setTotalPages(totalPages);
		board.setStartRow(startRow);
		board.setEndRow(endRow);
		
		List list = newsboardBoardDAO.getBoardList(session, board);
		
		return list;
	}
	
	public boolean createComment(Comment comment) {
		SqlSession session = sqlSessionFactory.openSession();
		boolean result = false;
		try {
			result = newsboardBoardDAO.insertComment(session, comment);
			session.commit(); // 트랜잭션 커밋
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return result;
	}
	
	public boolean updateComment(Comment comment) {
		SqlSession session = sqlSessionFactory.openSession();
		boolean result = false;
		try {
			result = newsboardBoardDAO.updateComment(session, comment);
			session.commit(); // 트랜잭션 커밋
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return result;
	}
	
	public boolean deleteComment(Comment comment) {
		SqlSession session = sqlSessionFactory.openSession();
		boolean result = false;
		try {
			result = newsboardBoardDAO.deleteComment(session, comment);
			session.commit(); // 트랜잭션 커밋
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return result;
	}
	
	public boolean viewCount(Board board) {
		SqlSession session = sqlSessionFactory.openSession();
		boolean result = false;
		try {
			result = newsboardBoardDAO.viewCount(session, board);
			session.commit(); // 트랜잭션 커밋
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return result;
	}
    


}