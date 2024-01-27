package com.jwt.integration.utility;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;

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
        //
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
