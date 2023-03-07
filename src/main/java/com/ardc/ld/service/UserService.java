package com.ardc.ld.service;

import com.ardc.ld.api.InvalidArgumentException;
import com.ardc.ld.api.NotFoundException;
import com.ardc.ld.model.Role;
import com.ardc.ld.model.User;
import com.ardc.ld.model.UserRolId;
import com.ardc.ld.model.UsersRoles;
import com.ardc.ld.repository.RoleRepository;
import com.ardc.ld.repository.UserRepository;
import com.ardc.ld.repository.UsersRolesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UsersRolesRepository userRoleRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findOne(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new NotFoundException();
        }
        return user.get();
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Transactional
    public User save(User user, User creator) {
        List<Role> roles = user.getRoles();
        user.setCreationDate(new Date());
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreator(creator);
        user.setRoles(null);
        User newUser = userRepository.save(user);
        user.setUserId(newUser.getUserId());
        assignRoles(user, roles);
        return newUser;
    }

    @Transactional
    public User edit(User user, Long id) {
        if (!user.getUserId().equals(id)) {
            throw new NotFoundException("USER_ID parameter is not valid.");
        }
        User exist = findOne(id);
        if (exist == null) {
            throw new NotFoundException("USER not found.");
        }
        user.setCreator(exist.getCreator());
        user.setPassword(user.getPassword() == null || user.getPassword().trim().isEmpty() ? exist.getPassword() : user.getPassword());

        List<UsersRoles> userRoles = userRoleRepository.findUserRolesByUserId(user);
        if (userRoles != null) {
            userRoleRepository.deleteAll(userRoles);
        }
        assignRoles(user, user.getRoles());
        return userRepository.save(user);
    }

    public Page<User> findAll(int page, int size, String[] sorting) {
        if (sorting != null && sorting.length > 0) {
            return userRepository.findAll(PageRequest.of(page, size, Sort.by(sorting)));
        }
        return userRepository.findAll(PageRequest.of(page, size));
    }

    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new InvalidArgumentException("USER can not delete, parent key found.");
        }
    }

    private void assignRoles(User user, List<Role> roles) {
        UsersRoles userRole;
        for (Role r : roles) {
            userRole = new UsersRoles();
            userRole.setId(new UserRolId(user.getUserId(), r.getRoleId()));
            userRoleRepository.save(userRole);
        }
    }

}
