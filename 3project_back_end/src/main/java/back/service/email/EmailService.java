package back.service.email;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static class VerificationInfo {
        String code;
        LocalDateTime timestamp;
        VerificationInfo(String code) {
            this.code = code;
            this.timestamp = LocalDateTime.now();
        }
    }

    private final Map<String, VerificationInfo> verificationMap = new ConcurrentHashMap<>();

    public void saveVerificationCode(String email, String code) {
        verificationMap.put(email, new VerificationInfo(code));
    }

    public boolean verifyCode(String email, String code) {
        VerificationInfo info = verificationMap.get(email);
        if (info == null) return false;

        // 3분 유효 시간 체크
        LocalDateTime now = LocalDateTime.now();
        if (info.timestamp.plusMinutes(3).isBefore(now)) {
            verificationMap.remove(email);
            return false;
        }

        boolean match = info.code.equals(code);
        if (match) verificationMap.remove(email); // 일회성 사용
        return match;
    }
    

    @Autowired
    private JavaMailSender mailSender;
    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

	
}
