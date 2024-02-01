package com.jwt.integration.filter;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwt.integration.constant.SecurityConstant;
import com.jwt.integration.domain.HttpResponse;
// import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler{

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException 
            {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.UNAUTHORIZED.value(),HttpStatus.UNAUTHORIZED,HttpStatus.UNAUTHORIZED.getReasonPhrase().toUpperCase(),SecurityConstant.ACCESS_DENIED_MESSAGE);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream,httpResponse);
        outputStream.flush();
    }
    
}
