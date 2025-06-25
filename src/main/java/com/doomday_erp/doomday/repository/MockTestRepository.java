package com.doomday_erp.doomday.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doomday_erp.doomday.entity.MockTest;

public interface MockTestRepository extends JpaRepository<MockTest, Long> {
    List<MockTest> findByUserId(Long userId);
}
