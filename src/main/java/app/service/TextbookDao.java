package app.service;

import app.auxiliary.Connector;
import app.model.Author;
import app.model.Textbook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TextbookDao implements TextbookService {
  private static TextbookDao textbookDao;

  private Connection connection;

  private PreparedStatement allTextbooks;

  public static TextbookDao getDao() throws SQLException {
    if(textbookDao == null){
      textbookDao = new TextbookDao();
    }
    return textbookDao;
  }

  private TextbookDao() throws SQLException {
    connection = Connector.getConnection();

    allTextbooks =
        connection.prepareStatement(
            "SELECT \n"
                + "    book_id, title, volume, edition, author_id, first_name, last_name\n"
                + "FROM\n"
                + "    ((book\n"
                + "    INNER JOIN textbook ON book_id = txtbk_book_id)\n"
                + "    INNER JOIN author ON txtbk_author_id = author_id)\n"
                + "ORDER BY book_id; ");
  }

  @Override
  public List<Textbook> getTextbooks() throws SQLException {
    ResultSet set = allTextbooks.executeQuery();
    List<Textbook> textbooks = new ArrayList<>();
    set.next();
    for (; set.isAfterLast(); ) {
      Textbook textbook =
          new Textbook(set.getInt(1), set.getString(2), set.getInt(3), set.getInt(4));
      int id = set.getInt(1);
      while (set.getInt(1) == id) {
        textbook.addAuthor(new Author(set.getInt(5), set.getString(6), set.getString(7)));
        if (!set.next()) {
          break;
        }
      }
      textbooks.add(textbook);
    }
    return textbooks;
  }
}
