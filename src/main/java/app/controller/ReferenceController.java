package app.controller;

import app.auxiliary.LogicException;
import app.model.Algorithm;
import app.model.AreaOfUse;
import app.model.Textbook;
import app.service.AlgorithmService;
import app.service.TextbookService;
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

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ReferenceController extends AbstractController {

  private static final Logger logger = LogManager.getLogger(ReferenceController.class);
  @FXML private ComboBox<Algorithm> areaAlgorithmCB;
  @FXML private Button addAreaButton;
  @FXML private Button searchApplicationsButton;
  @FXML private ComboBox<AreaOfUse> areaOfUseCB;
  @FXML private Button createAreaButton;
  @FXML private Button searchAlgorithmsButton;
  @FXML private Button deleteAreaButton;
  @FXML private ListView<String> areaListView;
  @FXML private ComboBox<Algorithm> refAlgorithmCB;
  @FXML private ComboBox<Textbook> bookCB;
  @FXML private Button createReferenceButton;
  @FXML private Button searchReferencesButton;
  @FXML private Button deleteReferenceButton;
  @FXML private ListView<String> referenceListView;
  private AlgorithmService algorithmService;
  private TextbookService textbookService;
  private NewItemBoxController newArea;

  public ReferenceController(AlgorithmService algorithmService, TextbookService textbookService) {
    this.algorithmService = algorithmService;
    this.textbookService = textbookService;
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
    createAreaButton.setOnAction(
        e -> {
          try {
            Optional<ButtonType> buttonType = newArea.show();
            if (buttonType.isPresent() && buttonType.get().equals(ButtonType.APPLY)) {
              String name = newArea.getRetrievedName();
              String description = newArea.getRetrievedDescription();
              if (StringUtils.isBlank(name)) {

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
          } catch (SQLException e1) {
            logger.error(e1);
            super.error();
          } catch (LogicException e1) {
            logger.warn(e1);
            info.setContentText("Area of use text field should have a name.");
            info.showAndWait();
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
              displayAlgorithms(areaOfUse, algorithmService.getAlgorithmsByArea(areaOfUse));
            }
          } catch (SQLException e1) {
            logger.error(e1);
            super.error();
          }
        });
    addAreaButton.setOnAction(
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
              algorithmService.createAlgorithmApplication(algorithm, areaOfUse);
            }
          } catch (SQLException e1) {
            logger.error(e1);
            super.error();
          } catch (LogicException e1) {
            logger.warn(e1);
            info.setContentText("This algorithm application does already exist");
            info.showAndWait();
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
              displayApplications(algorithm, algorithmService.getAreasByAlgorithm(algorithm));
            }
          } catch (SQLException e1) {
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
          } catch (SQLException e1) {
            logger.error(e1);
            super.error();
          }
        });
    areaAlgorithmCB.setOnMouseReleased(
        e -> {
          try {
            loadAlgorithms();
          } catch (SQLException e1) {
            logger.error(e1);
            error.setContentText(
                "List of algorithms isn't synchronized anymore.\nProbably, database connection is lost");
            error.showAndWait();
          }
        });
    refAlgorithmCB.setOnMouseReleased(
        e -> {
          try {
            loadAlgorithms();
          } catch (SQLException e1) {
            logger.error(e1);
            error.setContentText(
                "List of available algorithms isn't synchronized anymore.\n"
                    + "Check your database connection.");
            error.showAndWait();
          }
        });
    areaOfUseCB.setOnMouseReleased(
        e -> {
          try {
            loadAreas();
          } catch (SQLException e1) {
            logger.error(e1);
            error.setContentText(
                "List of available areas of use isn't synchronized anymore.\n"
                    + "Check your database connection.");
          }
        });
    bookCB.setOnMouseReleased(
        e -> {
          try {
            loadBooks();
          } catch (SQLException e1) {
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
            return areaOfUse.getAreaOfUse();
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
        new StringConverter<Textbook>() {
          @Override
          public String toString(Textbook textbook) {
            if (textbook.getVolume() != null) {
              return textbook.getTitle()
                  + " Vol. "
                  + textbook.getVolume()
                  + " "
                  + textbook.getEdition()
                  + " edition";
            } else {
              return textbook.getTitle() + " " + textbook.getEdition() + " edition";
            }
          }

          @Override
          public Textbook fromString(String string) {
            return bookCB
                .getItems()
                .stream()
                .filter(b -> string.equalsIgnoreCase(b.getTitle()))
                .findFirst()
                .orElse(null);
          }
        });
  }

  private void displayApplications(Algorithm algorithm, List<AreaOfUse> areas) {
    ObservableList<String> values = FXCollections.observableArrayList();
    values.add(
        algorithm.getName()
            + (StringUtils.containsIgnoreCase(algorithm.getName(), "algorithm")
                ? " algorithm has "
                : " has ")
            + (areas.size() == 0
                ? "no practical applications specified yet"
                : "following practical applications:"));
    for (AreaOfUse areaOfUse : areas) {
      values.add(
          "\t"
              + areaOfUse.getAreaOfUse()
              + (StringUtils.isBlank(areaOfUse.getDescription())
                  ? ""
                  : "\n\t\t(" + areaOfUse.getDescription() + ")"));
    }
    areaListView.setItems(values);
  }

  private void displayAlgorithms(AreaOfUse area, List<Algorithm> algorithms) {
    ObservableList<String> values = FXCollections.observableArrayList();
    values.add(
        area.getAreaOfUse()
            + (algorithms.size() == 0
                ? " has no connected algorithms yet"
                : " has following practical algorithmic solutions:"));
    for (Algorithm algorithm : algorithms) {
      values.add(
          "\t"
              + algorithm.getName()
              + (StringUtils.containsIgnoreCase(algorithm.getName(), "algorithm")
                  ? " "
                  : " algorithm "));
    }
    areaListView.setItems(values);
  }

  private void loadData() {
    try {
      loadAlgorithms();
      loadAreas();
      loadBooks();
    } catch (SQLException e) {
      logger.error(e);
      error.setContentText("Can't load data due to some internal error.\nSee logs for details.");
      error.showAndWait();
    }
  }

  private void loadAlgorithms() throws SQLException {
    ObservableList<Algorithm> algorithms =
        FXCollections.observableArrayList(algorithmService.getAllAlgorithms());
    algorithms.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
    areaAlgorithmCB.setItems(algorithms);
    refAlgorithmCB.setItems(algorithms);
  }

  private void loadAreas() throws SQLException {
    ObservableList<AreaOfUse> areas =
        FXCollections.observableArrayList(algorithmService.getAllAreas());
    areas.sort((a, b) -> a.getAreaOfUse().compareToIgnoreCase(b.getAreaOfUse()));
    areaOfUseCB.setItems(areas);
  }

  private void loadBooks() throws SQLException {
    ObservableList<Textbook> textbooks =
        FXCollections.observableArrayList(textbookService.getAllTextbooks());
    textbooks.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
    bookCB.setItems(textbooks);
  }

  private class AlgorithmStringConverter extends StringConverter<Algorithm> {
    @Override
    public String toString(Algorithm algorithm) {
      return algorithm.getName();
    }

    @Override
    public Algorithm fromString(String string) {
      return areaAlgorithmCB
          .getItems()
          .stream()
          .filter(a -> string.equalsIgnoreCase(a.getName()))
          .findFirst()
          .orElse(null);
    }
  }
}
