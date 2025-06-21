package com.doomday_erp.doomday.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileResponse {
    private String fullName;
    private String email;
    private String role;
}
