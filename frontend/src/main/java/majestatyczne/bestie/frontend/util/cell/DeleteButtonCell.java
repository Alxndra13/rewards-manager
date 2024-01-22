package majestatyczne.bestie.frontend.util.cell;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import majestatyczne.bestie.frontend.Constants;

import java.util.function.Consumer;

public class DeleteButtonCell<T> extends TableCell<T, Void> {
    private final Button button;

    public DeleteButtonCell(Consumer<T> onDeleteButtonClicked) {
        this.button = new Button(Constants.DELETE_BUTTON_TEXT);
        this.button.getStyleClass().add("delete-button");
        button.setOnAction(event -> {
            T item = getTableView().getItems().get(getIndex());
            onDeleteButtonClicked.accept(item);
        });
    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(button);
        }
    }
}

