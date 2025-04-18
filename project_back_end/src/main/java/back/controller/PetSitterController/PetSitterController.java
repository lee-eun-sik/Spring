package controller.PetSitterController;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.petSitter.PetSitter;
import service.petsitter.PetSitterService;
import service.petsitter.PetSitterServiceImpl;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Servlet implementation class PetSitterController
 */
@WebServlet("/petsitter/PetSitterlist.do")
public class PetSitterController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PetSitterService petSitterService = new PetSitterServiceImpl();
   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PetSitterController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 펫시터 리스트 조회
        List<PetSitter> sitterList = petSitterService.getAllSitters();

        // JSP에 데이터 전달
        request.setAttribute("petSitterList", sitterList);

        // JSP로 포워딩
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/petsitter/PetSitterlist.jsp");
        dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
