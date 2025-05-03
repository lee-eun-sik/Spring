package back.model.NewBoard;

import java.util.List;

import back.model.Model;
import back.model.common.PostFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NewBoard extends Model {

    private Long newboardId;
    private String title;
    private String content;
    private Integer viewCount;
    private String createId;
    private String updateId;
    private String delYn;

    // ✅ files 필드 추가
    private List<PostFile> files;

    public List<PostFile> getFiles() {
        return files;
    }

    public void setFiles(List<PostFile> files) {
        this.files = files;
    }
}
