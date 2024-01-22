package majestatyczne.bestie.frontend.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import majestatyczne.bestie.frontend.Constants;
import majestatyczne.bestie.frontend.model.FileFormat;
import majestatyczne.bestie.frontend.service.FileService;
import majestatyczne.bestie.frontend.util.AlertManager;
import majestatyczne.bestie.frontend.util.SaveFileException;
import org.apache.http.HttpStatus;

public class ExportPopupController {

    @FXML
    private ChoiceBox<FileFormat> formatChoiceBox;

    private Stage stage;

    private int quizId;

    public void setData(Stage stage, int quizId) {
        this.stage = stage;
        this.quizId = quizId;
        ObservableList<FileFormat> fileFormats = FXCollections.observableArrayList();
        fileFormats.addAll(FileFormat.values());
        formatChoiceBox.setItems(fileFormats);
    }

    @FXML
    private void onConfirmClicked() {
        FileFormat selectedFormat = formatChoiceBox.getValue();
        String format = selectedFormat == null ? "" : selectedFormat.name().toLowerCase();
        FileService service = new FileService();
        new Thread(() -> {
            try {
                int responseCode = service.exportResultsFile(quizId, format);
                Platform.runLater(() -> {
                    switch (responseCode) {
                        case HttpStatus.SC_OK -> AlertManager.showConfirmationAlert(Constants.EXPORT_FILE_INFO);
                        default -> AlertManager.showErrorAlert(responseCode, Constants.EXPORT_FILE_ERROR);
                    }
                });
            } catch (SaveFileException e) {
                Platform.runLater(() -> AlertManager.showWarningAlert(e.getMessage()));
            }
        }).start();

        stage.close();
    }
}
