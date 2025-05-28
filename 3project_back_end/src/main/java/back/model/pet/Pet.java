package back.model.pet;

import java.time.LocalDateTime;

import back.model.Model;
import lombok.Data;

@Data
public class Pet extends Model {
	private String name;
    private String species;
    private String adoptionDate;
    private String birthDate;
    private String gender;
    private String notes;
    private String profileImagePath;

    private String createId;
    private String updateId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
