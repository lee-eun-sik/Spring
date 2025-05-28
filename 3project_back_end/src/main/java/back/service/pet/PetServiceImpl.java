package back.service.pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import back.exception.HException;
import back.mapper.pet.PetMapper;
import back.model.pet.Pet;
import back.service.user.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
@Service //스프링이 관리함
@Slf4j
public class PetServiceImpl implements PetService {
	@Autowired
    private PetMapper petMapper;

    @Override
    @Transactional
    public boolean createPet(Pet pet) {
        try {
            return petMapper.insertPet(pet) > 0;
        } catch (Exception e) {
            log.error("반려동물 등록 중 오류", e);
            throw new HException("반려동물 등록 실패", e);
        }
    }
}
