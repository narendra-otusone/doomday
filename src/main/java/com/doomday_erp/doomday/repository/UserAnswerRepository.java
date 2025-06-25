package com.doomday_erp.doomday.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doomday_erp.doomday.entity.UserAnswer;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    List<UserAnswer> findByMockTestIdIn(List<Long> mockTestIds);
}
