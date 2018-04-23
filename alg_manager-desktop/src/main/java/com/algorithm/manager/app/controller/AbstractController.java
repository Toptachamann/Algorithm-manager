package com.algorithm.manager.app.controller;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public abstract class AbstractController {
  Alert confirm;
  Alert info;
  Alert error;

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
        .add(new Image(getClass().getResource("/confirm_icon_1.png").toString()));
  }

  protected void error() {
    error("Operation wasn't completed due to some internal error.\nSee logs for details.");
  }

  protected void error(String errorMessage) {
    error.setContentText(errorMessage);
    error.showAndWait();
  }

  protected void info(String infoMessage) {
    info.setContentText(infoMessage);
    info.showAndWait();
  }


  protected void warn(Throwable e) {
    String message = e.getMessage();
    String processed =
        message.substring(0, 1).toLowerCase() + message.substring(1, message.length());
    info.setContentText("Operation wasn't completed.\n" + "Cause: " + processed);
    info.showAndWait();
  }
}
