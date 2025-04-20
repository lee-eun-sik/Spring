package back.model.pet;

public class PetPicture {
	
	private String searchText;
	private int page;
	private int size;
	
	private String boardId;  
    private String petName;      
    private String content;  
    private String imagePath;   
    private String createId;
    private String createDate;    
    
    // 기본 생성자
    public PetPicture() {}
    




	public String getSearchText() {
		return searchText;
	}





	public void setSearchText(String searchText) {
		this.searchText = searchText;
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





	public String getBoardId() {
		return boardId;
	}





	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}





	public String getPetName() {
		return petName;
	}





	public void setPetName(String petName) {
		this.petName = petName;
	}





	public String getContent() {
		return content;
	}





	public void setContent(String content) {
		this.content = content;
	}





	public String getImagePath() {
		return imagePath;
	}





	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}





	public String getCreateId() {
		return createId;
	}





	public void setCreateId(String createId) {
		this.createId = createId;
	}





	public String getCreateDate() {
		return createDate;
	}





	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}





	// toString() 메서드 (디버깅을 위한 출력용)
    @Override
    public String toString() {
        return "PetPicture [petPictureId=" + boardId + ", petName=" + petName + ", content=" + content
                 + ", imagePath=" + imagePath + ", createDate=" + createDate + "]";
    }
	
}
