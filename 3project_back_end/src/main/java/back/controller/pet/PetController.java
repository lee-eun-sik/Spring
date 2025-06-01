package back.controller.pet;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import back.model.Pet.Pet;
import back.model.common.CustomUserDetails;

import back.service.pet.PetService;
import back.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @PostMapping("/animalregister.do")
    public ResponseEntity<?> registerPet(
            @RequestParam(value = "animalName", required = false) String animalName,
            @RequestParam(value = "animalSpecies", required = false) String animalSpecies,
            @RequestParam(value = "animalAdoptionDate", required = false) String animalAdoptionDate,
            @RequestParam(value = "birthDate", required = false) String birthDate,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "animalMemo", required = false) String animalMemo,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            String userId = userDetails.getUser().getUserId();

            Pet pet = new Pet();
            pet.setUserId(userId);
            pet.setAnimalName(animalName);
            pet.setAnimalSpecies(animalSpecies);
            pet.setAnimalAdoptionDate(animalAdoptionDate != null ? LocalDate.parse(animalAdoptionDate) : null);
            pet.setBirthDate(birthDate != null ? LocalDate.parse(birthDate) : null);
            pet.setGender(gender);
            pet.setAnimalMemo(animalMemo);
            pet.setCreateId(userId);
            pet.setUpdateId(userId);
            pet.setDelYn("N");

            log.info("반려동물 등록 요청: {}", pet);

            boolean success = petService.registerPet(pet, imageFile);
            return ResponseEntity.ok(new ApiResponse<>(success, success ? "반려동물 등록 성공" : "반려동물 등록 실패", null));
        } catch (Exception e) {
            log.error("반려동물 등록 중 예외 발생", e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "오류 발생: " + e.getMessage(), null));
        }
    }
}