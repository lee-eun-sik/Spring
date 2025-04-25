package front.controller.newboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class newboardBoardController {
	@GetMapping("/newboard/newboardlist")
	public String NewsBoardlist() {
		return "newboard/Newsboardlist";
	}
	
	@GetMapping("/newboard/notice")
	public String NewsBoard() {
		return "newboard/NewsBoard";
	}
}
