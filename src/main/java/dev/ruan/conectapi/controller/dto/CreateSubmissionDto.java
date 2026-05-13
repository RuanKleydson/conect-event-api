package dev.ruan.conectapi.controller.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CreateSubmissionDto(
        @NotNull(message = "File is required")
        MultipartFile file
) {
}
