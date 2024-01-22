package majestatyczne.bestie.frontend.service;

import majestatyczne.bestie.frontend.Constants;
import majestatyczne.bestie.frontend.model.Reward;
import org.apache.http.HttpStatus;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class RewardService {

    public List<Reward> getRewards() {
        APIService service = getAPIService();
        try {
            return service.getRewards().execute().body();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    public int updateReward(Reward reward) {
        APIService service = getAPIService();
        try {
            var response = service.updateReward(reward).execute();
            return response.code();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return HttpStatus.SC_INTERNAL_SERVER_ERROR;
        }
    }

    public int updateRewards(List<Reward> rewards) {
        APIService service = getAPIService();
        try {
            var response = service.updateRewards(rewards).execute();
            return response.code();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return HttpStatus.SC_INTERNAL_SERVER_ERROR;
        }
    }

    public int addReward(Reward reward) {
        APIService service = getAPIService();
        try {
            var response = service.addReward(reward).execute();
            return response.code();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return HttpStatus.SC_INTERNAL_SERVER_ERROR;
        }
    }

    public int deleteRewardById(int rewardId) {
        APIService service = getAPIService();
        try {
            var response = service.deleteRewardById(rewardId).execute();
            return response.code();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return HttpStatus.SC_INTERNAL_SERVER_ERROR;
        }
    }

    private APIService getAPIService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(APIService.class);
    }
}
