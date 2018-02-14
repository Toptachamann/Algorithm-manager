package app.controller;

import app.model.Author;
import app.model.Textbook;
import app.service.TextbookService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class TextbookController extends AnchorPane {
  private static final Logger logger = LogManager.getLogger(TextbookController.class);


  private TextbookService textbookService;

  @FXML private TableView<Textbook> textbookTableView;
  @FXML private TableColumn<Textbook, Integer> bookIdColumn;
  @FXML private TableColumn<Textbook, String> titleColumn;
  @FXML private TableColumn<Textbook, Integer> volumeColumn;
  @FXML private TableColumn<Textbook, Integer> editionColumn;
  @FXML private TableColumn<Textbook, List<Author>> authorColumn;
  @FXML private TextField titleTextField;
  @FXML private TextField authorsTextField;
  @FXML private ComboBox<String> editionCB;
  @FXML private ComboBox<String> volumeCB;
  @FXML private Button searchBookButton;
  @FXML private Button addBookButton;

  public TextbookController(TextbookService textbookService) {
    this.textbookService = textbookService;
  }

  public void initialize() {
    initTable();
    editionCB.setVisibleRowCount(5);
    editionCB.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
    volumeCB.setVisibleRowCount(5);
    volumeCB.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
    searchBookButton.setOnAction(
        e -> {
          try {
            List<Textbook> textbooks =
                textbookService.searchTextbooks(
                    titleTextField.getText(),
                    editionCB.getSelectionModel().getSelectedItem(),
                    volumeCB.getSelectionModel().getSelectedItem(),
                    authorsTextField.getText());
            textbookTableView.setItems(FXCollections.observableArrayList(textbooks));
          } catch (SQLException e1) {
            // TODO alert
            e1.printStackTrace();
          }
        });
    addBookButton.setOnAction(
        e -> {
          try {
            textbookTableView
                .getItems()
                .add(
                    textbookService.createTextbook(
                        titleTextField.getText(),
                        editionCB.getSelectionModel().getSelectedItem(),
                        volumeCB.getSelectionModel().getSelectedItem(),
                        authorsTextField.getText()));
          } catch (SQLException e1) {
            e1.printStackTrace();
          }
        });
  }

  private void initTable() {
    bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    volumeColumn.setCellValueFactory(new PropertyValueFactory<>("volume"));
    editionColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));
    authorColumn.setCellValueFactory(new PropertyValueFactory<>("authors"));
    volumeColumn.setCellFactory(
        param ->
            new TableCell<Textbook, Integer>() {
              @Override
              public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                  setText(null);
                } else if (item.equals(0)) {
                  setText("None");
                } else {
                  setText(item.toString());
                }
              }
            });
    authorColumn.setCellFactory(
        new Callback<TableColumn<Textbook, List<Author>>, TableCell<Textbook, List<Author>>>() {
          @Override
          public TableCell<Textbook, List<Author>> call(TableColumn<Textbook, List<Author>> param) {
            return new TableCell<Textbook, List<Author>>() {
              @Override
              protected void updateItem(List<Author> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                  setText(null);
                } else {
                  setText(
                      item.stream()
                          .map(a -> a.getFirstName() + " " + a.getLastName())
                          .collect(Collectors.joining(", ")));
                }
              }
            };
          }
        });
    textbookTableView.setOnKeyReleased(
        e -> {
          if (e.getCode().equals(KeyCode.DELETE)) {
            Textbook textbook = textbookTableView.getSelectionModel().getSelectedItem();
            if (textbook != null) {
              // TODO: ask to confirm
              try {
                textbookService.deleteTextbook(textbook);
                textbookTableView.getItems().remove(textbook);
              } catch (SQLException e1) {
                // TODO alert
                e1.printStackTrace();
              }
            }
          }
        });
    try {
      textbookTableView.setItems(
          FXCollections.observableArrayList(textbookService.getAllTextbooks()));
    } catch (SQLException e) {
      // TODO alert
      e.printStackTrace();
    }
  }
}
