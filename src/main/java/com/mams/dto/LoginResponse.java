package com.mams.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoginResponse {
    private String token;
    private Long userId;
    private String fullName;
    private String email;
    private String role;
    private Long baseId;
    private String baseName;
}
