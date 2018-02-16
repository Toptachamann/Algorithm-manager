package app.controller;

import app.auxiliary.InputException;
import app.model.Author;
import app.model.Textbook;
import app.service.TextbookService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TextbookController extends AbstractController {
  private static final Logger logger = LogManager.getLogger(TextbookController.class);

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
            Integer volume = null;
            Integer edition = null;
            String volumeStr = volumeCB.getSelectionModel().getSelectedItem();
            String editionStr = editionCB.getSelectionModel().getSelectedItem();
            if (volumeStr != null) {
              if (volumeStr.matches(numberPattern)) {
                volume = Integer.valueOf(volumeStr.trim());
              } else {
                info.setContentText("Volume should be numeric value");
                info.showAndWait();
                return;
              }
            }
            if (editionStr != null) {
              if (editionStr.matches(numberPattern)) {
                edition = Integer.valueOf(editionStr.trim());
              } else {
                info.setContentText("Edition should be numeric value");
                info.showAndWait();
                return;
              }
            }
            List<Author> authors = authorsFromStr(authorsTextField.getText());
            textbookTableView.setItems(
                FXCollections.observableArrayList(
                    textbookService.searchTextbooks(title, volume, edition, authors)));
          } catch (SQLException e1) {
            logger.catching(Level.ERROR, e1);
            error.setContentText(
                "Can't perform search due to some internal error.\n" + "See logs for details.");
            error.showAndWait();
          } catch (InputException e1) {
            logger.catching(Level.WARN, e1);
            info.setContentText(
                "Authors' first and last names should be specified in sequence, comma separated.\n"
                    + "For example, Vasya Pupkin, Fedya Nozkin");
            info.showAndWait();
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
              info.setContentText("Book's title shouldn't be blank");
              info.showAndWait();
            } else if (!StringUtils.isBlank(volumeStr) && !volumeStr.matches(numberPattern)) {
              info.setContentText("Book's volume value is entered wrong");
              info.showAndWait();
            } else if (StringUtils.isBlank(editionStr) || editionStr.matches(numberPattern)) {
              info.setContentText("Book's edition isn't specified or isn't numeric value");
              info.showAndWait();
            } else if (authors.size() == 0) {
              info.setContentText("Book should have at least one author");
              info.showAndWait();
            } else {
              Integer volume = Integer.valueOf(volumeStr);
              Integer edition = Integer.valueOf(editionStr);
              textbookTableView
                  .getItems()
                  .add(textbookService.createTextbook(title, edition, volume, authors));
              textbookTableView.scrollTo(textbookTableView.getItems().size() - 1);
            }
          } catch (SQLException e1) {
            logger.catching(Level.ERROR, e1);
            error.setContentText(
                "Can't add new algorithm due to some internal error.\n" + "See logs for details.");
            error.showAndWait();
          } catch (InputException e1) {
            logger.catching(Level.WARN, e1);
            error.setContentText(
                "Authors' first and last names should be specified in sequence, comma separated.\n"
                    + "For example, Vasya Pupkin, Fedya Nozhkin");
            error.showAndWait();
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
    volumeColumn.setCellFactory(param -> new VolumeCell());
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
              confirm.setContentText(
                  "Do you really want to delete thid book?\n" + "Operation is undoable.");
              Optional<ButtonType> buttonType = confirm.showAndWait();
              if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
                try {
                  textbookService.deleteTextbook(textbook);
                  textbookTableView.getItems().remove(textbook);
                } catch (SQLException e1) {
                  logger.catching(Level.ERROR, e1);
                  error.setContentText(
                      "Can't delete selected book due to some internal error.\n"
                          + "See logs for details.");
                  error.showAndWait();
                }
              }
            }
          }
        });
    titleColumn.setOnEditCommit(
        e -> {
          String newTitle = e.getNewValue();
          if (StringUtils.isBlank(newTitle)) {
            info.setContentText("Book's title should be not blank");
            info.showAndWait();
            textbookTableView.refresh();
          } else {
            try {
              newTitle = newTitle.trim();
              Textbook textbook = textbookTableView.getItems().get(e.getTablePosition().getRow());
              textbookService.updateTitle(textbook, newTitle);
              textbook.setTitle(newTitle);
            } catch (SQLException e1) {
              logger.catching(Level.ERROR, e1);
              error.setContentText(
                  "Book's title wasn't updated due to some internal error.\n"
                      + "See logs for details.");
              error.showAndWait();
            }
          }
        });

    editionColumn.setOnEditCommit(
        e -> {
          Integer newEdition = e.getNewValue();
          if (newEdition == null || newEdition < 1) {
            info.setContentText("Edition should be specified and should be greater than 0");
            info.showAndWait();
            textbookTableView.refresh();
          } else {
            try {
              Textbook textbook = textbookTableView.getItems().get(e.getTablePosition().getRow());
              textbookService.updateEdition(textbook, newEdition);
              textbook.setEdition(newEdition);
            } catch (SQLException e1) {
              logger.catching(Level.ERROR, e1);
              error.setContentText(
                  "Edition wasn't changed due to some internal error.\n" + "See logs for details.");
              error.showAndWait();
            }
          }
        });

    try {
      textbookTableView.setItems(
          FXCollections.observableArrayList(textbookService.getAllTextbooks()));
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      info.setContentText(
          "Can't load books' information due to some internal error.\n" + "See logs for details.");
      info.showAndWait();
    }
  }

  private List<Author> authorsFromStr(String authors) throws InputException {
    if (StringUtils.isBlank(authors)) {
      return new ArrayList<>();
    } else {
      String authorsPattern = "^(\\s*[a-zA-Z]+\\s+[a-zA-Z]+(,\\s+(?!$)|\\s*$))+";
      if (authors.matches(authorsPattern)) {
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
        throw logger.throwing(
            Level.WARN, new InputException("Authors' list doesn't match the pattern"));
      }
    }
  }

  private class AuthorCell extends TableCell<Textbook, List<Author>> {
    private TextField textField;

    AuthorCell() {
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
                logger.catching(Level.WARN, e1);
                info.setContentText(
                    "Authors' first and last names should be specified in sequence, comma separated.\n"
                        + "For example, Vasya Pupkin, Fedya Nozkin");
                info.showAndWait();
                cancelEdit();
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
        logger.catching(Level.ERROR, e);
        error.setContentText(
            "Authors weren't updated due to some internal error.\n" + "See logs for details.");
        error.showAndWait();
        textbookTableView.refresh();
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

  private class VolumeCell extends TableCell<Textbook, Integer> {
    private TextField textField;

    VolumeCell() {
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
              String volumeStr = textField.getText();
              if (StringUtils.isBlank(volumeStr)) {
                setItem(null);
                try {
                  Textbook rowValue = textbookTableView.getItems().get(getIndex());
                  textbookService.updateVolume(rowValue, null);
                  rowValue.setVolume(null);
                } catch (SQLException e1) {
                  logger.catching(Level.ERROR, e1);
                  error.setContentText(
                      "Can't set book's volume to none due to some internal error.\n"
                          + "See logs for details.");
                  error.showAndWait();
                }
              } else if (volumeStr.matches(numberPattern)) {
                commitEdit(Integer.valueOf(volumeStr.trim()));
              } else {
                info.setContentText("Volume should be numeric value");
                info.showAndWait();
                cancelEdit();
              }
            }
          });
    }

    @Override
    protected void updateItem(Integer item, boolean empty) {
      super.updateItem(item, empty);
      if (empty) {
        setText(null);
        setGraphic(null);
      } else {
        if (isEditing()) {
          textField.setText(getString());
          setText(null);
          setGraphic(textField);
        } else {
          setGraphic(null);
          setText(getString());
        }
      }
    }

    @Override
    public void startEdit() {
      if (!isEmpty()) {
        super.startEdit();
        setText(null);
        textField.setText(getText());
        setGraphic(textField);
        textField.requestFocus();
      }
    }

    @Override
    public void commitEdit(Integer newValue) {
      super.commitEdit(newValue);
      if (newValue < 1) {
        info.setContentText("Book's volume should be greater than 0");
        info.showAndWait();
        cancelEdit();
      } else {
        try {
          Textbook rowValue = textbookTableView.getItems().get(getIndex());
          textbookService.updateVolume(rowValue, newValue);
          rowValue.setVolume(newValue);
        } catch (SQLException e) {
          logger.catching(Level.ERROR, e);
          error.setContentText(
              "Can't set book's volume to none due to some internal error.\n"
                  + "See logs for details.");
          error.showAndWait();
        }
      }
    }

    @Override
    public void cancelEdit() {
      super.cancelEdit();
      setGraphic(null);
      setText(getString());
    }

    private String getString() {
      return getItem() == null ? "None" : getItem().toString();
    }
  }
}
