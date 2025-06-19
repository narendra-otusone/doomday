package com.doomday_erp.doomday.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doomday_erp.doomday.dto.RegisterRequest;
import com.doomday_erp.doomday.dto.RegisterResponse;
import com.doomday_erp.doomday.entity.Staff;
import com.doomday_erp.doomday.repository.StaffRepository;
import com.doomday_erp.doomday.services.JwtService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/staff/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        if (staffRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(new RegisterResponse(null, "Email already registered"));
        }

        Staff staff = Staff.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        staffRepository.save(staff);

        String token = jwtService.generateToken(staff.getEmail());

        return ResponseEntity.ok(new RegisterResponse(token, "Registration successful"));
    }
}
