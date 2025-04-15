package back.service.board;

import java.util.List;

import back.exception.HException;
import jakarta.servlet.http.HttpServletRequest;
import back.model.board.Board;
import back.model.board.Comment;

public interface BoardService {
    public List<Board> getBoardList(Board board);
    
    public Board getBoardById(String boardId);
    
    
    
    public boolean deleteBoard(Board board);
    
    public boolean createComment(Comment comment);
    
    public boolean updateComment(Comment comment);
    
    public boolean deleteComment(Comment comment);

	boolean createBoard(Board board);

	boolean updateBoard(Board board);


    
}
