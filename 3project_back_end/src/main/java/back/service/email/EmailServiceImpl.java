package back.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import back.exception.HException;
import back.mapper.auto.AutoInfoMapper;
import back.model.auto.AutoInfo;
import back.model.user.User;
import back.service.user.UserService;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserService userService;
    private final ConcurrentHashMap<String, VerificationInfo> verificationCodes = new ConcurrentHashMap<>();
    @Autowired
    private AutoInfoMapper autoInfoMapper;

    @Override
    public boolean sendEmail(String to, String code) {
        String subject = "이메일 인증번호";
        String text = "인증번호는 " + code + " 입니다.";
        try {
            sendEmail(to, subject, text);  // 실제 메일 발송 메서드 호출
            return true;
        } catch (Exception e) {
            throw new HException("이메일 전송 중 오류가 발생했습니다.", e);
        }
    }
    @Override
    public void saveVerificationCode(String email, String code) {
        verificationCodes.put(email, new VerificationInfo(code, LocalDateTime.now()));
    }

    @Override
    public boolean verifyCode(String email, String code) {
        VerificationInfo info = verificationCodes.get(email);
        if (info == null) return false;

        if (!info.getCode().equals(code)) return false;

        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(info.getGeneratedTime().plusMinutes(3));
    }

    @Override
    public boolean isEmailAlreadyRegistered(String email) {
    	return userService.isEmailRegistered(email); // UserService에 이메일 중복 체크 메서드 필요
    }

    private static class VerificationInfo {
        private final String code;
        private final LocalDateTime generatedTime;

        public VerificationInfo(String code, LocalDateTime generatedTime) {
            this.code = code;
            this.generatedTime = generatedTime;
        }

        public String getCode() {
            return code;
        }

        public LocalDateTime getGeneratedTime() {
            return generatedTime;
        }
    }
    @Override
    public boolean saveEmailCode(String email, String code) {
        AutoInfo existing = autoInfoMapper.selectByEmail(email);
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new HException("등록된 사용자 정보가 없습니다.");
        }
        if (existing == null) {
            AutoInfo info = new AutoInfo();
            info.setUser(user);
            info.setUsersId(null);
            info.setAuthEmail(email);
            info.setAuthNumber(code);
            info.setCreateId("system");
            info.setUpdateId("system");
            autoInfoMapper.insertAuthInfo(info);
        } else {
            existing.setAuthNumber(code);
            existing.setUpdateId("system");
            autoInfoMapper.updateAuthNumber(existing);
        }

        // 메모리에도 저장 (선택사항)
        verificationCodes.put(email, new VerificationInfo(code, LocalDateTime.now()));
		return false;
    }
    
    @Override
    public boolean verifyEmailCode(String email, String code) {
        VerificationInfo info = verificationCodes.get(email);
        if (info == null || !info.getCode().equals(code)) return false;

        if (LocalDateTime.now().isAfter(info.getGeneratedTime().plusMinutes(3))) return false;

        autoInfoMapper.verifyAuthEmail(email); // 인증 완료 처리
        return true;
    }
	@Override
	public boolean isEmailRegistered(String email) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void sendEmail(String to, String subject, String text) {
		// TODO Auto-generated method stub
		
	}
	
}
