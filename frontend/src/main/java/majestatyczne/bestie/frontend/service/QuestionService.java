package majestatyczne.bestie.frontend.service;

import majestatyczne.bestie.frontend.Constants;
import majestatyczne.bestie.frontend.model.Question;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class QuestionService {

    public List<Question> getQuestionsByQuizId(int quizId) {
        APIService service = getAPIService();
        try {
            return service.getQuestionsByQuizId(quizId).execute().body();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Collections.emptyList();
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
