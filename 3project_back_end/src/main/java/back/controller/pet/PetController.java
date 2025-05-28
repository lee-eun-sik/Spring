package back.controller.pet;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import back.model.common.CustomUserDetails;
import back.model.pet.Pet;
import back.service.pet.PetService;
import back.util.ApiResponse;
import back.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @PostMapping("/pet.do")
    public ResponseEntity<?> createPet(
        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
        @RequestPart("name") String name,
        @RequestPart("species") String species,
        @RequestPart("adoptionDate") String adoptionDate,
        @RequestPart("birthDate") String birthDate,
        @RequestPart("gender") String gender,
        @RequestPart("notes") String notes,
        HttpSession session
    ) throws IOException {

        CustomUserDetails userDetails = (CustomUserDetails) session.getAttribute("userDetails");

        Pet pet = new Pet();
        pet.setName(name);
        pet.setSpecies(species);
        pet.setAdoptionDate(adoptionDate);
        pet.setBirthDate(birthDate);
        pet.setGender(gender);
        pet.setNotes(notes);
        pet.setCreateId(userDetails != null ? userDetails.getUsername() : "SYSTEM");
        pet.setCreatedAt(LocalDateTime.now());

        if (profileImage != null && !profileImage.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
            String uploadDir = "uploads/images/pets/";
            File dest = new File(uploadDir + fileName);
            dest.getParentFile().mkdirs();
            profileImage.transferTo(dest);
            pet.setProfileImagePath(uploadDir + fileName);
        }

        boolean success = petService.createPet(pet);
        return ResponseEntity.ok(new ApiResponse<>(success, success ? "반려동물 등록 성공" : "등록 실패", pet));
    }
}