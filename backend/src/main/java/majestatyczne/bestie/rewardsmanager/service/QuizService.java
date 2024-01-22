package majestatyczne.bestie.rewardsmanager.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import majestatyczne.bestie.rewardsmanager.repository.QuizRepository;
import majestatyczne.bestie.rewardsmanager.model.Quiz;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;

    @Transactional
    public void add(Quiz quiz) {
        quizRepository.save(quiz);
    }

    public List<Quiz> findAll() {
        return quizRepository
                .findAll();
    }

    public Quiz findById(int quizId) {
        return quizRepository
                .findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException("quiz has not been found"));
    }

    @Transactional
    public void deleteById(int quizId) {
        findById(quizId); // throws error if quiz does not exist
        quizRepository.deleteById(quizId);
    }
}
