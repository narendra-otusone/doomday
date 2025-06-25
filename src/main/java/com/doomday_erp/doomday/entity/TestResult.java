package com.doomday_erp.doomday.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "test_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long mockTestId;

    private int totalMarks;
    private int marksObtained;

    private int correctAnswers;
    private int incorrectAnswers;

    private String strongSubjects; // Optional summary
    private String weakSubjects;

    private String performanceLevel; // EXCELLENT, AVERAGE, POOR
}
