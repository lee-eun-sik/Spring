package back.service.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;



import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import back.model.board.Comment;
import back.model.board.Board;
import back.model.common.PostFile;
import back.util.FileUploadUtil;
import back.util.MybatisUtil;

public class FreeBoardServiceImpl implements FreeBoardService {

	@Override
	public Board getBoardById(String boardId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getBoardList(Board freeBoard) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createBoard(Board freeBoard, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateBoard(Board freeBoard, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteBoard(Board freeBoard) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean viewCount(Board freeBoard) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createComment(Comment freeComment) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateComment(Comment freeComment) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteComment(Comment freeComment) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Comment> getCommentList(String boardId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
