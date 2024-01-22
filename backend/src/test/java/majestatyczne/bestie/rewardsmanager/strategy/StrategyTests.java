package majestatyczne.bestie.rewardsmanager.strategy;

import majestatyczne.bestie.rewardsmanager.model.*;
import majestatyczne.bestie.rewardsmanager.reward_selection_strategy.PercentageStrategy;
import majestatyczne.bestie.rewardsmanager.reward_selection_strategy.ScoreStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static majestatyczne.bestie.rewardsmanager.reward_selection_strategy.RewardStrategyType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)

public class StrategyTests {

    RewardStrategy rewardStrategy;

    RewardCategory firstCategory;

    RewardCategory secondCategory;

    List<Result> results;

    @Test
    public void shouldReturnPercentageStrategy() {
        rewardStrategy = mock(RewardStrategy.class);
        when(rewardStrategy.getRewardStrategyType()).thenReturn(PERCENTAGE);
        assertEquals(PercentageStrategy.class, getRewardSelectionStrategy(rewardStrategy).getClass());
    }

    @Test
    public void shouldReturnScoreStrategy() {
        rewardStrategy = mock(RewardStrategy.class);
        when(rewardStrategy.getRewardStrategyType()).thenReturn(SCORE);
        assertEquals(ScoreStrategy.class, getRewardSelectionStrategy(rewardStrategy).getClass());
    }

    @Test
    public void percentageStrategyShouldProperlyInsertRewards() {
        //given
        results = new ArrayList<>();
        var expectedResults = new ArrayList<Reward>();

        rewardStrategy = mock(RewardStrategy.class);

        firstCategory = mock(RewardCategory.class);
        Reward firstReward = mock(Reward.class);

        secondCategory = mock(RewardCategory.class);
        Reward secondReward = mock(Reward.class);

        RewardStrategyParameter firstParameter = mock(RewardStrategyParameter.class);
        RewardStrategyParameter secondParameter = mock(RewardStrategyParameter.class);

        Result result1 = new Result();
        result1.setScore(2);
        result1.setEndDate((new Date()));
        results.add(result1);

        Result result2 = new Result();
        result2.setScore(1);
        result2.setEndDate((new Date()));
        results.add(result2);

        Result result3 = new Result();
        result3.setScore(2);
        result3.setEndDate((new Date()));
        results.add(result3);

        Result result4 = new Result();
        result4.setScore(0);
        result4.setEndDate((new Date()));
        results.add(result4);

        expectedResults.add(firstReward);
        expectedResults.add(null);
        expectedResults.add(secondReward);
        expectedResults.add(null);

        //when
        whenMethodsForPercentage(firstReward, secondReward, firstParameter, secondParameter);

        //then
        assertEquals(expectedResults,
                getRewardSelectionStrategy(rewardStrategy).insertRewards(results, rewardStrategy, null, 2)
                        .stream().map(Result::getReward).toList());
    }

    private void whenMethodsForPercentage(Reward firstReward, Reward secondReward, RewardStrategyParameter firstParameter, RewardStrategyParameter secondParameter) {
        when(firstReward.getRewardCategory()).thenReturn(firstCategory);
        when(firstCategory.getRewards()).thenReturn(List.of(firstReward));

        when(secondReward.getRewardCategory()).thenReturn(secondCategory);
        when(secondCategory.getRewards()).thenReturn(List.of(secondReward));

        when(firstParameter.getParameterValue()).thenReturn(30);
        when(secondParameter.getParameterValue()).thenReturn(70);

        when(firstParameter.getRewardCategory()).thenReturn(firstCategory);
        when(secondParameter.getRewardCategory()).thenReturn(secondCategory);

        when(rewardStrategy.getRewardStrategyType()).thenReturn(PERCENTAGE);
        when(rewardStrategy.getParameters()).thenReturn(List.of(
                firstParameter,
                secondParameter
        ));
    }

    @Test
    public void scoreStrategyShouldProperlyInsertRewards() {
        //given
        results = new ArrayList<>();
        var expectedResults = new ArrayList<Reward>();

        rewardStrategy = mock(RewardStrategy.class);

        firstCategory = mock(RewardCategory.class);
        Reward firstReward = mock(Reward.class);

        secondCategory = mock(RewardCategory.class);
        Reward secondReward = mock(Reward.class);

        RewardStrategyParameter firstParameter = mock(RewardStrategyParameter.class);
        RewardStrategyParameter secondParameter = mock(RewardStrategyParameter.class);


        // The results have to be sorted by score in descending order
        Result result3 = new Result();
        result3.setScore(2);
        results.add(result3);

        Result result2 = new Result();
        result2.setScore(1);
        results.add(result2);

        Result result1 = new Result();
        result1.setScore(0);
        results.add(result1);

        Result result4 = new Result();
        result4.setScore(0);
        results.add(result4);

        expectedResults.add(firstReward);
        expectedResults.add(secondReward);
        expectedResults.add(secondReward);
        expectedResults.add(secondReward);

        //when
        whenMethodsForScore(firstReward, secondReward, firstParameter, secondParameter);

        //then

        assertEquals(expectedResults,
                getRewardSelectionStrategy(rewardStrategy).insertRewards(results, rewardStrategy, null, 2)
                        .stream().map(Result::getReward).toList());
    }

    private void whenMethodsForScore(Reward firstReward, Reward secondReward, RewardStrategyParameter firstParameter, RewardStrategyParameter secondParameter) {
        when(firstReward.getRewardCategory()).thenReturn(firstCategory);
        when(firstCategory.getRewards()).thenReturn(List.of(firstReward));

        when(secondReward.getRewardCategory()).thenReturn(secondCategory);
        when(secondCategory.getRewards()).thenReturn(List.of(secondReward));

        when(firstParameter.getParameterValue()).thenReturn(2);
        when(secondParameter.getParameterValue()).thenReturn(0);

        when(firstParameter.getRewardCategory()).thenReturn(firstCategory);
        when(secondParameter.getRewardCategory()).thenReturn(secondCategory);

        when(rewardStrategy.getRewardStrategyType()).thenReturn(SCORE);
        when(rewardStrategy.getParameters()).thenReturn(List.of(
                firstParameter,
                secondParameter
        ));
    }
}
