package com.doomday_erp.doomday.task.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.doomday_erp.doomday.task.dto.TaskRequest;
import com.doomday_erp.doomday.task.dto.TaskResponse;
import com.doomday_erp.doomday.task.entity.Task;
import com.doomday_erp.doomday.task.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public TaskResponse createTask(TaskRequest request) {
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .assignedBy(request.getAssignedBy())
                .assignedTo(request.getAssignedTo())
                .status(Task.Status.TODO)
                .createdAt(LocalDateTime.now())
                .dueDate(request.getDueDate())
                .build();
        Task saved = taskRepository.save(task);
        return mapToResponse(saved);
    }

    @Override
    public List<TaskResponse> getTasksForUser(String userId) {
        return taskRepository.findByAssignedTo(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponse updateStatus(Long taskId, String newStatus) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(Task.Status.valueOf(newStatus.toUpperCase()));
        return mapToResponse(taskRepository.save(task));
    }

    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .assignedBy(task.getAssignedBy())
                .assignedTo(task.getAssignedTo())
                .status(task.getStatus())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .build();
    }
}
