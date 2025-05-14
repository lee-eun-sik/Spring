package back.service.member;

import java.util.List;

import org.springframework.stereotype.Service;

import back.model.user.User;
@Service
public interface MemberService {
	List<User> getMemberList(int page, int pageSize, String sortField, String sortOrder);
    int getSearchMemberCount(String searchType, String searchKeyword);
    int getTotalMemberCount();
    boolean deleteUser(String userId);

    // 아래 메서드들은 불필요하면 삭제
    List<User> getAllMembers();
    boolean deleteMember(String userId);
    List<User> getMembersByPage(int page, int pageSize);
    
	List<User> searchMembersByKeyword(String searchType, String searchKeyword, int page, int pageSize, String sortField,
			String sortOrder, String startDate, String endDate);
}
