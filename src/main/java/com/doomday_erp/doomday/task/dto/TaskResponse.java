package com.doomday_erp.doomday.task.dto;

import java.time.LocalDateTime;

import com.doomday_erp.doomday.task.entity.Task.Status;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String assignedBy;
    private String assignedTo;
    private Status status;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
}
