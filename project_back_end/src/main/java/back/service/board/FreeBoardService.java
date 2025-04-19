package service.board;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import model.board.Comment;
import model.board.Board;

public interface FreeBoardService {

    public Board getBoardById(String boardId);
    
    public List getBoardList(Board freeBoard);
    
	public boolean createBoard(Board freeBoard, HttpServletRequest request);
	
	public boolean updateBoard(Board freeBoard, HttpServletRequest request);
	
	public boolean deleteBoard(Board freeBoard); 
	
	public boolean viewCount(Board freeBoard);
	
	public boolean createComment(Comment freeComment);
	
	public boolean updateComment(Comment freeComment);
	
	public boolean deleteComment(Comment freeComment); 
	
	public List<Comment> getCommentList(String boardId);

}