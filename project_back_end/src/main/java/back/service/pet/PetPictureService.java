package back.service.pet;

import java.util.List;

import back.model.pet.PetPicture;


public interface PetPictureService {
    
    PetPicture getPetPictureById(String petPictureId);  
    
    boolean petPictureCreate(PetPicture petPicture);  

    List<PetPicture> getPetPictureList(PetPicture search);
    int getPetPictureCount(PetPicture search);
    
    boolean updatePetPicture(PetPicture petPicture);

    boolean deletePetPicture(String petPictureId);
    
    
}