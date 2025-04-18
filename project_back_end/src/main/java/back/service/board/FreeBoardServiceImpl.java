package service.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.file.FileDAO;
import dao.board.FreeBoardDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import model.board.Comment;
import model.board.Board;
import model.common.PostFile;
import util.FileUploadUtil;
import util.MybatisUtil;

public class FreeBoardServiceImpl implements FreeBoardService {
	// Logger 인스턴스 생성 (로그 기록을 위한 Logger 객체)
	private static final Logger logger = LogManager.getLogger(FreeBoardServiceImpl.class);
	// 데이터베이스 접근 객체 (DAO)
	private FreeBoardDAO freeBoardDAO;
	private FileDAO fileDAO;
	// MyBatis의 SQL 세션 팩토리 (SQL 세션을 생성하기 위한 Factory 객체)
	private SqlSessionFactory sqlSessionFactory;

	/**
	 * BoardServiceImpl 생성자
	 * 
	 * - BoardDAO 객체 초기화 - MyBatis SQL 세션 팩토리 초기화 (MybatisUtil 클래스 이용)
	 */
	public FreeBoardServiceImpl() {
		this.freeBoardDAO = new FreeBoardDAO(); // DAO 객체 생성
		this.fileDAO = new FileDAO();
		try {
			// MyBatis의 SqlSessionFactory 초기화
			sqlSessionFactory = MybatisUtil.getSqlSessionFactory(); // SQL 세션 팩토리 초기화
		} catch (Exception e) {
			// 오류 발생 시 로그 출력
			logger.error("Mybatis 오류", e);
		}
	}

	/**
	 * 특정 게시글을 ID로 조회
	 * 
	 * @param boardId 조회할 게시글의 ID
	 * @return 조회된 게시글 객체 (Board)
	 */
	public Board getBoardById(String boardId) {
		// MyBatis의 SQL 세션 열기
		SqlSession session = sqlSessionFactory.openSession();
		// DAO를 통해 데이터베이스로부터 게시글 조회
		 Board selectBoard = freeBoardDAO.getBoardById(session, boardId);
		//파일목록 조회
		selectBoard.setPostFiles(fileDAO.getFilesByBoardId(session, boardId));
		//댓글 목록 조회
		selectBoard.setComments(freeBoardDAO.getCommentList(session, boardId));
		
		return selectBoard;
		// 조회된 게시글 객체 반환		
	}

	/**
	 * 새로운 게시글을 데이터베이스에 추가 (등록)
	 * 
	 * @param board 추가할 게시글 객체
	 * @return 게시글 등록 성공 여부 (true: 성공, false: 실패)
	 */

	public boolean createBoard(Board freeBoard, HttpServletRequest request) {
		// MyBatis의 SQL 세션 열기
		SqlSession session = sqlSessionFactory.openSession();
		// 결과 값을 저장할 변수 (기본값: false)
		boolean result = false;
		try {
			result = freeBoardDAO.createBoard(session, freeBoard);

			List<Part> fileParts = new ArrayList<>();
			for (Part part : request.getParts()) {
				if ("files".equals(part.getName()) && part.getSize() > 0) {
					fileParts.add(part);
				}
			}
			List<PostFile> fileList = FileUploadUtil.uploadFiles(fileParts, "freeBoard",
					Integer.parseInt(freeBoard.getBoardId()), freeBoard.getCreateId());

			for (PostFile postFile : fileList) {
				fileDAO.insertBoardFile(session, postFile);
			}
			// 트랜잭션 커밋 (정상적으로 데이터가 추가되었음을 확정)
			session.commit(); // 트랜잭션 커밋
		} catch (Exception e) {
			// 예외 발생 시 오류 메시지 출력
			e.printStackTrace();
			// 트랜잭션 롤백 (데이터 추가 작업 취소)
			session.rollback();
		}
		// 등록 결과 반환
		return result;
	}

	/**
	 * 게시글을 업데이트하는 메서드.
	 * 
	 * @param board 업데이트할 게시글 정보를 담고 있는 Board 객체.
	 * @return 업데이트 성공 여부. (true: 성공, false: 실패)
	 */
	public boolean updateBoard(Board freeBoard, HttpServletRequest request) {
		// MyBatis의 SQL 세션 열기
		SqlSession session = sqlSessionFactory.openSession();
		// 결과 값을 저장할 변수 (기본값: false)
		boolean result = false;
		try {
			// DAO를 통해 게시글을 데이터베이스에 추가 (등록)
			result = freeBoardDAO.updateBoard(session, freeBoard);
			if (result) {
				String postFilesParam = request.getParameter("remainingFileIds");
				List<String> postFiles = new ArrayList<String>();
				if (postFilesParam != null && !postFilesParam.trim().isEmpty()) {
					postFiles = Arrays.asList(postFilesParam.split(","));
					}
				List<PostFile> existingFiles = fileDAO.getFilesByBoardId(session, freeBoard.getBoardId());
				if (existingFiles != null && existingFiles.size() > 0) {
					boolean fileExists = false;
					for (PostFile existingFile : existingFiles) {
						fileExists = false;
						for (String fileId : postFiles) {
							if (existingFile.getFileId() == Integer.parseInt(fileId)) {
								fileExists = true;
								break;
							}
						}
						if (!fileExists) {
							existingFile.setUpdateId(freeBoard.getUpdateId());
							boolean deleteSuccess = fileDAO.deleteFile(session, existingFile);
							if (!deleteSuccess) {
								session.rollback();
								return false;
							}
						}
					}
				}

				List<Part> fileParts = new ArrayList<>();
				for (Part part : request.getParts()) {
					if ("files".equals(part.getName()) && part.getSize() > 0) {
						fileParts.add(part);
					}
				}
				List<PostFile> fileList = FileUploadUtil.uploadFiles(fileParts, "freeBoard",
						Integer.parseInt(freeBoard.getBoardId()), freeBoard.getUpdateId());

				for (PostFile postFile : fileList) {
					fileDAO.insertBoardFile(session, postFile);
				}
				// 트랜잭션 커밋 (정상적으로 데이터가 추가되었음을 확정)
			}
			session.commit(); // 트랜잭션 커밋
		} catch (Exception e) {
			// 예외 발생 시 오류 메시지 출력
			e.printStackTrace();
			// 트랜잭션 롤백 (데이터 추가 작업 취소)
			session.rollback();
		}
		// 등록 결과 반환
		return result;
	}

	public boolean deleteBoard(Board freeBoard) {
		// MyBatis의 SQL 세션 열기
		SqlSession session = sqlSessionFactory.openSession();
		// 결과 값을 저장할 변수 (기본값: false)
		boolean result = false;
		try {
			// DAO를 통해 게시글을 데이터베이스에 추가 (등록)
			result = freeBoardDAO.deleteBoard(session, freeBoard);
			// 트랜잭션 커밋 (정상적으로 데이터가 추가되었음을 확정)
			session.commit(); // 트랜잭션 커밋
		} catch (Exception e) {
			// 예외 발생 시 오류 메시지 출력
			e.printStackTrace();
			// 트랜잭션 롤백 (데이터 추가 작업 취소)
			session.rollback();
		}
		// 등록 결과 반환
		return result;
	}

	@Override
	public List getBoardList(Board freeBoard) {

		SqlSession session = sqlSessionFactory.openSession();

		int page = freeBoard.getPage();
		int size = freeBoard.getSize();

		int totalCount = freeBoardDAO.getTotalBoardCount(session, freeBoard);
		int totalPage = (int) Math.ceil((double) totalCount / size);

		int startRow = (page - 1) * size + 1;
		int endRow = page * size;

		freeBoard.setTotalCount(totalCount);
		freeBoard.setTotalPages(totalPage);
		freeBoard.setStartRow(startRow);
		freeBoard.setEndRow(endRow);

		List list = freeBoardDAO.getBoardList(session, freeBoard);

		return list;
	}

	@Override
	public boolean createComment(Comment freeComment) {
		// MyBatis의 SQL 세션 열기
		SqlSession session = sqlSessionFactory.openSession();
		// 결과 값을 저장할 변수 (기본값: false)
		boolean result = false;
		try {
			result = freeBoardDAO.insertComment(session, freeComment);

			session.commit(); // 트랜잭션 커밋
		} catch (Exception e) {
			// 예외 발생 시 오류 메시지 출력
			e.printStackTrace();
			// 트랜잭션 롤백 (데이터 추가 작업 취소)
			session.rollback();
		}
		// 등록 결과 반환
		return result;
	}

	@Override
	public boolean updateComment(Comment freeComment) {
		// MyBatis의 SQL 세션 열기
		SqlSession session = sqlSessionFactory.openSession();
		// 결과 값을 저장할 변수 (기본값: false)
		boolean result = false;
		try {
			result = freeBoardDAO.updateComment(session, freeComment);

			session.commit(); // 트랜잭션 커밋
		} catch (Exception e) {
			// 예외 발생 시 오류 메시지 출력
			e.printStackTrace();
			// 트랜잭션 롤백 (데이터 추가 작업 취소)
			session.rollback();
		}
		// 등록 결과 반환
		return result;
	}

	@Override
	public boolean deleteComment(Comment freeComment) {
		// MyBatis의 SQL 세션 열기
		SqlSession session = sqlSessionFactory.openSession();
		// 결과 값을 저장할 변수 (기본값: false)
		boolean result = false;
		try {
			result = freeBoardDAO.deleteComment(session, freeComment);

			session.commit(); // 트랜잭션 커밋
		} catch (Exception e) {
			// 예외 발생 시 오류 메시지 출력
			e.printStackTrace();
			// 트랜잭션 롤백 (데이터 추가 작업 취소)
			session.rollback();
		}
		// 등록 결과 반환
		return result;
	}

	@Override
	public List<Comment> getCommentList(String boardId) {
		SqlSession session = sqlSessionFactory.openSession();

		List<Comment> list = freeBoardDAO.getCommentList(session, boardId);

		return list;
	}
	
	public boolean viewCount(Board freeBoard) {
		SqlSession session = sqlSessionFactory.openSession();
		boolean result = false;
		try {
			result = freeBoardDAO.viewCount(session, freeBoard);
			session.commit(); // 트랜잭션 커밋
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return result;
	}

}
