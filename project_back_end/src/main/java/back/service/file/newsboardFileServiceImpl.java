package back.service.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import back.model.common.PostFile;
import back.util.FileUploadUtil;
import back.util.MybatisUtil;

public class newsboardFileServiceImpl implements newsboardFileService {

	
	@Override
	public HashMap insertBoardFiles(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PostFile getFileByFileId(PostFile file) {
		// TODO Auto-generated method stub
		return null;
	}
    


}