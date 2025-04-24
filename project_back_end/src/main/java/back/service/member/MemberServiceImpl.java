package back.service.member;

import java.util.List;

import org.springframework.stereotype.Service;


import org.springframework.beans.factory.annotation.Autowired;


import back.mapper.member.MemberMapper;  // Corrected import
import back.model.user.User;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;  // Inject the memberMapper

    @Override
    public List<User> getAllMembers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean deleteMember(String userId) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<User> getMembersByPage(int page, int pageSize) {
        // 페이지 번호에 맞게 startRow 계산
        int startRow = (page - 1) * pageSize + 1;
        return memberMapper.selectMembersByPage(startRow, pageSize);  // startRow와 pageSize만 전달
    }

    @Override
    public int getTotalMemberCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<User> searchMembersByKeyword(String searchType, String searchKeyword, int page, int pageSize) {
        // 페이지 번호에 맞게 startRow 계산
        int startRow = (page - 1) * pageSize + 1;
        int endRow = page * pageSize;
        return memberMapper.searchMembersByKeyword(searchType, searchKeyword, startRow, endRow, pageSize);  // 수정된 쿼리에 맞게 인자 전달
    }

    @Override
    public int getSearchMemberCount(String searchType, String searchKeyword) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<User> getMemberList(int page, int pageSize) {
        // Calculate start and end rows for pagination
        int startRow = (page - 1) * pageSize + 1;
        int endRow = page * pageSize;
        return memberMapper.selectMembersByPage(startRow, endRow);  // Corrected method call
    }

    @Override
    public boolean deleteUser(String userId) {
        // TODO Auto-generated method stub
        return false;
    }
}