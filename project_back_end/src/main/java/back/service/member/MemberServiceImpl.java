package back.service.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;


import org.springframework.beans.factory.annotation.Autowired;

import back.exception.HException;
import back.mapper.member.MemberMapper;  // Corrected import
import back.model.user.User;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public List<User> getMemberList(int page, int size, String searchType, String searchKeyword) {
        int startRow = (page - 1) * size + 1;
        int endRow = page * size;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("startRow", startRow);
        paramMap.put("endRow", endRow);
        paramMap.put("searchType", searchType);
        paramMap.put("searchKeyword", searchKeyword);
        paramMap.put("sortField", "create_dt");
        paramMap.put("sortOrder", "DESC");
        
        return memberMapper.searchMembersByKeyword(paramMap);
    }
    @Override
    public List<User> searchMembersByKeyword(String searchType, String searchKeyword,
                                             int page, int pageSize,
                                             String sortField, String sortOrder,
                                             String startDate, String endDate) {
        int startRow = (page - 1) * pageSize + 1;
        int endRow = page * pageSize;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("searchType", searchType);
        paramMap.put("searchKeyword", searchKeyword);
        paramMap.put("startRow", startRow);
        paramMap.put("endRow", endRow);
        paramMap.put("sortField", sortField);
        paramMap.put("sortOrder", sortOrder);
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        System.out.println("searchType: " + searchType);
        System.out.println("searchKeyword: " + searchKeyword);
        System.out.println("startDate: " + startDate);
        System.out.println("endDate: " + endDate);
        System.out.println("startRow: " + startRow);
        System.out.println("endRow: " + endRow);
        return memberMapper.searchMembersByKeyword(paramMap);
    }

    @Override
    public int getSearchMemberCount(String searchType, String searchKeyword) {
        return memberMapper.getSearchMemberCount(searchType, searchKeyword);
    }

    @Override
    public int getTotalMemberCount() {
        return memberMapper.selectTotalMemberCount();
    }

    @Override
    public boolean deleteUser(String userId) {
        return memberMapper.deleteMemberById(userId) > 0;
    }

    // 사용되지 않으므로 삭제해도 됨
    @Override
    public List<User> getAllMembers() {
        return null;
    }

    @Override
    public boolean deleteMember(String userId) {
        return false;
    }

    @Override
    public List<User> getMembersByPage(int page, int pageSize) {
        return null;
    }
	
	
}