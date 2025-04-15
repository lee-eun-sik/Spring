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
@RestController
@RequestMapping("/api/board")
@Slf4j
public class BoardController {

	@Autowired
	private BoardService boardService;
	/**
	 * 
	 */

	/**
	 * 게시글 목록 조회 (페이징 + 검색 조건)
	 */
	@PostMapping("/list.do")
	public ResponseEntity<?> getBoardList(@RequestBody Board board) {
		log.info(board.toString());
		List<Board> boardList = boardService.getBoardList(board);
		Map dataMap = new HashMap();
		dataMap.put("list", "board");
		return ResponseEntity.ok(new ApiResponse<>(true, "목록 조회 성공", dataMap));
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
	@PostMapping(value = "/create.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createBoard(
			@ModelAttribute Board board,
			@RequestPart(value = "files", required = false) List<MultipartFile> files
	) {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SecurityUtil.checkAuthorization(userDetails);
		board.setCreateId(userDetails.getUsername());
		board.setFiles(files);
		boolean isCreated = boardService.createBoard(board);
		return ResponseEntity.ok(new ApiResponse<>(isCreated, isCreated ? "게시글 등록 성공" : "게시글 등록 실패", null));
	}
	
}
