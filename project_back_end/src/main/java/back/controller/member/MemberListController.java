package back.controller.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam; 
import back.model.user.User;
import back.service.member.MemberService;
import back.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/member")
public class MemberListController {

    @Autowired
    private MemberService memberService;

    /**
     * Fetch users with pagination and optional search criteria
     */
    @GetMapping("/list.do")
    public ResponseEntity<ApiResponse> getMemberList(
    		 @RequestParam(name = "page", defaultValue = "1") int page,
    		    @RequestParam(name = "searchType", required = false) String searchType,
    		    @RequestParam(name = "searchKeyword", required = false) String searchKeyword)  {

        int pageSize = 10;  // Set your page size here
        List<User> userList;

        // If search criteria are provided, perform a search
        if (searchType != null && searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            userList = memberService.searchMembersByKeyword(searchType, searchKeyword, page, pageSize);
        } else {
            userList = memberService.getMemberList(page, pageSize);  // Default: fetch all members
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "회원 목록 조회 성공", userList));
    }

    /**
     * Delete a user
     */
    @PostMapping("/delete.do")
    public ResponseEntity<ApiResponse> deleteUser(@RequestParam String userId) {
        boolean isDeleted = memberService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(isDeleted, isDeleted ? "회원 탈퇴 성공" : "회원 탈퇴 실패", null));
    }
}
