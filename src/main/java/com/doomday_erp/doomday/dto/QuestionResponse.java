package com.doomday_erp.doomday.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionResponse {

    public enum QuestionType {
        MULTIPLE_CHOICE,
        FILL_IN_THE_BLANK,
        TRUE_FALSE,
        PLAIN
    }
    private Long id;
    private String className;
    private String subject;
    private String chapter;
    private String difficulty;
    private int marks;
    private QuestionType type;
    private String questionText;
    private List<String> options;
}
