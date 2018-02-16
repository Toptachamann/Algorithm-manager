package app.controller;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AbstractController {
  protected Alert confirm;
  protected Alert info;
  protected Alert error;

  public AbstractController() {
    info = new Alert(Alert.AlertType.INFORMATION);
    info.setTitle("Information dialog");
    info.setHeaderText(null);
    info.setGraphic(null);
    ((Stage) info.getDialogPane().getScene().getWindow())
        .getIcons()
        .add(new Image(getClass().getResource("/alert_icon.png").toString()));
    error = new Alert(Alert.AlertType.ERROR);
    error.setTitle("Error dialog");
    error.setHeaderText(null);
    error.setGraphic(null);
    ((Stage) error.getDialogPane().getScene().getWindow())
        .getIcons()
        .add(new Image(getClass().getResource("/error_icon.png").toString()));
    confirm = new Alert(Alert.AlertType.CONFIRMATION);
    confirm.setTitle("Confirm dialog");
    confirm.setHeaderText(null);
    confirm.setGraphic(null);
    ((Stage) confirm.getDialogPane().getScene().getWindow())
        .getIcons()
        .add(new Image(getClass().getResource("/confirm_icon.png").toString()));
  }
}
