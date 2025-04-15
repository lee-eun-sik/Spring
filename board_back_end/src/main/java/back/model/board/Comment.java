package back.model.board;



	
	import back.model.Model;

	public class Comment extends Model{
	    private int commentId;         // 댓글 고유 ID
	    private int boardId;           // 게시글 ID
	    private Integer parentCommentId; // 부모 댓글 ID (대댓글)
	    private String content;        // 댓글 내용
	    private String delYn;          // 삭제 여부 (Y/N)
	    

		public int getCommentId() {
			return commentId;
		}

		public void setCommentId(int commentId) {
			this.commentId = commentId;
		}

		public int getBoardId() {
			return boardId;
		}

		public void setBoardId(int boardId) {
			this.boardId = boardId;
		}

		public Integer getParentCommentId() {
			return parentCommentId;
		}

		public void setParentCommentId(Integer parentCommentId) {
			this.parentCommentId = parentCommentId;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getDelYn() {
			return delYn;
		}

		public void setDelYn(String delYn) {
			this.delYn = delYn;
		}



	    
	}

