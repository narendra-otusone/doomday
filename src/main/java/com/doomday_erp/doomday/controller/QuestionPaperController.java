package com.doomday_erp.doomday.controller;

import com.doomday_erp.doomday.entity.Question;
import com.doomday_erp.doomday.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionPaperController {

    private final QuestionRepository questionRepository;

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
}
