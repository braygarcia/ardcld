package com.ardc.ld.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JsonEncodedPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private String jsonUsername;
    private String jsonPassword;

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        String password = null;

        if (request.getHeader("Content-Type").toLowerCase().startsWith("application/json")) {
            password = this.jsonPassword;
        } else {
            password = super.obtainPassword(request);
        }

        return password;
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        String username = null;

        if (request.getHeader("Content-Type").toLowerCase().startsWith("application/json")) {
            username = this.jsonUsername;
        } else {
            username = super.obtainUsername(request);
        }

        return username;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if (request.getHeader("Content-Type").toLowerCase().startsWith("application/json")) {
            try {
                // json transformation
                ObjectMapper mapper = new ObjectMapper();
                LoginRequest loginRequest = mapper.readValue(request.getReader(), LoginRequest.class);

                this.jsonUsername = loginRequest.getUsername();
                this.jsonPassword = loginRequest.getPassword();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return super.attemptAuthentication(request, response);
    }
}
