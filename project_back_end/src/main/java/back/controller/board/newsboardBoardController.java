package controller.board;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.board.Board;
import model.board.Comment;
import model.user.User;
import service.board.newsboardBoardService;
import service.board.newsboardBoardServiceImpl;


@WebServlet("/newsboard/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,  // 1MB
        maxFileSize = 10 * 1024 * 1024,  // 10MB
        maxRequestSize = 50 * 1024 * 1024 // 50MB
)
public class newsboardBoardController extends HttpServlet {



private static final long serialVersionUID = -9486181121807237L;
private static final Logger logger = LogManager.getLogger(newsboardBoardController.class); 
private newsboardBoardService newsboardBoardService;

public newsboardBoardController() {
        super();
        newsboardBoardService = new newsboardBoardServiceImpl();
    }

private static final int DEFUALT_PAGE = 1;
private static final int DEFUALT_SIZE = 10;

/**
 * get 화면 이동용 및 조회용
 */
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  logger.info("BoardController doGet"); 
      String path = request.getRequestURI();
      logger.info("BoardController doGet path:" + path); 
      
      if ("/newsboard/newsboardview.do".equals(path)) {
    	  String boardId = request.getParameter("id");
    	 
    	  Board board = newsboardBoardService.getBoardById(boardId);
    	  
    	  request.setAttribute("board", board);
          request.getRequestDispatcher("/WEB-INF/jsp/newsboard/newsboardview.jsp").forward(request, response);
          
          
      } else if ("/newsboard/newsboardcreate.do".equals(path)) {
          request.getRequestDispatcher("/WEB-INF/jsp/newsboard/newsboardcreate.jsp").forward(request, response);
      } else if ("/newsboard/newsboardupdate.do".equals(path)) {
    	  String boardId = request.getParameter("id");
    	  Board board = newsboardBoardService.getBoardById(boardId);
    	  request.setAttribute("board", board);
    	  
          request.getRequestDispatcher("/WEB-INF/jsp/newsboard/newsboardupdate.jsp").forward(request, response);
      } else if ("/newsboard/newsboardlist.do".equals(path)) {
    	  String boardId = request.getParameter("id");
			String serachText = request.getParameter("searchText");
			String searchColumn = request.getParameter("searchColumn");
    	  int page = request.getParameter("page") != null ?
    			  Integer.parseInt(request.getParameter("page"))
    			  : DEFUALT_PAGE;
    	  int size = request.getParameter("size") != null ?
    			  Integer.parseInt(request.getParameter("size"))
    			  : DEFUALT_SIZE;
    	  
    	  Board board = new Board();
    	  board.setPage(page);
    	  board.setSize(size);
    	  board.setSearchText(serachText);
    	  board.setSearchColumn(searchColumn);
    	  
    	  List boardList = newsboardBoardService.getBoardList(board);
    	  
    	  request.setAttribute("boardList", boardList);
    	  request.setAttribute("currentPage", page);
    	  request.setAttribute("totalPages", board.getTotalPages());
    	  request.setAttribute("size", size);
    	  request.setAttribute("board", board);
    	  request.setAttribute("serachText", serachText);
		  request.setAttribute("searchColumn", searchColumn);
    	  
    	  request.getRequestDispatcher("/WEB-INF/jsp/newsboard/newsboardlist.jsp").forward(request, response);
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
        logger.info("BoardController doPost path:" + path);
        
        if ("/newsboard/newsboardcreate.do".equals(path)) { 
        	String title = request.getParameter("title");
        	String content = request.getParameter("content");
        	int viewCount = Integer.parseInt(request.getParameter("viewCount"));
        	String createId = request.getParameter("createId");
            
    		// Board 객체 생성
            Board board = new Board();
            board.setTitle(title);
            board.setContent(content);
            board.setViewCount(viewCount); 
            board.setCreateId(createId);
            
            //사용자 등록 처리
            boolean isCreate = newsboardBoardService.createBoard(board, request);
            jsonResponse.put("success", isCreate);
            jsonResponse.put("success", isCreate ?
            		"게시글이 성공적으로 등록되었습니다." : "게시글이 등록실패"); 
            
        } else if ("/newsboard/newsboardupdate.do".equals(path)) { 
        	String boardId = request.getParameter("boardId");
        	String title = request.getParameter("title");
        	String content = request.getParameter("content");
        	int viewCount = Integer.parseInt(request.getParameter("viewCount"));
        	String updateId = request.getParameter("updateId");
            
            Board board = new Board();
            board.setBoardId(boardId);
            board.setTitle(title);
            board.setContent(content);
            board.setViewCount(viewCount); 
            board.setUpdateId(updateId);
            
            //사용자 등록 처리
            boolean isUpdate = newsboardBoardService.updateBoard(board, request);
            logger.info("isUpdate "+isUpdate);
            jsonResponse.put("success", isUpdate);
            jsonResponse.put("message", isUpdate ?
            		"게시글이 성공적으로 수정되었습니다." : "게시글이 수정실패"); 
        } else if ("/newsboard/newsboarddelete.do".equals(path)) { 
        	String boardId = request.getParameter("boardId");
        	String updateId = request.getParameter("updateId");
            
            Board board = new Board();
            board.setBoardId(boardId);
            board.setUpdateId(updateId);
            
            //사용자 등록 처리
            boolean isDelete = newsboardBoardService.deleteBoard(board);
            jsonResponse.put("success", isDelete);
            jsonResponse.put("message", isDelete ?
            		"게시글이 성공적으로 삭제되었습니다." : "게시글 삭제실패"); 
        } else if ("/newsboard/comment/create.do".equals(path)) {
        	int boardId = Integer.parseInt(request.getParameter("boardId"));
        	String content = request.getParameter("content");
        	String createId = request.getParameter("createId");
        	int parentCommentId = Integer.parseInt(request.getParameter("parentCommentId"));

        	
        	Comment comment = new Comment();
        	comment.setBoardId(boardId);
        	comment.setContent(content);
        	comment.setCreateId(createId);
        	comment.setParentCommentId(parentCommentId);
        	
        	boolean isSuccess = newsboardBoardService.createComment(comment); // 댓글등록
        	jsonResponse.put("success", isSuccess);
            jsonResponse.put("message", isSuccess ?
            		"댓글 생성 성공" : "댓글 생성 실패");  // 응답 메시지
        } else if ("/newsboard/comment/update.do".equals(path)) {
        	int commentId = Integer.parseInt(request.getParameter("commentId"));
        	String content = request.getParameter("content");
        	String updateId = request.getParameter("updateId");
        	
        	Comment comment = new Comment();
        	comment.setCommentId(commentId);
        	comment.setContent(content);
        	comment.setUpdateId(updateId);
        	
        	boolean isSuccess = newsboardBoardService.updateComment(comment); // 댓글수정
        	jsonResponse.put("success", isSuccess);
            jsonResponse.put("message", isSuccess ?
            		"댓글 수정 성공" : "댓글 수정 실패");  // 응답 메시지
        } else if ("/newsboard/comment/delete.do".equals(path)) {
        	int commentId = Integer.parseInt(request.getParameter("commentId"));
        	String updateId = request.getParameter("updateId");
        	
        	Comment comment = new Comment();
        	comment.setCommentId(commentId);
        	comment.setUpdateId(updateId);
        	
        	boolean isSuccess = newsboardBoardService.deleteComment(comment); // 댓글삭제
        	jsonResponse.put("success", isSuccess);
            jsonResponse.put("message", isSuccess ?
            		"댓글 삭제 성공" : "댓글 삭제 실패");  // 응답 메시지
        } else if ("/newsboard/newsboardviewCount.do".equals(path)) {
        	int viewCount = Integer.parseInt(request.getParameter("viewCount"));
        	String boardId = request.getParameter("boardId");
        	viewCount = viewCount + 1;
        	
        	Board board = new Board();
        	board.setBoardId(boardId);
        	board.setViewCount(viewCount);

        	
        	boolean isSuccessView = newsboardBoardService.viewCount(board);
        	jsonResponse.put("success", isSuccessView);
            jsonResponse.put("message", isSuccessView ?
            		"성공" : "실패");  // 응답 메시지
        }
        
        
        
    } catch (Exception e) {
        jsonResponse.put("success", false); // 오류 발생 시
        jsonResponse.put("message", "서버 오류 발생"); // 오류 메시지
        logger.error("Error in BoardController doPost", e); // 오류 로그 추가
    }

    logger.info("jsonResponse.toString() : ", jsonResponse.toString()); 
    // JSON 응답 출력
    out.print(jsonResponse.toString());
    out.flush();
}

}