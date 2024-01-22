package majestatyczne.bestie.frontend.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import majestatyczne.bestie.frontend.Constants;
import majestatyczne.bestie.frontend.model.Result;
import org.apache.http.HttpStatus;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class QuizResultsService {
    public List<Result> getResults(int quizId) {
        APIService service = getAPIService();
        try {
            return service.getResults(quizId).execute().body();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    public int updateResult(Result result) {
        APIService service = getAPIService();
        try {
            return service.updateResult(result).execute().code();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return HttpStatus.SC_INTERNAL_SERVER_ERROR;
        }
    }

    private APIService getAPIService() {
        Gson gson = new GsonBuilder().setDateFormat(Constants.JSON_DATE_FORMAT).create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(APIService.class);
    }
}
