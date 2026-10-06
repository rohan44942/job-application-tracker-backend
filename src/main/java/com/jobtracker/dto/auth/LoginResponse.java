package com.jobtracker.dto.auth;

public record LoginResponse(String accessToken, String tokenType, long expiresIn) {
}
