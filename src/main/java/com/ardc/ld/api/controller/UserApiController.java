package com.ardc.ld.api.controller;

import com.ardc.ld.api.ApiController;
import com.ardc.ld.model.User;
import com.ardc.ld.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "User Service API")
public class UserApiController extends ApiController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/current")
    public @ResponseBody
    User findCurrent() {
        return getCurrentUser();
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasAnyAuthority('admin')")
    public @ResponseBody
    List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping(value = "/{id}")
    public @ResponseBody
    User findOne(@PathVariable("id") Long id) {
        return userService.findOne(id);
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('admin')")
    public @ResponseBody
    User save(@RequestBody User user) {
        return userService.save(user, getCurrentUser());
    }

    @PutMapping(value = "/{id}")
    public @ResponseBody
    User edit(@PathVariable("id") Long id, @RequestBody User user) {
        return userService.edit(user, id);
    }

    @GetMapping()
    public @ResponseBody
    Page<User> findAll(@RequestParam(value = "page", required = true, defaultValue = "0") int page,
                       @RequestParam(value = "size", required = true, defaultValue = "10") int size,
                       @RequestParam(value = "sort", required = false, defaultValue = "") String[] sorting) {
        return userService.findAll(page, size, sorting);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('admin')")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }
}
