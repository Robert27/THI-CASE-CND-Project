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
            privateKeyContent = "-----BEGIN PRIVATE KEY-----MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDgkSqA2ozM771CeSeqQQrbuRonyIa+cYXJUW3PBRC4h2Vk+MOvlTpIAGCWN1UZ1mem8pordla3NCOddUuCkjzSX7YuD5tUVfeapA6ULBxrdAut/OYRSIri1OsgaC1CdgElWddRZkqXapjEOUw3Aob538UNlOeeJ6uOLLdpkItm2dWdAUfRJGiUwksQIZ465+HzJ6n7wKUcc7nuO9MtGThPQQpqTCohFrmdAU79YFzN9laxU1dQft6nFrHqfXKSWq5yFe2BPUk6OP/bDchuYzPko3Sa48l9T1x9y3XxBS28fNiivkLI4Z47HHOKeFqeVkDJ845JTVsxcbtR6/uEQ21BAgMBAAECggEAAL73gHpGLNyANfGeFdAyf4laLDCoornqaY4WMP5bNsNOxtEwVGuvLmCl8Xz/xBHn9T0c3jEXitmm/QjQAxm5dWXXZPuTSgPt4+5QlOIF1CUmCfvRlDqEwsnvRmou+eekPrueKslas3pDEj/LrR3nVRQog1BWNqD66AJMN6ynWErP9Ycr5UnX8oNU3mRZ4EekNSbPkjH+tSe7wbXnp1wER8cPeRN6R6GW6mcm/eTlJ2ovqCQ/Xg/ExbpKrh5p8IoqCMuTW8z5MDcrt0x7F4E8bbDIymrOaw7Z4Xq/ZEVyefNQo7y22Sp9G5ri4U2kKYCTAOHuakbJNb18nxKKnNv7gQKBgQDyJLs7kydBAauR7tHFCN6w/iocC0i3oZ72AiMbS1/Z02uzUQZKnfhdByJ9hyZLnSj9U/+TgGG0KvptAulCn01FTEHqs6vvvlrqhq3tf6sG+bfhlgOiUZ39czRK0C1iKmI9Yek5EK9GB/Crsqm0NTq1mjwBVlxw2aKPSlA+X+TfkQKBgQDtavMd7aBzALXZFnhWg2PAqfHEuwDrihHmU0VT+DgWZ+mBvfYUTQ/XttWI2jI/3QdbGIe6+5S31f5kLAZrRVzvIknkuiCXO7W2O5DnD7r1YejGK5E02njhiJ3nuasNx0Qg5Sn1C+EeWfvR9+KQXplcjYPuElZL6hlNAPMCo346sQKBgQDTTpiRoZXE1ddKT3WBk376R6KpvHMlxIW6uxy1ye4IYcZ/wjpSTU6aqApCLvmNTMWjZHUI3CTnHAZTsRGr0OaWjbiMP2TQn3YpaYRIWHwAZ1U/KDFdqgnMToeYrdic7M20MUhcQyIhY/Zzb0NG/piQN9sbc6jR1ipVDw29kONi8QKBgGAXQlDhpWgGaJN1mb0CLrSC0/yY53Vti3MaTtv+gcznv8WoOdnbmsbnFCPwlBfvAJQpKH5QZs/FB9JJU0vFZijPSvMNNjWfkBsynqI2Zqf8ObdDWOD4b6ZwoeBw8D+CYBukRMgOR83+A4Lm/kcKUq34BOate0n8dMMK2FBAFc3xAoGAFnI8QxlXWPpAvaN+jTNPYHDBGSb2xf2jpPUhX8EB1ETfredJlcafVKdgCu1OQnZUqnOGHh6qsGBuondcepuw3d8NegrRjKf9AgKnqqUXHZLKEpiuAnUJDDUope8xAtKHbBTu+jV8XDQyhDbfmmMC+vrYnO3FlPBnB/sXgtrP9m8=-----END PRIVATE KEY-----";
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
                .issuedAt(new Date())
                .issuer("AuthService")
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256) // Use RS256 for signing
                .compact();
    }
    public static String generateAuthToken() {
        return Jwts.builder()
                .header().type("JWT").and()
                .subject("authservice")
                .issuedAt(new Date())
                .issuer("AuthService")
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256) // Use RS256 for signing
                .compact();
    }

}

