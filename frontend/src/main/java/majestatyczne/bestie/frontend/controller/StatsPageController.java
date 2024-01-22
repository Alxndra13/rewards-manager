package majestatyczne.bestie.frontend.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import majestatyczne.bestie.frontend.Constants;
import majestatyczne.bestie.frontend.HomePageApplication;
import majestatyczne.bestie.frontend.model.Answer;
import majestatyczne.bestie.frontend.model.Question;
import majestatyczne.bestie.frontend.model.view.QuestionView;
import majestatyczne.bestie.frontend.model.view.QuizView;
import majestatyczne.bestie.frontend.service.QuestionService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StatsPageController implements Initializable {

    @FXML
    private Label quizNameLabel;

    @FXML
    private Label quizDateLabel;

    @FXML
    private ImageView backIcon;

    @FXML
    private TableView<QuestionView> questionTable;

    @FXML
    private TableColumn<QuestionView, String> contentColumn;

    @FXML
    private TableColumn<QuestionView, String> scoreColumn;

    private QuizView quizView;

    private List<Question> questionList;

    private ObservableList<QuestionView> questions;

    private final QuestionService questionService = new QuestionService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backIcon.setImage(new Image(String.valueOf(HomePageApplication.class.getResource(Constants.BACK_ICON_RESOURCE))));
        initializeQuestionsTable();
    }

    public void setQuizView(QuizView quizView) {
        this.quizView = quizView;
        setData();
    }

    private void setData() {
        quizNameLabel.setText(quizView.getName());
        quizDateLabel.setText(quizView.getDate().toString());
        initializeQuestions();
        questionTable.setItems(questions);
    }

    private void initializeQuestions() {
        questionList = questionService.getQuestionsByQuizId(quizView.getId());
        questions = FXCollections.observableArrayList();
        questionList.forEach(question -> questions.add(new QuestionView(question)));
    }

    private void initializeQuestionsTable() {
        contentColumn.setCellValueFactory(value -> value.getValue().getContentProperty());
        scoreColumn.setCellValueFactory(value -> value.getValue().getScoreProperty());
    }

    @FXML
    private void onGoBackClicked() {
        FXMLLoader fxmlLoader = new FXMLLoader(HomePageApplication.class.getResource(Constants.FXML_QUIZ_PAGE_RESOURCE));
        Stage stage = (Stage) backIcon.getScene().getWindow();
        try {
            Scene scene = new Scene(fxmlLoader.load(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
            QuizPageController quizPageController = fxmlLoader.getController();
            quizPageController.setQuizView(quizView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void onQuestionSelected() {
        try {
            QuestionView selectedQuestion = questionTable.getSelectionModel().getSelectedItem();
            moveToAnswersPage(selectedQuestion);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    private void moveToAnswersPage(QuestionView selectedQuestion) {
        Stage stage = (Stage) questionTable.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HomePageApplication.class.getResource(Constants.FXML_ANSWERS_PAGE_RESOURCE));
        try {
            Scene scene = new Scene(fxmlLoader.load(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
            AnswersPageController answersPageController = fxmlLoader.getController();
            answersPageController.setData(selectedQuestion, getAnswerList(selectedQuestion), quizView);
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private List<Answer> getAnswerList(QuestionView selectedQuestion) {
        return questionList.stream()
                .filter(x -> x.getId() == selectedQuestion.getId())
                .findFirst()
                .map(Question::getAnswers).orElse(null);
    }
}
