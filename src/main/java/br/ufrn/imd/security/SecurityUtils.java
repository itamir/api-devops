package br.ufrn.imd.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public class SecurityUtils {

    static final long EXPIRATION_TIME = 860_000_000; // 10 days
    static final byte[] SECRET = "secret".getBytes();
    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_STRING = "Authorization";

    static Authentication getAuthentication(HttpServletRequest request) {
        var token = request.getHeader(HEADER_STRING);

        if (token != null) {
            var user = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, List.of());
            }
        }
        return null;
    }

    public static void addAuthorizationToHeader(HttpServletResponse response, Authentication authResult) {
        var jwt = Jwts.builder()
                .setSubject(authResult.getName())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();

        response.addHeader("Authorization", "Bearer " + jwt);
    }
}
