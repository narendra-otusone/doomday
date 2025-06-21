package com.doomday_erp.doomday.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doomday_erp.doomday.dto.StaffResponse;
import com.doomday_erp.doomday.entity.Staff;
import com.doomday_erp.doomday.repository.StaffRepository;
import com.doomday_erp.doomday.services.JwtService;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private StaffRepository staffRepository;

    @GetMapping("/staffs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StaffResponse>> getAllStaff() {
        List<Staff> staffList = staffRepository.findAll();

        List<StaffResponse> response = staffList.stream()
                .map(staff -> new StaffResponse(
                staff.getId().toString(),
                staff.getFullName(),
                staff.getEmail(),
                staff.getRole()
        ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

}
