package com.algorithm.manager.app.controller;

import com.algorithm.manager.auxiliary.InputException;
import com.algorithm.manager.model.Author;
import com.algorithm.manager.model.Book;
import com.algorithm.manager.service.BookService;
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
import javafx.util.converter.IntegerStringConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TextbookController extends AbstractController {
  private static final Logger logger = LogManager.getLogger(TextbookController.class);

  private final String numberPattern = "^\\s*[0-9]+\\s*$";

  private BookService bookService;

  @FXML private TableView<Book> textbookTableView;
  @FXML private TableColumn<Book, Integer> bookIdColumn;
  @FXML private TableColumn<Book, String> titleColumn;
  @FXML private TableColumn<Book, Integer> volumeColumn;
  @FXML private TableColumn<Book, Integer> editionColumn;
  @FXML private TableColumn<Book, List<Author>> authorColumn;
  @FXML private TextField titleTextField;
  @FXML private TextField authorsTextField;
  @FXML private ComboBox<String> editionCB;
  @FXML private ComboBox<String> volumeCB;
  @FXML private Button searchBookButton;
  @FXML private Button addBookButton;

  public TextbookController(BookService bookService) {
    this.bookService = bookService;
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
            if (!StringUtils.isBlank(volumeStr)) {
              if (volumeStr.matches(numberPattern)) {
                volume = Integer.valueOf(volumeStr.trim());
              } else {
                info.setContentText("Volume should be numeric value");
                info.showAndWait();
                return;
              }
            }
            if (!StringUtils.isBlank(editionStr)) {
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
                    bookService.searchBooks(title, volume, edition, authors)));
          } catch (InputException e1) {
            logger.warn(e1);
            info.setContentText(
                "Authors' first and last names should be specified in sequence, comma separated.\n"
                    + "For example, Vasya Pupkin, Fedya Nozkin");
            info.showAndWait();
          } catch (Exception e1) {
            logger.error(e1);
            error.setContentText(
                "Can't perform search due to some internal error.\n" + "See logs for details.");
            error.showAndWait();
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
            } else if (StringUtils.isBlank(editionStr) || !editionStr.matches(numberPattern)) {
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
                  .add(bookService.createBook(title, volume, edition, authors));
              textbookTableView.scrollTo(textbookTableView.getItems().size() - 1);
            }
          } catch (InputException e1) {
            logger.warn(e1);
            error.setContentText(
                "Authors' first and last names should be specified in sequence, comma separated.\n"
                    + "For example, Vasya Pupkin, Fedya Nozhkin");
            error.showAndWait();
          } catch (Exception e1) {
            logger.error(e1);
            error.setContentText(
                "Can't add new algorithm due to some internal error.\n" + "See logs for details.");
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
        TextFieldTableCell.forTableColumn(
            new IntegerStringConverter() {
              @Override
              public Integer fromString(String value) {
                if (StringUtils.isBlank(value)) {
                  return null;
                } else if (!value.matches(numberPattern)) {
                  return null;
                } else {
                  return Integer.valueOf(value);
                }
              }

              @Override
              public String toString(Integer value) {
                return super.toString(value);
              }
            }));
    authorColumn.setCellFactory(param -> new AuthorCell());
    textbookTableView.setOnKeyReleased(
        e -> {
          if (e.getCode().equals(KeyCode.DELETE)) {
            Book book = textbookTableView.getSelectionModel().getSelectedItem();
            if (book != null) {
              confirm.setContentText(
                  "Do you really want to deleteById this book?\n" + "Operation is undoable.");
              Optional<ButtonType> buttonType = confirm.showAndWait();
              if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
                try {
                  bookService.deleteBook(book);
                  textbookTableView.getItems().remove(book);
                } catch (Exception e1) {
                  logger.error(e1);
                  error.setContentText(
                      "Can't deleteById selected book due to some internal error.\n"
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
              Book book = textbookTableView.getItems().get(e.getTablePosition().getRow());
              bookService.updateTitle(book, newTitle);
              book.setTitle(newTitle);
            } catch (Exception e1) {
              logger.error(e1);
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
          if (newEdition == null || newEdition < 1 || newEdition > Short.MAX_VALUE) {
            info.setContentText(
                "Edition should be numeric value in range and should be greater than 0");
            info.showAndWait();
            textbookTableView.refresh();
          } else {
            try {
              Book book = textbookTableView.getItems().get(e.getTablePosition().getRow());
              bookService.updateEdition(book, newEdition);
              book.setEdition(newEdition);
            } catch (Exception e1) {
              logger.error(e1);
              error(
                  "Edition wasn't changed due to some internal error.\n" + "See logs for details.");
            }
          }
        });

    try {
      textbookTableView.setItems(FXCollections.observableArrayList(bookService.getAllBooks()));
    } catch (Exception e) {
      logger.error(e);
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

  private class AuthorCell extends TableCell<Book, List<Author>> {
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
                logger.debug(() -> "Entered authors: " + authorsFromInput.toString());
                commitEdit(authorsFromInput);
              } catch (InputException e1) {
                logger.error(e1);
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
      if (!isEmpty()) {
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
        Book book = getTableView().getItems().get(getTableRow().getIndex());
        List<Author> newAuthors = bookService.setAuthors(book, newValue);
        book.setAuthors(newAuthors);
        updateItem(newAuthors, false);
      } catch (Exception e) {
        logger.error(e);
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

  private class VolumeCell extends TableCell<Book, Integer> {
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
                  Book rowValue = textbookTableView.getItems().get(getIndex());
                  bookService.updateVolume(rowValue, null);
                  rowValue.setVolume(null);
                } catch (Exception e1) {
                  logger.error(e1);
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
        textField.setText(getString());
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
          Book rowValue = textbookTableView.getItems().get(getIndex());
          bookService.updateVolume(rowValue, newValue);
          rowValue.setVolume(newValue);
        } catch (Exception e) {
          logger.error(e);
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
