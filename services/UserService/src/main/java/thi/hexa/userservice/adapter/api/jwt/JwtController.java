package thi.hexa.userservice.adapter.api.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import thi.hexa.userservice.adapter.api.common.JwtUtil;
import thi.hexa.userservice.domain.AuthInfo;

@Service
public class JwtController {
    public AuthInfo getAuthInfo(String token) {
        Claims claims = JwtUtil.verifyToken(token);
        if (claims == null) {
            return null;
        }

        if(claims.getSubject().equals("authservice")) {
            return new AuthInfo(null, null, true);
        } else {
            return new AuthInfo(claims.get("username", String.class),Integer.parseInt(claims.getSubject()),false );
     }
 }

}
