package com.tam.security.Jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e)
            throws IOException, ServletException {
        final String expired = (String) request.getAttribute("expired");
        logger.error("Unauthorized error. Message - {}", e.getMessage());
        if (expired != null){
            System.out.println(expired);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, expired);
        }else{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error -> Unauthorized");
        }
    }
}
