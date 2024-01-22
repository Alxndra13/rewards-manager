package majestatyczne.bestie.rewardsmanager.reward_selection_strategy;

import lombok.AllArgsConstructor;
import majestatyczne.bestie.rewardsmanager.model.*;

import java.util.List;

@AllArgsConstructor
public class PercentageStrategy implements RewardSelectionStrategy {

    @Override
    public List<Result> insertRewards(List<Result> results, RewardStrategy rewardStrategy, List<Preference> preferences,
                                      int maxScore) {
        List<RewardStrategyParameter> rewardStrategyParameters =  rewardStrategy.getParameters();

        int resultIndex = 0;
        for (RewardStrategyParameter parameter: rewardStrategyParameters) {
            int quantity = parameter.getParameterValue() * results.size() / 100;

            RewardCategory rewardCategory = parameter.getRewardCategory();

            Reward reward = rewardCategory == null ? null : rewardCategory
                    .getRewards()
                    .stream()
                    .filter(r -> r.getRewardCategory().equals(parameter.getRewardCategory()))
                    .findFirst()
                    .orElseThrow();

            for (int i = 0; i < quantity; i++) {
                Result result = results.get(resultIndex);

                if (result.getScore() != maxScore) {
                    result.setReward(null);
                } else {
                    result.setReward(reward);
                }

                resultIndex++;
            }
        }

        while (resultIndex < results.size()) {
            results.get(resultIndex).setReward(null);
            resultIndex++;
        }

        return results;
    }
}
