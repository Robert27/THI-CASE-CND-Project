package thi.hexa.authservice.application;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


import java.security.Key;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "wMkm3yI4IArwHfkcc/l70I5+Fqs2VyH4LwhSAfpLeYasJs/S+UTp6Oz+D9lqb3BhrP0I790A3VujPmEfZyvrAYGj4T2POG7c3mllfzMNH9hqoEoiE10xkfuQUWOdA+Gv7NBfGK111EhO6hHDeX+/iltcdx1SZtnpcv7013/OLa5M4dwJ5fSRsFXcuWuhEK9f2wmfyoxqFj5ByAnbHszLhmh99A9FQhMTPbx2rHg6kVtGavtFXZWvEVENgbaMdhadFre4GzH/rSPSg52BQ4CD2cGbs02GsoOs9lx4aOGeupdM86uQAufAiV7TBW20pa5oCcu7y8SX1ln0RBXFcssOfg==";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days

    private static Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public static String generateToken(Integer user_id) {
        return Jwts.builder()
                .header().type("JWT").and()
                .subject(user_id.toString())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
