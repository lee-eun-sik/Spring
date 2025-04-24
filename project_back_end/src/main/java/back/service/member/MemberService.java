package back.service.member;

import java.util.List;

import org.springframework.stereotype.Service;

import back.model.user.User;
@Service
public interface MemberService {
	List<User> getAllMembers();
	boolean deleteMember(String userId);
	
	List<User> getMembersByPage(int page, int pageSize);
	int getTotalMemberCount();
	
	List<User> searchMembersByKeyword(String searchType, String searchKeyword, int page, int pageSize);
	int getSearchMemberCount(String searchType, String searchKeyword);
    boolean deleteUser(String userId);
	List<User> getMemberList(int page, int pageSize);
}
