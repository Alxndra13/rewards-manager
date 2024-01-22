package majestatyczne.bestie.frontend.service;

import majestatyczne.bestie.frontend.model.*;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface APIService {

    @GET("quizzes/{quizId}")
    Call<Quiz> getQuizById(@Path("quizId") int quizId);

    @GET("quizzes")
    Call<List<Quiz>> getQuizzes();

    @DELETE("quizzes/{quizId}")
    Call<Void> deleteQuizById(@Path("quizId") int quizId);

    @GET("results")
    Call<List<Result>> getResults(@Query("quizId") int quizId);

    @PUT("results")
    Call<Void> updateResult(@Body Result result);

    @GET("reward-categories")
    Call<List<RewardCategory>> getRewardCategories();

    @PUT("reward-categories")
    Call<Void> updateRewardCategory(@Body RewardCategory rewardCategory);

    @PUT("reward-categories/all")
    Call<Void> updateRewardCategories(@Body List<RewardCategory> rewardCategories);

    @POST("reward-categories")
    Call<Void> addRewardCategory(@Body RewardCategory rewardCategory);

    @DELETE("reward-categories/{rewardCategoryId}")
    Call<Void> deleteRewardCategoryById(@Path("rewardCategoryId") int rewardCategoryId);

    @GET("rewards")
    Call<List<Reward>> getRewards();

    @PUT("rewards")
    Call<Void> updateReward(@Body Reward reward);

    @PUT("rewards/all")
    Call<Void> updateRewards(@Body List<Reward> rewards);

    @POST("rewards")
    Call<Void> addReward(@Body Reward reward);

    @DELETE("rewards/{rewardId}")
    Call<Void> deleteRewardById(@Path("rewardId") int rewardId);

    @GET("reward-strategies/{quizId}")
    Call<RewardStrategy> getRewardStrategyByQuizId(@Path("quizId") int quizId);

    @POST("reward-strategies")
    Call<Void> addRewardStrategy(@Body RewardStrategy rewardStrategy);

    @PUT("reward-strategies")
    Call<Void> updateRewardStrategy(@Body RewardStrategy rewardStrategy);

    @GET("files")
    @Streaming
    Call<ResponseBody> exportResultsFile(@Query("quizId") int quizId, @Query("format") String format);

    @GET("questions")
    Call<List<Question>> getQuestionsByQuizId(@Query("quizId") int quizId);

}
