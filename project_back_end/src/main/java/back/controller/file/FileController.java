package controller.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;

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
import model.common.PostFile;
import service.file.FileService;
import service.file.FileServiceImpl;


@WebServlet("/file/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,  // 1MB
        maxFileSize = 10 * 1024 * 1024,  // 10MB
        maxRequestSize = 50 * 1024 * 1024 // 50MB
)
public class FileController extends HttpServlet {



	private static final long serialVersionUID = -9486181121807237L;
	private static final Logger logger = LogManager.getLogger(FileController.class); 
	private FileService FileService;
	
	public FileController() {
	        super();
	        FileService = new FileServiceImpl();
	    }
	
	/**
	 * get 화면 이동용 및 조회용
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  logger.info("FileController doGet"); 
	      String path = request.getRequestURI();
	      logger.info("FileController doGet path:" + path); 
	      
	      if ("/file/down.do".equals(path)) {
	    	  int fileId = Integer.parseInt(request.getParameter("fileId"));
	    	  PostFile file= new PostFile();
	    	  file.setFileId(fileId);
	    	  
	    	  PostFile selectFile = FileService.getFileByFileId(file);
	    	  
	    	  //파일 경로 및 파일 이름 설정
	    	  String filePath = selectFile.getFilePath();
	    	  File downloadFile = new File(filePath);
	    	  
	    	  if (!downloadFile.exists()) {
	    		  response.getWriter().write("파일이 존재하지 않습니다.");
	    		  return;
	    	  }
	    	  
	    	  //파일 이름 추출
	    	  String fileName = selectFile.getFileName();
	    	  
	    	  //응답 헤더 설정
	    	  response.setContentType("application/octet-stream");
	    	  response.setContentLength((int) downloadFile.length());
	    	  
	    	  //파일 다운로드 헤더 설정
	    	  String downladHeader = "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8");
	    	  response.setHeader("Content-Disposition", downladHeader);
	    	  
	    	  //파일을 읽어서 응답 스트림으로 전송
	    	  try (FileInputStream fileInputStream = new FileInputStream(downloadFile);
	    	      OutputStream out = response.getOutputStream()) {
	    		  
	    		  byte[] buffer = new byte[4096];
	    		  int bytesRead;
	    		  
	    		  while ((bytesRead = fileInputStream.read(buffer)) != -1) {
	    			  out.write(buffer, 0, bytesRead);
	    		  }
	    	  } catch (IOException e) {
	    		  response.getWriter().write("파일 다운로드 중 오류가 발생했습니다");
	    	  }
	      } else if ("/file/imgDown.do".equals(path)) {
	    	  int fileId = Integer.parseInt(request.getParameter("fileId"));
	    	  PostFile file= new PostFile();
	    	  file.setFileId(fileId);
	    	  
	    	  PostFile selectFile = FileService.getFileByFileId(file);
	    	  
	    	  //파일 경로 및 파일 이름 설정
	    	  String filePath = selectFile.getFilePath();
	    	  File downloadFile = new File(filePath);
	    	  
	    	  if (!downloadFile.exists()) {
	    		  response.getWriter().write("파일이 존재하지 않습니다.");
	    		  return;
	    	  }
	    	  
	    	  //파일 이름 추출
	    	  String fileName = selectFile.getFileName();
	    	  
	    	  
	    	  //MIME 타입을 파일 확장자에 맞게 설정
	    	  String mimeType = getServletContext().getMimeType(filePath);
	    	  if (mimeType == null) {
	                mimeType = "application/octet-stream";  // 기본 타입 설정
	          
	    	  }
	    	  //응답 헤더 설정
	    	  response.setContentType(mimeType);
	    	  response.setContentLength((int) downloadFile.length());
	    	  
	    	  //파일 다운로드 헤더 설정
	    	  String downladHeader = "inline; filename=" + URLEncoder.encode(fileName, "UTF-8");
	    	  response.setHeader("Content-Disposition", downladHeader);
	    	  
	    	  //파일을 읽어서 응답 스트림으로 전송
	    	  try (FileInputStream fileInputStream = new FileInputStream(downloadFile);
	    	      OutputStream out = response.getOutputStream()) {
	    		  
	    		  byte[] buffer = new byte[4096];
	    		  int bytesRead;
	    		  
	    		  while ((bytesRead = fileInputStream.read(buffer)) != -1) {
	    			  out.write(buffer, 0, bytesRead);
	    		  }
	    	  } catch (IOException e) {
	    		  response.getWriter().write("파일 다운로드 중 오류가 발생했습니다");
	    	  }
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
	        logger.info("FileController doPost path:" + path);
	        
	        if ("/file/imgUpload.do".equals(path)) { 
	        	HashMap resultMap = FileService.insertBoardFiles(request);
	        	
	            boolean isUploadFile = (boolean) resultMap.get("result");
	            
	            jsonResponse.put("success", isUploadFile);
	            jsonResponse.put("message", isUploadFile ?
	            		"파일이 성공적으로 업로드되었습니다." : "파일 업로드 실패");
	            if(isUploadFile) {
	            	jsonResponse.put("url", "/file/imgDown.do?fileId="+ resultMap.get("fileId"));
	            }
	        }
	        
	    } catch (Exception e) {
	        jsonResponse.put("success", false); // 오류 발생 시
	        jsonResponse.put("message", "서버 오류 발생"); // 오류 메시지
	        logger.error("Error in FileController doPost", e); // 오류 로그 추가
	    }
	    
	    logger.info("jsonResponse.toString() : ", jsonResponse.toString()); 
	    // JSON 응답 출력
	    out.print(jsonResponse.toString());
	    out.flush();
	
	}
}