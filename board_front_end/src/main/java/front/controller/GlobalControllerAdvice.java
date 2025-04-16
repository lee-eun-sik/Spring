package front.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Component
@ControllerAdvice
public class GlobalControllerAdvice {

    @Value("${myapp.apiBaseUrl}")
    private String apiBaseUrl;

    /**
     * 모든 컨트롤러에서 호출될 공통적인 설정값을 추가하는 메서드
     */
    @ModelAttribute
    public void addAttributes(Model model) {
        // 모든 모델에 공통적으로 apiBaseUrl 추가
        model.addAttribute("apiBaseUrl", apiBaseUrl);
    }
}
