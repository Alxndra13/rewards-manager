package majestatyczne.bestie.frontend.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import majestatyczne.bestie.frontend.Constants;
import majestatyczne.bestie.frontend.HomePageApplication;
import majestatyczne.bestie.frontend.model.*;
import majestatyczne.bestie.frontend.model.view.QuizView;
import majestatyczne.bestie.frontend.model.view.ResultView;
import majestatyczne.bestie.frontend.model.view.RewardView;
import majestatyczne.bestie.frontend.service.QuizResultsService;
import majestatyczne.bestie.frontend.service.RewardService;
import majestatyczne.bestie.frontend.util.cell.RewardChoiceCell;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class QuizPageController implements Initializable {

    private QuizView quizView;

    private ObservableList<ResultView> results;

    @FXML
    private TableView<ResultView> resultTable;

    @FXML
    private TableColumn<ResultView, String> nameColumn;

    @FXML
    private TableColumn<ResultView, Integer> scoreColumn;

    @FXML
    private TableColumn<ResultView, Date> endDateColumn;

    @FXML
    private TableColumn<ResultView, String> rewardColumn;

    @FXML
    private Label quizNameLabel;

    @FXML
    private Label quizDateLabel;

    @FXML
    private ImageView settingsIcon;

    @FXML
    private ImageView backIcon;

    private List<Result> resultList;

    private List<Reward> rewardList;

    private ObservableList<RewardView> rewards;

    private final RewardService rewardService = new RewardService();

    private final QuizResultsService resultService = new QuizResultsService();

    public void setQuizView(QuizView quizView) {
        this.quizView = quizView;
        setData();
    }

    private void initializeResults() {
        results = FXCollections.observableArrayList();
        var quizResultsService = new QuizResultsService();
        resultList = quizResultsService.getResults(quizView.getId());
        resultList.forEach(result -> results.add(new ResultView(result)));
    }

    private void initializeRewards() {
        rewardList = rewardService.getRewards();
        rewards = FXCollections.observableArrayList();
        rewardList.forEach(reward -> rewards.add(new RewardView(reward)));

        RewardView noReward = new RewardView(-1, null, Constants.REWARD_CHOICE_BOX_NO_REWARD, null);
        rewards.add(noReward);
    }

    private void setData() {
        initializeResults();
        initializeRewards();
        quizNameLabel.setText(quizView.getName());
        quizDateLabel.setText(quizView.getDate().toString());
        resultTable.setItems(results);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(nameValue -> nameValue.getValue().getPersonNameProperty());
        scoreColumn.setCellValueFactory(scoreValue -> scoreValue.getValue().getScoreProperty());
        endDateColumn.setCellValueFactory(dateValue -> dateValue.getValue().getEndDateProperty());
        rewardColumn.setCellValueFactory(rewardValue -> rewardValue.getValue().getRewardProperty());
        rewardColumn.setCellFactory(param -> new RewardChoiceCell(rewards, this::onChosenReward));
        settingsIcon.setImage(new Image(String.valueOf(HomePageApplication.class.getResource(Constants.SETTINGS_ICON_RESOURCE))));
        backIcon.setImage(new Image(String.valueOf(HomePageApplication.class.getResource(Constants.BACK_ICON_RESOURCE))));
    }

    private void onChosenReward(ResultView resultView, RewardView selectedReward) {
        if (selectedReward == null) {
            return;
        }
        Reward reward = rewardList.stream()
                .filter(x -> x.getId() == selectedReward.getId())
                .findFirst()
                .orElse(null);
        if (reward == null) {
            resultView.setReward(Constants.REWARD_CHOICE_BOX_NO_REWARD);
        } else {
            resultView.setReward(reward.getName());
        }

        Result resultToUpdate = resultList.stream()
                .filter(x -> x.getId() == resultView.getId())
                .findFirst()
                .orElse(null);
        resultToUpdate.setReward(reward);
        resultService.updateResult(resultToUpdate);
    }

    public void onGoBackClicked() {
        FXMLLoader fxmlLoader = new FXMLLoader(HomePageApplication.class.getResource(Constants.FXML_HOME_PAGE_RESOURCE));
        Stage stage = (Stage) resultTable.getScene().getWindow();
        try {
            Scene scene = new Scene(fxmlLoader.load(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void onSettingsClicked() {
        FXMLLoader fxmlLoader = new FXMLLoader(HomePageApplication.class.getResource(Constants.FXML_QUIZ_SETTINGS_RESOURCE));
        Stage stage = (Stage) resultTable.getScene().getWindow();
        try {
            Scene scene = new Scene(fxmlLoader.load(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
            QuizSettingsPageController quizSettingsPageController = fxmlLoader.getController();
            quizSettingsPageController.setQuizView(quizView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void onExportClicked() {
        FXMLLoader fxmlLoader = new FXMLLoader(HomePageApplication.class.getResource(Constants.FXML_EXPORT_POPUP_RESOURCE));
        try {
            Parent root = fxmlLoader.load();
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle(Constants.EXPORT_POPUP_WINDOW_TITLE);
            popupStage.setScene(new Scene(root));
            ExportPopupController popupController = fxmlLoader.getController();
            popupController.setData(popupStage, quizView.getId());
            popupStage.showAndWait();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void onStatsClicked() {
        FXMLLoader fxmlLoader = new FXMLLoader(HomePageApplication.class.getResource(Constants.FXML_STATS_PAGE_RESOURCE));
        Stage stage = (Stage) resultTable.getScene().getWindow();
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
