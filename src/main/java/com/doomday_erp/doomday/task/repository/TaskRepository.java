package com.doomday_erp.doomday.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doomday_erp.doomday.task.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedTo(String userId);
    List<Task> findByAssignedBy(String userId);
}
