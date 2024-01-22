package majestatyczne.bestie.rewardsmanager.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import majestatyczne.bestie.rewardsmanager.model.Quiz;
import majestatyczne.bestie.rewardsmanager.model.RewardStrategy;
import majestatyczne.bestie.rewardsmanager.model.RewardStrategyParameter;
import majestatyczne.bestie.rewardsmanager.repository.RewardStrategyRepository;
import majestatyczne.bestie.rewardsmanager.reward_selection_strategy.RewardSelectionStrategy;
import majestatyczne.bestie.rewardsmanager.reward_selection_strategy.RewardStrategyType;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor(onConstructor_ ={@Lazy})
public class RewardStrategyService {

    private final RewardStrategyRepository rewardStrategyRepository;

    private final RewardStrategyParameterService rewardStrategyParameterService;

    @Lazy
    private final QuizService quizService;

    @Transactional
    public RewardStrategy add(int quizId, RewardStrategyType rewardStrategyType) {
        Quiz quiz = quizService.findById(quizId);

        try {
            findByQuizId(quizId);
            throw new IllegalStateException("strategy for the given quiz already exists");
        } catch (EntityNotFoundException ignored) { }

        RewardStrategy rewardStrategy = new RewardStrategy();
        rewardStrategy.setQuiz(quiz);
        rewardStrategy.setRewardStrategyType(rewardStrategyType);
        rewardStrategy.setParameters(new ArrayList<>());

        rewardStrategyRepository.save(rewardStrategy);

        return rewardStrategy;
    }

    @Transactional
    public RewardStrategy update(int quizId, RewardStrategyType rewardStrategyType) {
        RewardStrategy rewardStrategy = findByQuizId(quizId);

        Quiz quiz = quizService.findById(quizId);

        rewardStrategy.setRewardStrategyType(rewardStrategyType);
        rewardStrategy.setQuiz(quiz);

        rewardStrategyRepository.saveAndFlush(rewardStrategy);

        return rewardStrategy;
    }

    public RewardStrategy findByQuizId(int quizId) {
        return rewardStrategyRepository
                .findByQuizId(quizId)
                .orElseThrow(() -> new EntityNotFoundException("reward selection strategy has not been found for the" +
                        " given quiz"));
    }

    @Transactional
    public void deleteAllByIds(List<Integer> rewardStrategiesIds) {
        rewardStrategyParameterService.deleteAllByRewardStrategyIds(rewardStrategiesIds);
        rewardStrategyRepository.deleteAllById(rewardStrategiesIds);
    }

    @Transactional
    public void insertRewards(RewardStrategy rewardStrategy) throws NoSuchElementException {
        RewardSelectionStrategy rewardSelectionStrategy = RewardStrategyType.getRewardSelectionStrategy(rewardStrategy);
        rewardSelectionStrategy.insertRewards(rewardStrategy.getQuiz().getResults(), rewardStrategy, null,
                rewardStrategy.getQuiz().getMaxScore());
    }

    @Transactional
    public void addParametersToStrategy(RewardStrategy rewardStrategy,
                                        List<RewardStrategyParameter> rewardStrategyParameters) {
        rewardStrategy.getParameters().addAll(rewardStrategyParameters);
        rewardStrategyRepository.saveAndFlush(rewardStrategy);
    }
}
