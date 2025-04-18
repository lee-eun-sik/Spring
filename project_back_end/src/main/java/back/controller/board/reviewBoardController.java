package controller.board;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.petSitter.PetSitter;
import model.board.Board;
import model.board.Comment;
import model.petSitter.PetSitter;
import service.board.newsboardBoardService;
import service.board.newsboardBoardServiceImpl;
import service.board.reviewBoardService;
import service.board.reviewBoardServiceImpl;


@WebServlet("/reviewboard/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,  // 1MB
        maxFileSize = 10 * 1024 * 1024,  // 10MB
        maxRequestSize = 50 * 1024 * 1024 // 50MB
)


public class reviewBoardController extends HttpServlet {

	private static final long serialVersionUID = 5726041575375068755L;
	private static final Logger logger = LogManager.getLogger(reviewBoardController.class); 
	private reviewBoardService reviewboardService;
	
	
	public reviewBoardController() {
        super();
        reviewboardService = new reviewBoardServiceImpl(); 
    }
	
	private static final int DEFAULT_PAGE = 1;
	private static final int DEFAULT_SIZE = 10;
	/**
	 * GET 화면 이동용 및 조회용
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  logger.info("BoardController doGet"); 
	      String path = request.getRequestURI();
	      logger.info("BoardController doGet path" + path); 
	      try {
		      if ("/reviewboard/reviewview.do".equals(path)) {
		    	  
		    	  String boardId = request.getParameter("id");
		    	  Board board = reviewboardService.getBoardById(boardId);
		    	  request.setAttribute("board", board);
		    	  
		          request.getRequestDispatcher("/WEB-INF/jsp/reviewboard/reviewview.jsp").forward(request, response);	    
		      } else if ("/reviewboard/reviewcreate.do".equals(path)) {
		    	  
		    	  List<PetSitter> sitterList = reviewboardService.getPetSitterList();
	              
	              logger.info("sitterList from DB: " + sitterList);  // 리스트 전체 확인
	              logger.info("sittserList size: " + (sitterList != null ? sitterList.size() : "null"));  // 사이즈 체크
	              
	              request.setAttribute("sitterList", sitterList);
		    	  
		    	  request.getRequestDispatcher("/WEB-INF/jsp/reviewboard/reviewcreate.jsp").forward(request, response);
		      } else if ("/reviewboard/reviewupdate.do".equals(path)) {
		    	  
		    	  String boardId = request.getParameter("id");
		    	  Board board = reviewboardService.getBoardById(boardId);
		    	  request.setAttribute("board", board);
		      
		    	  request.getRequestDispatcher("/WEB-INF/jsp/reviewboard/reviewupdate.jsp").forward(request, response);
		      } else if ("/reviewboard/reviewlist.do".equals(path)) {
		    	  
		    	    
	        	  String boardId = request.getParameter("id");
	  			String serachText = request.getParameter("searchText");
	  			String searchColumn = request.getParameter("searchColumn");
	  			
		    	  int page = request.getParameter("page") != null ?
		    			  Integer.parseInt(request.getParameter("page"))
		    			  : DEFAULT_PAGE;
		    	  int size = request.getParameter("size") != null ?
		    			  Integer.parseInt(request.getParameter("size"))
		    			  : DEFAULT_SIZE;

		    	  
						  
		    	  Board board = new Board();
		    	  board.setPage(page);
		    	  board.setSize(size);
		    	  board.setSearchText(serachText);
		    	  board.setSearchColumn(searchColumn);
		    	  
		    	  List boardList = reviewboardService.getBoardList(board);
		    	  
			    	// ★ 별점 통계/총 후기 개수 추가
			    	    List<Map<String, Object>> rawStats = reviewboardService.selectRatingStats();
			    	    Map<Integer, Integer> ratingStats = new HashMap<>();
	
			    	    for (int i = 0; i < rawStats.size(); i++) {
			    	        Map<String, Object> stat = rawStats.get(i);
			    	        Integer rating = ((Number) stat.get("RATING")).intValue();
			    	        Integer count = ((Number) stat.get("COUNT")).intValue();
			    	        ratingStats.put(rating, count);
			    	    }
			    	    
			    	    int totalReviewCount = reviewboardService.TotalReviewCount();
		
			    	    board.setRatingStats(ratingStats);
			    	    board.setTotalReviewCount(totalReviewCount);
	
		    	    
		    	    // View로 전달
		    	    // convertToRatingMap()을 쓰는 이유는 JSP에서 List<Map<String, Object>> 형태를 직접 다루기 어렵기 때문
	    	      request.setAttribute("ratingStats", ratingStats);
	    	      request.setAttribute("totalReviewCount", totalReviewCount);
		    	  request.setAttribute("boardList", boardList);
		    	  request.setAttribute("currentPage", page);
		    	  request.setAttribute("totalPages", board.getTotalPages());
		    	  request.setAttribute("size", size);
		    	  request.setAttribute("board", board);
		    	  request.setAttribute("serachText", serachText);
				  request.setAttribute("searchColumn", searchColumn);
		    	  
		    	  request.getRequestDispatcher("/WEB-INF/jsp/reviewboard/reviewlist.jsp").forward(request, response);
		      }
	    } catch (Exception e) {
            logger.error("Error in ReservationController doGet", e);
            request.setAttribute("errorMessage", "서버 오류 발생");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
	}
			  

		
	/**
	 * POST ajax 로직 처리용 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("BoardController doPost");
        String path = request.getRequestURI();
        response.setContentType("application/json; charset=UTF-8"); // 응답 타입 설정
        PrintWriter out = response.getWriter(); // PrintWriter 객체 생성
        JSONObject jsonResponse = new JSONObject(); // JSON 응답 객체 생성
        try {
            logger.info("BoardController doPost path: " + path);

            
            if ("/reviewboard/reviewcreate.do".equals(path)) {
            	String boardId = request.getParameter("board_id");
            	String title = request.getParameter("title");
            	String content = request.getParameter("content");
            	String createId = request.getParameter("createId");
            	Integer rating = Integer.valueOf(request.getParameter("rating"));
            	String sitter = request.getParameter("sitter");
            	System.out.println("BOARD_ID: " + boardId);

            	
            	Board board = new Board();
            	board.setTitle(boardId);
            	board.setTitle(title);
            	board.setContent(content);
            	board.setCreateId(createId);
            	board.setRating(rating);
            	board.setSitter(sitter);
            	
            	boolean isCreate = reviewboardService.createBoard(board, request);
            	jsonResponse.put("success", isCreate);
            	jsonResponse.put("message", isCreate ?
            			"게시글이 성공적으로 등록되었습니다." : "게시글이 등록실패");
            	
            	
            } else if ("/reviewboard/reviewupdate.do".equals(path)) {
            	String boardId = request.getParameter("boardId");
            	String title = request.getParameter("title");
            	String content = request.getParameter("content");
            	String createId = request.getParameter("createId");
            	String updateId = request.getParameter("updateId");
            	String sitter = request.getParameter("sitter");
            	
            	Board board = new Board();
            	board.setBoardId(boardId);
            	board.setTitle(title);
            	board.setContent(content);
            	board.setCreateId(createId);
            	board.setSitter(sitter);
            	
            	boolean isUpdate = reviewboardService.updateBoard(board, request);
            	jsonResponse.put("success", isUpdate);
            	jsonResponse.put("message", isUpdate ?
            			"게시글이 성공적으로 수정되었습니다." : "게시글 수정 실패");
            	
            	
            } else if ("/reviewboard/reviewdelete.do".equals(path)) {
            	String boardId = request.getParameter("boardId");
            	String updateId = request.getParameter("updateId");
            	
            	Board board = new Board();
            	board.setBoardId(boardId);
            	board.setUpdateId(updateId);
            	
            	boolean isDelete = reviewboardService.deleteBoard(board);
            	jsonResponse.put("success", isDelete);
            	jsonResponse.put("message", isDelete ?
            			"게시글이 성공적으로 삭제되었습니다." : "게시글 삭제 실패");
            	
            } else if ("/reviewboard/comment/create.do".equals(path)) {
            	int boardId = Integer.parseInt(request.getParameter("boardId"));
            	String content = request.getParameter("content");
            	String createId = request.getParameter("createId");
            	int parentCommentId = Integer.parseInt(request.getParameter("parentCommentId"));
            	
            	Comment comment = new Comment();
            	comment.setBoardId(boardId);
            	comment.setContent(content);
            	comment.setCreateId(createId);
            	comment.setParentCommentId(parentCommentId);
            	
            	boolean isSuccess = reviewboardService.createComment(comment); // 댓글 등록
            	jsonResponse.put("success", isSuccess); // 성공 여부
            	jsonResponse.put("message", isSuccess ? "댓글 생성 성공" : "댓글 생성 실패"); // 응답 메시지
            } else if ("/reviewboard/comment/update.do".equals(path)) {
            	    int commentId = Integer.parseInt(request.getParameter("commentId"));
            	    String content = request.getParameter("content");
            	    String updateId = request.getParameter("updateId");

            	    Comment comment = new Comment();
            	    comment.setContent(content);
            	    comment.setUpdateId(updateId);
            	    comment.setCommentId(commentId);

            	    boolean isSuccess = reviewboardService.updateComment(comment); // 댓글 수정
            	    jsonResponse.put("success", isSuccess); // 성공 여부
            	    jsonResponse.put("message", isSuccess ? "댓글 수정 성공" : "댓글 수정 실패"); // 응답 메시지     	
            } else if ("/reviewboard/comment/delete.do".equals(path)) {
            	    int commentId = Integer.parseInt(request.getParameter("commentId"));
            	    String updateId = request.getParameter("updateId");

            	    Comment comment = new Comment();
            	    comment.setUpdateId(updateId);
            	    comment.setCommentId(commentId);

            	    boolean isSuccess = reviewboardService.deleteComment(comment); // 댓글 삭제
            	    jsonResponse.put("success", isSuccess); // 성공 여부
            	    jsonResponse.put("message", isSuccess ? "댓글 삭제 성공" : "댓글 삭제 실패"); // 응답 메시지
            } else if ("/reviewboard/reviewviewCount.do".equals(path)) {
            	int viewCount = Integer.parseInt(request.getParameter("viewCount"));
            	String boardId = request.getParameter("boardId");
            	viewCount = viewCount + 1;
            	
            	Board board = new Board();
            	board.setBoardId(boardId);
            	board.setViewCount(viewCount);

            	
            	boolean isSuccessView = reviewboardService.viewCount(board);
            	jsonResponse.put("success", isSuccessView);
                jsonResponse.put("message", isSuccessView ?
                		"성공" : "실패");  // 응답 메시지
            }

            	
        }   catch (Exception e) {
            jsonResponse.put("success", false); // 오류 발생 시
            jsonResponse.put("message", "서버 오류 발생"); // 오류 메시지
            logger.error("Error in UserController doPost", e); // 오류 로그 추가
        }
        
        logger.info("jsonResponse.toString() : ", jsonResponse.toString()); 
        // JSON 응답 출력
        out.print(jsonResponse.toString());
        out.flush();
	}

}
