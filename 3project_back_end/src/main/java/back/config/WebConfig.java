package back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //api오류 promise, core에러 처리
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) { // 뭔가 디폴트한 값이 여기에 들어감
        registry.addMapping("/api/**") // 또는 "/**"주소를 갖고있는것이 오면, 프론트단 주소인지 확인
                .allowedOrigins("http://localhost:8081", "http://localhost:3000", "http://192.168.0.93:3000","http://192.168.0.23:3000") // 프론트 주소, 주소로 오는지 확인
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 통신 방식 열어줌, 쿠키 조회
                .allowedHeaders("*")
                .allowCredentials(true); // 쿠키 허용!
    }
}
