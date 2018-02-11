package app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class NewItemBoxController extends AnchorPane {

  @FXML AnchorPane rootAnchorPane;
  @FXML Label nameLabel;
  @FXML Label messageLabel;
  @FXML TextField nameTextField;
  @FXML TextArea descriptionTextArea;
  @FXML Button addButton;
  private Stage stage;
  private String retrievedName;
  private String retrievedDescription;

  public NewItemBoxController() {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/new_item_frame.fxml"));
    loader.setRoot(this);
    loader.setController(this);
    try {
      stage = new Stage();
      Parent root = loader.load();
      stage.setScene(new Scene(root));
      stage.initModality(Modality.APPLICATION_MODAL);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setParameters(
      String title, String message, String name, String namePrompt, String descriptionPrompt) {
    stage.setTitle(title);
    messageLabel.setText(message);
    nameLabel.setText(name);
    nameTextField.setPromptText(namePrompt);
    descriptionTextArea.setPromptText(descriptionPrompt);
  }

  public void show() {
    reload();
    stage.showAndWait();
  }

  public void reload() {
    nameTextField.setText("");
    descriptionTextArea.setText("");
  }

  public void initialize() {
    addButton.setOnAction(
        e -> {
          retrievedName = nameTextField.getText();
          retrievedDescription = descriptionTextArea.getText();
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
