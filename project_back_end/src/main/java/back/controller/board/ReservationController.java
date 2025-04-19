package controller.board;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.petSitter.PetSitter;
import model.reservation.Reservation;
import service.reservation.ReservationService;
import service.reservation.ReservationServiceImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import java.util.Date;

/**
 * ReservationController 클래스
 * - 게시글과 관련된 요청을 처리하는 서블릿입니다.
 * - 조회, 생성 기능을 포함합니다.
 */
@WebServlet("/reservation/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,  // 1MB
        maxFileSize = 10 * 1024 * 1024,  // 10MB
        maxRequestSize = 50 * 1024 * 1024 // 50MB
)
public class ReservationController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(ReservationController.class);
    private ReservationService reservationService;

    public ReservationController() {
        super();
        reservationService = new ReservationServiceImpl();
    }

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("ReservationController doGet");
        String path = request.getRequestURI();
        logger.info("ReservationController doGet path: " + path);

        try {
            if ("/reservation/create.do".equals(path)) {
                List<PetSitter> sitterList = reservationService.getPetSitterList();
                
                logger.info("sitterList from DB: " + sitterList);  // 리스트 전체 확인
                logger.info("sittserList size: " + (sitterList != null ? sitterList.size() : "null"));  // 사이즈 체크
                
                request.setAttribute("sitterList", sitterList);
                request.getRequestDispatcher("/WEB-INF/jsp/reservation/create.jsp").forward(request, response);
                

            } else if ("/reservation/list.do".equals(path)) {
            	String searchText = request.getParameter("searchText");
            	String searchColumn = request.getParameter("searchColumn");
            	
                int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : DEFAULT_PAGE;
                int size = request.getParameter("size") != null ? Integer.parseInt(request.getParameter("size")) : DEFAULT_SIZE;
                Reservation reservation = new Reservation();
                reservation.setSize(size);
                reservation.setPage(page);
                reservation.setSearchText(searchText);
                reservation.setSearchColumn(searchColumn);

                List<Reservation> reservationList = reservationService.getReservationList(reservation);

                request.setAttribute("reservationList", reservationList);
                request.setAttribute("currentPage", page);
                request.setAttribute("size", size);
                request.setAttribute("totalPage", reservation.getTotalPage());
                request.setAttribute("searchText",searchText);
                request.setAttribute("searchColumn",searchColumn);
                request.getRequestDispatcher("/WEB-INF/jsp/reservation/list.jsp").forward(request, response);

            } else if ("/reservation/view.do".equals(path)) {
                String boardId = request.getParameter("id");
                Reservation reservation = reservationService.getReservationById(boardId);
                request.setAttribute("reservation", reservation);
                request.getRequestDispatcher("/WEB-INF/jsp/reservation/view.jsp").forward(request, response);

            } else if ("/reservation/update.do".equals(path)) {
                String boardId = request.getParameter("id");
                logger.info("Received boardId: " + boardId);  // boardId 값이 제대로 들어왔는지 확인
                Reservation reservation = reservationService.getReservationById(boardId);
                logger.info("Fetched reservation: " + reservation.toString());

                // 날짜 포맷 변환
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    // 날짜를 받아와서 포맷 변경
                    Date parsedStartDate = inputFormat.parse(reservation.getStartDate());
                    Date parsedEndDate = inputFormat.parse(reservation.getEndDate());

                    // 변환된 날짜를 JSP에 전달할 포맷으로 변경
                    String formattedStartDate = outputFormat.format(parsedStartDate);
                    String formattedEndDate = outputFormat.format(parsedEndDate);
                    
                    logger.info("Formatted Start Date: " + formattedStartDate);
                    logger.info("Formatted End Date: " + formattedEndDate);

                    // PetSitter 목록과 변환된 날짜를 JSP에 전달
                    List<PetSitter> sitterList = reservationService.getPetSitterList();
                    request.setAttribute("sitterList", sitterList);
                    request.setAttribute("reservation", reservation);
                    request.setAttribute("formattedStartDate", formattedStartDate);
                    request.setAttribute("formattedEndDate", formattedEndDate);

                    // JSP로 포워딩
                    request.getRequestDispatcher("/WEB-INF/jsp/reservation/update.jsp").forward(request, response);

                } catch (ParseException e) {
                    logger.error("날짜 포맷 파싱 오류", e);
                    request.setAttribute("errorMessage", "날짜 형식이 잘못되었습니다.");
                    request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
                }
            }
        } catch (Exception e) {
            logger.error("Error in ReservationController doGet", e);
            request.setAttribute("errorMessage", "서버 오류 발생");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("ReservationController doPost");
        String path = request.getRequestURI();
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        try {
            if ("/reservation/create.do".equals(path)) {
                String startDate = request.getParameter("startDate");
                String endDate = request.getParameter("endDate");
                String reservationDate = request.getParameter("reservationDate");
                String address = request.getParameter("address");
                String variety = request.getParameter("variety");
                String petName = request.getParameter("petName");
                String phoneNumber = request.getParameter("phoneNumber");
                String sitter = request.getParameter("sitter");
                String price = request.getParameter("price");
                String reply = request.getParameter("reply");
                String createId = request.getParameter("createId");
                String addressDetail = request.getParameter("addressDetail");

                logger.info("[CREATE] 요청 데이터 - petName: {}, sitter: {}, startDate: {}, endDate: {}, price: {}",
                        petName, sitter, startDate, endDate, price);

                Reservation reservation = new Reservation();
                reservation.setStartDate(startDate);
                reservation.setEndDate(endDate);
                reservation.setReservationDate(reservationDate);
                reservation.setAddress(address);
                reservation.setVariety(variety);
                reservation.setPetName(petName);
                reservation.setPhoneNumber(phoneNumber);
                reservation.setSitter(sitter);
                reservation.setPrice(Integer.parseInt(price));
                reservation.setReply(reply);
                reservation.setCreateId(createId);
                reservation.setAddressDetail(addressDetail);

                boolean isCreate = reservationService.createReservation(reservation, request);
                logger.info("[CREATE] 예약 생성 결과: {}", isCreate ? "성공" : "실패");
                jsonResponse.put("success", isCreate);
                jsonResponse.put("message", isCreate ? "성공적으로 예약 되었습니다." : "예약 실패");

            } else if ("/reservation/update.do".equals(path)) {
                String boardId = request.getParameter("boardId");
                String startDate = request.getParameter("startDate");
                String endDate = request.getParameter("endDate");
                String reservationDate = request.getParameter("reservationDate");
                String address = request.getParameter("address");
                String variety = request.getParameter("variety");
                String petName = request.getParameter("petName");
                String phoneNumber = request.getParameter("phoneNumber");
                String sitter = request.getParameter("sitter");
                String price = request.getParameter("price");
                String reply = request.getParameter("reply");
                String createId = request.getParameter("createId");
                String addressDetail = request.getParameter("addressDetail");
                String updateId = request.getParameter("updateId");

               
                logger.info("reply: " + reply);

                Reservation reservation = new Reservation();
                reservation.setBoardId(boardId);
                reservation.setStartDate(startDate);
                reservation.setEndDate(endDate);
                reservation.setReservationDate(reservationDate);
                reservation.setAddress(address);
                reservation.setVariety(variety);
                reservation.setPetName(petName);
                reservation.setPhoneNumber(phoneNumber);
                reservation.setSitter(sitter);
                reservation.setPrice(Integer.parseInt(price));
                reservation.setReply(reply);
                reservation.setCreateId(createId);
                reservation.setAddressDetail(addressDetail);
                reservation.setUpdateId(updateId);
                
                logger.info("reply: " + reply);


                boolean isUpdate = reservationService.updateReservation(reservation, request);

                jsonResponse.put("success", isUpdate);
                jsonResponse.put("message", isUpdate ? "게시글이 성공적으로 수정 되었습니다." : "게시글 수정 실패");

            } else if ("/reservation/delete.do".equals(path)) {
                String boardId = request.getParameter("boardId");
                String updateId = request.getParameter("updateId");

                Reservation reservation = new Reservation();
                reservation.setBoardId(boardId);
                reservation.setUpdateId(updateId);

                boolean isDelete = reservationService.deleteReservation(reservation);

                jsonResponse.put("success", isDelete);
                jsonResponse.put("message", isDelete ? "게시글이 성공적으로 삭제 되었습니다." : "게시글 삭제 실패");
            } else if ("/reservation/accept.do".equals(path)) {
                String boardId = request.getParameter("boardId");
                String updateId = request.getParameter("updateId");
                String accept = request.getParameter("accept");
                String reason = request.getParameter("reason");

                Reservation reservation = new Reservation();
                reservation.setBoardId(boardId);
                reservation.setUpdateId(updateId);
                reservation.setAccept(accept);
                reservation.setReason(reason);



                boolean isAccept = reservationService.acceptReservation(reservation);

                jsonResponse.put("success", isAccept);
                jsonResponse.put("message", isAccept ? "예약수락거절이 성공적으로 되었습니다." : "예약수락거절 실패");
            }
        } catch (Exception e) {
            logger.error("Error in ReservationController doPost", e);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "서버 오류 발생");
        }

        out.print(jsonResponse.toString());
        out.flush();
    }
}
