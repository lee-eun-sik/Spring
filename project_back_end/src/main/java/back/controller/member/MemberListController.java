package back.controller.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam; 
import back.model.user.User;
import back.service.member.MemberService;
import back.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/member")
public class MemberListController {

    @Autowired
    private MemberService memberService;

    /**
     * Fetch users with pagination and optional search criteria
     */
    @PostMapping("/list.do")
    public ResponseEntity<ApiResponse> getMemberList(
    		@RequestParam(name = "page", defaultValue = "1") int page,
    	    @RequestParam(name = "searchType", required = false) String searchType,
    	    @RequestParam(name = "searchKeyword", required = false) String searchKeyword,
    	    @RequestParam(name = "startDate", required = false) String startDate,
    	    @RequestParam(name = "endDate", required = false) String endDate,
    	    @RequestParam(name = "sortField", defaultValue = "create_dt") String sortField,
    	    @RequestParam(name = "sortOrder", defaultValue = "desc") String sortOrder
    ) {
        int pageSize = 10;
        List<User> userList;
        int totalCount;

        if (searchType != null && searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            userList = memberService.searchMembersByKeyword(searchType, searchKeyword, page, pageSize, sortField, sortOrder);
            totalCount = memberService.getSearchMemberCount(searchType, searchKeyword);
        } else {
            userList = memberService.getMemberList(page, pageSize, sortField, sortOrder);
            totalCount = memberService.getTotalMemberCount();
        }

        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        Map<String, Object> result = new HashMap<>();
        result.put("list", userList);
        result.put("totalCount", totalCount);
        result.put("totalPages", totalPages);

        return ResponseEntity.ok(new ApiResponse<>(true, "회원 목록 조회 성공", result));
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
