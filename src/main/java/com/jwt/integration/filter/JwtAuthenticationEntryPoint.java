package com.jwt.integration.filter;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
// import org.apache.commons.logging.Log;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwt.integration.constant.SecurityConstant;
import com.jwt.integration.domain.HttpResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// import org.apache.commons.logging.LogFactory;

@Component
public class JwtAuthenticationEntryPoint extends Http403ForbiddenEntryPoint 
{
    // private static final Log logger = LogFactory.getLog(Http403ForbiddenEntryPoint.class);
    @Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException {
		HttpResponse httpResponse = new HttpResponse(HttpStatus.FORBIDDEN.value(),HttpStatus.FORBIDDEN,HttpStatus.FORBIDDEN.getReasonPhrase().toUpperCase(),SecurityConstant.FORBIDDEN_MESSAGE);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream,httpResponse);
        outputStream.flush();
		// response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
	}
}
