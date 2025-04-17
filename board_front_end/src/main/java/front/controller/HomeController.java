package front.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // 등록하기 객체 생성, 어노테이션을 달아주기
public class HomeController {
	
	@GetMapping({"/", "/main.do"}) // get방식
    public String main(Model model) {
  
        model.addAttribute("message", "Hello, Thymeleaf!");
        return "index"; 
	}
}
