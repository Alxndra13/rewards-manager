package majestatyczne.bestie.frontend.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import majestatyczne.bestie.frontend.Constants;
import majestatyczne.bestie.frontend.HomePageApplication;
import majestatyczne.bestie.frontend.model.Answer;
import majestatyczne.bestie.frontend.model.view.QuestionView;
import majestatyczne.bestie.frontend.model.view.QuizView;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AnswersPageController implements Initializable {

    @FXML
    private ImageView backIcon;

    @FXML
    private Label questionContentLabel;

    @FXML
    private BarChart<Integer, String> barChart;

    private QuizView quizView;

    private QuestionView questionView;

    private List<Answer> answerList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backIcon.setImage(new Image(String.valueOf(HomePageApplication.class.getResource(Constants.BACK_ICON_RESOURCE))));
    }

    private void initializeBarChart() {
        barChart.setTitle("ROZKŁAD ODPOWIEDZI");
        barChart.getXAxis().setLabel("Liczba osób");
        barChart.getYAxis().setLabel("Odpowiedź");
        XYChart.Series<Integer, String> series = new XYChart.Series<>();

        answerList.forEach(answer ->
                series.getData()
                        .add(new XYChart.Data<>(answer.getSelectionCount(), answer.getContent()))
        );
        barChart.getData().add(series);
        setChartDataStyles();
    }

    private void setChartDataStyles() {
        setCorrectAnswersToGreenColor();
        barChart.getYAxis().setTickLabelFont(new javafx.scene.text.Font(13));
        barChart.getXAxis().setTickLabelFont(new javafx.scene.text.Font(13));
    }

    private void setCorrectAnswersToGreenColor() {
        // this works, because the order of answers in the bar chart is the same as in the answer list
        for (int i = 0; i < answerList.size(); i++) {
            if (answerList.get(i).isCorrect()) {
                barChart.getData().get(0).getData().get(i).getNode().setStyle("-fx-bar-fill: green;");
            } else {
                barChart.getData().get(0).getData().get(i).getNode().setStyle("-fx-bar-fill: #dc3545;");
            }
        }
    }

    public void setData(QuestionView selectedQuestion, List<Answer> answerList, QuizView quizView) {
        this.quizView = quizView;
        this.questionView = selectedQuestion;
        this.answerList = answerList;
        questionContentLabel.setText(questionView.getContent());
        initializeBarChart();
    }

    @FXML
    private void onGoBackClicked() {
        FXMLLoader fxmlLoader = new FXMLLoader(HomePageApplication.class.getResource(Constants.FXML_STATS_PAGE_RESOURCE));
        Stage stage = (Stage) backIcon.getScene().getWindow();
        try {
            Scene scene = new Scene(fxmlLoader.load(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
            StatsPageController statsPageController = fxmlLoader.getController();
            statsPageController.setQuizView(quizView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
