package com.algorithm.manager.app.controller;

import com.algorithm.manager.auxiliary.LogicException;
import com.algorithm.manager.model.Algorithm;
import com.algorithm.manager.model.Application;
import com.algorithm.manager.model.AreaOfUse;
import com.algorithm.manager.model.Book;
import com.algorithm.manager.model.Reference;
import com.algorithm.manager.app.service.AlgorithmService;
import com.algorithm.manager.app.service.BookService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class ReferenceController extends AbstractController {

  private static final Logger logger = LogManager.getLogger(ReferenceController.class);
  @FXML private ComboBox<Algorithm> areaAlgorithmCB;
  @FXML private Button addApplicationButton;
  @FXML private Button searchApplicationsButton;
  @FXML private Button deleteApplicationButton;
  @FXML private ComboBox<AreaOfUse> areaOfUseCB;
  @FXML private Button createAreaButton;
  @FXML private Button searchAlgorithmsButton;
  @FXML private Button deleteAreaButton;
  @FXML private ListView<String> areaListView;
  @FXML private ComboBox<Algorithm> refAlgorithmCB;
  @FXML private ComboBox<Book> bookCB;
  @FXML private Button createReferenceButton;
  @FXML private Button searchReferencesButton;
  @FXML private Button deleteReferenceButton;
  @FXML private ListView<String> referenceListView;
  private AlgorithmService algorithmService;
  private BookService bookService;
  private NewItemBoxController newArea;

  public ReferenceController(AlgorithmService algorithmService, BookService bookService) {
    this.algorithmService = algorithmService;
    this.bookService = bookService;
  }

  public void initialize() {
    loadData();
    newArea =
        new NewItemBoxController(
            "New area of use box",
            "Enter new area of use parameters",
            "Area of use",
            "Enter area of use",
            "Area description");
    addApplicationButton.setOnAction(
        e -> {
          try {
            Algorithm algorithm = areaAlgorithmCB.getSelectionModel().getSelectedItem();
            AreaOfUse areaOfUse = areaOfUseCB.getSelectionModel().getSelectedItem();
            if (algorithm == null) {
              info.setContentText("Select an algorithm to specify its area of use");
              info.showAndWait();
            } else if (areaOfUse == null) {
              info.setContentText("Select an area of use to connect it to specified algorithm");
              info.showAndWait();
            } else {
              algorithmService.createApplication(algorithm, areaOfUse);
            }
          } catch (LogicException e1) {
            logger.warn(e1);
            super.warn(e1);
          } catch (Exception e1) {
            logger.error(e1);
            super.error();
          }
        });
    searchApplicationsButton.setOnAction(
        e -> {
          try {
            Algorithm algorithm = areaAlgorithmCB.getSelectionModel().getSelectedItem();
            if (algorithm == null) {
              info.setContentText("Select an algorithm to find its practical application");
              info.showAndWait();
            } else {
              displayApplications(algorithm, algorithmService.getApplicationsAlgorithm(algorithm));
            }
          } catch (Exception e1) {
            logger.error(e1);
            super.error();
          }
        });
    deleteApplicationButton.setOnAction(
        e -> {
          try {
            Algorithm algorithm = areaAlgorithmCB.getSelectionModel().getSelectedItem();
            AreaOfUse areaOfUse = areaOfUseCB.getSelectionModel().getSelectedItem();
            if (algorithm == null) {
              info.setContentText("To delete an algorithmic application select an algorithm first");
              info.showAndWait();
            } else if (areaOfUse == null) {
              info.setContentText(
                  "Select an area of use to delete an application of the specified algorithm");
              info.showAndWait();
            } else {
              algorithmService.deleteApplication(algorithm, areaOfUse);
            }
          } catch (LogicException e1) {
            logger.warn(e1);
            super.warn(e1);
          } catch (Exception e1) {
            logger.error(e1);
            super.error();
          }
        });

    createAreaButton.setOnAction(
        e -> {
          try {
            Optional<ButtonType> buttonType = newArea.show();
            if (buttonType.isPresent() && buttonType.get().equals(ButtonType.APPLY)) {
              String name = newArea.getRetrievedName();
              String description = newArea.getRetrievedDescription();
              if (StringUtils.isBlank(name)) {
                info.setContentText(
                    "Area of use should have at least a name.\nSpecify it at area text field.");
                info.showAndWait();
              } else {
                AreaOfUse newArea;
                if (StringUtils.isBlank(description)) {
                  newArea = algorithmService.createAreaOfUse(name.trim());
                } else {
                  newArea = algorithmService.createAreaOfUse(name.trim(), description.trim());
                }
                areaOfUseCB.getItems().add(newArea);
              }
            }
          } catch (LogicException e1) {
            logger.warn(e1);
            super.warn(e1);
          } catch (Exception e1) {
            logger.error(e1);
            super.error();
          }
        });
    searchAlgorithmsButton.setOnAction(
        e -> {
          try {
            AreaOfUse areaOfUse = areaOfUseCB.getSelectionModel().getSelectedItem();
            if (areaOfUse == null) {
              info.setContentText(
                  "To find algorithms for an area of use you have to select it first");
              info.showAndWait();
            } else {
              displayAlgorithms(areaOfUse, algorithmService.getApplicationsByArea(areaOfUse));
            }
          } catch (Exception e1) {
            logger.error(e1);
            super.error();
          }
        });
    deleteAreaButton.setOnAction(
        e -> {
          try {
            AreaOfUse selected = areaOfUseCB.getSelectionModel().getSelectedItem();
            int index = areaOfUseCB.getSelectionModel().getSelectedIndex();
            if (selected == null) {
              info.setContentText(
                  "To delete an area of use you have to select it in area of use combo-box");
              info.showAndWait();
            } else {
              confirm.setContentText(
                  "Do you really want to delete this area of use?\n"
                      + "All algorithms' applications, connected to this area of use, will also be deleted.\n"
                      + "This operation is undoable.");
              Optional<ButtonType> buttonType = confirm.showAndWait();
              if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
                algorithmService.deleteAreaOfUse(selected);
                areaOfUseCB.getItems().remove(index);
              }
            }
          } catch (Exception e1) {
            logger.error(e1);
            super.error();
          }
        });

    createReferenceButton.setOnAction(
        e -> {
          try {
            Algorithm algorithm = refAlgorithmCB.getSelectionModel().getSelectedItem();
            Book book = bookCB.getSelectionModel().getSelectedItem();
            if (algorithm == null) {
              info.setContentText("To persist a reference to an algorithm select it first");
              info.showAndWait();
            } else if (book == null) {
              info.setContentText("To persist a reference you have to select a book");
              info.showAndWait();
            } else {
              bookService.createReference(algorithm, book);
            }
          } catch (LogicException e1) {
            logger.warn(e1);
            super.warn(e1);
          } catch (Exception e1) {
            logger.error(e1);
            super.error();
          }
        });
    searchReferencesButton.setOnAction(
        e -> {
          try {
            Algorithm algorithm = refAlgorithmCB.getSelectionModel().getSelectedItem();
            if (algorithm == null) {
              info.setContentText("Select an algorithm to find its applications");
              info.showAndWait();
            } else {
              displayReferences(algorithm, bookService.getReferences(algorithm));
            }
          } catch (Exception e1) {
            logger.error(e1);
            super.error();
          }
        });
    deleteReferenceButton.setOnAction(
        e -> {
          try {
            Algorithm algorithm = refAlgorithmCB.getSelectionModel().getSelectedItem();
            Book book = bookCB.getSelectionModel().getSelectedItem();
            if (algorithm == null) {
              info.setContentText("To delete a reference select an algorithm first");
              info.showAndWait();
            } else if (book == null) {
              info.setContentText(
                  "To delete a reference to the selected algorithm you have to select a book");
              info.showAndWait();
            } else {
              bookService.deleteReference(algorithm, book);
            }
          } catch (LogicException e1) {
            logger.warn(e1);
            super.warn(e1);
          } catch (Exception e1) {
            logger.error(e1);
            super.error();
          }
        });

    areaAlgorithmCB.setOnMouseClicked(
        e -> {
          try {
            loadAlgorithms();
          } catch (Exception e1) {
            logger.error(e1);
            error.setContentText(
                "List of algorithms isn't synchronized anymore.\nCheck your database connection.");
            error.showAndWait();
          }
        });
    refAlgorithmCB.setOnMouseClicked(
        e -> {
          try {
            loadAlgorithms();
          } catch (Exception e1) {
            logger.error(e1);
            error.setContentText(
                "List of available algorithms isn't synchronized anymore.\n"
                    + "Check your database connection.");
            error.showAndWait();
          }
        });
    areaOfUseCB.setOnMouseClicked(
        e -> {
          try {
            loadAreas();
          } catch (Exception e1) {
            logger.error(e1);
            error.setContentText(
                "List of available areas of use isn't synchronized anymore.\n"
                    + "Check your database connection.");
          }
        });
    bookCB.setOnMouseClicked(
        e -> {
          try {
            loadBooks();
          } catch (Exception e1) {
            logger.error(e1);
            error.setContentText(
                "List of available books isn't synchronized anymore.\n"
                    + "Check your database connection");
            error.showAndWait();
          }
        });

    areaAlgorithmCB.setConverter(new AlgorithmStringConverter());
    refAlgorithmCB.setConverter(new AlgorithmStringConverter());
    areaOfUseCB.setConverter(
        new StringConverter<AreaOfUse>() {
          @Override
          public String toString(AreaOfUse areaOfUse) {
            return areaOfUse == null ? null : areaOfUse.getAreaOfUse();
          }

          @Override
          public AreaOfUse fromString(String string) {
            return areaOfUseCB
                .getItems()
                .stream()
                .filter(a -> string.equalsIgnoreCase(a.getAreaOfUse()))
                .findFirst()
                .orElse(null);
          }
        });

    bookCB.setConverter(
        new StringConverter<Book>() {
          @Override
          public String toString(Book book) {
            if (book == null) {
              return null;
            }
            return book.getTitle()
                + (book.getVolume() == null ? " " : " Vol. " + book.getVolume() + " ")
                + book.getEdition()
                + " edition";
          }

          @Override
          public Book fromString(String string) {
            return bookCB
                .getItems()
                .stream()
                .filter(b -> StringUtils.startsWithIgnoreCase(string, b.getTitle()))
                .findFirst()
                .orElse(null);
          }
        });
  }

  private void displayApplications(Algorithm algorithm, List<Application> areas) {
    ObservableList<String> values = FXCollections.observableArrayList();
    values.add(
        algorithm.getName()
            + (StringUtils.containsIgnoreCase(algorithm.getName(), "algorithm")
                ? " algorithm has "
                : " has ")
            + (areas.size() == 0
                ? "no practical applications specified yet"
                : "following practical applications:"));
    areas.forEach(
        a ->
            values.add(
                "\t"
                    + a.getAreaOfUse().getAreaOfUse()
                    + (StringUtils.isBlank(a.getAreaOfUse().getDescription())
                        ? ""
                        : "\n\t\t(" + a.getAreaOfUse().getDescription() + ")")));
    areaListView.setItems(values);
  }

  private void displayAlgorithms(AreaOfUse area, List<Application> algorithms) {
    ObservableList<String> values = FXCollections.observableArrayList();
    values.add(
        area.getAreaOfUse()
            + (area.getDescription() == null ? " " : " (" + area.getDescription() + ") (")
            + (algorithms.size() == 0
                ? "has no connected algorithms yet"
                : "has following practical algorithmic solutions:"));
    algorithms.forEach(
        a ->
            values.add(
                "\t"
                    + a.getAlgorithm().getName()
                    + (StringUtils.containsIgnoreCase(a.getAlgorithm().getName(), "algorithm")
                        ? " "
                        : " algorithm ")));
    areaListView.setItems(values);
  }

  private void displayReferences(Algorithm algorithm, List<Reference> books) {
    ObservableList<String> values = FXCollections.observableArrayList();
    values.add(
        algorithm.getName()
            + (StringUtils.containsIgnoreCase(algorithm.getName(), "algorithm")
                ? " "
                : " algorithm ")
            + (books.size() == 0
                ? "has no book references specified yet"
                : "is referenced in the following books:"));
    books.forEach(
        t ->
            values.add(
                "\t"
                    + t.getBook().getTitle()
                    + (t.getBook().getVolume() == null ? " " : " Vol. " + t.getBook().getVolume() + " ")
                    + t.getBook().getEdition()
                    + " edition"));
    referenceListView.setItems(values);
  }

  private void loadData() {
    try {
      loadAlgorithms();
      loadAreas();
      loadBooks();
    } catch (Exception e) {
      logger.error(e);
      error.setContentText("Can't load data due to some internal error.\nSee logs for details.");
      error.showAndWait();
    }
  }

  private void loadAlgorithms() throws Exception {
    ObservableList<Algorithm> algorithms =
        FXCollections.observableArrayList(algorithmService.getAllAlgorithms());
    algorithms.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
    areaAlgorithmCB.setItems(algorithms);
    refAlgorithmCB.setItems(algorithms);
  }

  private void loadAreas() throws Exception {
    ObservableList<AreaOfUse> areas =
        FXCollections.observableArrayList(algorithmService.getAllAreas());
    areas.sort((a, b) -> a.getAreaOfUse().compareToIgnoreCase(b.getAreaOfUse()));
    areaOfUseCB.setItems(areas);
  }

  private void loadBooks() throws Exception {
    ObservableList<Book> books = FXCollections.observableArrayList(bookService.getAllBooks());
    books.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
    bookCB.setItems(books);
  }

  private class AlgorithmStringConverter extends StringConverter<Algorithm> {
    @Override
    public String toString(Algorithm algorithm) {
      return algorithm == null ? null : algorithm.getName();
    }

    @Override
    public Algorithm fromString(String string) {
      return areaAlgorithmCB
          .getItems()
          .stream()
          .filter(a -> StringUtils.equalsIgnoreCase(a.getName(), string))
          .findFirst()
          .orElse(null);
    }
  }
}
