
package back.service.pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import back.exception.HException;
import back.mapper.pet.PetMapper;
import back.model.Pet.Pet;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class PetServiceImpl implements PetService {

    @Autowired
    private PetMapper petMapper;

    // 반려동물 등록 처리
    @Override
    @Transactional
    public boolean registerPet(Pet pet, MultipartFile imageFile) {
        try {
            // 파일 업로드 처리
            if (imageFile != null && !imageFile.isEmpty()) {
                String originalFilename = imageFile.getOriginalFilename();
                String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
                String storedFileName = UUID.randomUUID().toString() + extension;

                // 파일 저장 경로 설정
                String uploadDir = "C:/upload/petImages/"; // 서버 경로에 맞게 수정
                File uploadPath = new File(uploadDir);

                if (!uploadPath.exists()) {
                    uploadPath.mkdirs(); // 디렉토리 생성
                }

                File destFile = new File(uploadPath, storedFileName);
                imageFile.transferTo(destFile);

                pet.setProfileImagePath("/petImages/" + storedFileName); // DB 저장용 상대 경로
            }

            return petMapper.insertPet(pet) > 0;

        } catch (IOException e) {
            log.error("이미지 업로드 중 오류", e);
            throw new HException("이미지 업로드 실패", e);
        } catch (Exception e) {
            log.error("반려동물 등록 중 오류", e);
            throw new HException("반려동물 등록 실패", e);
        }
    }

	@Override
	public boolean registerPet(Pet pet) {
		// TODO Auto-generated method stub
		return false;
	}
}

