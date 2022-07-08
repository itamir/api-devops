package br.ufrn.imd.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class SecurityUtils {

    public static final String HEADER_STRING = "Authorization";
    static final byte[] SECRET = "secret".getBytes();

    static Authentication getAuthentication(HttpServletRequest request) {
        var token = request.getHeader(HEADER_STRING);
        if (token == null) {
            return null;
        }
        return parseTokenSubject(token);
    }

    private static UsernamePasswordAuthenticationToken parseTokenSubject(String token) {
        var jwt = JWT.require(Algorithm.HMAC512(SECRET))
                .withIssuer("auth0")
                .build()
                .verify(token);

        if(jwt.getExpiresAtAsInstant().isBefore(Instant.now())) {
            return null;
        }

        var subject = jwt.getSubject();
        if(subject == null) {
            return null;
        }
        return new UsernamePasswordAuthenticationToken(subject, null, List.of());
    }

    public static String buildJwt(String user) {
        return JWT.create().withIssuer("auth0")
                .withSubject(user)
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .sign(Algorithm.HMAC512(SECRET));
    }
}
