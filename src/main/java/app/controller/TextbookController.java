package app.controller;

import app.model.Author;
import app.model.Textbook;
import app.service.TextbookDao;
import app.service.TextbookService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.List;

public class TextbookController extends AnchorPane {
  @FXML private TableView<Textbook> textbookTableView;
  @FXML private TableColumn<Textbook, Integer> bookIdColumn;
  @FXML private TableColumn<Textbook, String> titleColumn;
  @FXML private TableColumn<Textbook, Integer> volumeColumn;
  @FXML private TableColumn<Textbook, Integer> editionColumn;
  @FXML private TableColumn<Textbook, List<Author>> authorColumn;

  private TextbookDao textbookDao;

  public void initialize() {
    initTable();
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
            ListView<Author> listView = new ListView<>();
            listView.setCellFactory(
                param1 ->
                    new ListCell<Author>() {
                      @Override
                      protected void updateItem(Author item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                          setText(null);
                        } else if (item == null) {
                          setText("null");
                        } else {
                          setText(item.getFirstName() + " " + item.getLastName());
                        }
                      }
                    });
            return new TableCell<Textbook, List<Author>>(){
              @Override
              protected void updateItem(List<Author> item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null){
                  setGraphic(null);
                }else{
                  listView.setItems(FXCollections.observableArrayList(item));
//                  listView.setOrientation(Orientation.HORIZONTAL);
                  listView.getStylesheets().add("/app_style.css");
                  setGraphic(listView);
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
