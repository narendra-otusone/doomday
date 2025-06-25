package com.doomday_erp.doomday.dto;

import java.util.List;

import lombok.Data;

@Data
public class SubmitTestRequest {
    private Long mockTestId;
    private List<AnswerPayload> answers;

    @Data
    public static class AnswerPayload {
        private Long questionId;
        private String selectedAnswer;
    }
}

