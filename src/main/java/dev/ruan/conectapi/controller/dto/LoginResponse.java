package dev.ruan.conectapi.controller.dto;

public record LoginResponse(String access_token, Long expires_in) {
}
