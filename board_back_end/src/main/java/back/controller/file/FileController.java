package back.controller.file;

package controller.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.board.Board;
import model.common.PostFile;
import service.board.BoardService;
import service.board.BoardServiceImpl;
import service.file.FileService;
import service.file.FileServiceImpl;


@WebServlet("/file/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,  // 1MB
        maxFileSize = 10 * 1024 * 1024,  // 10MB
        maxRequestSize = 50 * 1024 * 1024 // 50MB
)
public class FileController extends HttpServlet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4865470127006529201L;
	private static final Logger logger = LogManager.getLogger(FileController.class); 
	private FileService fileService;
	
	
	public FileController() {
        super();
        fileService = new FileServiceImpl(); 
    }
	/**
	 * GET 화면 이동용 및 조회용
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  logger.info("FileController doGet"); 
	      String path = request.getRequestURI();
	      logger.info("FileController doGet path" + path); 
	      
	      if ("/file/down.do".equals(path)) {
	    	    int fileId = Integer.parseInt(request.getParameter("fileId"));
	    	    PostFile file = new PostFile();
	    	    file.setFileId(fileId);
	    	    
	    	    PostFile selectFile = fileService.getFileByBoardIdAndFileId(file);
	    	    
	    	    if (selectFile == null) {
	    	    	response.getWriter().write("파일을 찾을 수 없습니다.");
	    	    	return;
	    	    }
	    	    
	    	    // 파일 경로 및 파일 이름 설정
	    	    String filePath = selectFile.getFilePath();	// 데이터베이스에서 얻은 파일 경로
	    	    File downloadFile = new File(filePath);
	    	    
	    	    if(!downloadFile.exists()) {
	    	    	response.getWriter().write("파일이 존재하지 않습니다.");
	    	    	return;
	    	    }
	    	    
	    	    // 파일 이름 추출
	    	    String fileName = selectFile.getFileName();
	    	    
	    	    // 현재 우리가 파일을 보낼때 셋팅시키고 애픞리케이션에서 스트림을 보낼것임.
	    	    // 응답 헤더 설정
	    	    response.setContentType("application/octet-stream");
	    	    response.setContentLength((int) downloadFile.length());
	    	    
	    	    // 파일 다운로드 헤더 설정
	    	    String downloadHeader = "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8");
	    	    response.setHeader("Content-Disposition", downloadHeader);
	    	    
	    	    // 파일을 읽어서 응답 스트림으로 전송
	    	    try {
	    	    	FileInputStream fis = new FileInputStream(downloadFile);
	    	    	OutputStream out = response.getOutputStream();
	    	    	
	    	    	byte[] buffer = new byte[4096];
	    	    	int bytesRead;
	    	    	
	    	    	while ((bytesRead = fis.read(buffer)) != -1) { // 값을 넣어줌
	    	    		out.write(buffer, 0, bytesRead);
	    	    	}
	    	    } catch (IOException e) {
	    	    	response.getWriter().write("파일 다운로드 중 오류가 발생했습니다.");
	    	    }
	    	    // 
//	            request.getRequestDispatcher("/WEB-INF/jsp/board/view.jsp").forward(request, response);
	      } else if ("/file/imgDown.do".equals(path)) {
	    	    int fileId = Integer.parseInt(request.getParameter("fileId"));
	    	    PostFile file = new PostFile();
	    	    file.setFileId(fileId);
	    	    
	    	    PostFile selectFile = fileService.getFileByBoardIdAndFileId(file);
	    	    
	    	    if (selectFile == null) {
	    	    	response.getWriter().write("파일을 찾을 수 없습니다.");
	    	    	return;
	    	    }
	    	    
	    	    // 파일 경로 및 파일 이름 설정
	    	    String filePath = selectFile.getFilePath();	// 데이터베이스에서 얻은 파일 경로
	    	    File downloadFile = new File(filePath);
	    	    
	    	    if(!downloadFile.exists()) {
	    	    	response.getWriter().write("파일이 존재하지 않습니다.");
	    	    	return;
	    	    }
	    	    
	    	    // 파일 이름 추출
	    	    String fileName = selectFile.getFileName();
	    	    
	    	    
	    	    // MIME 타입을 파일 확장자에 맞게 설정
	    	    String mimeType = getServletContext().getMimeType(filePath); // 안에다가 파일패스적으면 MINE타입을 읽음
	            if (mimeType == null) {
	                mimeType = "application/octet-stream";  // 기본 타입 설정, 갖고와서 넣어줌
	            }
	            
	    	    // 현재 우리가 파일을 보낼때 셋팅시키고 애픞리케이션에서 스트림을 보낼것임.
	    	    // 응답 헤더 설정
	    	    response.setContentType(mimeType);
	    	    response.setContentLength((int) downloadFile.length()); //바이트 크기 리턴
	    	    
	    	    // 파일 다운로드 헤더 설정
	    	    String downloadHeader = "inline; filename=" + URLEncoder.encode(fileName, "UTF-8"); //원본파일 인코딩해서 넣어줌
	    	    response.setHeader("Content-Disposition", downloadHeader);
	    	    
	    	    // 파일을 읽어서 응답 스트림으로 전송
	    	    try {
	    	    	FileInputStream fis = new FileInputStream(downloadFile); //파일읽음
	    	    	OutputStream out = response.getOutputStream(); //응답한곳에 파일을 넘김
	    	    	
	    	    	byte[] buffer = new byte[4096]; //조각조각해서 읽기
	    	    	int bytesRead;
	    	    	
	    	    	while ((bytesRead = fis.read(buffer)) != -1) { // 값을 넣어줌, 위치를 잡아줌. 4씩증가...4,8,12 다 읽을 때까지 반복
	    	    		out.write(buffer, 0, bytesRead); //끝까지 다할 때까지 반복, 읽을때 버퍼가 필요함
	    	    	}
	    	    } catch (IOException e) {
	    	    	response.getWriter().write("파일 다운로드 중 오류가 발생했습니다.");
	    	    }
	    	    // 
//	            request.getRequestDispatcher("/WEB-INF/jsp/board/view.jsp").forward(request, response);
	      }
	} 
	

	/**
	 * POST ajax 로직 처리용 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("FileController doPost");
        String path = request.getRequestURI();
        response.setContentType("application/json; charset=UTF-8"); // 응답 타입 설정
        PrintWriter out = response.getWriter(); // PrintWriter 객체 생성
        JSONObject jsonResponse = new JSONObject(); // JSON 응답 객체 생성
        try {
            logger.info("FileController doPost path: " + path);
            //유저 가입시 처리
            if ("/file/imgUpload.do".equals(path)) { 
            	
            	HashMap resultMap = fileService.insertBoardFiles(request);
            	
            	boolean isUploadFile = (boolean) resultMap.get("result");
            	jsonResponse.put("success", isUploadFile);
            	jsonResponse.put("message", isUploadFile ? 
            			"파일 업로드 성공되었습니다." : "파일 업로드 실패");
            	if(isUploadFile) {
            		jsonResponse.put("url", "/file/imgDown.do?fileId="
            							+ resultMap.get("fileId"));//주소넘기면 이미지 다운받아서 넘겨준다.
            	}
            } 
        } catch (Exception e) {
            jsonResponse.put("success", false); // 오류 발생 시
            jsonResponse.put("message", "서버 오류 발생"); // 오류 메시지
            logger.error("Error in FileControllers doPost", e); // 오류 로그 추가
        }
        
        logger.info("jsonResponse.toString() : ", jsonResponse.toString()); 
        // JSON 응답 출력
        out.print(jsonResponse.toString());
        out.flush();
	}

}