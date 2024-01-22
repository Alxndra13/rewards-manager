package majestatyczne.bestie.rewardsmanager.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import majestatyczne.bestie.rewardsmanager.dto.QuestionDTO;
import majestatyczne.bestie.rewardsmanager.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping()
    public ResponseEntity<?> getAllByQuizId(@RequestParam int quizId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    questionService.findAllByQuizId(quizId)
                    .stream()
                    .map(QuestionDTO::convertToDTO)
                    .toList());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
