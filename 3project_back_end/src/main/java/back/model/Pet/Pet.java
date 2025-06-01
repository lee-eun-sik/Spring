package back.model.Pet;

import java.time.LocalDate;
import java.time.LocalDateTime;

import back.model.Model;
import lombok.Data;


@Data
public class Pet extends Model {
    private Long animalId;            // ANIMAL_ID
    private String userId;            // USER_ID
    private Long fileId;              // FILE_ID
    
    private String animalName;        // ANIMAL_NAME
    private String animalSpecies;     // ANIMAL_SPECIES
    private LocalDate animalAdoptionDate; // ANIMAL_ADOPTIONDATE
    private LocalDate birthDate;      // BIRTH_DATE
    private String gender;            // GENDER
    private String animalMemo;        // ANIMAL_MEMO

    private LocalDateTime createDt;   // CREATE_DT
    private LocalDateTime updateDt;   // UPDATE_DT
    private String createId;          // CREATE_ID
    private String updateId;          // UPDATE_ID

    private String delYn;             // DEL_YN ('N' 또는 'Y')

	public void setProfileImagePath(String string) {
		// TODO Auto-generated method stub
		
	}
}