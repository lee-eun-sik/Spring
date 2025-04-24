package back.controller.reservation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import back.controller.PetSitter.PetSitterController;
import back.model.reservation.Reservation;
import back.service.reservation.ReservationService;
import back.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    /**
     * 예약 목록 조회
     */
    @PostMapping("/list.do")
    public ResponseEntity<?> getReservationList(@RequestBody Reservation reservation) {
        try {
            List<Reservation> reservationList = reservationService.getReservationList(reservation);
            Map<String, Object> response = new HashMap<>();
            response.put("list", reservationList);
            return ResponseEntity.ok(new ApiResponse<>(true, "예약 목록 조회 성공", response));
        } catch (Exception e) {
            log.error("예약 목록 조회 실패", e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "예약 목록 조회 실패", null));
        }
    }
}
