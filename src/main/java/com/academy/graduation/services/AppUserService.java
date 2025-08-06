package com.academy.graduation.services;

import com.academy.graduation.entities.AppUser;
import com.academy.graduation.repositories.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {
    @Autowired
    private AppUserRepo appUserRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AppUser register(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (!user.getRole().startsWith("ROLE_")) {
            user.setRole("ROLE_" + user.getRole().toUpperCase());
        }
        return appUserRepo.save(user);
    }
}
