package back.mapper.file;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import back.model.common.PostFile;
@Mapper
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
