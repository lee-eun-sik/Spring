package front.controller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	private static final Logger logger = LogManager.getLogger(HomeController.class);
	@GetMapping({"/"})
    public String main(Model model) {
    	logger.info("홈 페이지 요청");
        model.addAttribute("message", "Hello, Thymeleaf!");
        return "index"; 
	}
}
