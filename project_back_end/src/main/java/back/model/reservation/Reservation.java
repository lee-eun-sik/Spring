package model.reservation;

import java.util.List;

import model.board.Comment;
import model.Model;
import model.common.PostFile;

public class Reservation extends Model {
	
	
	private String boardId;
	private String startDate;
	private String endDate;
	private String reservationDate;
	private String address;
	private String variety;
	private String addressDetail;
	private String petName;
	private String phoneNumber;
	private String sitter;
	private int price;
	private String reply;
	private String searchText;
	private String searchColumn;
	private String accept;
    private String reason;
	
	
	
	public String getSearchColumn() {
		return searchColumn;
	}



	public void setSearchColumn(String searchColumn) {
		this.searchColumn = searchColumn;
	}



	public String getSearchText() {
		return searchText;
	}

	
	


	public String getAccept() {
		return accept;
	}



	public void setAccept(String accept) {
		this.accept = accept;
	}



	public String getReason() {
		return reason;
	}



	public void setReason(String reason) {
		this.reason = reason;
	}



	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}



	private int rn;
	private int startRow;
	private int endRow;
	
	private int page;
	private int size;
	
	private int totalCount;
	private int totalPage;
	
   private List<PostFile> postFiles;
   
   private List<Comment> comments;
	
	
	@Override
	public String toString() {
	    return "Reservation [boardId=" + boardId + ", reservationDate=" + reservationDate + 
	    	   ", startDate=" + startDate+ ", endDate=" + endDate + 
	           ", address=" + address + ", variety=" + variety + ", petName=" + petName + 
	           ", phoneNumber=" + phoneNumber + ", sitter=" + sitter + ", price=" + price + 
	           
	           ", rn=" + rn + ", startRow=" + startRow + ", endRow=" + endRow +
	           ", page=" + page + ", size=" + size + ", totalCount=" + totalCount +
	            ", totalPage=" + totalPage + ", createId=" + createId + ", updateId=" + updateId +
	            ", createDt=" + createDt + ", updateDt=" + updateDt + 
	           ", reply=" + reply + ", addressDetail=" + addressDetail +"]";
	}
	
	

	public String getBoardId() {
		return boardId;
	}



	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}



	public String getReservationDate() {
		return reservationDate;
	}



	public void setReservationDate(String reservationDate) {
		this.reservationDate = reservationDate;
	}

	

	public String getStartDate() {
	    return startDate == null ? "" : startDate;
	}



	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}



	public String getEndDate() {
	    return endDate == null ? "" : endDate;
	}



	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}
	
	

	public String getVariety() {
		return variety;
	}



	public void setVariety(String variety) {
		this.variety = variety;
	}


	public String getPetName() {
		return petName;
	}



	public void setPetName(String petName) {
		this.petName = petName;
	}



	public String getPhoneNumber() {
		return phoneNumber;
	}



	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}



	public String getSitter() {
		return sitter;
	}



	public void setSitter(String sitter) {
		this.sitter = sitter;
	}



	public int getPrice() {
		return price;
	}



	public void setPrice(int price) {
		this.price = price;
	}



	public String getReply() {
		return reply;
	}



	public void setReply(String reply) {
		this.reply = reply;
	}



	public Reservation() {
		
	}
	
	public String getAddressDetail() {
		return addressDetail;
	}



	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
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



	public int getTotalPage() {
		return totalPage;
	}



	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
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

	
	
	
}

	
	


	
