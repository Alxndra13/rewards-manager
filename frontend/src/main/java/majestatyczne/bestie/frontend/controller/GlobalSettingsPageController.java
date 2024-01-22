package majestatyczne.bestie.frontend.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import majestatyczne.bestie.frontend.Constants;
import majestatyczne.bestie.frontend.HomePageApplication;
import majestatyczne.bestie.frontend.model.*;
import majestatyczne.bestie.frontend.model.view.RewardCategoryView;
import majestatyczne.bestie.frontend.model.view.RewardView;
import majestatyczne.bestie.frontend.service.RewardCategoryService;
import majestatyczne.bestie.frontend.service.RewardService;
import majestatyczne.bestie.frontend.util.AlertManager;
import majestatyczne.bestie.frontend.util.cell.DeleteButtonCell;
import majestatyczne.bestie.frontend.util.cell.RewardCategoryChoiceCell;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GlobalSettingsPageController implements Initializable {

    @FXML
    private ImageView backIcon;

    private ObservableList<RewardView> rewards;

    private List<Reward> rewardList;

    private ObservableList<RewardCategoryView> rewardCategories;

    private List<RewardCategory> rewardCategoryList;

    @FXML
    private TableView<RewardView> rewardsTable;

    @FXML
    private TableColumn<RewardView, String> rewardNameColumn;

    @FXML
    private TableColumn<RewardView, String> rewardCategoryColumn;

    @FXML
    private TableColumn<RewardView, String> rewardDescriptionColumn;

    @FXML
    private TableColumn<RewardView, Void> rewardDeleteColumn;

    @FXML
    private TableView<RewardCategoryView> categoryTable;

    @FXML
    private TableColumn<RewardCategoryView, String> categoryNameColumn;

    @FXML
    private TableColumn<RewardCategoryView, Void> categoryDeleteColumn;

    @FXML
    private TextField newCategoryTextField;

    @FXML
    private TextField newRewardNameTextField;

    @FXML
    private TextField newRewardDescriptionTextField;

    private final RewardService rewardService = new RewardService();

    private final RewardCategoryService rewardCategoryService = new RewardCategoryService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setData();
        backIcon.setImage(new Image(String.valueOf(HomePageApplication.class.getResource(Constants.BACK_ICON_RESOURCE))));
        newCategoryTextField.setPromptText(Constants.NEW_REWARD_CATEGORY_PROMPT);
        newRewardNameTextField.setPromptText(Constants.NEW_REWARD_NAME_PROMPT);
        newRewardDescriptionTextField.setPromptText(Constants.NEW_REWARD_DESCRIPTION_PROMPT);
    }

    private void initializeRewardCategoriesTable() {
        categoryNameColumn.setCellValueFactory(value -> value.getValue().getNameProperty());
        categoryNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        categoryNameColumn.setOnEditCommit(this::onCategoryEdit);
        categoryDeleteColumn.setCellFactory(param -> new DeleteButtonCell<>(this::onDeleteCategoryClicked));
    }

    private void onDeleteCategoryClicked(RewardCategoryView rewardCategoryView) {
        int responseCode = rewardCategoryService.deleteRewardCategoryById(rewardCategoryView.getId());
        switch (responseCode) {
            case HttpStatus.SC_OK, HttpStatus.SC_ACCEPTED ->
                    AlertManager.showConfirmationAlert(Constants.DELETE_REWARD_CATEGORY_INFO);
            default -> AlertManager.showErrorAlert(responseCode, Constants.DELETE_REWARD_CATEGORY_ERROR);
        }
        setData();
    }

    private void onCategoryEdit(TableColumn.CellEditEvent<RewardCategoryView, String> event) {
        event.getRowValue().setName(event.getNewValue());
    }

    private void initializeRewardsTable() {
        rewardNameColumn.setCellValueFactory(value -> value.getValue().getNameProperty());
        rewardCategoryColumn.setCellValueFactory(value -> value.getValue().getRewardCategoryNameProperty());
        rewardDescriptionColumn.setCellValueFactory(value -> value.getValue().getDescriptionProperty());

        rewardNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        rewardDescriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        rewardNameColumn.setOnEditCommit(this::onRewardNameEdit);
        rewardDescriptionColumn.setOnEditCommit(this::onRewardDescriptionEdit);

        rewardCategoryColumn.setCellFactory(param -> new RewardCategoryChoiceCell<>(rewardCategories, this::onChosenRewardCategory));
        rewardDeleteColumn.setCellFactory(param -> new DeleteButtonCell<>(this::onDeleteRewardClicked));
    }

    private void onChosenRewardCategory(RewardView selectedReward, RewardCategoryView selectedCategory) {
        if (selectedReward != null && selectedCategory != null) {
            selectedReward.setRewardCategory(selectedCategory);
        }
    }

    private void onDeleteRewardClicked(RewardView rewardView) {
        int responseCode = rewardService.deleteRewardById(rewardView.getId());
        switch (responseCode) {
            case HttpStatus.SC_OK, HttpStatus.SC_ACCEPTED ->
                    AlertManager.showConfirmationAlert(Constants.DELETE_REWARD_INFO);
            default -> AlertManager.showErrorAlert(responseCode, Constants.DELETE_REWARD_ERROR);
        }
        initializeRewards();
    }

    private void onRewardDescriptionEdit(TableColumn.CellEditEvent<RewardView, String> event) {
        event.getRowValue().setDescription(event.getNewValue());
    }

    private void onRewardNameEdit(TableColumn.CellEditEvent<RewardView, String> event) {
        event.getRowValue().setName(event.getNewValue());
    }

    private void setData() {
        initializeRewardsTable();
        initializeRewardCategoriesTable();
        initializeRewardCategories();
        initializeRewards();
    }

    private void initializeRewardCategories() {
        rewardCategories = FXCollections.observableArrayList();
        rewardCategoryList = rewardCategoryService.getRewardCategories();
        rewardCategoryList.forEach(rewardCategory -> rewardCategories.add(new RewardCategoryView(rewardCategory)));
        categoryTable.setItems(rewardCategories);
    }

    private void initializeRewards() {
        rewards = FXCollections.observableArrayList();
        rewardList = rewardService.getRewards();
        rewardList.forEach(reward -> rewards.add(new RewardView(reward)));
        rewardsTable.setItems(rewards);
    }

    @FXML
    public void onGoBackClicked(MouseEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HomePageApplication.class.getResource(Constants.FXML_HOME_PAGE_RESOURCE));
        try {
            Scene scene = new Scene(fxmlLoader.load(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void onAddRewardClicked() {
        String rewardName = newRewardNameTextField.getText();
        String rewardDescription = newRewardDescriptionTextField.getText();
        if (rewardName.isEmpty()) {
            AlertManager.showWarningAlert(Constants.ADD_REWARD_EMPTY_WARNING);
            return;
        }
        Reward reward = new Reward(rewardName, rewardDescription);
        int responseCode = rewardService.addReward(reward);
        switch (responseCode) {
            case HttpStatus.SC_OK, HttpStatus.SC_ACCEPTED -> {
                newRewardNameTextField.clear();
                newRewardDescriptionTextField.clear();
                AlertManager.showConfirmationAlert(Constants.ADD_REWARD_INFO);
            }
            default -> AlertManager.showErrorAlert(responseCode, Constants.ADD_REWARD_ERROR_TITLE);
        }
        initializeRewards();
    }

    @FXML
    public void onAddCategoryClicked() {
        String categoryName = newCategoryTextField.getText();
        if (categoryName.isEmpty()) {
            AlertManager.showWarningAlert(Constants.ADD_REWARD_CATEGORY_EMPTY_WARNING);
            return;
        }
        int responseCode = rewardCategoryService.addRewardCategory(new RewardCategory(categoryName));
        switch (responseCode) {
            case HttpStatus.SC_OK, HttpStatus.SC_CREATED -> {
                newCategoryTextField.clear();
                AlertManager.showConfirmationAlert(Constants.ADD_REWARD_CATEGORY_INFO);
            }
            default -> AlertManager.showErrorAlert(responseCode, Constants.ADD_REWARD_CATEGORY_ERROR_TITLE);
        }
        setData();
    }

    @FXML
    private void onSaveRewardCategoriesClicked() {
        if (rewardCategories.stream().anyMatch(category -> category.getName().isEmpty())) {
            AlertManager.showWarningAlert(Constants.UPDATE_REWARD_CATEGORY_EMPTY_ERROR);
            return;
        }
        rewardCategories.forEach(rewardCategoryView ->
                rewardCategoryList.stream()
                        .filter(rewardCategory -> rewardCategory.getId() == rewardCategoryView.getId())
                        .findFirst()
                        .ifPresent(rewardCategory -> rewardCategory.setName(rewardCategoryView.getName())));
        int responseCode = rewardCategoryService.updateRewardCategories(rewardCategoryList);
        switch (responseCode) {
            case HttpStatus.SC_OK, HttpStatus.SC_ACCEPTED ->
                    AlertManager.showConfirmationAlert(Constants.UPDATE_REWARD_CATEGORIES_INFO);
            default -> AlertManager.showErrorAlert(responseCode, Constants.UPDATE_REWARD_CATEGORIES_ERROR_TITLE);
        }
        setData();
    }

    @FXML
    private void onSaveRewardsClicked() {
        rewards.forEach(rewardView -> rewardList.stream()
                .filter(reward -> reward.getId() == rewardView.getId())
                .findFirst()
                .ifPresent(reward -> {
                    reward.setName(rewardView.getName());
                    reward.setRewardCategory(rewardView.getRewardCategory() != null
                            ? rewardCategoryList.stream()
                            .filter(category -> category.getId() == rewardView.getRewardCategory().getId())
                            .findFirst()
                            .orElse(null)
                            : null);
                    reward.setDescription(rewardView.getDescription());
                })
        );
        int responseCode = rewardService.updateRewards(rewardList);
        switch (responseCode) {
            case HttpStatus.SC_OK, HttpStatus.SC_ACCEPTED ->
                    AlertManager.showConfirmationAlert(Constants.UPDATE_REWARDS_INFO);
            default -> AlertManager.showErrorAlert(responseCode, Constants.UPDATE_REWARDS_ERROR_TITLE);
        }
        initializeRewards();
    }
}
