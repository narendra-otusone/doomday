package com.doomday_erp.doomday.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doomday_erp.doomday.dto.StartTestRequest;
import com.doomday_erp.doomday.dto.SubmitTestRequest;
import com.doomday_erp.doomday.entity.MockTest;
import com.doomday_erp.doomday.entity.Question;
import com.doomday_erp.doomday.entity.TestResult;
import com.doomday_erp.doomday.entity.UserAnswer;
import com.doomday_erp.doomday.repository.MockTestRepository;
import com.doomday_erp.doomday.repository.QuestionRepository;
import com.doomday_erp.doomday.repository.TestResultRepository;
import com.doomday_erp.doomday.repository.UserAnswerRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RestController
@RequestMapping("/api/v1/mock-tests")
@RequiredArgsConstructor
public class MockTestController {

    private final QuestionRepository questionRepository;
    private final MockTestRepository mockTestRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final TestResultRepository testResultRepository;

    @PostMapping("/start")
    public ResponseEntity<?> startTest(@RequestBody StartTestRequest request) {
        List<Question> questions;

        if (request.getQuestionIds() != null && !request.getQuestionIds().isEmpty()) {
            questions = request.getQuestionIds().stream()
                    .map(questionRepository::findById)
                    .flatMap(Optional::stream)
                    .collect(Collectors.toList());
        } else {
            // TODO: Add auto-generate logic here if required
            return ResponseEntity.badRequest().body("Provide question IDs or use auto-generate.");
        }

        MockTest mockTest = MockTest.builder()
                .userId(request.getUserId())
                .testTitle(request.getTestTitle())
                .className(request.getClassName())
                .subject(request.getSubject())
                .createdAt(new Date())
                .isCompleted(false)
                .questionIds(questions.stream().map(Question::getId).toList())
                .build();

        mockTestRepository.save(mockTest);

        return ResponseEntity.ok(Map.of(
                "testId", mockTest.getId(),
                "questions", questions
        ));
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitAnswers(@RequestBody SubmitTestRequest request) {
        Optional<MockTest> mockTestOpt = mockTestRepository.findById(request.getMockTestId());
        if (mockTestOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid Test ID");
        }

        MockTest test = mockTestOpt.get();
        if (test.isCompleted()) {
            return ResponseEntity.badRequest().body("Test already submitted.");
        }

        int totalMarks = 0;
        int marksObtained = 0;
        int correct = 0, incorrect = 0;

        List<UserAnswer> answers = new ArrayList<>();

        for (SubmitTestRequest.AnswerPayload ans : request.getAnswers()) {
            Question question = questionRepository.findById(ans.getQuestionId()).orElse(null);
            if (question == null) continue;

            boolean isCorrect = question.getCorrectAnswer().equalsIgnoreCase(ans.getSelectedAnswer());
            int obtained = isCorrect ? question.getMarks() : 0;

            totalMarks += question.getMarks();
            marksObtained += obtained;

            if (isCorrect) correct++; else incorrect++;

            answers.add(UserAnswer.builder()
                    .mockTestId(test.getId())
                    .questionId(question.getId())
                    .selectedAnswer(ans.getSelectedAnswer())
                    .isCorrect(isCorrect)
                    .obtainedMarks(obtained)
                    .build());
        }

        userAnswerRepository.saveAll(answers);

        test.setCompleted(true);
        mockTestRepository.save(test);

        String level = calculatePerformanceLevel(marksObtained, totalMarks);

        TestResult result = TestResult.builder()
                .mockTestId(test.getId())
                .totalMarks(totalMarks)
                .marksObtained(marksObtained)
                .correctAnswers(correct)
                .incorrectAnswers(incorrect)
                .performanceLevel(level)
                .build();

        testResultRepository.save(result);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/analytics/{userId}")
    public ResponseEntity<?> getUserAnalytics(@PathVariable Long userId) {
        List<MockTest> tests = mockTestRepository.findByUserId(userId);
        List<Long> testIds = tests.stream().map(MockTest::getId).toList();

        List<TestResult> results = testResultRepository.findByMockTestIdIn(testIds);
        List<UserAnswer> allAnswers = userAnswerRepository.findByMockTestIdIn(testIds);

        Map<String, SubjectAnalytics> subjectMap = new HashMap<>();
        for (MockTest test : tests) {
            List<UserAnswer> testAnswers = allAnswers.stream()
                    .filter(ans -> ans.getMockTestId().equals(test.getId()))
                    .toList();

            int total = testAnswers.stream().mapToInt(UserAnswer::getObtainedMarks).sum();
            subjectMap.computeIfAbsent(test.getSubject(), k -> new SubjectAnalytics())
                      .addMarks(total);
        }

        return ResponseEntity.ok(subjectMap);
    }

    private String calculatePerformanceLevel(int marks, int total) {
        if (total == 0) return "NO DATA";
        double percentage = (marks * 100.0) / total;
        if (percentage >= 80) return "EXCELLENT";
        if (percentage >= 50) return "AVERAGE";
        return "POOR";
    }

    @Getter @Setter
    static class SubjectAnalytics {
        private int totalMarks = 0;
        private int testCount = 0;

        public void addMarks(int marks) {
            this.totalMarks += marks;
            this.testCount += 1;
        }

        public double getAvgMarks() {
            return testCount == 0 ? 0 : (totalMarks * 1.0) / testCount;
        }
    }
}
