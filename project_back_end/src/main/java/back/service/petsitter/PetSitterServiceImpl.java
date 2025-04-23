package back.service.petsitter;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import back.mapper.PetSitter.PetSitterMapper;
import back.model.petSitter.PetSitter;
import back.service.user.UserServiceImpl;
import back.util.MybatisUtil;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class PetSitterServiceImpl implements PetSitterService{


	@Override
	public List<PetSitter> getAllSitters() {
	    try (SqlSession session = MybatisUtil.getSqlSessionFactory().openSession()) {
	        PetSitterMapper mapper = session.getMapper(PetSitterMapper.class);
	        List<PetSitter> list = mapper.getPetSitterList();

	        // 리스트가 비어 있는지 확인
	        if (list == null || list.isEmpty()) {
	            log.info("펫시터 목록이 없습니다.");
	        } else {
	            log.info("펫시터 목록 개수: {}", list.size());
	        }

	        return list;
	    } catch (Exception e) {
	        log.error("펫시터 목록 조회 중 오류 발생", e);
	        throw new RuntimeException("펫시터 목록 조회 실패", e);
	    }
	}
}