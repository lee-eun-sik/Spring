package front.controller.PetSitter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PetSitterController {
	@GetMapping("/petsitter/petsitterlist")
	public String petsitterlist() {
		return "petsitter/PetSitterlist";
	}
}
