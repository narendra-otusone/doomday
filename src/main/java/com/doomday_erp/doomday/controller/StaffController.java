package com.doomday_erp.doomday.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doomday_erp.doomday.dto.ProfileResponse;
import com.doomday_erp.doomday.entity.Staff;
import com.doomday_erp.doomday.repository.StaffRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffRepository staffRepository;

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getProfile(Authentication authentication) {
        String email = authentication.getName();

        Staff staff = staffRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProfileResponse profile = new ProfileResponse(
                staff.getFullName(),
                staff.getEmail(),
                staff.getRole()
        );

        return ResponseEntity.ok(profile);
    }

}
