package com.doomday_erp.doomday.dto;

import java.util.List;

import lombok.Data;

@Data
public class QuestionRequest {

    public enum QuestionType {
        MULTIPLE_CHOICE,
        FILL_IN_THE_BLANK,
        TRUE_FALSE,
        PLAIN
    }
    private String className;
    private String subject;
    private String chapter;
    private String difficulty;
    private int marks;
    private QuestionType type;
    private String questionText;
    private List<String> options; // Optional
    private String correctAnswer;
}
