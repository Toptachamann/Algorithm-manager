package app.service;

import app.auxiliary.Connector;
import app.auxiliary.Util;
import app.model.Author;
import app.model.Textbook;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class TextbookDao implements TextbookService {
  private static TextbookDao textbookDao;

  private Connection connection;

  private PreparedStatement allTextbooks;
  private PreparedStatement insertBook;
  private PreparedStatement insertAuthor;
  private PreparedStatement insertBookAuthor;

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
                + "ORDER BY book_id, author_id");
    insertBook =
        connection.prepareStatement("INSERT INTO book (title, volume, edition) VALUE (?, ?, ?)");
    insertAuthor =
        connection.prepareStatement("INSERT INTO author (first_name, last_name) VALUE (?, ?)");
    insertBookAuthor =
        connection.prepareStatement(
            "INSERT INTO textbook (txtbk_book_id, txtbk_author_id) VALUE (?, ?)");
  }

  public static TextbookDao getDao() throws SQLException {
    if (textbookDao == null) {
      textbookDao = new TextbookDao();
    }
    return textbookDao;
  }

  @Override
  public Textbook insertTextbook(String title, Integer volume, Integer edition, String authors)
      throws SQLException {
    title = title.trim();
    insertBook.setString(1, title);
    if(volume == null){
      insertBook.setNull(2, Types.INTEGER);
    }else{
      insertBook.setInt(2, volume);
    }
    insertBook.setInt(3, edition);
    insertBook.executeUpdate();
    int id = Util.getLastId(connection);
    Textbook textbook = new Textbook(id, title, volume, edition);

    String[] authorsArr = authors.split(",\\s*");
    List<Integer> authorIds = new ArrayList<>();
    for(String authorFullName : authorsArr){
      String[] fullName = authorFullName.split("\\s+");
      insertAuthor.setString(1, fullName[0]);
      insertAuthor.setString(2, fullName[1]);
      insertAuthor.executeUpdate();
      int authorId = Util.getLastId(connection);
      textbook.addAuthor(new Author(authorId, fullName[0], fullName[1]));
      authorIds.add(authorId);
    }
    for(int authorId : authorIds){
      insertBookAuthor.setInt(1, id);
      insertBookAuthor.setInt(2, authorId);
      insertBookAuthor.executeUpdate();
    }
    return textbook;
  }

  @Override
  public List<Textbook> getTextbooks() throws SQLException {
    return textbooksFromSet(allTextbooks.executeQuery());
  }

  @Override
  public List<Textbook> searchTextbook(
      String title, Integer edition, Integer volume, String authors) throws SQLException {
    StringBuilder builder = new StringBuilder();
    if (!StringUtils.isBlank(title)) {
      builder.append(" AND title LIKE ").append(Util.fixForLike(title.trim()));
    }
    if (edition != null && edition > 0) {
      builder.append(" AND edition = ").append(edition);
    }
    if (volume != null && volume > 0) {
      builder.append(" AND volume = ").append(volume);
    }
    if (!StringUtils.isBlank(authors)) {
      String[] authorsArr = authors.split(", ");
      if (authorsArr.length > 0) {
        builder.append(" AND ( 1 != 1");
        for (String author : authorsArr) {
          builder.append(" OR CONCAT(first_name, last_name) LIKE ").append(Util.fixForLike(author));
        }
        builder.append(")");
      }
    }
    if (builder.length() == 0) {
      return getTextbooks();
    } else {
      builder.insert(
          0,
          "SELECT DISTINCT book_id from ((book"
              + " INNER JOIN textbook ON book_id = txtbk_book_id)"
              + " INNER JOIN author ON txtbk_author_id = author_id) "
              + " WHERE 1 = 1 ");
      builder.append(" ORDER by book_id");
      Statement statement = connection.createStatement();
      System.out.println(builder.toString());
      ResultSet set = statement.executeQuery(builder.toString());
      List<Integer> ids = new ArrayList<>();
      for (; set.next(); ) {
        ids.add(set.getInt(1));
      }
      return getByIds(ids);
    }
  }

  private List<Textbook> textbooksFromSet(ResultSet set) throws SQLException {
    List<Textbook> textbooks = new ArrayList<>();
    set.next();
    for (; !set.isAfterLast(); ) {
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

  private List<Textbook> getByIds(List<Integer> ids) throws SQLException {
    if (ids.size() == 0) {
      return new ArrayList<>();
    } else {
      StringBuilder builder =
          new StringBuilder(
              "SELECT \n"
                  + "    book_id, title, volume, edition, author_id, first_name, last_name\n"
                  + "FROM\n"
                  + "    ((book\n"
                  + "    INNER JOIN textbook ON book_id = txtbk_book_id)\n"
                  + "    INNER JOIN author ON txtbk_author_id = author_id)\n"
                  + "where book_id in (");
      for (Integer id : ids) {
        builder.append(id).append(", ");
      }
      builder.setCharAt(builder.length() - 2, ')');
      builder.append("ORDER BY book_id, author_id");
      return textbooksFromSet(connection.createStatement().executeQuery(builder.toString()));
    }
  }
}
