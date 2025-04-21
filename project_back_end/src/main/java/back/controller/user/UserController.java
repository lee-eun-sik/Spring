package back.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import back.model.user.User;
import back.service.user.UserService;
import back.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Login page
    @GetMapping("/login")
    public String loginPage() {
        return "user/login"; // Thymeleaf template for login page
    }

    // Login check (POST)
    @PostMapping("/login.do")
    public ResponseEntity<?> login(@RequestParam String id, @RequestParam String pass, HttpServletRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(id, pass)
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
            HttpSession session = request.getSession(true);
            session.setAttribute(
                org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
            );

            return ResponseEntity.ok(new ApiResponse<>(true, "로그인 성공", null));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401)
                .body(new ApiResponse<>(false, "아이디 또는 비밀번호가 일치하지 않습니다.", null));
        }
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // Invalidate the session on logout
        request.getSession().invalidate();
        return "redirect:/user/login"; // Redirect to login page after logout
    }
}