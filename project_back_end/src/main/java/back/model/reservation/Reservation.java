package back.model.reservation;

import java.sql.Date;
import java.util.List;

import back.model.board.Comment;
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
public class Reservation extends Model {
	
	
	private String createId;
	private Date startDate;
	private Date endDate;
	private int reservationDays; // 기존 reservationDate → 예약 일수
	private String address;
	private String variety;
	private String addressDetail;
	private String petName;
	private String phoneNumber;
	private String sitterName;
	private int price;
	private String reply;
	private int reservationId;            // PK 컬럼 매핑
	private Date reservationDate;         // 예약일
	private String updateId;
	private String searchText;

   public String getUpdateId() {
		return updateId;
	}



	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}



	public int getReservationId() {
		return reservationId;
	}



	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}



	public Date getReservationDate() {
		return reservationDate;
	}



	public void setReservationDate(Date reservationDate) {
		this.reservationDate = reservationDate;
	}



private List<Comment> comments;



	public String getCreateId() {
		return createId;
	}
	
	
	
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	
	
	
	public Date getStartDate() {
		return startDate;
	}
	
	
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	
	
	public Date getEndDate() {
		return endDate;
	}
	
	
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
	
	public int getReservationDays() {
		return reservationDays;
	}
	
	
	
	public void setReservationDays(int reservationDays) {
		this.reservationDays = reservationDays;
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
	
	
	
	public String getAddressDetail() {
		return addressDetail;
	}
	
	
	
	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
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
	
	
	
	public String getSitterName() {
		return sitterName;
	}
	
	
	
	public void setSitterName(String sitterName) {
		this.sitterName = sitterName;
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
	
	
	
	public List<Comment> getComments() {
		return comments;
	}
	
	
	
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}



	public String getSearchText() {
		return searchText;
	}



	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

}
	
	


	
