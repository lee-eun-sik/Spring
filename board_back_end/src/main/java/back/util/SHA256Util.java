package back.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import back.exception.HException;

public class SHA256Util {
    /**
     * 입력받은 문자열을 SHA-256 방식으로 암호화하는 메서드
     * 
     * @param input 암호화할 문자열 (비밀번호)
     * @return 암호화된 해시값 (64자리의 16진수 문자열)
     * @throws HException 
     */
    public static String encrypt(String input) throws HException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256"); // SHA-256 알고리즘 사용
            byte[] hash = md.digest(input.getBytes()); // 바이트 배열로 변환 후 해싱

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b); // 16진수 변환
                if (hex.length() == 1) {
                    hexString.append('0'); // 한 자리일 경우 앞에 0 추가
                }
                hexString.append(hex);
            }
            return hexString.toString(); // 최종적으로 64자리의 해시 문자열 반환
        } catch (NoSuchAlgorithmException e) {
            throw new HException("SHA-256 암호화 오류 발생", e);
        }
    }
}
