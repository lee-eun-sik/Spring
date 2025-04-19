package back.controller.board;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import back.model.board.Comment;
import back.model.board.Board;
import back.service.board.FreeBoardService;
import back.service.board.FreeBoardServiceImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * BoardController 클래스
 * 
 * - 게시글과 관련된 요청을 처리하는 서블릿입니다.
 * - 조회, 생성 기능을 포함합니다.
 */
@WebServlet("/freeBoard/*")// 이 서블릿은 "/board/"로 시작하는 모든 URL 요청을 처리합니다.
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,  // 1MB
        maxFileSize = 10 * 1024 * 1024,  // 10MB
        maxRequestSize = 50 * 1024 * 1024 // 50MB
)

public class FreeBoardController extends HttpServlet {


	private static final long serialVersionUID = 1L;


	// 로깅을 위한 Logger 객체 생성
	private static final Logger logger = LogManager.getLogger(FreeBoardController.class); 
	  // BoardService 인터페이스를 구현한 서비스 객체
	private FreeBoardService freeBoardService; 
	  /**
     * 생성자
     * 
     * - BoardService 객체를 초기화합니다.
     */
	public FreeBoardController() {
		super();  // 부모 클래스(HttpServlet)의 생성자 호출
		freeBoardService = new FreeBoardServiceImpl();  // BoardService 구현체 생성 및 할당
	}
	
	private static final int  DEFAULT_PAGE =1;
	private static final int  DEFAULT_SIZE =10;
	
	 /**
     * GET 요청 처리 메서드 (조회용)
     * 
     * @param request  클라이언트로부터 받은 요청 객체
     * @param response 클라이언트로 응답하기 위한 응답 객체
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		logger.info("FreeBoardController doGet");  // 요청이 들어왔음을 로그로 기록
		 // 요청된 URI 경로를 가져옴
		String path = request.getRequestURI();
		
		logger.info("FreeBoardController doGet path" + path); // 요청된 경로를 로그로 기록
		
		// 게시글 조회 요청일 경우
		if ("/freeBoard/view.do".equals(path)) {
			
			String boardId = request.getParameter("id");  // 조회할 게시글의 ID를 요청 파라미터에서 가져옴
			
			logger.info("Board ID received: " + boardId); // boardId 확인용 로그 추가
			 // 서비스 객체를 이용하여 해당 ID의 게시글을 조회
			Board freeBoard = freeBoardService.getBoardById(boardId);
			List<Comment> freeComments = freeBoardService.getCommentList(boardId);
			// 조회한 게시글 객체를 request 객체에 저장하여 JSP로 전달s
			request.setAttribute("freeBoard", freeBoard);
			request.setAttribute("freeComments", freeComments);
			 // JSP 페이지(view.jsp)로 포워딩하여 결과를 출력
			logger.info("freeBoard"+ freeBoard); // 요청된 경로를 로그로 기록
			
			request.getRequestDispatcher("/WEB-INF/jsp/freeBoard/view.jsp").forward(request, response);
			
		} else if ("/freeBoard/create.do".equals(path)) {
			// 게시글 생성 페이지를 요청 시, create.jsp로 포워딩
			request.getRequestDispatcher("/WEB-INF/jsp/freeBoard/create.jsp").forward(request, response);		
			
		} else if ("/freeBoard/update.do".equals(path)) {			  
				String boardId = request.getParameter("id");  // 조회할 게시글의 ID를 요청 파라미터에서 가져옴
				 // 서비스 객체를 이용하여 해당 ID의 게시글을 조회
				Board freeBoard = freeBoardService.getBoardById(boardId);
				logger.info(freeBoard.toString());
				// 조회한 게시글 객체를 request 객체에 저장하여 JSP로 전달
				request.setAttribute("freeBoard", freeBoard);
				 // JSP 페이지(view.jsp)로 포워딩하여 결과를 출력
				request.getRequestDispatcher("/WEB-INF/jsp/freeBoard/update.jsp").forward(request, response);
				
			} else if ("/freeBoard/list.do".equals(path)) {
				String boardId = request.getParameter("id");
				String serachText = request.getParameter("searchText");
				String searchColumn = request.getParameter("searchColumn");
				int page = request.getParameter("page") != null ? 
						Integer.parseInt(request.getParameter("page"))
						: DEFAULT_PAGE;
				int size = request.getParameter("size") != null ? 
						Integer.parseInt(request.getParameter("size"))
						: DEFAULT_SIZE;
				Board freeBoard = new Board();
				
				freeBoard.setSize(size);
				freeBoard.setPage(page);
				freeBoard.setSearchText(serachText);
				freeBoard.setSearchColumn(searchColumn);
				
				
				List boardList = freeBoardService.getBoardList(freeBoard);
				
				request.setAttribute("boardList", boardList);
				request.setAttribute("currentPage", page);
				request.setAttribute("size", size);
				request.setAttribute("totalPage", freeBoard.getTotalPages());
				request.setAttribute("freeBoard", freeBoard);
				request.setAttribute("serachText", serachText);
				request.setAttribute("searchColumn", searchColumn);
				
				request.getRequestDispatcher("/WEB-INF/jsp/freeBoard/list.jsp").forward(request, response);
				
					}
	}

	/**
	 * POST 요청 처리 메서드 (등록용)
	 * 
	 * @param request  클라이언트로부터 받은 요청 객체
	 * @param response 클라이언트로 응답하기 위한 응답 객체
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		logger.info("FreeBoardController doPost");   // 요청이 들어왔음을 로그로 기록
		String path = request.getRequestURI();   // 요청된 URI 경로를 가져옴
		response.setContentType("application/json; charset=UTF-8");  // 응답 타입 설정 (JSON 응답)
		PrintWriter out = response.getWriter();   // PrintWriter 객체 생성하여 응답 준비
		JSONObject jsonResponse = new JSONObject(); // JSON 객체 생성
		try {
			logger.info("FreeBoardController doPost path: " + path);  // 요청된 경로를 로그로 기록
			
			if ("/freeBoard/create.do".equals(path)) {
				// 게시글 생성 요청인 경우
				String title = request.getParameter("title");  // 제목 파라미터 가져오기
				String content = request.getParameter("content");   // 내용 파라미터 가져오기
				int viewCount = Integer.parseInt(request.getParameter("viewCount"));  // 조회수 파라미터 가져오기
				String createId = request.getParameter("createId");   // 생성자 ID 파라미터 가져오기
				
				// Board 객체 생성하여 데이터를 셋팅
				Board freeBoard= new Board();
				freeBoard.setTitle(title);
				freeBoard.setContent(content);
				freeBoard.setViewCount(viewCount);
				freeBoard.setCreateId(createId);
				
				logger.info(freeBoard.toString());
				// 서비스 객체를 이용하여 게시글 생성
				Boolean isCreate = freeBoardService.createBoard(freeBoard,request);
				// JSON 응답에 생성 결과를 담기
				jsonResponse.put("success", isCreate);
				jsonResponse.put("message", isCreate ?
						"게시글이 성공적으로 등록 되었습니다." :"게시글이 등록 실패");
				   // 로그인 처리
			} else  if ("/freeBoard/update.do".equals(path)) {
				// 게시글 생성 요청인 경우
				String boardId = request.getParameter("boardId");  // 제목 파라미터 가져오기
				String title = request.getParameter("title");  // 제목 파라미터 가져오기
				String content = request.getParameter("content");   // 내용 파라미터 가져오기
				int viewCount = Integer.parseInt(request.getParameter("viewCount"));  // 조회수 파라미터 가져오기
				String updateId = request.getParameter("updateId");   // 생성자 ID 파라미터 가져오기
				// Board 객체 생성하여 데이터를 셋팅
				Board freeBoard = new Board();
				freeBoard.setBoardId(boardId);
				freeBoard.setTitle(title);
				freeBoard.setContent(content);
				freeBoard.setViewCount(viewCount);
				freeBoard.setUpdateId(updateId);
				// 서비스 객체를 이용하여 게시글 생성
				Boolean isupdateId = freeBoardService.updateBoard(freeBoard,request);
				// JSON 응답에 생성 결과를 담기
				jsonResponse.put("success", isupdateId);
				jsonResponse.put("message", isupdateId ?
						"게시글이 성공적으로 수정 되었습니다." :"게시글 수정 실패");
				   // 로그인 처리
			} else if ("/freeBoard/delete.do".equals(path)) {
				// 게시글 생성 요청인 경우
				String boardId = request.getParameter("boardId");  // 제목 파라미터 가져오기				
				String updateId = request.getParameter("updateId");   // 생성자 ID 파라미터 가져오기
				// Board 객체 생성하여 데이터를 셋팅
				Board freeBoard = new Board();
				freeBoard.setBoardId(boardId);			
				freeBoard.setUpdateId(updateId);
				// 서비스 객체를 이용하여 게시글 생성
				Boolean isdeleteId = freeBoardService.deleteBoard(freeBoard);
				// JSON 응답에 생성 결과를 담기
				jsonResponse.put("success", isdeleteId);
				jsonResponse.put("message", isdeleteId ?
						"게시글이 성공적으로 삭제 되었습니다." :"게시글 삭제 실패");
				   // 로그인 처리
				
			} else if ("/freeBoard/freeComment/create.do".equals(path)) {
				int boardId = Integer.parseInt(request.getParameter("boardId"));
				String content = request.getParameter("content");  // 댓글 내용(content) 파라미터 가져오기			
				String createId = request.getParameter("createId");   // 생성자 ID 파라미터 가져오기
				int parentCommentId = Integer.parseInt(request.getParameter("parentCommentId"));
				
				Comment freeComment = new Comment();
				freeComment.setBoardId(boardId);			
				freeComment.setContent(content);
				freeComment.setCreateId(createId);
				freeComment.setParentCommentId(parentCommentId);
				
				Boolean isuccess = freeBoardService.createComment(freeComment);
				// JSON 응답에 생성 결과를 담기
				jsonResponse.put("success", isuccess);
				jsonResponse.put("message", isuccess ? "댓글 생성 성공" : "댓글 생성 실패");
				
			}	else  if ("/freeBoard/freeComment/update.do".equals(path)) {
				int commentId = Integer.parseInt(request.getParameter("commentId"));
				String content = request.getParameter("content");  // 댓글 내용(content) 파라미터 가져오기	
				String updateId = request.getParameter("updateId");   // 수정자 ID 파라미터 가져오기
				
				Comment freeComment = new Comment();				
			
				freeComment.setContent(content);
				freeComment.setUpdateId(updateId);
				freeComment.setCommentId(commentId);
				
				Boolean isuccess = freeBoardService.updateComment(freeComment); // 수정 API 호출
				// JSON 응답에 생성 결과를 담기
				jsonResponse.put("success", isuccess);
				jsonResponse.put("message", isuccess ? "댓글 수정 성공" : "댓글 수정 실패");
				
			}	else  if ("/freeBoard/freeComment/delete.do".equals(path)) {
				int commentId = Integer.parseInt(request.getParameter("commentId"));					
				String updateId = request.getParameter("updateId");    // 삭제 요청자 ID 파라미터 가져오기
				
				Comment freeComment = new Comment();					
				freeComment.setUpdateId(updateId);
				freeComment.setCommentId(commentId);
				
				Boolean isuccess = freeBoardService.deleteComment(freeComment); // 삭제 API 호출
				// JSON 응답에 생성 결과를 담기
				jsonResponse.put("success", isuccess);
				jsonResponse.put("message", isuccess ? "댓글 삭제 성공" : "댓글 삭제 실패");
			}	else if ("/freeBoard/freeBoardviewCount.do".equals(path)) {
	        	int viewCount = Integer.parseInt(request.getParameter("viewCount"));
	        	String boardId = request.getParameter("boardId");
	        	viewCount = viewCount + 1;
	        	
	        	Board freeBoard = new Board();
	        	freeBoard.setBoardId(boardId);
	        	freeBoard.setViewCount(viewCount);

	        	
	        	boolean isSuccessView = freeBoardService.viewCount(freeBoard);
	        	jsonResponse.put("success", isSuccessView);
	            jsonResponse.put("message", isSuccessView ?
	            		"성공" : "실패");  // 응답 메시지
	        }			
		} catch (Exception e) {
			  // 예외 발생 시 처리
			jsonResponse.put("success", false); // 오류 발생 시
			jsonResponse.put("message", "서버 오류 발생"); // 오류 메시지
			logger.error("Error in UserController doPost", e); // 오류 로그 추가
		}
		 // 응답 JSON 출력
		logger.info("jsonResponse.toString() : ", jsonResponse.toString()); // 응답 로그 기록
		out.print(jsonResponse.toString()); // JSON 응답 전송
		out.flush();  // 출력 스트림을 플러시
	}

}
