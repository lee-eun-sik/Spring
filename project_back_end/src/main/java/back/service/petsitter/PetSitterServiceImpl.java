package service.petsitter;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import dao.petSitter.PetSitterDAO;
import model.petSitter.PetSitter;
import util.MybatisUtil;

public class PetSitterServiceImpl implements PetSitterService{

    private PetSitterDAO petSitterDAO = new PetSitterDAO();

    @Override
    public List<PetSitter> getAllSitters() {
        return petSitterDAO.getAllPetSitters(); // DAO 메서드 호출
    }
}