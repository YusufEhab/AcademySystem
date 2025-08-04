package com.iacademy.AcademySystem.Controller;

import com.iacademy.AcademySystem.Dto.AuthLogin;
import com.iacademy.AcademySystem.Dto.AuthResponse;
import com.iacademy.AcademySystem.Dto.RegisterRequest;
import com.iacademy.AcademySystem.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthLogin request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
