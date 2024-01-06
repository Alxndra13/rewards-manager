package majestatyczne.bestie.rewardsmanager.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import majestatyczne.bestie.rewardsmanager.model.Person;
import majestatyczne.bestie.rewardsmanager.model.Quiz;
import majestatyczne.bestie.rewardsmanager.model.Reward;
import majestatyczne.bestie.rewardsmanager.repository.ResultRepository;
import majestatyczne.bestie.rewardsmanager.model.Result;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ResultService {

    private final ResultRepository resultRepository;

    private final PersonService personService;

    private final RewardService rewardService;

    public List<Result> findAllByQuizId(int quizId) {
        return resultRepository.findAllByQuizId(quizId);
    }

    @Transactional
    public void addAll(List<Result> results) {
        results.forEach(personService::updatePersonInResults);
        resultRepository.saveAll(results);
    }

    public Optional<Result> findById(int resultId) {
        return resultRepository.findById(resultId);
    }

    @Transactional
    public boolean update(int resultId, Person person, Date startDate, Date endDate, int score, int rewardId) {
        Optional<Reward> reward = rewardService.findById(rewardId);

        if (reward.isEmpty()) {
            throw new EntityNotFoundException("reward has not been found");
        }

        return findById(resultId)
                .map(result -> update(resultId, result.getQuiz(), person, startDate, endDate, score, reward.get()))
                .orElse(false);
    }

    @Transactional
    public boolean update(int resultId, Quiz quiz, Person person, Date startDate, Date endDate, int score,
                          Reward reward) {

        return findById(resultId)
                .map(result -> {
                    result.setPerson(person);
                    result.setQuiz(quiz);
                    result.setStartDate(startDate);
                    result.setEndDate(endDate);
                    result.setScore(score);
                    result.setReward(reward);

                    resultRepository.save(result);

                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public void deleteAllByIds(List<Integer> resultIds) {
        resultRepository.deleteAllById(resultIds);
    }
}
