package service.pet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.pet.PetPictureDAO;
import model.pet.PetPicture;


public class PetPictureServiceImpl implements PetPictureService {

    private PetPictureDAO petPictureDAO;

    public PetPictureServiceImpl() {
        petPictureDAO = new PetPictureDAO();
    }

    @Override
    public PetPicture getPetPictureById(String petPictureId) {
        return petPictureDAO.getPetPictureById(petPictureId);  // 이름을 수정
    }

    @Override
    public boolean petPictureCreate(PetPicture petPicture) {
        return petPictureDAO.petPictureCreate(petPicture);
    }
    
    @Override
    public List<PetPicture> getPetPictureList(PetPicture search) {
        return petPictureDAO.getPetPictureList(search);
    }

    @Override
    public int getPetPictureCount(PetPicture search) {
        return petPictureDAO.getPetPictureCount(search);
    }


    
    @Override
    public boolean updatePetPicture(PetPicture petPicture) {
        return petPictureDAO.updatePetPicture(petPicture);
    }

    @Override
    public boolean deletePetPicture(String petPictureId) {
        return petPictureDAO.deletePetPicture(petPictureId);  // 이름을 수정
    }
    
    
}