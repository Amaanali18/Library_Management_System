package com.amaan.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/testrole")
public class TestController {

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> checkUser() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/librarian")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> checkLibrarian() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> checkAdmin() {
        return ResponseEntity.ok().build();
    }
}
