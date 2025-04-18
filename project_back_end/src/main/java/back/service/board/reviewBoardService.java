package service.board;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import model.board.Board;
import model.board.Comment;
import model.petSitter.PetSitter;

public interface reviewBoardService {
	
	public List getBoardList(Board board);
	
	public Board getBoardById(String boardId);
	
	public List<Map<String, Object>> selectRatingStats();
	
	public int TotalReviewCount();
	
	 public boolean createBoard(Board board, HttpServletRequest request);
	 
	 public boolean updateBoard(Board board, HttpServletRequest request);
	 
	 public boolean deleteBoard(Board board);
	 
	 public boolean viewCount(Board board);
	 
	 public List<PetSitter> getPetSitterList();
	 
	 public boolean createComment(Comment comment);
	 
	 public boolean updateComment(Comment comment);
	 
	 public boolean deleteComment(Comment comment);
	 
}
