package com.doomday_erp.doomday.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.doomday_erp.doomday.entity.Staff;
import com.doomday_erp.doomday.repository.StaffRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final StaffRepository staffRepository;


    @Override
public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    System.out.println("🔍 Loading user: " + email);
    Staff staff = staffRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return User.builder()
            .username(staff.getEmail())
            .password(staff.getPassword())
            .roles(staff.getRole())
            .build();
}

}
