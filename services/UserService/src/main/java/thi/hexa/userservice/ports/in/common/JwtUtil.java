package thi.hexa.userservice.ports.in.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.*;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class JwtUtil {
    private static PublicKey getPublicKey() throws RuntimeException {
        String publicKeyContent = System.getenv("JWT_PUBLIC_KEY");
        if (publicKeyContent == null || publicKeyContent.isEmpty()) {
            throw new RuntimeException("Public key not found in environment variables.");
        }

        publicKeyContent = publicKeyContent
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", ""); // Remove headers and whitespace

        byte[] keyBytes = Base64.getDecoder().decode(publicKeyContent);
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load public key", e);
        }
    }

    private static PublicKey publicKey = getPublicKey();

    // Verify the JWT and return claims
    public static Claims verifyToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(publicKey) // Set the public key for verification
                    .build()
                    .parseSignedClaims(token) // Parse the token
                    .getPayload(); // Extract the claims
        } catch (JwtException e) {
            // Token is invalid
            throw new RuntimeException("Invalid JWT token: " + e.getMessage(), e);
        }


    }
}