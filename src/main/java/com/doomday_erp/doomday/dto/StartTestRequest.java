package com.doomday_erp.doomday.dto;

import java.util.List;

import lombok.Data;

@Data
public class StartTestRequest {
    private Long userId;
    private String testTitle;
    private String className;
    private String subject;
    private List<Long> questionIds;
}

