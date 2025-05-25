package back.util;

import java.util.Base64;

import io.jsonwebtoken.security.Keys;

public class SecretKeyGenerator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		byte[] keyBytes = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256).getEncoded();
        String base64Key = Base64.getUrlEncoder().encodeToString(keyBytes);
        System.out.println("Base64 Encoded Secret Key: " + base64Key);
	}

}
