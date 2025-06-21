package com.doomday_erp.doomday.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;

    // Getters and setters (or use Lombok @Data)
    @Override
    public String toString() {
        return "LoginRequest{"
                + "email='" + email + '\''
                + ", password='" + password + '\''
                + '}';
    }
}
