package back.service.reservation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import back.model.board.Comment;
import back.mapper.reservation.ReservationMapper;
import back.model.board.Board;
import back.model.common.PostFile;
import back.model.petSitter.PetSitter;
import back.model.reservation.Reservation;
import back.util.FileUploadUtil;
import back.util.MybatisUtil;
@Service
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	private final SqlSessionFactory sqlSessionFactory = MybatisUtil.getSqlSessionFactory();

	@Override
    public List<Reservation> getReservationList(Reservation reservation) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ReservationMapper mapper = session.getMapper(ReservationMapper.class);
            return mapper.getReservationList(reservation);
        }
    }

    @Override
    public List<Reservation> getAllReservations() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ReservationMapper mapper = session.getMapper(ReservationMapper.class);
            return mapper.getReservationList(new Reservation()); // 전체 예약 목록 조회
        }
    }

}
