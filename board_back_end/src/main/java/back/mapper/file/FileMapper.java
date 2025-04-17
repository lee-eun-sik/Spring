package back.mapper.file;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import back.model.common.PostFile;
@Mapper // final로 되있는 모든 것들을 갖다 붙임, 롬복관련해서 어느정도 붙임. Slf4j 로거롤 가져다가 쓴다. log하면 어디선가 날라와서 갖다줌.
public interface FileMapper {
	
	//게시글에 첨부된 파일 저장
	int insertFile(PostFile file);
	
	//파일 ID로 첨부된 파일 조회
	PostFile getFileByFileId(PostFile file);
	
	//게시글 ID로 첨부된 파일 목록 조회
	List<PostFile> getFilesByBoardId(String boardId);
	
	//게시글에 첨부된 파일 삭제 (DEL_YN = 'Y' 처리)
	int deleteFile(PostFile file);
}
