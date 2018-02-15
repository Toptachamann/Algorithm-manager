package app.controller;

import app.model.Algorithm;
import app.model.DesignParadigm;
import app.model.FieldOfStudy;
import app.service.AlgorithmService;
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
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

public class AlgorithmController {
  private static final Logger logger = LogManager.getLogger(AlgorithmController.class);

  private AlgorithmService algorithmService;

  @FXML private TableView<Algorithm> algorithmTableView;
  @FXML private TableColumn<Algorithm, Integer> algoId;
  @FXML private TableColumn<Algorithm, String> algoName;
  @FXML private TableColumn<Algorithm, String> algoComplexity;
  @FXML private TableColumn<Algorithm, DesignParadigm> algoDesignParadigm;
  @FXML private TableColumn<Algorithm, FieldOfStudy> algoFieldOfStudy;
  @FXML private TextField algoNameTextField;
  @FXML private TextField algoComplexityTextField;
  @FXML private ComboBox<DesignParadigm> designParadigmCB;
  @FXML private ComboBox<FieldOfStudy> fieldOfStudyCB;
  @FXML private Button addParadigmButton;
  @FXML private Button addFieldButton;
  @FXML private Button addAlgorithmButton;
  @FXML private Button searchAlgorithmButton;

  private NewItemBoxController newParadigm;
  private NewItemBoxController newField;

  public AlgorithmController(AlgorithmService algorithmService) {
    this.algorithmService = algorithmService;
  }

  public void initialize() {

    initTableView();

    designParadigmCB.setConverter(
        new StringConverter<DesignParadigm>() {
          @Override
          public String toString(DesignParadigm object) {
            return object.getParadigm();
          }

          @Override
          public DesignParadigm fromString(String string) {
            return designParadigmCB
                .getItems()
                .stream()
                .filter(dp -> dp.getParadigm().equalsIgnoreCase(string))
                .findFirst()
                .orElse(null);
          }
        });
    fieldOfStudyCB.setConverter(
        new StringConverter<FieldOfStudy>() {
          @Override
          public String toString(FieldOfStudy object) {
            return object.getField();
          }

          @Override
          public FieldOfStudy fromString(String string) {
            return fieldOfStudyCB
                .getItems()
                .stream()
                .filter(fos -> fos.getField().equals(string))
                .findFirst()
                .orElse(null);
          }
        });

    try {
      algorithmTableView.setItems(
          FXCollections.observableArrayList(algorithmService.getAllAlgorithms()));
      designParadigmCB.setItems(
          FXCollections.observableArrayList(algorithmService.getDesignParadigms()));
      fieldOfStudyCB.setItems(
          FXCollections.observableArrayList(algorithmService.getAllFieldsOfStudy()));
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
          try {
            newParadigm.show();
            DesignParadigm paradigm =
                algorithmService.insertDesignParadigm(
                    newParadigm.getRetrievedName(), newParadigm.getRetrievedDescription());
            designParadigmCB.getItems().add(paradigm);
          } catch (SQLException e1) {
            e1.printStackTrace();
          }
        });
    addFieldButton.setOnAction(
        e -> {
          try {
            newField.show();
            FieldOfStudy field =
                algorithmService.insertFieldOfStudy(
                    newField.getRetrievedName(), newField.getRetrievedDescription());
            fieldOfStudyCB.getItems().add(field);
          } catch (SQLException e1) {
            e1.printStackTrace();
          }
        });
    addAlgorithmButton.setOnAction(
        e -> {
          try {
            String name = algoNameTextField.getText();
            String complexity = algoComplexityTextField.getText();
            DesignParadigm designParadigm = designParadigmCB.getSelectionModel().getSelectedItem();
            FieldOfStudy fieldOfStudy = fieldOfStudyCB.getSelectionModel().getSelectedItem();
            if (StringUtils.isBlank(name)) {
              // TODO: alert
            }
            if (StringUtils.isBlank(complexity)) {
              // TODO: alert
            }
            if (designParadigm == null) {
              // TODO: alert
            }
            if (fieldOfStudy == null) {
              // TODO: alert
            }
            Algorithm algorithm =
                algorithmService.createAlgorithm(name, complexity, designParadigm, fieldOfStudy);
            algorithmTableView.getItems().add(algorithm);
          } catch (SQLException e1) {
            e1.printStackTrace();
          }
        });
    searchAlgorithmButton.setOnAction(
        e -> {
          try {
            List<Algorithm> algorithms =
                algorithmService.searchAlgorithm(
                    algoNameTextField.getText(),
                    algoComplexityTextField.getText(),
                    designParadigmCB.getSelectionModel().getSelectedItem(),
                    fieldOfStudyCB.getSelectionModel().getSelectedItem());
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

    algoDesignParadigm.setCellFactory(
        param ->
            new TableCell<Algorithm, DesignParadigm>() {
              @Override
              protected void updateItem(DesignParadigm item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                  setText(null);
                } else {
                  setText(item.getParadigm());
                }
              }
            });
    algoFieldOfStudy.setCellFactory(
        param ->
            new TableCell<Algorithm, FieldOfStudy>() {
              @Override
              protected void updateItem(FieldOfStudy item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                  setText(null);
                } else {
                  setText(item.getField());
                }
              }
            });

    algoId.setEditable(false);
    algoName.setCellFactory(TextFieldTableCell.forTableColumn());
    algoComplexity.setCellFactory(TextFieldTableCell.forTableColumn());
    algoDesignParadigm.setCellFactory(param -> new ParadigmCell());
    algoFieldOfStudy.setCellFactory(param -> new FieldCell());

    algorithmTableView.setOnKeyReleased(
        e -> {
          Algorithm algorithm = algorithmTableView.getSelectionModel().getSelectedItem();
          if (algorithm != null) {
            if (e.getCode().equals(KeyCode.DELETE)) {
              // TODO: ask whether wants to delete
              try {
                algorithmService.deleteItem(algorithm);
                algorithmTableView.getItems().remove(algorithm);
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
          String newValue = e.getNewValue();
          if (StringUtils.isBlank(newValue)) {
            // TODO alert
          } else {
            try {
              newValue = newValue.trim();
              algorithmService.updateAlgorithmName(algorithm, newValue);
              algorithm.setName(newValue);
            } catch (SQLException e1) {
              // TODO: alert
              e1.printStackTrace();
              algorithmTableView.refresh();
            }
          }
        });
    algoComplexity.setOnEditCommit(
        e -> {
          Algorithm algorithm = e.getRowValue();
          String newValue = e.getNewValue();
          if(StringUtils.isBlank(newValue)){
            //TODO alert
          }
          try {
            newValue = newValue.trim();
            algorithmService.updateAlgorithmComplexity(e.getRowValue(), newValue);
            algorithm.setComplexity(newValue);
          } catch (SQLException e1) {
            // TODO: alert
            e1.printStackTrace();
          }
        });
  }

  private <T> void updateTableView(
      Class<T> tClass, List<T> items, String fieldName, Object oldValue, Object newValue) {
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
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  private <T> void updateList(List<T> list, T oldValue, T newValue) {
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).equals(oldValue)) {
        list.set(i, newValue);
      }
    }
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

  private class FieldCell extends TableCell<Algorithm, FieldOfStudy> {
    private TextField textField;

    public FieldCell() {
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
            FieldOfStudy field;
            if ((field = getItem()) != null) {
              if (e.getCode().equals(KeyCode.ENTER)) {
                commitEdit(
                    new FieldOfStudy(field.getId(), textField.getText(), field.getDescription()));
              }
            }
          });
    }

    @Override
    protected void updateItem(FieldOfStudy item, boolean empty) {
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
          setText(item.getField());
        }
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
    public void commitEdit(FieldOfStudy newValue) {
      super.commitEdit(newValue);
      FieldOfStudy oldValue = getItem();
      try {
        algorithmService.updateFieldOfStudy(oldValue, newValue.getField());
        AlgorithmController.this.updateTableView(
            Algorithm.class, algorithmTableView.getItems(), "fieldOfStudy", oldValue, newValue);
        AlgorithmController.this.updateList(fieldOfStudyCB.getItems(), oldValue, newValue);
        algorithmTableView.refresh();
      } catch (SQLException e1) {
        // TODO: alert
        e1.printStackTrace();
        setText(oldValue.getField());
        setGraphic(null);
        algorithmTableView.refresh();
      }
    }

    @Override
    public void cancelEdit() {
      super.cancelEdit();
      setGraphic(null);
      setText(getString());
    }

    private String getString() {
      FieldOfStudy item = getItem();
      return item == null ? "None" : item.getField();
    }
  }

  private class ParadigmCell extends TableCell<Algorithm, DesignParadigm> {
    private TextField textField;

    public ParadigmCell() {
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
            DesignParadigm paradigm;
            if ((paradigm = getItem()) != null) {
              if (e.getCode().equals(KeyCode.ENTER)) {
                commitEdit(
                    new DesignParadigm(
                        paradigm.getId(), textField.getText(), paradigm.getDescription()));
              }
            }
          });
    }

    @Override
    protected void updateItem(DesignParadigm item, boolean empty) {
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
          setText(item.getParadigm());
        }
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
    public void commitEdit(DesignParadigm newValue) {
      logger.trace("Committing changes, new value {}", newValue);
      super.commitEdit(newValue);
      DesignParadigm oldValue = getItem();
      try {
        algorithmService.updateDesignParadigm(oldValue, newValue.getParadigm());
        AlgorithmController.this.updateTableView(
            Algorithm.class, algorithmTableView.getItems(), "designParadigm", oldValue, newValue);
        AlgorithmController.this.updateList(designParadigmCB.getItems(), oldValue, newValue);
        algorithmTableView.refresh();
      } catch (SQLException e1) {
        // TODO: alert
        e1.printStackTrace();
        setText(oldValue.getParadigm());
        setGraphic(null);
        algorithmTableView.refresh();
      }
    }

    @Override
    public void cancelEdit() {
      super.cancelEdit();
      setGraphic(null);
      setText(getString());
    }

    private String getString() {
      DesignParadigm item = getItem();
      return item == null ? "None" : item.getParadigm();
    }
  }
}
