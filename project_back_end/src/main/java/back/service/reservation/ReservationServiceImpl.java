package service.reservation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import dao.file.FileDAO;
import dao.petSitter.PetSitterDAO;
import dao.reservation.ReservationDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import model.board.Comment;
import model.board.Board;
import model.common.PostFile;
import model.petSitter.PetSitter;
import model.reservation.Reservation;
import util.FileUploadUtil;
import util.MybatisUtil;

public class ReservationServiceImpl implements ReservationService {
	
	// Logger 인스턴스 생성 (로그 기록을 위한 Logger 객체)
	private static final Logger logger = LogManager.getLogger(ReservationServiceImpl.class);
	// 데이터베이스 접근 객체 (DAO)
	private ReservationDAO reservationDAO;
	private FileDAO fileDAO;
	private PetSitterDAO petSitterDAO;
	// MyBatis의 SQL 세션 팩토리 (SQL 세션을 생성하기 위한 Factory 객체)
	private SqlSessionFactory sqlSessionFactory;

	/**
	 * BoardServiceImpl 생성자
	 * 
	 * - BoardDAO 객체 초기화 - MyBatis SQL 세션 팩토리 초기화 (MybatisUtil 클래스 이용)
	 */
	public ReservationServiceImpl() {
		this.reservationDAO = new ReservationDAO(); // DAO 객체 생성
		this.fileDAO = new FileDAO();
		this.petSitterDAO = new PetSitterDAO();
		try {
			// MyBatis의 SqlSessionFactory 초기화
			sqlSessionFactory = MybatisUtil.getSqlSessionFactory(); // SQL 세션 팩토리 초기화
		} catch (Exception e) {
			// 오류 발생 시 로그 출력
			logger.error("Mybatis 오류", e);
		}
	}

	//예약신청 생성---------------------------------------------------------------------------------
	public boolean createReservation(Reservation reservation, HttpServletRequest request) {
		// MyBatis의 SQL 세션 열기
		SqlSession session = sqlSessionFactory.openSession();
		// 결과 값을 저장할 변수 (기본값: false)
		boolean result = false;
		try {
			result = reservationDAO.createReservation(session, reservation);

			List<Part> fileParts = new ArrayList<>();
			for (Part part : request.getParts()) {
				if ("files".equals(part.getName()) && part.getSize() > 0) {
					fileParts.add(part);
				}
			}
			List<PostFile> fileList = FileUploadUtil.uploadFiles(fileParts, "board",
					Integer.parseInt(reservation.getBoardId()), reservation.getCreateId());

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
	
	//예약목록 아이디로 조회---------------------------------------------------------------------------------
	public Reservation getReservationById(String boardId) {
		// MyBatis의 SQL 세션 열기
		SqlSession session = sqlSessionFactory.openSession();
		// DAO를 통해 데이터베이스로부터 게시글 조회
		Reservation selectBoard = reservationDAO.getReservationById(session, boardId);
		//파일목록 조회
		selectBoard.setPostFiles(fileDAO.getFilesByBoardId(session, boardId));
		
		return selectBoard;
		// 조회된 게시글 객체 반환		
	}
	
	//예약목록 조회---------------------------------------------------------------------------------
	public List getReservationList(Reservation reservation) {

		SqlSession session = sqlSessionFactory.openSession();

		int page = reservation.getPage();
		int size = reservation.getSize();

		int totalCount = reservationDAO.getTotalReservationCount(session);
		int totalPage = (int) Math.ceil((double) totalCount / size);

		int startRow = (page - 1) * size + 1;
		int endRow = page * size;

		reservation.setTotalCount(totalCount);
		reservation.setTotalPage(totalPage);
		reservation.setStartRow(startRow);
		reservation.setEndRow(endRow);

		List list = reservationDAO.getReservationList(session, reservation);

		return list;
	}
	
	// 예약 등록 화면에 펫시터 리스트를 보여주는 메서드
	public List<PetSitter> getPetSitterList() {
	    SqlSession session = sqlSessionFactory.openSession();
	    List<PetSitter> sitterList = petSitterDAO.getPetSitterList(session);
	    session.close();
	    return sitterList;
	}
	
	//예약수정 ---------------------------------------------------------------------------------
	public boolean updateReservation(Reservation reservation, HttpServletRequest request) {
		// MyBatis의 SQL 세션 열기
		SqlSession session = sqlSessionFactory.openSession();
		// 결과 값을 저장할 변수 (기본값: false)
		boolean result = false;
		try {
			// DAO를 통해 게시글을 데이터베이스에 추가 (등록)
			result = reservationDAO.updateReservation(session, reservation);
			if (result) {
				String postFilesParam = request.getParameter("remainingFileIds");
				List<String> postFiles = new ArrayList<String>();
				if (postFilesParam != null && !postFilesParam.trim().isEmpty()) {
					postFiles = Arrays.asList(postFilesParam.split(","));
					}
				List<PostFile> existingFiles = fileDAO.getFilesByBoardId(session, reservation.getBoardId());
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
							existingFile.setUpdateId(reservation.getUpdateId());
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
				List<PostFile> fileList = FileUploadUtil.uploadFiles(fileParts, "board",
						Integer.parseInt(reservation.getBoardId()), reservation.getUpdateId());

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
	
	//예약삭제 ---------------------------------------------------------------------------------
	public boolean deleteReservation(Reservation reservation) {
		// MyBatis의 SQL 세션 열기
		SqlSession session = sqlSessionFactory.openSession();
		// 결과 값을 저장할 변수 (기본값: false)
		boolean result = false;
		try {
			// DAO를 통해 게시글을 데이터베이스에 추가 (등록)
			result = reservationDAO.deleteReservation(session, reservation);
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
	
	
	public boolean acceptReservation(Reservation reservation) {
        // MyBatis의 SQL 세션 열기
        SqlSession session = sqlSessionFactory.openSession();
        // 결과 값을 저장할 변수 (기본값: false)
        boolean result = false;
        try {
            // DAO를 통해 게시글을 데이터베이스에 추가 (등록)
            result = reservationDAO.acceptReservation(session, reservation);
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

}
