package majestatyczne.bestie.frontend.service;

import majestatyczne.bestie.frontend.Constants;
import majestatyczne.bestie.frontend.model.Quiz;
import org.apache.http.HttpStatus;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class QuizService {

    public Optional<Quiz> getQuizById(int quizId) {
        APIService service = getAPIService();
        try {
            return Optional.ofNullable(service.getQuizById(quizId).execute().body());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public List<Quiz> getQuizzes() {
        APIService service = getAPIService();
        try {
            return service.getQuizzes().execute().body();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    public int deleteQuizById(int quizId) {
        APIService service = getAPIService();
        try {
            var response = service.deleteQuizById(quizId).execute();
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
