package back.mapper.board;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import back.model.board.Board;
import back.model.board.Comment;


@Mapper// 마이바티스에서 mapper 리포지토리 대신함. 내부적으로 빈에 붙임, 맴퍼아이디가 동일하게 하기
public interface BoardMapper {
	public List<Board> getBoardList(Board board); // 넘기고 리턴을 받는다. 
	public int getTotalBoardCount(Board board);
	public Board getBoardById(String boardId);
	public int create(Board board);
	public int update(Board board);
	public int delete(Board board);
	public List<Comment> getCommentsByBoardId(String board);
	public int insertComment(Comment comment); // 생성을 수정하든 생성하든 건 수를 리턴받는다. 
	public int updateComment(Comment comment);
	public int deleteComment(Comment comment);
}
