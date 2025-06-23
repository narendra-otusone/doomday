package com.doomday_erp.doomday.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class AutoGeneratePaperRequest {
    private String className;
    private List<String> subjects;
    private List<String> chapters;
    private List<String> types;
    private int totalMarks;
    private Map<String, Integer> difficultyDistribution; // e.g. {"EASY": 40, "MEDIUM": 40, "HARD": 20}
}
