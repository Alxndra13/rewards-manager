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

@Service
@AllArgsConstructor
public class ResultService {

    private final ResultRepository resultRepository;

    private final PersonService personService;

    private final RewardService rewardService;

    public List<Result> findAllByQuizId(int quizId) {
        return resultRepository.findAllByQuizIdOrderByScoreDescEndDateAsc(quizId);
    }

    @Transactional
    public void addAll(List<Result> results) {
        results.forEach(personService::updatePersonInResults);
        resultRepository.saveAll(results);
    }

    public Result findById(int resultId) {
        return resultRepository
                .findById(resultId)
                .orElseThrow(() -> new EntityNotFoundException("result has not been found"));
    }

    @Transactional
    public void update(int resultId, Person person, Date startDate, Date endDate, int score, int rewardId) {
        Result result = findById(resultId);
        Reward reward = rewardId == -1 ? null : rewardService.findById(rewardId);
        update(result, result.getQuiz(), person, startDate, endDate, score, reward);
    }

    @Transactional
    public void update(Result result, Quiz quiz, Person person, Date startDate, Date endDate, int score,
                          Reward reward) {
        result.setPerson(person);
        result.setQuiz(quiz);
        result.setStartDate(startDate);
        result.setEndDate(endDate);
        result.setScore(score);
        result.setReward(reward);
    }

    @Transactional
    public void deleteAllByIds(List<Integer> resultIds) {
        resultRepository.deleteAllById(resultIds);
    }
}
