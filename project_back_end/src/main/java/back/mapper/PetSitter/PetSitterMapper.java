package back.mapper.PetSitter;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import back.model.petSitter.PetSitter;
@Mapper
public interface PetSitterMapper {
	List<PetSitter> getPetSitterList();
}
