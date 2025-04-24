package front.controller.newboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class newboardBoardController {
	@GetMapping("/newboard/newboardlist")
	public String petsitterlist() {
		return "newboard/Newboardlist";
	}
}
