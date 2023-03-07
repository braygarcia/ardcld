package com.ardc.ld.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        if (request.getHeader("Content-Type").toLowerCase().startsWith("application/json")) {
            response.getWriter().print("{\"responseCode\":\"fail\"}");
            response.getWriter().flush();
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }

}
