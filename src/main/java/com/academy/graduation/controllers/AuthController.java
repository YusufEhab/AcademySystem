package com.academy.graduation.controllers;

import com.academy.graduation.entities.AppUser;
import com.academy.graduation.services.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private AppUserService userService;
    @PostMapping("/register")
    public ResponseEntity<AppUser> register(@RequestBody AppUser user) {
        AppUser saved = userService.register(user);
        return ResponseEntity.ok(saved);
    }
}
