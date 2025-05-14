package back.controller.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse> getMemberList(@RequestBody Map<String, Object> params) {
        int page = Integer.parseInt(params.getOrDefault("page", 1).toString());
        int size = Integer.parseInt(params.getOrDefault("size", 10).toString());
        String searchType = (String) params.get("searchType");
        String searchKeyword = (String) params.get("searchKeyword");
        String sortField = (String) params.get("sortField");
        String sortOrder = (String) params.get("sortOrder");
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");

        System.out.println("Received Parameters:");
        System.out.println("page: " + page);
        System.out.println("size: " + size);
        System.out.println("searchType: " + searchType);
        System.out.println("searchKeyword: " + searchKeyword);
        System.out.println("startDate: " + startDate);
        System.out.println("endDate: " + endDate);

        Map<String, Object> result = new HashMap<>();

        List<User> userList = memberService.searchMembersByKeyword(
            searchType, searchKeyword, page, size,
            sortField, sortOrder, startDate, endDate
        );
        int totalCount = memberService.getSearchMemberCount(searchType, searchKeyword);
        int totalPages = (int) Math.ceil((double) totalCount / size);

        result.put("list", userList);
        result.put("totalPages", totalPages);
        result.put("currentPage", page);

        return ResponseEntity.ok(new ApiResponse<>(true, "목록 조회 성공", result));
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
