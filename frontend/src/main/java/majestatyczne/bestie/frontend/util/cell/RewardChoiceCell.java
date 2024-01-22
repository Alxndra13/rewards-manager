package majestatyczne.bestie.frontend.util.cell;

import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import majestatyczne.bestie.frontend.model.view.ResultView;
import majestatyczne.bestie.frontend.model.view.RewardView;

import java.util.function.BiConsumer;

public class RewardChoiceCell extends TableCell<ResultView, String> {

    private final ChoiceBox<RewardView> choiceBox;

    public RewardChoiceCell(ObservableList<RewardView> rewards, BiConsumer<ResultView, RewardView> onChosenReward) {
        choiceBox = new ChoiceBox<>(rewards);

        choiceBox.setOnAction(event -> {
            ResultView item = getTableView().getItems().get(getIndex());
            RewardView selectedReward = choiceBox.getValue();
            onChosenReward.accept(item, selectedReward);
        });
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
            setText(null);
        } else {
            RewardView selectedReward = choiceBox.getItems().stream()
                    .filter(reward -> reward.getName().equals(item))
                    .findFirst()
                    .orElse(null);
            choiceBox.setValue(selectedReward);
            setGraphic(choiceBox);
        }
    }
}
