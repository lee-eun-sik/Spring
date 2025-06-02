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
 *  // 경로에 config파일을 다 넣기
 * ✔ 세션 기반 인증 방식 사용 (프론트엔드 분리 구조 대응) 스프링 부트에게 위임 시켜서 우리가 관리함
 * ✔ JSON 기반 API 요청 처리 (폼 로그인 X) 폼 통신 안함
 * ✔ CORS 설정 포함 (withCredentials + 세션 쿠키 허용) Cross 도메인 아이피 주소와 폰트 주소가 동일하지 않으면 안된다.
 */
@Configuration // config파일로 인정함. 로그인 관리, 세션관리를 spring boot가 해줌. 보안 사용하겠다.
@EnableWebSecurity //스프링 보안을 사용하겠다. 객체에 설정한데로 객체 내용에 따라서 작동한다.
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
    @Bean// 리턴값 등록한다.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http // 객체안에서 함수 호출 데이터 실행문을 넣음, 보안 받음  리턴 타입이 http임
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
            .csrf(csrf -> csrf.disable()) // 기본적으로 뭔가 요청 기본적으로 html로 받음, json 방식으로 리턴 비활성해야 가능 함수를 간단히함. 객체와 클래스를 생략함 함수 내용을 입력함. 함수뿐만아니라 객체를 리턴함. 인터페이스 구현객체를 받아서 실행시킴 리턴문 생략가능 a+b를 리턴시킴 익명 객체, 익며 함수를 만들고, 인터페이스를 반환함. 인터페이스에 값받아서 비활성화, http반환 값을 채워줌. 객체 변수 값을 채워줌.

            /**
             * ✅ 요청 경로별 인증/인가 정책 정의, 로그인 체크 할 것 안한 것이 있다. 이것을 여기에 넣음
             */
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                	"/favicon.ico",
                    "/api/user/login.do", // 로그인 하지 않아도 넘길 주소, 
                    "/api/user/logout.do",
                    "/api/user/register.do",
                    "/api/user/checkUserId.do",
                    "/api/petsitter/list.do",
                    "/api/file/down.do",
                    "/api/file/imgDown.do",
                    "/api/file/imgUpload.do",
                    "/api/email/send-code.do",
                    "/api/email/verify-code.do",
                    "/api/find/findId.do",
                    "/api/find/findPw.do",
                    "/api/find/resetPassword.do",
                    "/api/pet/animalregister.do",
                    "/ws/**",
                    "/ws"
                ).permitAll() // 로그인을 안해도 로그인, 로그아웃, 회원가입은 누구나 접근 가능,부모 관련 비교할 경우, 인가함수
                .anyRequest().authenticated() // 그 외는 인증 필요, 요청은 다 체크ㅡ 나머지 모드는 인증 요청한다. 
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
                .authenticationEntryPoint((req, res, e) -> { //인증 익셉션 너는 인가된 사용자가 아님
                	 res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //공인 인증에러 담음 401 Unauthorized, 로그인 되지 않음
                     res.setContentType("application/json; charset=UTF-8");// 보내기, json방식

                     ApiResponse<Object> apiResponse = new ApiResponse<>(false, "권한 없음", null);//공통만듦. 담아서 작슨 객체를 넘겨줌, 데이터를 넘길 필요없다.

                     // ApiResponse를 JSON으로 변환하여 응답
                     ObjectMapper objectMapper = new ObjectMapper(); // 
                     String json = objectMapper.writeValueAsString(apiResponse); // json형태받음
                     res.getWriter().write(json);//json화시켜서 던져줌
                })
            );

        return http.build(); // 빌드시키고 끝남 결과리턴 필터에서 잘했다 못했다함
    }
}