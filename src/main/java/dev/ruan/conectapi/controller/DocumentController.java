package dev.ruan.conectapi.controller;

import dev.ruan.conectapi.controller.dto.CreateSubmissionDto;
import dev.ruan.conectapi.entities.Submission;
import dev.ruan.conectapi.entities.User;
import dev.ruan.conectapi.repository.SubmissionRepository;
import dev.ruan.conectapi.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public DocumentController(SubmissionRepository submissionRepository, UserRepository userRepository) {
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/template")
    public ResponseEntity<Resource> downloadTemplate() {
        Resource resource = new ClassPathResource("templates/modelo.docx");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=template.docx")
                .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                .body(resource);
    }

    @PostMapping("/submissions")
    public ResponseEntity<Submission> createSubmission(
            @ModelAttribute @Valid CreateSubmissionDto dto,
            @AuthenticationPrincipal Jwt jwt) throws IOException {

        String userId = jwt.getSubject();
        User usuario = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        MultipartFile file = dto.file();

        Path userDir = Paths.get(uploadDir).resolve(userId).toAbsolutePath().normalize();
        Files.createDirectories(userDir);

        String fileName = "modelo_preenchido.docx";
        Path filePath = userDir.resolve(fileName);

        file.transferTo(filePath);

        Submission submission = new Submission();
        submission.setFileName(fileName);
        submission.setFilePath(filePath.toString());
        submission.setStatus(Submission.Status.PENDING);
        submission.setSubmissionDate(Instant.now());
        submission.setParticipant(usuario);

        Submission saved = submissionRepository.save(submission);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}