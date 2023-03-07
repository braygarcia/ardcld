package com.ardc.ld.api;

import com.ardc.ld.model.User;
import com.ardc.ld.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class ApiController {

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        User user = null;
        if (isAuthenticated()) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            user = userRepository.findByUsername(username);
        }
        return user;
    }

    public Boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() == null ? false : SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }
}
