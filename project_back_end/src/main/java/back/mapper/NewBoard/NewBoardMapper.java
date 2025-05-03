package back.mapper.NewBoard;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import back.model.NewBoard.NewBoard;
import back.model.common.PostFile;

@Mapper
public interface NewBoardMapper {
    List<NewBoard> getAllNotices();
    void insertComment(NewBoard newBoard);
    void insertPostFile(PostFile postFile);
}
