package com.doomday_erp.doomday.task.service;

import java.util.List;

import com.doomday_erp.doomday.task.dto.TaskRequest;
import com.doomday_erp.doomday.task.dto.TaskResponse;

public interface TaskService {
    TaskResponse createTask(TaskRequest request);
    List<TaskResponse> getTasksForUser(String userId);
    TaskResponse updateStatus(Long taskId, String newStatus);
}
