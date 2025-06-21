package com.doomday_erp.doomday.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doomday_erp.doomday.dto.LoginRequest;
import com.doomday_erp.doomday.dto.LoginResponse;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/staff/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        System.out.println("Received LoginRequest: " + request);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // If successful, generate token
            String token = jwtService.generateToken(request.getEmail());

            return ResponseEntity.ok(new LoginResponse(token, "Login successful"));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(null, "Invalid email or password"));
        }
    }


  
    @PostMapping("/admin/login")
    public ResponseEntity<LoginResponse> adminLogin(@RequestBody LoginRequest request) {
        System.out.println("Received LoginRequest: " + request);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // If successful, generate token
            String token = jwtService.generateToken(request.getEmail());

            return ResponseEntity.ok(new LoginResponse(token, "Login successful"));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(null, "Invalid email or password"));
        }
    }

}
