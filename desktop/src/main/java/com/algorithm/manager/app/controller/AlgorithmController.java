package com.algorithm.manager.app.controller;

import com.algorithm.manager.service.AlgorithmService;
import com.algorithm.manager.auxiliary.LogicException;
import com.algorithm.manager.model.Algorithm;
import com.algorithm.manager.model.DesignParadigm;
import com.algorithm.manager.model.FieldOfStudy;
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
            return object == null ? null : object.getName();
          }

          @Override
          public DesignParadigm fromString(String string) {
            return designParadigmCB
                .getItems()
                .stream()
                .filter(dp -> StringUtils.equalsIgnoreCase(dp.getName(), string))
                .findFirst()
                .orElse(null);
          }
        });
    fieldOfStudyCB.setConverter(
        new StringConverter<FieldOfStudy>() {
          @Override
          public String toString(FieldOfStudy object) {
            return object == null ? null : object.getName();
          }

          @Override
          public FieldOfStudy fromString(String string) {
            return fieldOfStudyCB
                .getItems()
                .stream()
                .filter(fos -> StringUtils.equalsIgnoreCase(fos.getName(), string))
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
    } catch (Exception e) {
      logger.error(e);
      info("Can't load algorithms' data. Check server connection.");
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
                info("Design paradigm must have a name");
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
          } catch (LogicException e1) {
            logger.warn(e1);
            info("Addition of the new design paradigm failed:\n" + e1.getMessage());
          } catch (Exception e1) {
            logger.error(e1);
            super.error();
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
                info("Field of study must have a new name");
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
          } catch (LogicException e1) {
            logger.warn(e1);
            info("Addition of new field of study failed:\n" + e1.getMessage());
          } catch (Exception e1) {
            logger.error(e1);
            error.setContentText(
                "Addition of the new field of study failed due to some internal error.\n"
                    + "See logs for details.");
            error.showAndWait();
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
              info("Algorithm must have a name");
            } else if (StringUtils.isBlank(complexity)) {
              info("Algorithm must have a complexity");
            } else if (designParadigm == null) {
              info("Algorithm must have a design paradigm");
            } else if (fieldOfStudy == null) {
              info("Algorithm must have a field of study");
            } else {
              Algorithm algorithm =
                  algorithmService.persistAlgorithm(name, complexity, designParadigm, fieldOfStudy);
              algorithmTableView.getItems().add(algorithm);
              algorithmTableView.scrollTo(algorithmTableView.getItems().size() - 1);
            }
          } catch (LogicException e1) {
            logger.warn(e1);
            info("Addition of a new algorithm failed:\n" + e1.getMessage());
          } catch (Exception e1) {
            logger.error(e1);
            error.setContentText(
                "Addition of a new algorithm failed failed due to some internal error.\n"
                    + "See logs for details.");
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
          } catch (Exception e1) {
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
                  setText(item.getName());
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
                  setText(item.getName());
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
                  "Do you really want to deleteById this algorithm?\n" + "This operation is undoable");
              Optional<ButtonType> buttonType = confirm.showAndWait();
              if (buttonType.isPresent() && buttonType.get() == ButtonType.OK) {
                try {
                  algorithmService.deleteAlgorithm(algorithm);
                  algorithmTableView.getItems().remove(algorithm);
                } catch (Exception e1) {
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
            info("Algorithm's name can't be blank");
            algorithmTableView.refresh();
          } else {
            try {
              newValue = newValue.trim();
              algorithmService.setAlgorithmName(algorithm, newValue);
              algorithm.setName(newValue);
            } catch (LogicException e1) {
              logger.warn(e1);
              info("Algorithm's name wasn't changed:\n" + e1.getMessage());
            } catch (Exception e1) {
              logger.error(e1);
              error.setContentText(
                  "Algorithm's name wasn't changed due to some internal error.\n"
                      + "See logs for details.");
              error.showAndWait();
              algorithmTableView.refresh();
            }
          }
        });
    algoComplexity.setOnEditCommit(
        e -> {
          Algorithm algorithm = e.getRowValue();
          String newValue = e.getNewValue();
          if (StringUtils.isBlank(newValue)) {
            info("Algorithm's complexity can't be blank");
            algorithmTableView.refresh();
          }
          try {
            newValue = newValue.trim();
            algorithmService.setAlgorithmComplexity(e.getRowValue(), newValue);
            algorithm.setComplexity(newValue);
          } catch (Exception e1) {
            logger.error(e1);
            error.setContentText(
                "Algorithm's complexity wasn't changed due to some internal error.\n"
                    + "See logs for details.");
            error.showAndWait();
          }
        });
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
                    new FieldOfStudy(textField.getText(), field.getDescription()));
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
          setText(item.getName());
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
      if (StringUtils.isBlank(newValue.getName())) {
        info("Algorithm's field of study can't be blank");
        algorithmTableView.refresh();
      } else {
        try {
          Algorithm rowAlgorithm = algorithmTableView.getItems().get(getTableRow().getIndex());
          algorithmService.setFieldOfStudy(rowAlgorithm, newValue);
        } catch (Exception e1) {
          logger.error(e1);
          error.setContentText(
              "Algorithm's design paradigm wasn't changed due to some internal error.\n"
                  + "See logs for details.");
          error.showAndWait();
          setText(getItem().getName());
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
      return item == null ? "None" : item.getName();
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
                    new DesignParadigm(textField.getText(), paradigm.getDescription()));
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
          setText(item.getName());
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
      if (StringUtils.isBlank(newValue.getName())) {
        info("Algorithm's design paradigm can't be blank");
        algorithmTableView.refresh();
      } else {
        try {
          Algorithm rowAlgorithm = algorithmTableView.getItems().get(getTableRow().getIndex());
          algorithmService.setDesignParadigm(rowAlgorithm, newValue);
        } catch (Exception e1) {
          logger.error(e1);
          error.setContentText(
              "Algorithm's design paradigm wasn't changed due to some internal error.\n"
                  + "See logs for details.");
          setText(getItem().getName());
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
      return item == null ? "None" : item.getName();
    }
  }
}
