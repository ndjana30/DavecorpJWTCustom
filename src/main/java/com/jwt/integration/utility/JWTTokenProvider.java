package com.jwt.integration.utility;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jwt.integration.constant.SecurityConstant;
import com.jwt.integration.domain.UserPrincipal;

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

    }

    private String[] getClaimsFromUser(UserPrincipal userPrincipal) {
        
    }
}
