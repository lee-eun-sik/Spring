package back.service.reservation;

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
import back.model.petSitter.PetSitter;
import back.model.reservation.Reservation;
import back.util.FileUploadUtil;
import back.util.MybatisUtil;

public class ReservationServiceImpl implements ReservationService {

	@Override
	public boolean createReservation(Reservation reservation, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Reservation getReservationById(String boardId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getReservationList(Reservation reservation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PetSitter> getPetSitterList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateReservation(Reservation reservation, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteReservation(Reservation reservation) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean acceptReservation(Reservation reservation) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
