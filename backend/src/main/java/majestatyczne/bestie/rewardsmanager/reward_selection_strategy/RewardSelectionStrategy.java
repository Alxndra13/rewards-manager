package majestatyczne.bestie.rewardsmanager.reward_selection_strategy;

import majestatyczne.bestie.rewardsmanager.model.Preference;
import majestatyczne.bestie.rewardsmanager.model.Result;
import majestatyczne.bestie.rewardsmanager.model.RewardStrategy;

import java.util.List;
import java.util.NoSuchElementException;

public interface RewardSelectionStrategy {

    List<Result> insertRewards(List<Result> results, RewardStrategy rewardStrategy, List<Preference> preferences,
                               int maxScore) throws NoSuchElementException;
}
