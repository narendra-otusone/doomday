package com.doomday_erp.doomday.entity;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "questions")
@Builder 
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String className; // e.g., "10th", "12th"
    private String subject;   // e.g., "Math", "Science"
    private String chapter;   // e.g., "Trigonometry"

    private String difficulty; // EASY, MEDIUM, HARD
    private int marks;

    @Enumerated(EnumType.STRING)
    private QuestionType type; // MULTIPLE_CHOICE, FILL_IN_THE_BLANK, TRUE_FALSE, PLAIN

    private String questionText;

    // Multiple choice-specific
    @ElementCollection
    private List<String> options;

    private String correctAnswer;

    private boolean isActive = true;

    public enum QuestionType {
    MULTIPLE_CHOICE,
    FILL_IN_THE_BLANK,
    TRUE_FALSE,
    PLAIN
}

}
