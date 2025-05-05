package back.controller.newboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import back.model.common.PostFile;
import back.model.user.User;
import back.controller.PetSitter.PetSitterController;
import back.model.NewBoard.NewBoard;
import back.service.file.newsboardFileService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
@Slf4j
@RestController
@RequestMapping("/api/newboard")
public class newboardController {

    

    @Autowired
    private newsboardFileService newsboardFileService;

    @PostMapping("/list.do")
    public ResponseEntity<?> getNoticeList(@RequestParam("page") int page) {
        int pageSize = 10;  // Example, you can adjust this as needed
        List<NewBoard> noticeList = newsboardFileService.getNoticeList(page, pageSize);
        int totalCount = newsboardFileService.getNoticeCount();
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        
        return ResponseEntity.ok().body(Map.of(
            "success", true,
            "data", Map.of("list", noticeList, "page", page, "totalPages", totalPages,  "totalCount", totalCount )
        ));
    }

    // API for creating a new notice
    @PostMapping("/notice.do")
    public ResponseEntity<?> createNotice(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            HttpSession session
    ) {
        
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "로그인한 사용자만 게시글을 작성할 수 있습니다."));
        }
        String loginId = loginUser.getUserId();
        NewBoard newBoard = new NewBoard();
        newBoard.setTitle(title);
        newBoard.setContent(content);
        newBoard.setViewCount(0);
        newBoard.setCreateId(loginId);
        newBoard.setUpdateId(loginId);

        if (files != null && !files.isEmpty()) {
            List<PostFile> postFiles = new ArrayList<>();
            for (MultipartFile file : files) {
                PostFile postFile = new PostFile();
                postFile.setFile(file);
                postFiles.add(postFile);
            }
            newBoard.setFiles(postFiles);
        }

        newsboardFileService.createNotice(newBoard);
        return ResponseEntity.ok().body(Map.of("success", true));
    }
    // API for file upload (same as in your provided controller)
    @PostMapping("/imgUpload.do")
    public ResponseEntity<?> uploadImage(
        @RequestPart(value = "file", required = false) MultipartFile file,
        @RequestBody(required = false) PostFile postFileBody
    ) {
        PostFile postFile = new PostFile();

        if (file != null) {
            postFile.setFile(file);
        } else if (postFileBody != null) {
            postFile = postFileBody;
        } else {
            return ResponseEntity.badRequest().body("No valid file data provided.");
        }

        return newsboardFileService.uploadImage(postFile);
    }
}