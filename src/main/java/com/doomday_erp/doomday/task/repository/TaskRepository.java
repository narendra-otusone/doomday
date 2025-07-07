package com.doomday_erp.doomday.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doomday_erp.doomday.entity.User;
import com.doomday_erp.doomday.task.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAssignedTo(User user);

    List<Task> findByAssignedBy(User userId);
}
