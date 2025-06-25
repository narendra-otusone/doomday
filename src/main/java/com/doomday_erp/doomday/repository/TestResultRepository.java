package com.doomday_erp.doomday.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doomday_erp.doomday.entity.TestResult;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findByMockTestIdIn(List<Long> mockTestIds);
}
