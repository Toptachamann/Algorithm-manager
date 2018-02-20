package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.awt.Dimension;
import java.awt.Toolkit;

public class App extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    try{
      Class.forName("org.springframework.context.ApplicationContext");
    }catch (ClassNotFoundException e){
      e.printStackTrace();
    }
    ApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/beans.xml");
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/main_frame.fxml"));
    loader.setControllerFactory(context::getBean);
    Parent root = loader.load();
    primaryStage.setTitle("Algorithm manager");
    primaryStage.getIcons().add(new Image(getClass().getResource("/main_icon.png").toString()));
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    primaryStage.setScene(new Scene(root, screenSize.width * 5 / 7, screenSize.height * 11 / 14));
    primaryStage.show();
  }
}
