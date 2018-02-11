package app.auxiliary;

import app.model.Algorithm;
import app.model.Author;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;

import java.util.ArrayList;
import java.util.List;

public class ListTableCell extends TableCell<Algorithm, String> {
  private ListView<String> list;

  public ListTableCell(List<Author> authors){
    List<String> authorsStr = new ArrayList<>();
    authors.forEach(a -> authorsStr.add(a.getFirstName() + " " + a.getLastName()));
    list = new ListView<>(FXCollections.observableArrayList());
  }
}
