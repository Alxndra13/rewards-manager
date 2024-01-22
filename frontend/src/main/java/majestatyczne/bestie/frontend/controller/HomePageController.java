package majestatyczne.bestie.frontend.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import majestatyczne.bestie.frontend.Constants;
import majestatyczne.bestie.frontend.HomePageApplication;
import majestatyczne.bestie.frontend.service.FileService;
import majestatyczne.bestie.frontend.service.QuizService;
import majestatyczne.bestie.frontend.model.Quiz;
import majestatyczne.bestie.frontend.model.view.QuizView;
import majestatyczne.bestie.frontend.util.AlertManager;
import majestatyczne.bestie.frontend.util.cell.DeleteButtonCell;
import org.apache.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class HomePageController implements Initializable {

    @FXML
    private TableView<QuizView> quizTable;

    @FXML
    private TableColumn<QuizView, String> nameColumn;

    @FXML
    private TableColumn<QuizView, Date> dateColumn;

    @FXML
    private TableColumn<QuizView, Void> deleteColumn;

    @FXML
    private ImageView settingsIcon;

    private ObservableList<QuizView> quizzes;

    private final FileService fileService = new FileService();

    private final FileChooser fileChooser = new FileChooser();

    @FXML
    public void onImportFile() {
        File file = fileChooser.showOpenDialog(new Stage());
        if (file == null) {
            return;
        }
        new Thread(() -> {
            int statusCode = fileService.uploadFile(file);
            Platform.runLater(() -> {
                switch (statusCode) {
                    case HttpStatus.SC_OK, HttpStatus.SC_ACCEPTED ->
                            AlertManager.showConfirmationAlert(Constants.FILE_UPLOAD_ACCEPTED_TITLE);
                    default -> AlertManager.showErrorAlert(statusCode, Constants.FILE_UPLOAD_ERROR_TITLE);
                }
                setData();
            });
        }).start();

    }

    @FXML
    public void onQuizSelected() {
        try {
            QuizView selectedQuiz = quizTable.getSelectionModel().getSelectedItem();
            moveToQuizPage(selectedQuiz);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    public void moveToQuizPage(QuizView quizView) {
        Stage stage = (Stage) quizTable.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HomePageApplication.class.getResource(Constants.FXML_QUIZ_PAGE_RESOURCE));
        try {
            Scene scene = new Scene(fxmlLoader.load(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
            QuizPageController quizPageController = fxmlLoader.getController();
            quizPageController.setQuizView(quizView);
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void onSettingsClicked() {
        Stage stage = (Stage) quizTable.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HomePageApplication.class.getResource(Constants.FXML_GLOBAL_SETTINGS_RESOURCE));
        try {
            Scene scene = new Scene(fxmlLoader.load(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeDefaultUploadDirectory();
        nameColumn.setCellValueFactory(nameValue -> nameValue.getValue().getNameProperty());
        dateColumn.setCellValueFactory(dateValue -> dateValue.getValue().getDateProperty());
        deleteColumn.setCellFactory(param -> new DeleteButtonCell<>(this::onDeleteButtonClicked));
        setData();
        settingsIcon.setImage(new Image(String.valueOf(HomePageApplication.class.getResource(Constants.SETTINGS_ICON_RESOURCE))));
    }

    private void onDeleteButtonClicked(QuizView quizView) {
        QuizService quizService = new QuizService();
        int responseCode = quizService.deleteQuizById(quizView.getId());
        switch (responseCode) {
            case HttpStatus.SC_OK, HttpStatus.SC_ACCEPTED ->
                    AlertManager.showConfirmationAlert(Constants.DELETE_QUIZ_INFO);
            default -> AlertManager.showErrorAlert(responseCode, Constants.DELETE_QUIZ_ERROR);
        }
        setData();
    }

    private void initializeQuizzes() {
        QuizService quizService = new QuizService();
        quizzes = FXCollections.observableArrayList();
        List<Quiz> quizList = quizService.getQuizzes();
        quizList.forEach(quiz -> quizzes.add(new QuizView(quiz.getId(), quiz.getName(), quiz.getMaxScore(), quiz.getDate())));
    }

    private void initializeDefaultUploadDirectory() {
        String home = System.getProperty("user.home");
        fileChooser.setInitialDirectory(new File(home + Constants.DEFAULT_UPLOAD_DIRECTORY));
    }

    public void setData() {
        initializeQuizzes();
        quizTable.setItems(quizzes);
    }
}
