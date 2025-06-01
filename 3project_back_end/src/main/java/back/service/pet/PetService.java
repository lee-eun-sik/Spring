package back.service.pet;

import org.springframework.web.multipart.MultipartFile;

import back.model.Pet.Pet;

public interface PetService {
    public boolean registerPet(Pet pet, MultipartFile imageFile);
}