package app;

import app.controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.Dimension;
import java.awt.Toolkit;

public class App extends Application {

  private Controller algoController;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/main_frame.fxml"));
    primaryStage.setTitle("Algorithm manager");
    primaryStage.getIcons().add(new Image("/icon.png"));
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    primaryStage.setScene(new Scene(root, screenSize.width * 5 / 7, screenSize.height * 11 / 14));
    primaryStage.show();
  }
}
