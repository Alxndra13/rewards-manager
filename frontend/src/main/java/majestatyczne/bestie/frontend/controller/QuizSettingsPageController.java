package majestatyczne.bestie.frontend.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import majestatyczne.bestie.frontend.Constants;
import majestatyczne.bestie.frontend.HomePageApplication;
import majestatyczne.bestie.frontend.model.*;
import majestatyczne.bestie.frontend.model.view.QuizView;
import majestatyczne.bestie.frontend.model.view.RewardCategoryView;
import majestatyczne.bestie.frontend.model.view.RewardStrategyParameterView;
import majestatyczne.bestie.frontend.service.QuizService;
import majestatyczne.bestie.frontend.service.RewardCategoryService;
import majestatyczne.bestie.frontend.service.RewardStrategyService;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import majestatyczne.bestie.frontend.util.AlertManager;
import majestatyczne.bestie.frontend.util.cell.RewardCategoryChoiceCell;
import org.apache.http.HttpStatus;


public class QuizSettingsPageController implements Initializable {

    private QuizView quizView;

    private RewardStrategy strategy;

    private RewardStrategy existingStrategy;

    private ObservableList<RewardStrategyParameterView> parameters;

    private ObservableList<RewardCategoryView> rewardCategories;

    private List<RewardCategory> rewardCategoryList;

    @FXML
    private ImageView backIcon;

    @FXML
    private Label quizNameLabel;

    @FXML
    private Label quizScoreLabel;

    @FXML
    private ChoiceBox<RewardStrategyType> rewardStrategyTypeBox;

    @FXML
    private Button saveButton;

    @FXML
    private VBox percentageVBox;

    @FXML
    private VBox scoreVBox;

    @FXML
    private Label percentageStrategyInfo;

    @FXML
    private Label scoreStrategyInfo;

    @FXML
    private TableView<RewardStrategyParameterView> parametersTable;

    @FXML
    private TableColumn<RewardStrategyParameterView, Integer> priorityColumn;

    @FXML
    private TableColumn<RewardStrategyParameterView, Integer> parameterValueColumn;

    @FXML
    private TableColumn<RewardStrategyParameterView, String> rewardCategoryColumn;

    private final RewardStrategyService rewardStrategyService = new RewardStrategyService();

    private final RewardCategoryService rewardCategoryService = new RewardCategoryService();

    private final QuizService quizService = new QuizService();

    public void setQuizView(QuizView quizView) {
        this.quizView = quizView;
        setData();
    }

    private void setData() {
        quizNameLabel.setText(quizView.getName());
        quizScoreLabel.setText(Constants.QUIZ_MAX_SCORE_INFO + quizView.getMaxScore());
        initializeStrategy();
        initializeRewardCategories();
    }

    private void initializeStrategy() {
        parameters = FXCollections.observableArrayList();
        existingStrategy = rewardStrategyService.getRewardStrategyByQuizId(quizView.getId()).orElse(null);
        strategy = new RewardStrategy();
        if (existingStrategy != null) {
            initializeStrategyFromExistingStrategy();
            return;
        }
        initializeEmptyStrategy();
    }

    private void initializeStrategyFromExistingStrategy() {
        strategy.setQuiz(existingStrategy.getQuiz());
        strategy.setRewardStrategyType(existingStrategy.getRewardStrategyType());
        existingStrategy.getParameters().forEach(parameter -> parameters.add(new RewardStrategyParameterView(parameter)));
        rewardStrategyTypeBox.setValue(strategy.getRewardStrategyType());
    }

    private void initializeEmptyStrategy() {
        parametersTable.setVisible(false);
        parametersTable.setManaged(false);
        strategy.setQuiz(quizService.getQuizById(quizView.getId()).orElseThrow(() -> new IllegalArgumentException("Quiz not found")));
    }

    private void initializeRewardCategories() {
        rewardCategories = FXCollections.observableArrayList();
        rewardCategoryList = rewardCategoryService.getRewardCategories();
        rewardCategoryList.forEach(rewardCategory -> rewardCategories.add(new RewardCategoryView(rewardCategory.getId(), rewardCategory.getName())));

        RewardCategoryView noCategory = new RewardCategoryView(-1, Constants.REWARD_CATEGORY_CHOICE_BOX_NO_CATEGORY);
        rewardCategories.add(0, noCategory);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backIcon.setImage(new Image(String.valueOf(HomePageApplication.class.getResource(Constants.BACK_ICON_RESOURCE))));
        rewardStrategyTypeBox.getItems().addAll(RewardStrategyType.values());
        initializeParametersTable();
    }

    private void initializeParametersTable() {
        priorityColumn.setCellValueFactory(value -> value.getValue().getPriorityProperty());
        parameterValueColumn.setCellValueFactory(value -> value.getValue().getParameterValueProperty());
        rewardCategoryColumn.setCellValueFactory(value -> value.getValue().getRewardCategoryNameProperty());

        parameterValueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        parameterValueColumn.setOnEditCommit(this::onParameterValueEdit);
        rewardCategoryColumn.setCellFactory(param -> new RewardCategoryChoiceCell<>(rewardCategories, this::onChosenRewardCategory));
    }

    private void onParameterValueEdit(TableColumn.CellEditEvent<RewardStrategyParameterView, Integer> event) {
        event.getRowValue().setParameterValue(event.getNewValue());
    }

    private void onChosenRewardCategory(RewardStrategyParameterView selectedStrategyParameter, RewardCategoryView selectedCategory) {
        if (selectedStrategyParameter == null || selectedCategory == null) {
            return;
        }
        RewardCategory category = rewardCategoryList.stream().filter(x -> x.getId() == selectedCategory.getId()).findFirst().orElse(null);
        selectedStrategyParameter.setRewardCategory(category);
    }

    @FXML
    public void onGoBackClicked() {
        Stage stage = (Stage) rewardStrategyTypeBox.getScene().getWindow();
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
    public void onRewardStrategyTypeBoxClicked() {
        saveButton.setDisable(false);
        RewardStrategyType selectedType = rewardStrategyTypeBox.getValue();
        parametersTable.setVisible(true);
        parametersTable.setManaged(true);
        prepareStrategyDetails(selectedType);
        switch (selectedType) {
            case PERCENTAGE -> showPercentageStrategyDetails();
            case SCORE -> showScoreStrategyDetails();
        }
    }

    private void prepareStrategyDetails(RewardStrategyType strategyType) {
        parameters.clear();
        if (existingStrategy == null || existingStrategy.getRewardStrategyType() != strategyType) {
            generateParameters(strategyType);
        } else {
            existingStrategy.getParameters().forEach(parameter -> parameters.add(new RewardStrategyParameterView(parameter)));
        }
        parametersTable.setItems(parameters);
        strategy.setRewardStrategyType(strategyType);
    }

    private void generateParameters(RewardStrategyType strategyType) {
        switch (strategyType) {
            case PERCENTAGE -> {
                for (int i = 0; i < Constants.PERCENTAGE_STRATEGY_PARAMETERS_NUMBER; i++) {
                    parameters.add(new RewardStrategyParameterView(i + 1, i + 1, 0, null));
                }
            }
            case SCORE -> {
                for (int i = 0; i < quizView.getMaxScore() + 1; i++) {
                    parameters.add(new RewardStrategyParameterView(i + 1, i + 1, quizView.getMaxScore() - i, null));
                }
            }
        }

    }

    private void showPercentageStrategyDetails() {
        percentageStrategyInfo.setText(Constants.PERCENTAGE_REWARD_STRATEGY_DESCRIPTION);
        percentageVBox.setVisible(true);
        percentageVBox.setManaged(true);
        scoreVBox.setVisible(false);
        scoreVBox.setManaged(false);
        parameterValueColumn.setText(Constants.PERCENTAGE_STRATEGY_PARAMETER_COLUMN_NAME);
    }

    private void showScoreStrategyDetails() {
        scoreStrategyInfo.setText(Constants.SCORE_REWARD_STRATEGY_DESCRIPTION);
        scoreVBox.setVisible(true);
        scoreVBox.setManaged(true);
        percentageVBox.setVisible(false);
        percentageVBox.setManaged(false);
        parameterValueColumn.setText(Constants.SCORE_STRATEGY_PARAMETER_COLUMN_NAME);
    }

    @FXML
    public void onSaveButtonClicked() {
        strategy.setParameters(parameters.stream().map(RewardStrategyParameterView::toRewardStrategyParameter).toList());
        try {
            validateParameters();
        } catch (IllegalArgumentException e) {
            return;
        }
        if (existingStrategy == null) {
            handleSaveRewardStrategy(rewardStrategyService.addRewardStrategy(strategy), Constants.ADD_STRATEGY_INFO, Constants.ADD_STRATEGY_ERROR);
        } else {
            strategy.setId(existingStrategy.getId());
            handleSaveRewardStrategy(rewardStrategyService.updateRewardStrategy(strategy), Constants.UPDATE_STRATEGY_INFO, Constants.UPDATE_STRATEGY_ERROR);
        }
        onGoBackClicked();
    }

    private void handleSaveRewardStrategy(int responseCode, String successInfo, String errorInfo) {
        switch (responseCode) {
            case HttpStatus.SC_OK, HttpStatus.SC_ACCEPTED -> AlertManager.showConfirmationAlert(successInfo);
            default -> AlertManager.showErrorAlert(responseCode, errorInfo);
        }
    }

    private void validateParameters() {
        switch (strategy.getRewardStrategyType()) {
            case PERCENTAGE -> checkPercentageParameters();
            case SCORE -> checkScoreParameters();
        }
    }

    private void checkPercentageParameters() {
        strategy.getParameters().forEach(parameter -> {
            if (parameter.getParameterValue() > 100 || parameter.getParameterValue() < 0) {
                AlertManager.showWarningAlert(Constants.PERCENTAGE_STRATEGY_PARAMETER_OUT_OF_BOUND_WARNING);
                throw new IllegalArgumentException();
            }
        });
        int sum = strategy.getParameters().stream()
                .mapToInt(RewardStrategyParameter::getParameterValue)
                .sum();
        if (sum > 100) {
            AlertManager.showWarningAlert(Constants.PERCENTAGE_STRATEGY_PARAMETER_SUM_OVER_100_WARNING);
            throw new IllegalArgumentException();
        }
    }

    private void checkScoreParameters() {
        Set<Integer> uniqueValues = new HashSet<>();
        strategy.getParameters().forEach(parameter -> {
            int parameterValue = parameter.getParameterValue();
            if (parameterValue < 0 || parameterValue > quizView.getMaxScore()) {
                AlertManager.showWarningAlert(Constants.SCORE_STRATEGY_PARAMETER_OUT_OF_BOUND_WARNING);
                throw new IllegalArgumentException();
            }
            if (!uniqueValues.add(parameterValue)) {
                AlertManager.showWarningAlert(Constants.SCORE_STRATEGY_PARAMETER_DUPLICATE_WARNING);
                throw new IllegalArgumentException();
            }
        });
    }
}
