package dev.ruan.conectapi.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateSubmissionDto(
        @NotBlank(message = "File name is required")
        String fileName,

        @NotBlank(message = "File path is required")
        String filePath
) {
}
