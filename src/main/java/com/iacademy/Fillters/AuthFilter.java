package com.iacademy.AcademySystem.Fillters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iacademy.AcademySystem.Utills.JwtUtils;
import com.iacademy.AcademySystem.Exception.ErrorResponse;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    private List<String> whiteList;

    @PostConstruct
    public void init() {
        whiteList = new ArrayList<>();
        whiteList.add("/api/auth");
        whiteList.add("/api/auth/");
        whiteList.add("/api/auth/**");

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();


        for (String whiteListPath : whiteList) {
            if (path.startsWith(whiteListPath.replace("/**", ""))) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                sendErrorResponse(response, "Missing or invalid Authorization header");
                return;
            }

            String token = authHeader.substring(7);

            if (!jwtUtils.validateToken(token)) {
                sendErrorResponse(response, "Invalid or expired JWT token");
                return;
            }

            String username = jwtUtils.getUsernameFromToken(token);
            String role = jwtUtils.getRoleFromToken(token);

            UserDetails user = userDetailsService.loadUserByUsername(username);
            if (user == null) {
                sendErrorResponse(response, "User not found");
                return;
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user, null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + role))
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            sendErrorResponse(response, e.getMessage());
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        ErrorResponse error = ErrorResponse.builder()
                .message(message)
                .status(HttpStatus.valueOf(HttpStatus.FORBIDDEN.value()))
                .build();

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");

        new ObjectMapper().writeValue(response.getWriter(), error);
    }
}
