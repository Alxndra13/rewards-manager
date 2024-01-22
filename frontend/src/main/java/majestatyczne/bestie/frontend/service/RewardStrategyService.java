package majestatyczne.bestie.frontend.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import majestatyczne.bestie.frontend.Constants;
import majestatyczne.bestie.frontend.model.RewardStrategy;
import org.apache.http.HttpStatus;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Optional;

public class RewardStrategyService {
    public Optional<RewardStrategy> getRewardStrategyByQuizId(int quizId) {
        APIService service = getAPIService();
        try {
            return Optional.ofNullable(service.getRewardStrategyByQuizId(quizId).execute().body());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public int addRewardStrategy(RewardStrategy rewardStrategy) {
        APIService service = getAPIService();
        try {
            var response = service.addRewardStrategy(rewardStrategy).execute();
            return response.code();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return HttpStatus.SC_INTERNAL_SERVER_ERROR;
        }
    }

    public int updateRewardStrategy(RewardStrategy rewardStrategy) {
        APIService service = getAPIService();
        try {
            var response = service.updateRewardStrategy(rewardStrategy).execute();
            return response.code();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return HttpStatus.SC_INTERNAL_SERVER_ERROR;
        }
    }

    private APIService getAPIService() {
        Gson gson = new GsonBuilder()
                .setDateFormat(Constants.JSON_DATE_FORMAT)
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(APIService.class);
    }
}
