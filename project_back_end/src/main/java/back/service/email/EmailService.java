package back.service.email;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailService {

    private final Map<String, VerificationCode> verificationCodes = new ConcurrentHashMap<>();

    @Autowired
    private JavaMailSender mailSender; 

    // 인증 코드 저장
    public void saveVerificationCode(String email, String code) {
        VerificationCode verification = new VerificationCode(code, LocalDateTime.now().plusMinutes(5));
        verificationCodes.put(email, verification);
    }
    
    // 인증 코드 검증
    public boolean verifyCode(String email, String inputCode) {
        VerificationCode stored = verificationCodes.get(email);
        if (stored == null || stored.getExpireAt().isBefore(LocalDateTime.now())) {
            return false;
        }
        return stored.getCode().equals(inputCode);
    }
    
    // 이메일 전송
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    // 내부 클래스
    private static class VerificationCode {
        private final String code;
        private final LocalDateTime expireAt;
        
        public VerificationCode(String code, LocalDateTime expireAt) {
            this.code = code;
            this.expireAt = expireAt;
        }
        
        public String getCode() { return code; }
        public LocalDateTime getExpireAt() { return expireAt; }
    }
}
