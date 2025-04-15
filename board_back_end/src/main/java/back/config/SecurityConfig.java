package back.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.fasterxml.jackson.databind.ObjectMapper;

import back.service.common.CustomUserDetailsService;
import back.util.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 🔐 Spring Security 설정 클래스
 * 
 * ✔ 세션 기반 인증 방식 사용 (프론트엔드 분리 구조 대응)
 * ✔ JSON 기반 API 요청 처리 (폼 로그인 X) 폼 통신 안함
 * ✔ CORS 설정 포함 (withCredentials + 세션 쿠키 허용) Cross 도메인 아이피 주소와 폰트 주소가 동일하지 않으면 안된다.
 */
@Configuration
@EnableWebSecurity //스프링 보안을 사용하겠다. 객체에 설정한데로
public class SecurityConfig {

    @Autowired // 빈등록 자동주입해주겠다.
    private CustomUserDetailsService userDetailsService;

    @Autowired // 빈등록 자동주입함 new 한게 들어감, 보안 관련 설정함, 암호화 방식중 기본방식, 빈등록을 해주어야함, 사용자 상세 사용자 정보를 가져오는 비즈니스 로직
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * ✅ 사용자 인증 관련 설정
     * - CustomUserDetailsService + 비밀번호 인코더 등록 빈등록하면 2녀석 넣어줌
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
    }

    /**
     * ✅ 보안 필터 체인 정의 중요!! 보안 필터 체인 재정의 
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            /** 객체에서 뭔가 뽑아옴 자동주입 도는 시점에서
             * ✅ CORS 설정 적용
             * - WebConfig의 WebMvcConfigurer에서 정의한 CORS 정책을 활성화
             * - withCredentials(true)를 위해 꼭 필요함!
             */
        	.cors(Customizer.withDefaults()) ///뭔가 디폴트값을 설정함

            /**
             * ✅ CSRF 비활성화
             * - 프론트엔드가 JSON으로 요청을 보낼 경우 보통 비활성화
             */
            .csrf(csrf -> csrf.disable()) // 기본적으로 뭔가 요청 기본적으로 html로 받음, json 방식으로 리턴 비활성해야 가능

            /**
             * ✅ 요청 경로별 인증/인가 정책 정의, 로그인 체크 할 것 안한 것이 있다. 이것을 여기에 넣음
             */
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/user/login.do",
                    "/api/user/logout.do",
                    "/api/user/register.do",
                    "/api/file/down.do",
                    "/api/file/imgDown.do",
                    "/api/file/imgUpload.do"
                    
                ).permitAll() // 로그인을 안해도 로그인, 로그아웃, 회원가입은 누구나 접근 가능
                .anyRequest().authenticated() // 그 외는 인증 필요, 요청은 다 체크
            )

            /**
             * ✅ 로그인 방식 설정
             * - formLogin: 사용 안 함 (프론트에서 JSON으로 처리)
             * - httpBasic: 디버깅용 기본 인증 (필요 없으면 나중에 제거 가능)
             */
            .formLogin(form -> form.disable()) //비활성화
            .httpBasic(Customizer.withDefaults()) // 기본인증 쓰기

            /**
             * ✅ 세션 관리 정책
             * - IF_REQUIRED: 인증 시에만 세션 생성
             * - STATELESS: 토큰 기반 인증 시 사용 (지금은 X)
             */
            .sessionManagement(session -> session //세션방식이 예전방식 리액트 토큰방식
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )

            /**
             * ✅ 인증 실패 시 처리 방식
             * - 인증되지 않은 사용자가 요청 시 JSON 형식의 401 응답 전송
             */
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) -> {
                	 res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //공인 인증에러 담음 401 Unauthorized
                     res.setContentType("application/json; charset=UTF-8");// 보내기

                     ApiResponse<Object> apiResponse = new ApiResponse<>(false, "권한 없음", null);//공통만듦. 담아서 작슨 객체를 넘겨줌

                     // ApiResponse를 JSON으로 변환하여 응답
                     ObjectMapper objectMapper = new ObjectMapper(); // 
                     String json = objectMapper.writeValueAsString(apiResponse); // json형태받음
                     res.getWriter().write(json);//json화시켜서 던져줌
                })
            );

        return http.build(); // 빌드시키고 끝남 결과리턴 필터에서 잘했다 못했다함
    }
}