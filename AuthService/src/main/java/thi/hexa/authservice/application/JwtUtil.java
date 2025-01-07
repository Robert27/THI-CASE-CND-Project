package thi.hexa.authservice.application;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import thi.hexa.authservice.domain.User;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.KeyFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

public class JwtUtil {

    private static final long EXPIRATION_TIME = 864_000_000; // 10 days

    // Load private key from environment variable
    private static PrivateKey getPrivateKey() throws RuntimeException {
        String privateKeyContent = System.getenv("JWT_PRIVATE_KEY");
        if (privateKeyContent == null || privateKeyContent.isEmpty()) {
            throw new RuntimeException("Private key not found in environment variables.");
        }

        privateKeyContent = privateKeyContent
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", ""); // Remove headers and whitespace

        System.out.println(privateKeyContent);
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyContent);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        try {
            return keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    // Generate JWT token
    public static String generateToken(User user) {
        return Jwts.builder()
                .header().type("JWT").and()
                .subject(String.valueOf(user.getUser_id()))
                .claim("username", user.getUsername())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256) // Use RS256 for signing
                .compact();
    }
}

