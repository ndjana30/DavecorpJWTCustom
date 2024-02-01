package com.jwt.integration.filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import com.jwt.integration.constant.SecurityConstant;
import com.jwt.integration.utility.JWTTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Override
    protected void doFilterInternal
    (@NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull FilterChain filterChain
    )
    throws ServletException, IOException {
        
            if (request.getMethod().equalsIgnoreCase(SecurityConstant.OPTIONS_HTTP_METHOD)) {
                                response.setStatus(HttpStatus.OK.value());
                } else {
                    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
                    if (authorizationHeader == null || !authorizationHeader.startsWith(SecurityConstant.TOKEN_PREFIX)) {
                        filterChain.doFilter(request, response);
                        return;
                    }
                    String token = authorizationHeader.substring(SecurityConstant.TOKEN_PREFIX.length());
                    String username = jwtTokenProvider.getSubject(token);
                    if (jwtTokenProvider.isTokenValid(username, token) && SecurityContextHolder.getContext().getAuthentication() == null) {
                        List<GrantedAuthority> authorities = jwtTokenProvider.getAuthorities(token);
                        Authentication authentication = jwtTokenProvider.getAuthentication(username, authorities, request);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        SecurityContextHolder.clearContext();
                    }
                }
                filterChain.doFilter(request, response);
}
}

