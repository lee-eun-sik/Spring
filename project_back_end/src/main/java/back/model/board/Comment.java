package model.board;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Model;

public class Comment{
    private int commentId;         // 댓글 고유 ID
    private int boardId;           // 게시글 ID
    private Integer parentCommentId; // 부모 댓글 ID (대댓글)
    private String content;        // 댓글 내용
    private String createId;
    private String updateId;
    private Date createDt;
    private Date updateDt;
    private String delYn;          // 삭제 여부 (Y/N)

    private List<Comment> replies; // 대댓글 리스트

    // 기본 생성자
    public Comment() {
        this.replies = new ArrayList<>();
    }

    // 모든 필드를 포함한 생성자
    public Comment(int commentId, int boardId, Integer parentCommentId, String content,
                   String createId, String updateId) {
        this.commentId = commentId;
        this.boardId = boardId;
        this.parentCommentId = parentCommentId;
        this.content = content;
        this.createId = createId;
        this.updateId = updateId;
        this.replies = new ArrayList<>();
    }

    // Getter 및 Setter
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

   

   
    public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getUpdateId() {
		return updateId;
	}

	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public Date getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

	public String getDelYn() {
        return delYn;
    }

    public void setDelYn(String delYn) {
        this.delYn = delYn;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    @Override
    public String toString() {
        return "Comment [commentId=" + commentId + ", boardId=" + boardId + ", parentCommentId=" + parentCommentId
                + ", content=" + content + ", createId=" + createId + ", updateId=" + updateId + ", createDt="
                + createDt + ", updateDt=" + updateDt + ", delYn=" + delYn + ", replies=" + replies + "]";
    }
}
