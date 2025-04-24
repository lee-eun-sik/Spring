package front.controller.Reservation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReservationController {
  @GetMapping("reservation/reservationlist")
  public String ReservationListpage() {
	  return "reservation/Reservationlist";
  }
}
