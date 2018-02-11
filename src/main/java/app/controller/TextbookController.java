package app.controller;

import app.model.Author;
import app.model.Textbook;
import app.service.TextbookDao;
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
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class TextbookController extends AnchorPane {
  private final String numberPattern = "^\\s*[0-9]+\\s*$";
  private final String authorsPattern = "^(\\s*[a-zA-Z]+\\s*[a-zA-Z]+(,\\s+(?!$)|\\s*$))+";

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

  public void initialize() {
    initTable();
    editionCB.setVisibleRowCount(5);
    editionCB.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
    volumeCB.setVisibleRowCount(5);
    volumeCB.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
    searchBookButton.setOnAction(
        e -> {
          try {
            String title = titleTextField.getText();
            Integer edition = null;
            Integer volume = null;
            String authors = authorsTextField.getText();

            String editionStr = editionCB.getSelectionModel().getSelectedItem();
            if (editionStr != null) {
              if (editionStr.matches(numberPattern)) {
                edition = Integer.valueOf(editionStr);
              } else {
                // TODO: alert
              }
            }
            String volumeStr = volumeCB.getSelectionModel().getSelectedItem();
            if (volumeStr != null) {
              if (volumeStr.matches(numberPattern)) {
                volume = Integer.valueOf(volumeStr);
              } else {
                // TODO alert
              }
            }

            List<Textbook> textbooks = getService().searchTextbook(title, edition, volume, authors);
            textbookTableView.setItems(FXCollections.observableArrayList(textbooks));
          } catch (SQLException e1) {
            e1.printStackTrace();
          }
        });
    addBookButton.setOnAction(
        e -> {
          try {
            String title = titleTextField.getText();
            String editionStr = editionCB.getSelectionModel().getSelectedItem();
            String volumeStr = volumeCB.getSelectionModel().getSelectedItem();
            String authors = authorsTextField.getText();
            if (StringUtils.isBlank(title)) {
              // TODO alert
            }
            if (StringUtils.isBlank(editionStr) || editionStr.matches(numberPattern)) {
              // TODO alert
            }
            if (!StringUtils.isBlank(volumeStr) && !volumeStr.matches(numberPattern)) {
              // TODO alert
            }
            if (StringUtils.isBlank(authors) || !authors.matches(authorsPattern)) {
              // TODO alert
            }
            Integer edition = StringUtils.isBlank(volumeStr) ? null : Integer.valueOf(editionStr);
            Integer volume = Integer.valueOf(volumeStr);
            textbookTableView
                .getItems()
                .add(getService().insertTextbook(title, edition, volume, authors));
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

    try {
      textbookTableView.setItems(FXCollections.observableArrayList(getService().getTextbooks()));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private TextbookService getService() throws SQLException {
    return TextbookDao.getDao();
  }
}
