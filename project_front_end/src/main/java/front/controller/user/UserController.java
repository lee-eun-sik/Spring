package front.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
@Controller
public class UserController {
	

    @GetMapping("/user/login")
    public String login() {
        return "user/login"; // templates/user/login.html
    }

    @GetMapping("/user/join")
    public String join() {
        return "user/join"; // templates/user/join.html
    }

    @GetMapping("/user/main")
    public String main() {
        return "user/main"; // templates/user/main.html
    }

    @GetMapping("/user/manager")
    public String adminMain() {
        return "user/manager"; // 관리자용 메인 페이지
    }
	
}
