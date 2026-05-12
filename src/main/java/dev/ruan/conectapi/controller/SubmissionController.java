package dev.ruan.conectapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/documents")
public class SubmissionController {
    
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/submissions/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }
}
