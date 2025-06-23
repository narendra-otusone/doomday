package com.doomday_erp.doomday.dto;

import java.util.List;

import lombok.Data;

@Data
public class QuestionPaperRequest {
    private List<Long> questionIds;
}
