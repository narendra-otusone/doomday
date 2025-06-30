package com.doomday_erp.doomday.task.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status; // TODO, IN_PROGRESS, DONE

    private String assignedBy; // User ID or name
    private String assignedTo; // User ID or name

    private LocalDateTime createdAt;
    private LocalDateTime dueDate;

    public enum Status {
        TODO, IN_PROGRESS, DONE
    }
}
