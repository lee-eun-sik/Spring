package front.controller.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {
	@GetMapping("/member/memberlist")
	public String petsitterlist() {
		return "member/Memberlist";
	}
}
