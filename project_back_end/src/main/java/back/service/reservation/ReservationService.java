package back.service.reservation;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import back.model.board.Comment;
import back.model.petSitter.PetSitter;
import back.model.reservation.Reservation;
import back.model.board.Board;
@Service
public interface ReservationService {
	
	
	
    
    //예약목록을 조회하는 메서드
	public List<Reservation> getReservationList(Reservation reservation);
    
    

	public List<Reservation> getAllReservations();

}