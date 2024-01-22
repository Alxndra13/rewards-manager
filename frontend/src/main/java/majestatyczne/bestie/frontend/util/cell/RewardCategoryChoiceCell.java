package majestatyczne.bestie.frontend.util.cell;

import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import majestatyczne.bestie.frontend.model.view.RewardCategoryView;

import java.util.function.BiConsumer;


public class RewardCategoryChoiceCell<T> extends TableCell<T, String> {

    private final ChoiceBox<RewardCategoryView> choiceBox;

    public RewardCategoryChoiceCell(ObservableList<RewardCategoryView> rewardCategories, BiConsumer<T, RewardCategoryView> onChosenRewardCategory) {
        choiceBox = new ChoiceBox<>(rewardCategories);
        choiceBox.setOnAction(event -> {
            T item = getTableView().getItems().get(getIndex());
            RewardCategoryView selectedCategory = choiceBox.getValue();
            onChosenRewardCategory.accept(item, selectedCategory);
        });
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
            setText(null);
        } else {
            RewardCategoryView selectedCategory = choiceBox.getItems().stream()
                    .filter(category -> category.getName().equals(item))
                    .findFirst()
                    .orElse(null);
            choiceBox.setValue(selectedCategory);
            if (selectedCategory == null) {
                setText(null);
            }
            setGraphic(choiceBox);
        }
    }

}

