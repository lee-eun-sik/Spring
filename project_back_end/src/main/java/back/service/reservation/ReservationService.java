package back.service.reservation;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import back.model.board.Comment;
import back.model.petSitter.PetSitter;
import back.model.reservation.Reservation;
import back.model.board.Board;

public interface ReservationService {
	//예약신청
	public boolean createReservation(Reservation reservation, HttpServletRequest request);
	
	//예약목록 ID를 이용해 특정예약찾기
    public Reservation getReservationById(String boardId);
    
    //예약목록을 조회하는 메서드
    public List getReservationList(Reservation reservation);
    
    //펫시터목록을 조회하는 메서드
    public List<PetSitter> getPetSitterList();
    
    //예약수정 메서드
    public boolean updateReservation(Reservation reservation, HttpServletRequest request);
    
    //예약삭제 메서드
    public boolean deleteReservation(Reservation reservation);
    
    public boolean acceptReservation(Reservation reservation);

}