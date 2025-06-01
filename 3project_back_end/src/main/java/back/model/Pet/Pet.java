package back.model.Pet;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Pet {
    private Long animalId;
    private String userId;
    private Long fileId;
    private String animalName;
    private String animalSpecies;
    private LocalDate animalAdoptionDate;
    private LocalDate birthDate;
    private String gender;
    private String animalMemo;
    private LocalDate createDt;
    private LocalDate updateDt;
    private String createId;
    private String updateId;
    private String delYn;
    private String imagePath;
	public void setImagePath(String string) {
		// TODO Auto-generated method stub
		
	}
}
