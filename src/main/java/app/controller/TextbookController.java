package app.controller;

import app.model.Textbook;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class TextbookController extends AnchorPane {
  @FXML private TableView<Textbook> textbookTableView;
  @FXML private TableColumn<Textbook, String> bookIdColumn;
  @FXML private TableColumn<Textbook, String> titleColumn;
  @FXML private TableColumn<Textbook, String> volumeColumn;
  @FXML private TableColumn<Textbook, String> editionColumn;
  @FXML private TableColumn<Textbook, String> authorColumn;

  public TextbookController() throws IOException {
    System.out.println("Constructor is called");
    FXMLLoader loader =
        new FXMLLoader(getClass().getClassLoader().getResource("textbook_tab.fxml"));
    loader.setRoot(this);
    loader.setController(this);
    loader.load();
  }

  public void initialize() {
    System.out.println("Initialized is called");
    bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
  }
}
