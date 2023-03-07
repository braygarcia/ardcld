package com.ardc.ld.security;

import com.ardc.ld.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ardc.ld.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.stereotype.Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Service
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired(required = true)
    private UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication auth) throws IOException, ServletException {
        if (request.getHeader("Content-Type").toLowerCase().startsWith("application/json")) {
            org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
            // Username stores a reference to the authentication identity
            User user = userRepository.findByUsername(principal.getUsername());

            if (user == null) {
                throw new InvalidRequestException("Invalid User, please contact the administrator");
            }

            PrintWriter out = response.getWriter();
            objectMapper.writeValue(out, user);
            out.flush();

        } else {
            super.onAuthenticationSuccess(request, response, auth);
        }
    }
}
