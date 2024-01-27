package com.jwt.integration.utility;
import static java.util.Arrays.stream;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.jwt.integration.constant.SecurityConstant;
import com.jwt.integration.domain.UserPrincipal;

import jakarta.servlet.http.HttpServletRequest;

public class JWTTokenProvider 
{
    @Value("${jwt.secret}")
    private String secret;

    public String generateJwtToken(UserPrincipal userPrincipal)
    {
        String[] claims = getClaimsFromUser(userPrincipal);
        return JWT.create()
        .withIssuer(SecurityConstant.GET_ARRAYS_DAVECORP)
        .withAudience(SecurityConstant.GET_ARRAYS_ADMINNISTRATION)
        .withIssuedAt(new Date())
        .withSubject(userPrincipal.getUsername())
        .withArrayClaim(SecurityConstant.AUTHORITIES,claims)
        .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
        .sign(Algorithm.HMAC512(secret.getBytes()));
        //
    }

    public List<GrantedAuthority> getAuthorities(String token)
    {
        String[] claims = getClaimsFromToken(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request)
    {
        return null;
    }

    private String[] getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(SecurityConstant.AUTHORITIES).asArray(String.class);
    }

    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            verifier=JWT.require(algorithm)
            .withIssuer(SecurityConstant.GET_ARRAYS_DAVECORP)
            .build();

        } catch (JWTVerificationException exception) 
        {
            throw new JWTVerificationException(SecurityConstant.TOKEN_CANNOT_BE_VERIFIED);
        }
        return verifier;
    }

    private String[] getClaimsFromUser(UserPrincipal userPrincipal)
    {
        List<String> authorities = new ArrayList<String>();
        for(GrantedAuthority grantedAuthority : userPrincipal.getAuthorities())
        {
            authorities.add(grantedAuthority.getAuthority());
        }
        return authorities.toArray(new String[0]);
    }
}
