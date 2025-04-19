package service.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.board.reviewBoardDAO;
import dao.file.FileDAO;
import dao.petSitter.PetSitterDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import model.board.Board;
import model.board.Comment;
import model.common.PostFile;
import model.petSitter.PetSitter;
import util.FileUploadUtil;
import util.MybatisUtil;

public class reviewBoardServiceImpl implements reviewBoardService {
    private static final Logger logger = LogManager.getLogger(reviewBoardServiceImpl.class);
    private reviewBoardDAO reviewboardDAO;//DB접속용
    private FileDAO fileDAO;
    private PetSitterDAO petSitterDAO;
    

    private SqlSessionFactory sqlSessionFactory; // MyBatis SQL 세션 팩토리
    
    /**
     * BoardServiceImpl 생성자 //서비스 생성 이유: 비즈니스 로직을 담기 위해(하나의 비즈니스 로직은 하나의 함수만 담음)
     */
    public reviewBoardServiceImpl() {
        this.reviewboardDAO = new reviewBoardDAO();
        this.fileDAO = new FileDAO();
        this.petSitterDAO = new PetSitterDAO();
        try {
            sqlSessionFactory = MybatisUtil.getSqlSessionFactory(); // SQL 세션 팩토리 초기화
        } catch (Exception e) {
            logger.error("Mybatis 오류", e); // 오류 발생 시 로그 출력
        }
        
    }
    
    //유저 정보를 조회
    public Board getBoardById(String boardId) {
    	SqlSession session = sqlSessionFactory.openSession();
    	Board selectBoard = reviewboardDAO.getBoardById(session, boardId);
    	if (selectBoard != null) {
            // 🔹 rating이 null이면 기본값 0으로 설정
            Integer rating = selectBoard.getRating();
            selectBoard.setRating((rating != null) ? rating : 0);
        }
    	//파일목록조회
    	selectBoard.setPostFiles(fileDAO.getFilesByBoardId(session, boardId));
    	//댓글목록조회
    	selectBoard.setComments(reviewboardDAO.getCommentList(session, boardId));
    	
    	return selectBoard;
    }
    
    public boolean createBoard(Board board, HttpServletRequest request) {

    	SqlSession session = sqlSessionFactory.openSession();
    	boolean result = false; 
    	
    	
    	try {
    		result= reviewboardDAO.createBoard(session, board);
    		
    		//파일 업로드 파트 필터링
	    	List<Part> fileParts=new ArrayList<>();
	    	for(Part part: request.getParts()) {
	    		if("files".equals(part.getName())&&part.getSize()>0) {
	    			fileParts.add(part);
	    		}
	    	}
	    	//업로드 된 파일들을 처리하여 PostFile 객체 리스트 반환
	    	List<PostFile> fileList = FileUploadUtil.uploadFiles(fileParts, "board", 
	    					Integer.parseInt(board.getBoardId()), board.getCreateId());
   
    		for (PostFile postFile : fileList) {
    			fileDAO.insertBoardFile(session, postFile);
    		}
	    	
            session.commit(); // 트랜잭션 커밋
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
		}
        return result;
    }
    
    public boolean updateBoard(Board board, HttpServletRequest request) {
    	SqlSession session=sqlSessionFactory.openSession();
    	boolean result=false;
    	try {
    		result = reviewboardDAO.updateBoard(session, board);
    		if (result) {
    			String postFilesParam = request.getParameter("remainingFileIds");
    			
    			List<String> postFiles = new ArrayList<String>();
    			if(postFilesParam !=null && !postFilesParam.trim().isEmpty()) {
    				postFiles = Arrays.asList(postFilesParam.split(","));
    			}
    			
    			//기존 파일 조회
    			List<PostFile> existingFiles = fileDAO.getFilesByBoardId(session,(board.getBoardId()));
    			
    			//기존 파일 리스트가 있을 때
    			if(existingFiles!= null && existingFiles.size() > 0) {
	    			boolean fileExists = false;
	    			for(PostFile existingFile : existingFiles) {
	    				fileExists = false;
	    				for(String fileId :postFiles) {
	    					if(existingFile.getFileId() == Integer.parseInt(fileId)) {
	    						fileExists =true;
	    						break;
	    					}
	    				}
    				
    				//넘어온 파일 목록에 없으면 삭제
    				if(! fileExists) {
    					existingFile.setUpdateId(board.getUpdateId());
    					boolean deleteSuccess = fileDAO.deleteFile(session, existingFile);
    					if(! deleteSuccess) {
    						session.rollback();
    						return false;
    					}
    				}
    			}
    		}	
    			
    			//새로운 파일 업로드
    	    	List<Part> fileParts=new ArrayList<>();
    	    	for(Part part: request.getParts()) {
    	    		if("files".equals(part.getName())&&part.getSize()>0) {
    	    			fileParts.add(part);
    	    		}
    	    	}
    	    	//업로드 된 파일들을 처리하여 PostFile 객체 리스트 반환
    	    	List<PostFile> fileList = FileUploadUtil.uploadFiles(fileParts, "board", 
    	    					Integer.parseInt(board.getBoardId()), board.getUpdateId());
       
        		for (PostFile postFile : fileList) {
        			fileDAO.insertBoardFile(session, postFile);
        		}
			}
    		
    		session.commit();
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
    }
    	return result;
  }

	@Override
	public boolean deleteBoard(Board board) {
		SqlSession session=sqlSessionFactory.openSession();
    	boolean result=false;
    	try {
    		result = reviewboardDAO.deleteBoard(session, board);
    		session.commit();
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
		System.out.println("✅ 한 페이지당 게시글 개수 (size): " + size);
		
		
		int totalCount = reviewboardDAO.getTotalBoardCount(session, board);
		int totalPages = (int)Math.ceil((double) totalCount/size);
				
		int startRow = (page -1) * size + 1;
		int endRow = page * size;
		
		board.setTotalCount(totalCount);
		board.setTotalPages(totalPages);
		board.setStartRow(startRow);
		board.setEndRow(endRow);
		
		// 🔹 추가된 부분 (userType 설정)
	    String userType = board.getUserType();
	    if (userType != null && !userType.isEmpty()) {
	        board.setUserType(userType);
	    }
	    
		List list = reviewboardDAO.getBoardList(session,board);
		
		
		return list;
	}

	@Override
	public boolean createComment(Comment comment) {
		SqlSession session = sqlSessionFactory.openSession();
    	boolean result = false;    	   	
    	try {
    		result= reviewboardDAO.insertComment(session, comment);
    		session.commit();	
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
		}
        return result;
	}

	@Override
	public boolean updateComment(Comment comment) {
			SqlSession session = sqlSessionFactory.openSession();
	    	boolean result = false;    	   	
	    	try {
	    		result= reviewboardDAO.updateComment(session, comment);
	    		session.commit();	
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    		session.rollback();
			}
	        return result;
		}

	@Override
	public boolean deleteComment(Comment comment) {
			SqlSession session = sqlSessionFactory.openSession();
	    	boolean result = false;    	   	
	    	try {
	    		result= reviewboardDAO.deleteComment(session, comment);
	    		session.commit();	
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    		session.rollback();
			}
	        return result;
		}
	
	//⭐ 별점 통계 조회
	@Override
    public List<Map<String, Object>> selectRatingStats() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return reviewboardDAO.getRatingStat(session);
	    }
	}
	
	// ⭐ 총 후기 개수 조회
	@Override
    public int TotalReviewCount() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return reviewboardDAO.TotalReviewCount(session);
	    }
	}
	
	public boolean viewCount(Board board) {
		SqlSession session = sqlSessionFactory.openSession();
		boolean result = false;
		try {
			result = reviewboardDAO.viewCount(session, board);
			session.commit(); // 트랜잭션 커밋
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return result;
	}
	
	// 예약 등록 화면에 펫시터 리스트를 보여주는 메서드
		public List<PetSitter> getPetSitterList() {
		    SqlSession session = sqlSessionFactory.openSession();
		    List<PetSitter> sitterList = petSitterDAO.getPetSitterList(session);
		    session.close();
		    return sitterList;
		}
	
}
	