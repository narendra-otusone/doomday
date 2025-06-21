package com.doomday_erp.doomday.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doomday_erp.doomday.dto.QuestionRequest;
import com.doomday_erp.doomday.entity.Question;
import com.doomday_erp.doomday.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionRepository questionRepository;

    @PostMapping("/questions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addQuestion(@RequestBody QuestionRequest request) {
        Question question = mapToEntity(request);
        questionRepository.save(question);
        return ResponseEntity.ok("✅ Question added successfully");
    }

    @PostMapping("/questions/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addMultipleQuestions(@RequestBody List<QuestionRequest> requests) {
        List<Question> questions = requests.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());

        questionRepository.saveAll(questions);
        return ResponseEntity.ok("✅ " + questions.size() + " questions added successfully");
    }

    // 🔄 Helper method
    private Question mapToEntity(QuestionRequest request) {
        return Question.builder()
                .className(request.getClassName())
                .subject(request.getSubject())
                .chapter(request.getChapter())
                .difficulty(request.getDifficulty())
                .marks(request.getMarks())
                .type(com.doomday_erp.doomday.entity.Question.QuestionType.valueOf(request.getType().name()))
                .questionText(request.getQuestionText())
                .options(request.getOptions())
                .correctAnswer(request.getCorrectAnswer())
                .isActive(true)
                .build();
    }

    @PutMapping("/questions/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateQuestion() {
        return ResponseEntity.ok("Question updated successfully");
    }

    @DeleteMapping("/questions/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteQuestion() {
        return ResponseEntity.ok("Question deleted successfully");
    }
}
