package back.service.email;

public interface EmailService {
	void sendEmail(String to, String subject, String text);
    boolean saveEmailCode(String email, String code);
    boolean verifyEmailCode(String email, String code);
    boolean isEmailAlreadyRegistered(String email);
    boolean isEmailRegistered(String email);
    boolean sendEmail(String to, String code); // 새로 추가
	void saveVerificationCode(String email, String code);
	boolean verifyCode(String email, String code);
   
}
