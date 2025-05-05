package back.model.board;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import back.model.Model;
import back.model.common.PostFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자 자동 생성 게터 세터 쉽게 구성함. 편의 기능임
@EqualsAndHashCode(callSuper = true)
public class Board extends Model{
	//검색 필터
	private String searchStartDate;
	private String searchEndDate;
	private String startDate;
	private String endDate;
	private String searchText;
	//게시글 정보
	private String boardId;
	private String title;
	private String content;
	private String viewCount;
	private String createId; // 작성자 ID
	private String updateId;
	//페이징
	private int rn;
	private int startRow;
	private int endRow;
	private int page = 1; // 기본 페이지 1
	private int size = 10; // 기본 페이지 크기 10
	private int totalCount;
	private int totalPages;

	private String sortField = "CREATE_DT";
    private String sortOrder = "DESC"; //정렬
	// 연관 데이터
	private List<PostFile> postFiles;
	private List<Comment> comments;
	
	private List<MultipartFile> files;
	private String remainingFileIds;
	
	
}
