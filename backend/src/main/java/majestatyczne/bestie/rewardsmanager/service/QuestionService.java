package majestatyczne.bestie.rewardsmanager.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import majestatyczne.bestie.rewardsmanager.model.Question;
import majestatyczne.bestie.rewardsmanager.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<Question> findAllByQuizId(int quizId) {
        return questionRepository.findAllByQuizId(quizId);
    }

    @Transactional
    public void addAll(List<Question> questions) {
        questionRepository.saveAll(questions);
    }
}
