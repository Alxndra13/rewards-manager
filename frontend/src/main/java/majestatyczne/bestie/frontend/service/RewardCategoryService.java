package majestatyczne.bestie.frontend.service;

import majestatyczne.bestie.frontend.Constants;
import majestatyczne.bestie.frontend.model.RewardCategory;
import org.apache.http.HttpStatus;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class RewardCategoryService {

    public List<RewardCategory> getRewardCategories() {
        APIService service = getAPIService();
        try {
            return service.getRewardCategories().execute().body();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    public int updateRewardCategories(List<RewardCategory> rewardCategories) {
        APIService service = getAPIService();
        try {
            var response = service.updateRewardCategories(rewardCategories).execute();
            return response.code();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return HttpStatus.SC_INTERNAL_SERVER_ERROR;
        }
    }

    public int addRewardCategory(RewardCategory rewardCategory) {
        APIService service = getAPIService();
        try {
            var response = service.addRewardCategory(rewardCategory).execute();
            return response.code();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return HttpStatus.SC_INTERNAL_SERVER_ERROR;
        }
    }

    public int deleteRewardCategoryById(int rewardCategoryId) {
        APIService service = getAPIService();
        try {
            var response = service.deleteRewardCategoryById(rewardCategoryId).execute();
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
