package back.mapper.member;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import back.model.user.User;
@Mapper
public interface MemberMapper {

	// 회원 목록 조회 (페이지네이션)
    List<User> selectMembersByPage(@Param("startRow") int startRow, @Param("pageSize") int pageSize);
    
    // 검색된 회원 목록 조회
    List<User> searchMembersByKeyword(
        @Param("searchType") String searchType, 
        @Param("searchKeyword") String searchKeyword, 
        @Param("startRow") int startRow, 
        @Param("endRow") int endRow,
        @Param("pageSize") int pageSize
    );
}
