package back.mapper.reservation;

import java.util.List;

import back.model.reservation.Reservation;

public interface ReservationMapper {
	List<Reservation> getReservationList(Reservation reservation);
}
