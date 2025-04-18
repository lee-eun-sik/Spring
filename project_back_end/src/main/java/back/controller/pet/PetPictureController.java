package controller.pet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.pet.PetPicture;
import service.pet.PetPictureService;
import service.pet.PetPictureServiceImpl;

@WebServlet("/pet/*")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,  // 1MB
    maxFileSize = 10 * 1024 * 1024,  // 10MB
    maxRequestSize = 50 * 1024 * 1024 // 50MB
)
public class PetPictureController extends HttpServlet {

    private static final long serialVersionUID = -7176350493203082610L;
    private static final Logger logger = LogManager.getLogger(PetPictureController.class); 
    private PetPictureService petPictureService;

    public PetPictureController() {
        super();
        petPictureService = new PetPictureServiceImpl();
    }

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 8;

    // GET 화면 이동용 및 조회용
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("PetPictureController doGet");
        String path = request.getRequestURI(); 
        logger.info("PetPictureController doGet path: " + path);

        if ("/pet/petPictureView.do".equals(path)) {
            String petPictureId = request.getParameter("id");
            int petId = Integer.parseInt(request.getParameter("petId"));
            
            PetPicture petPicture = petPictureService.getPetPictureById(petPictureId); // 수정된 부분
            request.setAttribute("petPicture", petPicture); // 수정된 부분
            
            request.getRequestDispatcher("/WEB-INF/jsp/pet/petPictureView.jsp").forward(request, response);

        } else if ("/pet/petPictureCreate.do".equals(path)) {
            request.getRequestDispatcher("/WEB-INF/jsp/pet/petPictureCreate.jsp").forward(request, response);

        } else if ("/pet/petPictureUpdate.do".equals(path)) {
        	String petPictureId = request.getParameter("id");
            PetPicture petPicture = petPictureService.getPetPictureById(petPictureId); // 수정된 부분
            request.setAttribute("petPicture", petPicture); // 수정된 부분
            request.getRequestDispatcher("/WEB-INF/jsp/pet/petPictureUpdate.jsp").forward(request, response);

        } else if ("/pet/petPictureList.do".equals(path)) {
        	
        	int page = request.getParameter("page") != null ?
                    Integer.parseInt(request.getParameter("page")) : 1;
            int size = request.getParameter("size") != null ?
                    Integer.parseInt(request.getParameter("size")) : 8;
            String searchText = request.getParameter("searchText");
            
         // 검색 및 페이징 정보를 담을 객체
            PetPicture search = new PetPicture();
            search.setPage(page);
            search.setSize(size);
            search.setSearchText(searchText);
            
         // 총 게시글 수
            int totalCount = petPictureService.getPetPictureCount(search);
            int totalPages = (int) Math.ceil((double) totalCount / size);

            // 게시글 리스트 조회
            List<PetPicture> postList = petPictureService.getPetPictureList(search);

         // 뷰로 전달할 데이터 설정
            request.setAttribute("postList", postList);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("size", size);
            request.setAttribute("board", search); // 검색 조건 다시 넘김


            request.getRequestDispatcher("/WEB-INF/jsp/pet/petPictureList.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("PetPictureController doPost");

        String path = request.getRequestURI(); // 또는 getServletPath()
        
        logger.info("PetPictureController doPost path: " + path);
        
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        try {
            logger.info("PetPictureController doPost path: " + path);

            if ("/pet/petPictureCreate.do".equals(path)) {
                
                String petName = request.getParameter("petName");
                String content = request.getParameter("content");
                String createId = request.getParameter("createId");
                

                Collection<Part> parts = request.getParts();
                String imagePath = null;
                String uploadPath = request.getServletContext().getRealPath("/images");
                System.out.println("이미지 저장 경로: " + uploadPath);

                for (Part part : parts) {
                    if (part.getName().equals("files") && part.getSize() > 0) {
                        imagePath = saveFile(part, uploadPath); // ✅ 경로 전달
                        break;
                    }
                }


                PetPicture petPicture = new PetPicture();
             
                petPicture.setPetName(petName);
                petPicture.setContent(content);
                petPicture.setCreateId(createId);
                petPicture.setImagePath(imagePath);

                boolean isCreate = petPictureService.petPictureCreate(petPicture);
                jsonResponse.put("success", isCreate);
                jsonResponse.put("message", isCreate ? "게시글이 성공적으로 등록되었습니다." : "게시글 등록 실패");
            }

        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "서버 오류 발생");
            logger.error("Error in PetPictureController doPost", e);
        }

        out.print(jsonResponse.toString());
        out.flush();
    }

    private String saveFile(Part part, String uploadPath) throws IOException {
        // 원본 파일명 추출 (예: dog.png)
        String originalFileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();

        // UUID로 고유 파일명 생성 (예: f23c..._dog.png)
        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;

        // 저장 경로 설정
        Path filePath = Paths.get(uploadPath, uniqueFileName);

        // 실제 파일 저장
        try (InputStream input = part.getInputStream()) {
            Files.copy(input, filePath);
        }

        // DB에 저장할 파일명 리턴
        return uniqueFileName;
    }


}
