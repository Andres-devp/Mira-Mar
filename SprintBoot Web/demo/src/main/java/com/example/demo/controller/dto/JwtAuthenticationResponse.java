package com.example.demo.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long userId;
    private String role;
    private String username;
    private long expiresIn;

    public static JwtAuthenticationResponse of(String accessToken, String refreshToken, Long userId, String role, String username, long expiresIn) {
        return JwtAuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .userId(userId)
                .role(role)
                .username(username)
                .expiresIn(expiresIn)
                .build();
    }
}
