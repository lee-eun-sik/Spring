package back.controller.board;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import back.exception.HException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import back.model.board.Board;
import back.model.board.Comment;
import back.model.common.CustomUserDetails;
import back.service.board.BoardService;
import back.service.board.BoardServiceImpl;
import back.util.ApiResponse;
import back.util.SecurityUtil;


/**
 * 게시판 관련 요청을 처리하는 REST 컨트롤러
 */
@RestController// 리스폰스 바디도 받겠다. json반환, consumes 받는것 json, produce 보내는 것은 json
@RequestMapping("/api/board")
@Slf4j
public class BoardController {

	@Autowired // 자동 찾아서 넣어줌, 자동 주입, 의존성 주입, 스프링이 해줌. 이것을 생성
	private BoardService boardService;
	/**
	 * 
	 */

	/**
	 * 게시글 목록 조회 (페이징 + 검색 조건)
	 */// 매개변수받아서 넣음
	@PostMapping("/list.do")
	public ResponseEntity<?> getBoardList(@RequestBody Board board) { // json형태 받는다.
		log.info(board.toString());
		List<Board> boardList = boardService.getBoardList(board); //서비스 조회
		Map dataMap = new HashMap();
		dataMap.put("board", board);        // ✅ 검색 조건 등 보낼 데이터
	    dataMap.put("list", boardList);     // ✅ 게시글 목록
		return ResponseEntity.ok(new ApiResponse<>(true, "목록 조회 성공", dataMap)); //리턴했을 때, 성공실패 리턴함 스프링 부트에서 제공
	}
	/**
	 * 게시글 단건 조회
	 */
	@PostMapping("/view.do")
	public ResponseEntity<?> getBoard(@RequestBody Board board) {
		Board selectBoard = boardService.getBoardById(board.getBoardId());
		return ResponseEntity.ok(new ApiResponse<>(true, "조회 성공", selectBoard));
	}
	
	/**
	 *  게시글 등록
	 */
	@PostMapping(value = "/create.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // 파일은 데이터형 지정함 폼형태, 네임명에 따라 객체안에 값을 넣음
	public ResponseEntity<?> createBoard(
			@ModelAttribute Board board,
			@RequestPart(value = "files", required = false) List<MultipartFile> files// 파일 받음 리스트 멀티파트 파일로 받음 파일처리 객체임
	) {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SecurityUtil.checkAuthorization(userDetails);
		board.setCreateId(userDetails.getUsername());
		board.setFiles(files); // 파일을 넣어줌
		boolean isCreated = boardService.createBoard(board); // 처리함. 파일을 다르게 받아야함.
		return ResponseEntity.ok(new ApiResponse<>(isCreated, isCreated ? "게시글 등록 성공" : "게시글 등록 실패", null));
	}
	
	
	/**
     * 게시판 수정
     */
    @PostMapping(value = "/update.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateBoard(
        @ModelAttribute Board board,
        @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        SecurityUtil.checkAuthorization(userDetails);
        board.setUpdateId(userDetails.getUsername());
        board.setFiles(files);
        boolean isUpdated = boardService.updateBoard(board);
        return ResponseEntity.ok(new ApiResponse<>(isUpdated, isUpdated ? "게시글 수정 성공" : "게시글 수정 실패", null));
    }
    

    /**
     * 게시판 삭제
     */
    
    @PostMapping(value = "/delete.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> deleteBoard(
        @ModelAttribute Board board,
        @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        SecurityUtil.checkAuthorization(userDetails);
        board.setFiles(files);
        boolean isDeleted = boardService.deleteBoard(board);
        return ResponseEntity.ok(new ApiResponse<>(isDeleted, isDeleted ? "게시글 수정 성공" : "게시글 수정 실패", null));
    }
    

    /**
     * 댓글 등록
     */
    
    @PostMapping("/comment/create.do")
    public ResponseEntity<?> createComment(@RequestBody Comment comment){
    	CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
    			.getAuthentication().getPrincipal();
    	SecurityUtil.checkAuthorization(userDetails);
    	comment.setCreateId(userDetails.getUsername());
    	boolean isCreated = boardService.createComment(comment);
    	return ResponseEntity.ok(new ApiResponse<>(isCreated, isCreated ? "댓글 등록 성공" : "댓글 등록 실패", null));
    }
    
    /**
     * 댓글 등록
     */

   @PostMapping("/comment/update.do")
   public ResponseEntity<?> updateComment(@RequestBody Comment comment){
	   CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
			   .getAuthentication().getPrincipal();
	   SecurityUtil.checkAuthorization(userDetails);
	   comment.setUpdateId(userDetails.getUsername());
   	boolean isUpdated = boardService.updateComment(comment);
   	return ResponseEntity.ok(new ApiResponse<>(isUpdated, isUpdated ? "댓글 수정 성공" : "댓글 수정 실패", null));
   }
   
   /**
    * 댓글 삭제
    */

  @PostMapping("/comment/delete.do")
  public ResponseEntity<?> deleteComment(@RequestBody Comment comment){
	   CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
			   .getAuthentication().getPrincipal();
	   SecurityUtil.checkAuthorization(userDetails);
	   comment.setUpdateId(userDetails.getUsername());
  	boolean isDeleted = boardService.deleteComment(comment);
  	return ResponseEntity.ok(new ApiResponse<>(isDeleted, isDeleted ? "댓글 삭제 성공" : "댓글 삭제 실패", null));
  }
}
