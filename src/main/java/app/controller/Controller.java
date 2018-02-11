package app.controller;

import app.model.Algorithm;
import app.model.DesignParadigm;
import app.model.FieldOfStudy;
import app.service.AlgorithmDao;
import app.service.AlgorithmService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
  @FXML private TextbookController controller;
  @FXML private AnchorPane textbookAnchorPane;

  @FXML private TableView<Algorithm> algorithmTableView;
  @FXML private TableColumn<Algorithm, Integer> algoId;
  @FXML private TableColumn<Algorithm, String> algoName;
  @FXML private TableColumn<Algorithm, String> algoComplexity;
  @FXML private TableColumn<Algorithm, String> algoDesignParadigm;
  @FXML private TableColumn<Algorithm, String> algoFieldOfStudy;
  @FXML private TextField algoNameTextField;
  @FXML private TextField algoComplexityTextField;
  @FXML private ComboBox<String> designParadigmCB;
  @FXML private ComboBox<String> fieldOfStudyCB;
  @FXML private Button addParadigmButton;
  @FXML private Button addFieldButton;
  @FXML private Button addAlgorithmButton;
  @FXML private Button searchAlgorithmButton;
  @FXML private Tab textbookTab;

  private List<String> designParadigms;
  private List<String> fieldsOfStudy;

  private NewItemBoxController newParadigm;
  private NewItemBoxController newField;

  public void initialize() {
    try {
      controller = new TextbookController();
      textbookTab.setContent(controller);
    } catch (IOException e) {
      e.printStackTrace();
    }
    initTableView();

    try {
      loadAllAlgorithms(getAlgorithmService());
      loadDesignParadigms(getAlgorithmService());
      loadFieldsOfStudy(getAlgorithmService());
    } catch (SQLException e) {
      e.printStackTrace();
    }
    /*designParadigmCB
        .getEditor()
        .textProperty()
        .addListener(
            (obs, oldText, newText) -> {
              designParadigmCB.setValue(newText);
            });
    designParadigmCB.setOnKeyPressed(
        e -> {
          if (e.getCode().isLetterKey()) {
            String text = designParadigmCB.getEditor().getText();
            System.out.println(text);
            if (text.length() == 0) {
              designParadigmCB.setItems(FXCollections.observableArrayList(designParadigms));
            } else {
              ObservableList<String> intersection = FXCollections.observableArrayList();
              designParadigms.forEach(
                  a -> {
                    if (text.length() > a.length()) {
                      if (a.substring(0, text.length()).equalsIgnoreCase(text)) {
                        intersection.add(a);
                      }
                    }
                  });
              designParadigmCB.setItems(intersection);
            }
            System.out.println("Values: " + designParadigmCB.getItems());
          }
        });*/

    newParadigm = new NewItemBoxController();
    newParadigm.setParameters(
        "New design paradigm",
        "Enter new design paradigm parameters",
        "Design paradigm",
        "Enter design paradigm",
        "Enter paradigm description");
    newField = new NewItemBoxController();
    newField.setParameters(
        "New field of study",
        "Enter field of study parameters",
        "Field of Study",
        "Enter field of study",
        "Enter field's of study description");

    addParadigmButton.setOnAction(
        e -> {
          newParadigm.show();
          String paradigm = newParadigm.getRetrievedName();
          String description = newParadigm.getRetrievedDescription();
          if (StringUtils.isBlank(paradigm)) {
            // TODO: new try
          } else if (StringUtils.isBlank(description)) {
            try {
              getAlgorithmService().insertParadigm(paradigm);
              designParadigmCB.getItems().add(paradigm);
            } catch (SQLException e1) {
              e1.printStackTrace();
            }
          } else {
            try {
              getAlgorithmService().insertParadigm(paradigm, description);
              designParadigmCB.getItems().add(paradigm);
            } catch (SQLException e1) {
              e1.printStackTrace();
            }
          }
        });
    addFieldButton.setOnAction(
        e -> {
          newField.show();
          String paradigm = newField.getRetrievedName().trim();
          String description = newField.getRetrievedDescription().trim();
          if (StringUtils.isBlank(paradigm)) {

          } else if (StringUtils.isBlank(description)) {
            try {
              getAlgorithmService().insertFieldOfStudy(paradigm);
              fieldOfStudyCB.getItems().add(paradigm);
            } catch (SQLException e1) {
              e1.printStackTrace();
            }
          } else {
            try {
              getAlgorithmService().insertFieldOfStudy(paradigm, description);
              fieldOfStudyCB.getItems().add(paradigm);
            } catch (SQLException e1) {
              e1.printStackTrace();
            }
          }
        });
    addAlgorithmButton.setOnAction(
        e -> {
          String name = algoNameTextField.getText().trim();
          String complexity = algoComplexityTextField.getText().trim();
          String designParadigm = designParadigmCB.getSelectionModel().getSelectedItem();
          String fieldOfStudy = fieldOfStudyCB.getSelectionModel().getSelectedItem();
          if (StringUtils.isBlank(name)) {}

          if (StringUtils.isBlank(complexity)) {}

          if (designParadigm == null) {}

          if (fieldOfStudy == null) {}

          try {
            Algorithm algorithm =
                getAlgorithmService()
                    .insertAlgorithm(name, complexity, designParadigm, fieldOfStudy);
            algorithmTableView.getItems().add(algorithm);
            algoNameTextField.setText("");
            algoComplexityTextField.setText("");
            designParadigmCB.getSelectionModel().select(-1);
            fieldOfStudyCB.getSelectionModel().select(-1);
          } catch (SQLException e1) {
            e1.printStackTrace();
          }
        });
    searchAlgorithmButton.setOnAction(
        e -> {
          String algorithm = algoNameTextField.getText();
          String complexity = algoComplexityTextField.getText();
          String designParadigm = designParadigmCB.getSelectionModel().getSelectedItem();
          String fieldOfStudy = fieldOfStudyCB.getSelectionModel().getSelectedItem();
          try {
            List<Algorithm> algorithms =
                getAlgorithmService()
                    .searchAlgorithm(algorithm, complexity, designParadigm, fieldOfStudy);
            algorithmTableView.setItems(FXCollections.observableArrayList(algorithms));
          } catch (SQLException e1) {
            e1.printStackTrace();
          }
        });
  }

  private void initTableView() {
    algorithmTableView.setEditable(true);

    algoId.setMinWidth(200);
    algoName.setMinWidth(200);
    algoComplexity.setMinWidth(300);
    algoDesignParadigm.setMinWidth(300);
    algoFieldOfStudy.setMinWidth(400);

    algoId.setCellValueFactory(new PropertyValueFactory<>("id"));
    algoName.setCellValueFactory(new PropertyValueFactory<>("name"));
    algoComplexity.setCellValueFactory(new PropertyValueFactory<>("complexity"));
    algoDesignParadigm.setCellValueFactory(new PropertyValueFactory<>("designParadigm"));
    algoFieldOfStudy.setCellValueFactory(new PropertyValueFactory<>("fieldOfStudy"));

    algoId.setEditable(false);
    algoName.setCellFactory(TextFieldTableCell.forTableColumn());
    algoComplexity.setCellFactory(TextFieldTableCell.forTableColumn());
    algoDesignParadigm.setCellFactory(TextFieldTableCell.forTableColumn());
    algoFieldOfStudy.setCellFactory(TextFieldTableCell.forTableColumn());

    algorithmTableView.setOnKeyPressed(
        e -> {
          /*System.out.println(e.getText());
          System.out.println(e.getText().length());
          e.getCode().is*/
          Algorithm selectedRow = algorithmTableView.getSelectionModel().getSelectedItem();
          if (selectedRow != null) {
            if (e.getCode().equals(KeyCode.DELETE)) {
              // TODO: ask whether wants to delete
              try {
                getAlgorithmService().deleteItem("algorithm", "algorithm_id", selectedRow.getId());
                algorithmTableView.getItems().remove(selectedRow);
              } catch (SQLException e1) {
                // TODO: alert
                e1.printStackTrace();
              }
            }
          }
        });
    algoName.setOnEditCommit(
        e -> {
          Algorithm algorithm = e.getRowValue();
          try {
            String newName = e.getNewValue().trim();
            if (getAlgorithmService().getAlgorithmByName(newName) != null) {
              // TODO: alert
              algorithmTableView.refresh();
            } else {
              getAlgorithmService()
                  .updateEntry(
                      "algorithm", "algorithm", newName, "algorithm_id", algorithm.getId());
              algorithmTableView
                  .getItems()
                  .get(e.getTablePosition().getRow())
                  .setName(e.getOldValue());
            }
          } catch (SQLException e1) {
            // TODO: alert
            e1.printStackTrace();
            algorithmTableView
                .getItems()
                .get(e.getTablePosition().getRow())
                .setName(e.getOldValue());
            algorithmTableView.refresh();
          }
        });
    algoComplexity.setOnEditCommit(
        e -> {
          try {
            String newValue = e.getNewValue().trim();
            getAlgorithmService()
                .updateEntry(
                    "algorithm", "algorithm", newValue, "algorithm_id", e.getRowValue().getId());
            algorithmTableView
                .getItems()
                .get(e.getTablePosition().getRow())
                .setComplexity(newValue);
          } catch (SQLException e1) {
            // TODO: alert
            e1.printStackTrace();
            algorithmTableView
                .getItems()
                .get(e.getTablePosition().getRow())
                .setComplexity(e.getOldValue());
            algorithmTableView.refresh();
          }
        });
    algoDesignParadigm.setOnEditCommit(
        e -> {
          try {
            String newValue = e.getNewValue().trim();
            String oldValue = e.getOldValue();
            if (getAlgorithmService().getParadigmByName(newValue) != null) {
              // TODO: alert
              algorithmTableView.refresh();
            } else {
              getAlgorithmService()
                  .updateEntry(
                      "design_paradigm", "paradigm", newValue, "paradigm", e.getOldValue());
              updateTableView(
                  Algorithm.class,
                  algorithmTableView.getItems(),
                  "designParadigm",
                  e.getOldValue(),
                  newValue);
              algorithmTableView.refresh();
              List<String> paradigms = designParadigmCB.getItems();
              int position = paradigms.indexOf(oldValue);
              // TODO: if position == -1
              paradigms.set(position, newValue);
            }
          } catch (SQLException e1) {
            // TODO: alert
            e1.printStackTrace();
            algorithmTableView.refresh();
          } catch (InvocationTargetException e1) {
            e1.printStackTrace();
          }
        });
    algoFieldOfStudy.setOnEditCommit(
        e -> {
          try {
            String newValue = e.getNewValue().trim();
            String oldValue = e.getOldValue();
            if (getAlgorithmService().getFieldByName(newValue) != null) {
              // TODO: alert
              algorithmTableView.refresh();
            } else {
              getAlgorithmService()
                  .updateEntry("field_of_study", "field", newValue, "field", e.getOldValue());
              updateTableView(
                  Algorithm.class,
                  algorithmTableView.getItems(),
                  "fieldOfStudy",
                  e.getOldValue(),
                  newValue);
              algorithmTableView.refresh();
              List<String> fields = fieldOfStudyCB.getItems();
              int position = fields.indexOf(oldValue);
              // TODO: if position == -1
              fields.set(position, newValue);
            }
          } catch (SQLException e1) {
            // TODO: alert
            e1.printStackTrace();
            algorithmTableView.refresh();
          } catch (InvocationTargetException e1) {
            e1.printStackTrace();
          }
        });
  }

  private <T> void updateTableView(
      Class<T> tClass, List<T> items, String fieldName, Object oldValue, Object newValue)
      throws InvocationTargetException {
    try {
      String getter = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
      String setter = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
      Method getterMethod = tClass.getMethod(getter);
      Method setterMethod = tClass.getMethod(setter, newValue.getClass());
      if (!oldValue.getClass().equals(getterMethod.getReturnType())
          || setterMethod.getParameterCount() != 1
          || !newValue.getClass().equals(setterMethod.getParameterTypes()[0])) {
        throw new IllegalAccessError();
      }
      for (T item : items) {
        if (oldValue.equals(getterMethod.invoke(item))) {
          setterMethod.invoke(item, newValue);
        }
      }
    } catch (NoSuchMethodException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  private AlgorithmService getAlgorithmService() throws SQLException {
    return AlgorithmDao.getDao();
  }

  private void loadAllAlgorithms(AlgorithmService service) throws SQLException {
    List<Algorithm> algorithms = service.getAlgorithms();
    ObservableList<Algorithm> observableList = FXCollections.observableArrayList(algorithms);
    algorithmTableView.setItems(observableList);
  }

  private void loadDesignParadigms(AlgorithmService service) throws SQLException {
    List<DesignParadigm> paradigms = service.getDesignParadigms();
    ObservableList<String> designParadigms = FXCollections.observableArrayList();
    paradigms.forEach(paradigm -> designParadigms.add(paradigm.getParadigm()));
    designParadigms.sort(String::compareToIgnoreCase);
    designParadigmCB.setItems(FXCollections.observableArrayList(designParadigms));
  }

  private void loadFieldsOfStudy(AlgorithmService service) throws SQLException {
    List<FieldOfStudy> fields = service.getFieldsOfStudy();
    fieldsOfStudy = new ArrayList<>();
    fields.forEach(field -> fieldsOfStudy.add(field.getField()));
    fieldsOfStudy.sort(String::compareToIgnoreCase);
    fieldOfStudyCB.setItems(FXCollections.observableArrayList(fieldsOfStudy));
  }

  /*private class KeyHandler implements EventHandler<KeyEvent> {
    ObservableList<String> items;
    ObservableList<String> displayedItems;
    private ComboBox<String> comboBox;
    private String filterString;

    public KeyHandler(ComboBox<String> comboBox, ObservableList<String> items) {
      this.comboBox = comboBox;
      this.items = items;
      this.displayedItems = FXCollections.observableArrayList();
      this.filterString = "";
    }



    @Override
    public void handle(KeyEvent event) {
      String text = event.getText();
      if (event.getCode() == KeyCode.BACK_SPACE && filterString.length() > 0) {
        filterString = filterString.substring(0, filterString.length() - 1);
        if (filterString.length() == 0) {
          comboBox.setItems(items);
          comboBox.hide();
          comboBox.setVisibleRowCount(Math.min(items.size(), 10));
          comboBox.show();
          System.out.println(comboBox.getItems());
        } else {
          filter();
        }
      } else if (!StringUtils.isBlank(text) || text.matches("[ \t]")) {
        filterString += text;
        filter();
      }
    }

    private void filter() {
      displayedItems.clear();
      for (String item : items) {
        if (item.length() >= filterString.length()
            && item.toLowerCase().startsWith(filterString.toLowerCase())) {
          displayedItems.add(item);
        }
      }
      comboBox.setItems(displayedItems);
      comboBox.hide();
      comboBox.setVisibleRowCount(Math.min(10, displayedItems.size()));
      comboBox.show();
    }
  }*/
}
