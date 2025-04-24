package back.model.board;

import java.util.List;
import java.util.Map;

import back.model.common.PostFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import back.model.Model;
import back.model.NewBoard.NewBoard;
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Board extends Model {

	// 별점 통계 (별점 값별 개수)
	private Map<Integer, Integer> ratingStats;
	private int totalReviewCount;
	
	

	private String startDate;
	private String endDate;
	private String userType; //
	private Integer rating = 0;
	
	private String boardId;
	private String title;
	private String content;
	private int viewCount;
	private String createId;
	private String createDt;
	private String sitter;
	
	private String searchText;
    private String searchColumn;
	
	private int rn;
	private int startRow;
	private int endRow;
	
	private int page;
	private int size;
	
	private int totalCount;
	private int totalPages;
	
	private List<PostFile> postFiles;
	private List<Comment> comments;
	
	public String getSitter() {
		return sitter;
	}



	public void setSitter(String sitter) {
		this.sitter = sitter;
	}



	public Map<Integer, Integer> getRatingStats() {
		return ratingStats;
	}
	public void setRatingStats(Map<Integer, Integer> ratingStats) {
		this.ratingStats = ratingStats;
	}
	public int getTotalReviewCount() {
		return totalReviewCount;
	}
	public void setTotalReviewCount(int totalReviewCount) {
		this.totalReviewCount = totalReviewCount;
	}
	public String getSearchText() {
		return searchText;
	}
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public String getBoardId() {
		return boardId;
	}
	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public String getCreateDt() {
		return createDt;
	}
	public void setCreateDt(String createDt) {
		this.createDt = createDt;
	}
	public int getRn() {
		return rn;
	}
	public void setRn(int rn) {
		this.rn = rn;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public int getEndRow() {
		return endRow;
	}
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public List<PostFile> getPostFiles() {
		return postFiles;
	}
	public void setPostFiles(List<PostFile> postFiles) {
		this.postFiles = postFiles;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public String getSearchColumn() {
		return searchColumn;
	}

	public void setSearchColumn(String searchColumn) {
		this.searchColumn = searchColumn;
	}
	
	
	
}	
	
	