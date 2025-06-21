package com.doomday_erp.doomday.dto;

import lombok.Data;

@Data
public class GeneratePaperRequest {
    // Define fields according to your requirements
    private String className;
    private String subject;
    private String difficulty;
    private String type;
    private String chapter;
    private int numberOfQuestions;
}