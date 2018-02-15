package app.controller;

import app.auxiliary.InputException;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TextbookController extends AnchorPane {
  private static final Logger logger = LogManager.getLogger(TextbookController.class);

  private final String authorsPattern = "^(\\s*[a-zA-Z]+\\s+[a-zA-Z]+(,\\s+(?!$)|\\s*$))+";
  private final String numberPattern = "^\\s*[0-9]+\\s*$";

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
            String title = titleTextField.getText();
            if (StringUtils.isBlank(title)) {
              // TODO alert
            }
            Integer volume = null;
            Integer edition = null;
            String volumeStr = volumeCB.getSelectionModel().getSelectedItem();
            String editionStr = editionCB.getSelectionModel().getSelectedItem();
            if (volumeStr != null) {
              if (volumeStr.matches(numberPattern)) {
                volume = Integer.valueOf(volumeStr.trim());
              } else {
                // TODO alert
              }
            }
            if (editionStr != null) {
              if (editionStr.matches(numberPattern)) {
                edition = Integer.valueOf(editionStr.trim());
              } else {
                // TODO: alert
              }
            }
            List<Author> authors = authorsFromStr(authorsTextField.getText());
            textbookTableView.setItems(
                FXCollections.observableArrayList(
                    textbookService.searchTextbooks(title, volume, edition, authors)));
          } catch (SQLException e1) {
            // TODO alert
            e1.printStackTrace();
          } catch (InputException e1) {
            // TODO alert
            e1.printStackTrace();
          }
        });
    addBookButton.setOnAction(
        e -> {
          try {
            String title = titleTextField.getText();
            String volumeStr = volumeCB.getSelectionModel().getSelectedItem();
            String editionStr = editionCB.getSelectionModel().getSelectedItem();
            List<Author> authors = authorsFromStr(authorsTextField.getText());
            if (StringUtils.isBlank(title)) {
              // TODO alert
            }
            if (!StringUtils.isBlank(volumeStr) && !volumeStr.matches(numberPattern)) {
              // TODO alert
            }
            if (StringUtils.isBlank(editionStr) || editionStr.matches(numberPattern)) {
              // TODO alert
            }
            if (authors.size() == 0) {
              // TODO alert
            }
            Integer volume = Integer.valueOf(volumeStr);
            Integer edition = Integer.valueOf(editionStr);
            if (authors.size() == 0) {
              // TODO alert
            }
            textbookTableView
                .getItems()
                .add(textbookService.createTextbook(title, edition, volume, authors));
          } catch (SQLException e1) {
            // TODO alert
            e1.printStackTrace();
          } catch (InputException e1) {
            // TODO alert
            e1.printStackTrace();
          }
        });
  }

  private void initTable() {
    textbookTableView.setEditable(true);

    bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    volumeColumn.setCellValueFactory(new PropertyValueFactory<>("volume"));
    editionColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));
    authorColumn.setCellValueFactory(new PropertyValueFactory<>("authors"));

    bookIdColumn.setEditable(false);
    titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    volumeColumn.setCellFactory(
        param ->
            new TableCell<Textbook, Integer>() {
              @Override
              public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                  setText(null);
                } else if (item.equals(0)) {
                  setText("None");
                } else {
                  setText(item.toString());
                }
              }
            });
    editionColumn.setCellFactory(
        param ->
            new TableCell<Textbook, Integer>() {
              @Override
              protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                  setText(null);
                } else {
                  setText(item.toString());
                }
              }
            });
    authorColumn.setCellFactory(param -> new AuthorCell());
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
    titleColumn.setOnEditCommit(
        e -> {
          String newTitle = e.getNewValue();
          if (StringUtils.isBlank(newTitle)) {
            // TODO alert
            textbookTableView.refresh();
          } else {
            try {
              newTitle = newTitle.trim();
              Textbook textbook = textbookTableView.getItems().get(e.getTablePosition().getRow());
              textbookService.updateTitle(textbook, newTitle);
              textbook.setTitle(newTitle);
            } catch (SQLException e1) {
              // TODO alert
              e1.printStackTrace();
            }
          }
        });

    volumeColumn.setOnEditCommit(
        e -> {
          Integer newVolume = e.getNewValue();
          if (newVolume == null || newVolume < 1) {
            // TODO alert
            textbookTableView.refresh();
          } else {
            try {
              Textbook textbook = textbookTableView.getItems().get(e.getTablePosition().getRow());
              textbookService.updateVolume(textbook, newVolume);
              textbook.setVolume(newVolume);
            } catch (SQLException e1) {
              e1.printStackTrace();
            }
          }
        });
    editionColumn.setOnEditCommit(
        e -> {
          Integer newEdition = e.getNewValue();
          if (newEdition == null || newEdition < 1) {
            // TODO alert
            textbookTableView.refresh();
          } else {
            try {
              Textbook textbook = textbookTableView.getItems().get(e.getTablePosition().getRow());
              textbookService.updateEdition(textbook, newEdition);
              textbook.setEdition(newEdition);
            } catch (SQLException e1) {
              // TODO alert
              e1.printStackTrace();
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

  private List<Author> authorsFromStr(String authors) throws InputException {
    if (authors != null && authors.matches(authorsPattern)) {
      List<Author> result = new ArrayList<>();
      if (StringUtils.isBlank(authors)) {
        return result;
      }
      String[] fullNames = authors.split(",\\s+");
      for (String fullName : fullNames) {
        String[] fullNameArr = fullName.split("\\s+");
        result.add(new Author(-1, fullNameArr[0], fullNameArr[1]));
      }
      return result;
    } else {
      throw new InputException("Authors' list doesn't match the pattern");
    }
  }

  private class AuthorCell extends TableCell<Textbook, List<Author>> {
    private TextField textField;

    public AuthorCell() {
      textField = new TextField();
      textField
          .focusedProperty()
          .addListener(
              (obs, oldValue, newValue) -> {
                if (newValue && !oldValue) {
                  textField.selectAll();
                }
              });
      textField.setOnKeyReleased(
          e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
              try {
                List<Author> authorsFromInput = authorsFromStr(textField.getText());
                commitEdit(authorsFromInput);
              } catch (InputException e1) {
                // TODO alert
                cancelEdit();
                e1.printStackTrace();
              }
            }
          });
    }

    private String getString() {
      List<Author> authors = getItem();
      if (authors == null || authors.size() == 0) {
        return "No authors";
      } else {
        return authors
            .stream()
            .map(a -> a.getFirstName() + " " + a.getLastName())
            .collect(Collectors.joining(", "));
      }
    }

    @Override
    public void startEdit() {
      if (isEditable()) {
        super.startEdit();
        setText(null);
        setGraphic(textField);
        textField.setText(getString());
        textField.requestFocus();
      }
    }

    @Override
    public void commitEdit(List<Author> newValue) {
      super.commitEdit(newValue);
      try {
        Textbook textbook = getTableView().getItems().get(getTableRow().getIndex());
        List<Author> newAuthors = textbookService.updateAuthors(textbook, newValue);
        setItem(newAuthors);
        updateItem(newAuthors, false);
      } catch (SQLException e) {
        // TODO alert
        e.printStackTrace();
        cancelEdit();
      }
    }

    @Override
    public void cancelEdit() {
      super.cancelEdit();
      setGraphic(null);
      setText(getString());
    }

    @Override
    protected void updateItem(List<Author> item, boolean empty) {
      super.updateItem(item, empty);
      if (empty) {
        setText(null);
        setGraphic(null);
      } else if (isEditing()) {
        textField.setText(getString());
        setText(null);
        setGraphic(textField);
      } else {
        setGraphic(null);
        setText(getString());
      }
    }
  }
}
