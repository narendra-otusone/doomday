package com.doomday_erp.doomday.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doomday_erp.doomday.entity.Staff;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByEmail(String email);
}
