package back.mapper.member;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import back.model.user.User;
@Mapper
public interface MemberMapper {

	    List<User> selectMembersByPage(
		    @Param("startRow") int startRow, 
		    @Param("endRow") int endRow
		);

		List<User> searchMembersByKeyword(
		    @Param("searchType") String searchType,
		    @Param("searchKeyword") String searchKeyword,
		    @Param("startRow") int startRow,
		    @Param("endRow") int endRow
		);

	    int getSearchMemberCount(
	        @Param("searchType") String searchType, 
	        @Param("searchKeyword") String searchKeyword
	    );

	    int deleteMemberById(@Param("userId") String userId);

	    int selectTotalMemberCount();

	    List<User> searchMembersByKeyword(Map<String, Object> paramMap);

		List<User> selectMembersByPage(Map<String, Object> paramMap);
}
