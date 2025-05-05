package back.service.file;



import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import back.model.NewBoard.NewBoard;
import back.model.board.Board;
import back.model.common.PostFile;
@Service
public interface newsboardFileService {
	List<NewBoard> getNoticeList(int page, int pageSize);
	public ResponseEntity<?> uploadImage(PostFile postFile);
	public void createNotice(NewBoard newBoard);
	List<NewBoard> getNoticeList();
	int getNoticeCount();
}