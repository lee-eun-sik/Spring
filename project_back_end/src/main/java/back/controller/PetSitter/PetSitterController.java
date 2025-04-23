package back.controller.PetSitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import back.model.petSitter.PetSitter;
import back.service.petsitter.PetSitterService;
import back.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/petsitter")
public class PetSitterController {

    @Autowired
    private PetSitterService petSitterService;

    /**
     * 펫시터 전체 목록 조회 (JSON 반환)
     */
    @GetMapping("/list.do")
    public ResponseEntity<?> getPetSitterList() {
        List<PetSitter> petSitterList = petSitterService.getAllSitters();
        if (petSitterList == null || petSitterList.isEmpty()) {
            log.warn("펫시터 목록이 비어 있습니다.");
        }
        log.info("펫시터 목록 조회 - 총 {}건", petSitterList.size());

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("list", petSitterList);
        return ResponseEntity.ok(new ApiResponse<>(true, "펫시터 목록 조회 성공", dataMap));
    }
}