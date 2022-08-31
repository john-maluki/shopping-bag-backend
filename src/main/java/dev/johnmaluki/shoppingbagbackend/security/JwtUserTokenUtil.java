package dev.johnmaluki.shoppingbagbackend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;

public final class JwtUserTokenUtil {
    private UserPrincipal userPrincipal;
    private String insurer;

    public JwtUserTokenUtil() {
    }
    public JwtUserTokenUtil(UserPrincipal userPrincipal, String insurer) {
        this.userPrincipal = userPrincipal;
        this.insurer = insurer;
    }

    public String obtainUserAccessToken() {
        return JWT.create()
                .withSubject(userPrincipal.getUsername())
                .withClaim("id", userPrincipal.getId())
                .withClaim("roles", userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withClaim("firstName", userPrincipal.firstName())
                .withClaim("lastName", userPrincipal.lastName())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withIssuer(this.insurer)
                .sign(this.getJwtAlgorithm());
    }

    public String obtainUserRefreshToken() {
        return JWT.create()
                .withSubject(userPrincipal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 20000 * 60 * 1000))
                .withIssuer(this.insurer)
                .sign(this.getJwtAlgorithm());
    }

    public String getSubjectFromJwtToken(String token){
        JWTVerifier verifier = JWT.require(this.getJwtAlgorithm()).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }

    private Algorithm getJwtAlgorithm() {
        return Algorithm.HMAC512(JwtProperties.SECRET.getBytes());
    }
}
