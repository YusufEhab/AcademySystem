package com.iacademy.AcademySystem.Service;

import com.iacademy.AcademySystem.Document.User;
import com.iacademy.AcademySystem.Dto.AuthLogin;
import com.iacademy.AcademySystem.Dto.AuthResponse;
import com.iacademy.AcademySystem.Dto.RegisterRequest;
import com.iacademy.AcademySystem.Exception.BadRequestException;
import com.iacademy.AcademySystem.Repository.UserRepository;
import com.iacademy.AcademySystem.Utills.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BadRequestException("Username is already taken");
        }


        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email is already registered");
        }


        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .enabled(true)
                .build();

        userRepository.save(user);


        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return new AuthResponse(token);
    }

    public AuthResponse login(AuthLogin request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (!user.isEnabled()) {
            throw new BadRequestException("User is disabled");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return new AuthResponse(token);
    }
}
