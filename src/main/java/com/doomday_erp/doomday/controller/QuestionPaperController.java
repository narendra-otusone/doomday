package com.doomday_erp.doomday.controller;

import com.doomday_erp.doomday.dto.AutoGeneratePaperRequest;
import com.doomday_erp.doomday.dto.QuestionPaperRequest;
import com.doomday_erp.doomday.entity.Question;
import com.doomday_erp.doomday.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.criteria.*;

import java.util.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionPaperController {

    private final QuestionRepository questionRepository;

    // 🔍 Filter questions (general purpose)
    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<Question>> filterQuestions(
            @RequestParam(required = false) String className,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String chapter,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer marks,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        Specification<Question> spec = Specification.where(null);

        if (className != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("className"), className));
        if (subject != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("subject"), subject));
        if (chapter != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("chapter"), chapter));
        if (difficulty != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("difficulty"), difficulty));
        if (type != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("type"), type));
        if (marks != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("marks"), marks));

        List<Question> questions = questionRepository.findAll(spec, PageRequest.of(page, size)).getContent();
        return ResponseEntity.ok(questions);
    }

    // 🎯 Generate paper by selected questions
    @PostMapping("/generate-paper")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<?> generateQuestionPaper(@RequestBody QuestionPaperRequest request) {
        List<Question> questions = request.getQuestionIds().stream()
                .map(questionRepository::findById)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        if (questions.isEmpty()) {
            return ResponseEntity.badRequest().body("No valid questions found for the given IDs");
        }

        int totalMarks = questions.stream().mapToInt(Question::getMarks).sum();

        return ResponseEntity.ok(Map.of(
                "selectedQuestions", questions,
                "totalMarks", totalMarks
        ));
    }

    // ⚙️ Auto-generate paper based on rules
    @PostMapping("/auto-generate")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<?> autoGeneratePaper(@RequestBody AutoGeneratePaperRequest request) {
        int totalMarksNeeded = request.getTotalMarks();

        int percentSum = request.getDifficultyDistribution().values().stream().mapToInt(Integer::intValue).sum();
        if (percentSum != 100) {
            return ResponseEntity.badRequest().body("Difficulty distribution must add up to 100%");
        }

        List<Question> allFilteredQuestions = questionRepository.findAll(getSpecFromRequest(request));
        if (allFilteredQuestions.isEmpty()) {
            return ResponseEntity.badRequest().body("No questions found for given filters.");
        }

        Map<String, List<Question>> byDifficulty = allFilteredQuestions.stream()
                .collect(Collectors.groupingBy(q ->
                        Optional.ofNullable(q.getDifficulty()).orElse("UNKNOWN").toUpperCase()
                ));

        List<Question> selected = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : request.getDifficultyDistribution().entrySet()) {
            String level = entry.getKey().toUpperCase();
            int percent = entry.getValue();

            int levelMarksTarget = (totalMarksNeeded * percent) / 100;
            int currentLevelMarks = 0;

            List<Question> pool = new ArrayList<>(byDifficulty.getOrDefault(level, Collections.emptyList()));
            Collections.shuffle(pool); // randomize

            for (Question q : pool) {
                if (currentLevelMarks + q.getMarks() <= levelMarksTarget) {
                    selected.add(q);
                    currentLevelMarks += q.getMarks();
                }
                if (currentLevelMarks >= levelMarksTarget) break;
            }
        }

        int achievedMarks = selected.stream().mapToInt(Question::getMarks).sum();

        return ResponseEntity.ok(Map.of(
                "selectedQuestions", selected,
                "totalMarksAchieved", achievedMarks,
                "goalMarks", totalMarksNeeded
        ));
    }

    // 🔍 Private reusable filter method
    private Specification<Question> getSpecFromRequest(AutoGeneratePaperRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getClassName() != null)
                predicates.add(cb.equal(root.get("className"), request.getClassName()));

            if (request.getSubjects() != null && !request.getSubjects().isEmpty())
                predicates.add(root.get("subject").in(request.getSubjects()));

            if (request.getChapters() != null && !request.getChapters().isEmpty())
                predicates.add(root.get("chapter").in(request.getChapters()));

            if (request.getTypes() != null && !request.getTypes().isEmpty())
                predicates.add(root.get("type").in(request.getTypes()));

            predicates.add(cb.isTrue(root.get("isActive")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
