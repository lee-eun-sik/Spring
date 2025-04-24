package back.model.NewBoard;

import back.model.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NewBoard extends Model{

    private Long newboardId;     // NEWBOARD_ID
    private String title;        // TITLE
    private String content;      // CONTENT
    private Integer viewCount;   // VIEW_COUNT
    private String createId;     // CREATE_ID
    private String updateId;     // UPDATE_ID
    private String delYn;        // DEL_YN ('Y' or 'N')
}
