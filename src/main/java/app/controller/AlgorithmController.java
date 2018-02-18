package app.controller;

import app.auxiliary.LogicException;
import app.model.Algorithm;
import app.model.DesignParadigm;
import app.model.FieldOfStudy;
import app.service.AlgorithmService;
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
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AlgorithmController extends AbstractController {
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
            return object == null ? null : object.getParadigm();
          }

          @Override
          public DesignParadigm fromString(String string) {
            return designParadigmCB
                .getItems()
                .stream()
                .filter(dp -> StringUtils.equalsIgnoreCase(dp.getParadigm(), string))
                .findFirst()
                .orElse(null);
          }
        });
    fieldOfStudyCB.setConverter(
        new StringConverter<FieldOfStudy>() {
          @Override
          public String toString(FieldOfStudy object) {
            return object == null ? null : object.getField();
          }

          @Override
          public FieldOfStudy fromString(String string) {
            return fieldOfStudyCB
                .getItems()
                .stream()
                .filter(fos -> StringUtils.equalsIgnoreCase(fos.getField(), string))
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
      logger.error(e);
      info.setContentText("Can't load algorithms' data. Check server connection.");
      info.showAndWait();
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

    newParadigm =
        new NewItemBoxController(
            "New design paradigm",
            "Enter new design paradigm parameters",
            "Design paradigm",
            "Enter design paradigm",
            "Enter paradigm description");
    newField =
        new NewItemBoxController(
            "New field of study",
            "Enter field of study parameters",
            "Field of Study",
            "Enter field of study",
            "Enter field's of study description");

    addParadigmButton.setOnAction(
        e -> {
          try {
            Optional<ButtonType> buttonType = newParadigm.show();
            if (buttonType.isPresent() && buttonType.get().equals(ButtonType.APPLY)) {
              String retrievedName = newParadigm.getRetrievedName();
              String retrievedDescription = newParadigm.getRetrievedDescription();
              if (StringUtils.isBlank(retrievedName)) {
                info.setContentText("Design paradigm must have a name");
                info.showAndWait();
              } else {
                DesignParadigm designParadigm;
                if (StringUtils.isBlank(retrievedDescription)) {
                  designParadigm = algorithmService.createDesignParadigm(retrievedName.trim());
                } else {
                  designParadigm =
                      algorithmService.createDesignParadigm(
                          retrievedName.trim(), retrievedDescription.trim());
                }
                designParadigmCB.getItems().add(designParadigm);
              }
            }
          } catch (SQLException e1) {
            logger.error(e1);
            super.error();
          } catch (LogicException e1) {
            logger.warn(e1);
            info.setContentText("Addition of the new design paradigm failed:\n" + e1.getMessage());
            info.showAndWait();
          }
        });
    addFieldButton.setOnAction(
        e -> {
          try {
            Optional<ButtonType> buttonType = newField.show();
            if (buttonType.isPresent() && buttonType.get().equals(ButtonType.APPLY)) {
              String retrievedName = newField.getRetrievedName();
              String retrievedDescription = newField.getRetrievedDescription();
              if (StringUtils.isBlank(retrievedName)) {
                info.setContentText("Field of study must have a new name");
                info.showAndWait();
              } else {
                FieldOfStudy fieldOfStudy;
                if (StringUtils.isBlank(retrievedDescription)) {
                  fieldOfStudy = algorithmService.createFieldOfStudy(retrievedName.trim());
                } else {
                  fieldOfStudy =
                      algorithmService.createFieldOfStudy(
                          retrievedName.trim(), retrievedDescription.trim());
                }
                fieldOfStudyCB.getItems().add(fieldOfStudy);
              }
            }
          } catch (SQLException e1) {
            logger.error(e1);
            error.setContentText(
                "Addition of the new field of study failed due to some internal error.\n"
                    + "See logs for details.");
            error.showAndWait();
          } catch (LogicException e1) {
            logger.warn(e1);
            info.setContentText("Addition of new field of study failed:\n" + e1.getMessage());
            info.showAndWait();
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
              info.setContentText("Algorithm must have a name");
              info.showAndWait();
            } else if (StringUtils.isBlank(complexity)) {
              info.setContentText("Algorithm must have a complexity");
              info.showAndWait();
            } else if (designParadigm == null) {
              info.setContentText("Algorithm must have a design paradigm");
              info.showAndWait();
            } else if (fieldOfStudy == null) {
              info.setContentText("Algorithm must have a field of study");
              info.showAndWait();
            } else {
              Algorithm algorithm =
                  algorithmService.createAlgorithm(name, complexity, designParadigm, fieldOfStudy);
              algorithmTableView.getItems().add(algorithm);
              algorithmTableView.scrollTo(algorithmTableView.getItems().size() - 1);
            }
          } catch (SQLException e1) {
            logger.error(e1);
            error.setContentText(
                "Addition of a new algorithm failed failed due to some internal error.\n"
                    + "See logs for details.");
          } catch (LogicException e1) {
            logger.warn(e1);
            info.setContentText("Addition of a new algorithm failed:\n" + e1.getMessage());
            info.showAndWait();
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
            logger.error(e1);
            error.setContentText("Search failed due to some internal error.\nSee logs for details");
            error.showAndWait();
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
              confirm.setContentText(
                  "Do you really want to delete this algorithm?\n" + "This operation is undoable");
              Optional<ButtonType> buttonType = confirm.showAndWait();
              if (buttonType.isPresent() && buttonType.get() == ButtonType.OK) {
                try {
                  algorithmService.deleteAlgorithm(algorithm);
                  algorithmTableView.getItems().remove(algorithm);
                } catch (SQLException e1) {
                  logger.error(e1);
                  error.setContentText(
                      "Deletion of the algorithm failed due to some internal error.\n"
                          + "See logs for details.");
                  error.showAndWait();
                }
              }
            }
          }
        });
    algoName.setOnEditCommit(
        e -> {
          Algorithm algorithm = e.getRowValue();
          String newValue = e.getNewValue();
          if (StringUtils.isBlank(newValue)) {
            info.setContentText("Algorithm's name can't be blank");
            info.showAndWait();
            algorithmTableView.refresh();
          } else {
            try {
              newValue = newValue.trim();
              algorithmService.updateAlgorithmName(algorithm, newValue);
              algorithm.setName(newValue);
            } catch (SQLException e1) {
              logger.error(e1);
              error.setContentText(
                  "Algorithm's name wasn't changed due to some internal error.\n"
                      + "See logs for details.");
              error.showAndWait();
              algorithmTableView.refresh();
            } catch (LogicException e1) {
              logger.warn(e1);
              info.setContentText("Algorithm's name wasn't changed:\n" + e1.getMessage());
              info.showAndWait();
            }
          }
        });
    algoComplexity.setOnEditCommit(
        e -> {
          Algorithm algorithm = e.getRowValue();
          String newValue = e.getNewValue();
          if (StringUtils.isBlank(newValue)) {
            info.setContentText("Algorithm's complexity can't be blank");
            info.showAndWait();
            algorithmTableView.refresh();
          }
          try {
            newValue = newValue.trim();
            algorithmService.updateAlgorithmComplexity(e.getRowValue(), newValue);
            algorithm.setComplexity(newValue);
          } catch (SQLException e1) {
            logger.error(e1);
            error.setContentText(
                "Algorithm's complexity wasn't changed due to some internal error.\n"
                    + "See logs for details.");
            error.showAndWait();
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

    FieldCell() {
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
      if (StringUtils.isBlank(newValue.getField())) {
        info.setContentText("Algorithm's field of study can't be blank");
        info.showAndWait();
        algorithmTableView.refresh();
      } else {
        String newFieldOfStudy = newValue.getField().trim();
        FieldOfStudy oldValue = getItem();
        try {
          Optional<FieldOfStudy> field = algorithmService.getFieldByName(newFieldOfStudy);
          if (field.isPresent()) {
            Algorithm rowAlgorithm = algorithmTableView.getItems().get(getTableRow().getIndex());
            algorithmService.setFieldOfStudy(rowAlgorithm, field.get());
            rowAlgorithm.setFieldOfStudy(field.get());
          } else {
            FieldOfStudy fieldOfStudy = algorithmService.updateFieldName(oldValue, newFieldOfStudy);
            AlgorithmController.this.updateTableView(
                Algorithm.class,
                algorithmTableView.getItems(),
                "fieldOfStudy",
                oldValue,
                fieldOfStudy);
            AlgorithmController.this.updateList(fieldOfStudyCB.getItems(), oldValue, fieldOfStudy);
            algorithmTableView.refresh();
          }
        } catch (SQLException e1) {
          logger.error(e1);
          error.setContentText(
              "Algorithm's design paradigm wasn't changed due to some internal error.\n"
                  + "See logs for details.");
          error.showAndWait();
          setText(oldValue.getField());
          setGraphic(null);
          algorithmTableView.refresh();
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
      FieldOfStudy item = getItem();
      return item == null ? "None" : item.getField();
    }
  }

  private class ParadigmCell extends TableCell<Algorithm, DesignParadigm> {
    private TextField textField;

    ParadigmCell() {
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
      if (StringUtils.isBlank(newValue.getParadigm())) {
        info.setContentText("Algorithm's design paradigm can't be blank");
        info.showAndWait();
        algorithmTableView.refresh();
      } else {
        String newName = newValue.getParadigm().trim();
        DesignParadigm oldValue = getItem();
        try {
          Optional<DesignParadigm> paradigm = algorithmService.getParadigmByName(newName);
          if (paradigm.isPresent()) {
            Algorithm rowAlgorithm = algorithmTableView.getItems().get(getTableRow().getIndex());
            algorithmService.setDesignParadigm(rowAlgorithm, paradigm.get());
            rowAlgorithm.setDesignParadigm(paradigm.get());
          } else {
            DesignParadigm designParadigm =
                algorithmService.updateDesignParadigm(oldValue, newValue.getParadigm());
            AlgorithmController.this.updateTableView(
                Algorithm.class,
                algorithmTableView.getItems(),
                "designParadigm",
                oldValue,
                designParadigm);
            AlgorithmController.this.updateList(
                designParadigmCB.getItems(), oldValue, designParadigm);
            algorithmTableView.refresh();
          }
        } catch (SQLException e1) {
          logger.error(e1);
          error.setContentText(
              "Algorithm's design paradigm wasn't changed due to some internal error.\n"
                  + "See logs for details.");
          setText(oldValue.getParadigm());
          setGraphic(null);
          algorithmTableView.refresh();
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
      DesignParadigm item = getItem();
      return item == null ? "None" : item.getParadigm();
    }
  }
}
