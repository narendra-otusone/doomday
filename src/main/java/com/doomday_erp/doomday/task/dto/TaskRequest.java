package com.doomday_erp.doomday.task.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TaskRequest {
    private String title;
    private String description;
    private String assignedTo;
    private String assignedBy;
    private LocalDateTime dueDate;
}
