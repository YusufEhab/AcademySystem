package com.iacademy.AcademySystem.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminEndpoint() {
        return ResponseEntity.ok(" Welcome Admin!");
    }

    @GetMapping("/any")
    public ResponseEntity<String> anyEndpoint() {
        return ResponseEntity.ok(" Any valid token can access this endpoint.");
    }
}
