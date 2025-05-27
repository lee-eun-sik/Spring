package back.service.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;



import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import back.model.board.Board;
import back.model.board.Comment;
import back.model.common.PostFile;
import back.model.user.User;
import back.util.FileUploadUtil;


public class newsboardBoardServiceImpl implements newsboardBoardService {

	@Override
	public List getBoardList(Board board) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Board getBoardById(String boardId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createBoard(Board board, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateBoard(Board board, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteBoard(Board board) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean viewCount(Board board) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createComment(Comment comment) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateComment(Comment comment) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteComment(Comment comment) {
		// TODO Auto-generated method stub
		return false;
	}
    


}