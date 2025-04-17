package front.controller.board;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class BoardController {
	/**
	 * 게시판 목록 화면
	 * @return
	 */
	@GetMapping("/board/list.do")
	public String listPage() {
		return "board/list";
	}
	
	/**
	 * 게시글 상세보기 화면
	 */
	
	@GetMapping("/board/view.do") 
	public String viewPage(@RequestParam("id") String boardId, Model model) { //이름을 가진것을 boardId에 넣음, 어노테이션 작업으로 한방에 한다. 
		model.addAttribute("id", boardId); //화면에서 필요하면 id만 넘김
		return "board/view";
	}
	
	/**
	 * 게시글 작성 화면
	 */
	
	@GetMapping("/board/create.do")
	public String createPage() {
		return "board/create";
	}
	
	/**
	 * 게시글 수정 화면	 
	 */
	
	@GetMapping("/board/update.do")
	public String updatePage(@RequestParam("id") String boardId, Model model) {
		model.addAttribute("id", boardId); //화면에서 필요하면 id만 넘김
		return "board/update";
	}
}
