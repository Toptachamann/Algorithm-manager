package app.dao;

import app.auxiliary.Connector;
import app.auxiliary.Util;
import app.model.Author;
import app.model.Textbook;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class TextbookDaoImpl implements TextbookDao {
  private static final Logger logger = LogManager.getLogger(TextbookDaoImpl.class);

  private Connection connection;
  private PreparedStatement allTextbooks;
  private PreparedStatement insertBook;
  private PreparedStatement insertTextbook;
  private PreparedStatement deleteBook;

  public TextbookDaoImpl() throws SQLException {
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
    insertTextbook =
        connection.prepareStatement(
            "INSERT INTO textbook (txtbk_book_id, txtbk_author_id) VALUE (?, ?)");
    deleteBook = connection.prepareStatement("DELETE FROM book WHERE book_id = ?");
  }

  @Override
  public Textbook insertTextbook(
      String title, @Nullable Integer volume, Integer edition, List<Author> authors)
      throws SQLException {
    // TODO add transaction
    insertBook.setString(1, title.trim());
    if (volume == null) {
      insertBook.setNull(2, Types.INTEGER);
    } else {
      insertBook.setInt(2, volume);
    }
    insertBook.setInt(3, edition);
    insertBook.executeUpdate();
    int bookId = Util.getLastId(connection);
    for (Author author : authors) {
      insertTextbook.setInt(1, bookId);
      insertTextbook.setInt(2, author.getId());
    }
    return new Textbook(bookId, title, volume, edition, authors);
  }

  @Override
  public List<Textbook> getTextbooks() throws SQLException {
    return textbooksFromSet(allTextbooks.executeQuery());
  }

  @Override
  public List<Textbook> searchTextbook(
      String title, Integer volume, Integer edition, String authors) throws SQLException {
    StringBuilder builder = new StringBuilder();
    if (!StringUtils.isBlank(title)) {
      builder.append(" AND b1.title LIKE ").append(Util.fixForLike(title.trim()));
    }
    if (volume != null && volume > 0) {
      builder.append(" AND b1.volume = ").append(volume);
    }
    if (edition != null && edition > 0) {
      builder.append(" AND b1.edition = ").append(edition);
    }
    if (!StringUtils.isBlank(authors)) {
      String[] authorsArr = authors.split(", ");
      if (authorsArr.length > 0) {
        builder.append(" AND ( 1 != 1");
        for (String author : authorsArr) {
          builder
              .append(" OR CONCAT(a1.first_name, a1.last_name) LIKE ")
              .append(Util.fixForLike(author));
        }
        builder.append(")");
      }
    }
    builder.append(")");
    if (builder.length() == 0) {
      return getTextbooks();
    } else {
      builder.insert(
          0,
          new StringBuilder()
              .append("SELECT \n")
              .append("    b2.book_id,\n")
              .append("    b2.title,\n")
              .append("    b2.volume,\n")
              .append("    b2.edition,\n")
              .append("    a2.author_id,\n")
              .append("    a2.first_name,\n")
              .append("    a2.last_name\n")
              .append("FROM\n")
              .append("    ((SELECT \n")
              .append("        b1.book_id, b1.title, b1.edition, b1.volume\n")
              .append("    FROM\n")
              .append("        ((author AS a1\n")
              .append("    INNER JOIN textbook AS t1 ON a1.author_id = t1.txtbk_author_id)\n")
              .append("    INNER JOIN book AS b1 ON t1.txtbk_book_id = b1.book_id)")
              .append(" WHERE 1 = 1 ")
              .toString());
      builder
          .append(" AS b2\n")
          .append("    INNER JOIN textbook AS t2 ON b2.book_id = t2.txtbk_book_id)\n")
          .append("    INNER JOIN author AS a2 ON a2.author_id = t2.txtbk_author_id\n")
          .append("    order by b2.book_id, a2.author_id");
      Statement statement = connection.createStatement();
      logger.info(Util.format(builder.toString()));
      return textbooksFromSet(statement.executeQuery(builder.toString()));
    }
  }

  @Override
  public void updateTitle(String column, String value, int id) throws SQLException {
    connection
        .createStatement()
        .executeUpdate(
            "UPDATE book SET "
                + column
                + " = "
                + Util.fixForString(value)
                + " WHERE book_id = "
                + id);
  }

  @Override
  public void deleteTextbookById(int id) throws SQLException {
    deleteBook.setInt(1, id);
    deleteBook.executeUpdate();
  }

  private List<Textbook> textbooksFromSet(ResultSet set) throws SQLException {
    List<Textbook> textbooks = new ArrayList<>();
    if (!set.next()) {
      return new ArrayList<>();
    }
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
}
