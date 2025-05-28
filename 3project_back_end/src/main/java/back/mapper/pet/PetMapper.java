package back.mapper.pet;

import org.apache.ibatis.annotations.Mapper;

import back.model.pet.Pet;

@Mapper
public interface PetMapper {
	int insertPet(Pet pet);
}
