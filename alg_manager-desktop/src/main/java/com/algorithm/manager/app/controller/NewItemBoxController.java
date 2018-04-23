package com.algorithm.manager.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class NewItemBoxController extends AnchorPane {

  @FXML AnchorPane rootAnchorPane;
  @FXML private Label nameLabel;
  @FXML private Label messageLabel;
  @FXML private TextField nameTextField;
  @FXML private TextArea descriptionTextArea;
  @FXML private Button addButton;
  private Stage stage;
  private String retrievedName;
  private String retrievedDescription;
  private ButtonType buttonType;

  public NewItemBoxController() {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/new_item_frame.fxml"));
    loader.setRoot(this);
    loader.setController(this);
    try {
      stage = new Stage();
      Parent root = loader.load();
      stage.setScene(new Scene(root));
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.setOnCloseRequest(e -> buttonType = ButtonType.CANCEL);
      stage.setOnShowing(e -> buttonType = null);
      descriptionTextArea.setWrapText(true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public NewItemBoxController(
      String title, String message, String name, String namePrompt, String descriptionPrompt) {
    this();
    stage.setTitle(title);
    messageLabel.setText(message);
    nameLabel.setText(name);
    nameTextField.setPromptText(namePrompt);
    descriptionTextArea.setPromptText(descriptionPrompt);
  }

  public Optional<ButtonType> show() {
    reload();
    stage.showAndWait();
    return Optional.of(buttonType);
  }

  private void reload() {
    nameTextField.setText("");
    descriptionTextArea.setText("");
  }

  public void initialize() {
    addButton.setOnAction(
        e -> {
          retrievedName = nameTextField.getText();
          retrievedDescription = descriptionTextArea.getText();
          buttonType = ButtonType.APPLY;
          stage.hide();
        });
  }

  public String getRetrievedName() {
    return retrievedName;
  }

  public String getRetrievedDescription() {
    return retrievedDescription;
  }
}
