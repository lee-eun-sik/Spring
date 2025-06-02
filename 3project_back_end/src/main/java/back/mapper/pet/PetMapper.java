package back.mapper.pet;

import org.apache.ibatis.annotations.Mapper;

import back.model.pet.Pet;




@Mapper
public interface PetMapper {
	public int insertPet(Pet pet);
}
