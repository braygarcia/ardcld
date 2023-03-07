package com.ardc.ld.security;

import com.ardc.ld.model.Role;
import com.ardc.ld.model.User;
import com.ardc.ld.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameAndActive(username, true);
        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " not found or is not active.");
        }

        List<GrantedAuthority> auth = new ArrayList();
        for (Role role : user.getRoles()) {
            auth.add(new SimpleGrantedAuthority(role.getName()));
        }

        String password = user.getPassword();
        return new org.springframework.security.core.userdetails.User(username, password, auth);
    }
}
