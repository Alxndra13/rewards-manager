package majestatyczne.bestie.rewardsmanager.reward_selection_strategy;

import majestatyczne.bestie.rewardsmanager.model.*;

import java.util.Comparator;
import java.util.List;

public class ScoreStrategy implements RewardSelectionStrategy {

    @Override
    public List<Result> insertRewards(List<Result> results, RewardStrategy rewardStrategy, List<Preference> preferences,
                                      int maxScore) {
        List<RewardStrategyParameter> rewardStrategyParameters =  rewardStrategy.getParameters();

        validateParameters(rewardStrategyParameters);

        for (Result result : results) {
            int score = result.getScore();

            RewardCategory rewardCategory =
                    rewardStrategyParameters
                    .stream()
                    .filter(parameter -> parameter.getParameterValue() <= score)
                    .max(Comparator.comparingInt(RewardStrategyParameter::getParameterValue))
                    .orElseThrow()
                    .getRewardCategory();

            Reward reward = rewardCategory == null ? null : rewardCategory.getRewards().get(0);

            result.setReward(reward);
        }

        return results;
    }

    private void validateParameters(List<RewardStrategyParameter> rewardStrategyParameters) {
        boolean areValuesUnique =
                rewardStrategyParameters
                .stream()
                .map(RewardStrategyParameter::getParameterValue)
                .distinct()
                .count() == rewardStrategyParameters.size();

        if (!areValuesUnique) {
            throw new IllegalArgumentException("ambiguous parameters. Parameter value in score strategy must be unique");
        }
    }
}
