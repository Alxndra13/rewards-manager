package majestatyczne.bestie.rewardsmanager.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import majestatyczne.bestie.rewardsmanager.model.Answer;
import majestatyczne.bestie.rewardsmanager.repository.AnswerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    @Transactional
    public void addAll(List<Answer> answers) {
        answerRepository.saveAll(answers);
    }
}
