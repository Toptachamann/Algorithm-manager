package app.dao;

import app.auxiliary.Connector;
import app.auxiliary.Util;
import app.model.Algorithm;
import app.model.Author;
import app.model.Textbook;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
  private PreparedStatement countReferences;
  private PreparedStatement createReference;
  private PreparedStatement connectAlgorithm;
  private PreparedStatement getTextbooksByAlgorithm;
  private PreparedStatement deleteBook;
  private PreparedStatement deleteReference;
  private PreparedStatement resetAuthors;

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
    countReferences =
        connection.prepareStatement(
            "SELECT COUNT(*) FROM algorithm_reference WHERE ref_algorithm_id = ? AND ref_book_id = ?");
    createReference =
        connection.prepareStatement(
            "INSERT INTO algorithm_reference (ref_algorithm_id, ref_book_id) VALUE (?, ?)");
    connectAlgorithm =
        connection.prepareStatement(
            "INSERT INTO algorithm_reference (ref_textbook_id, ref_algorithm_id) "
                + "VALUE (?, ?)");
    getTextbooksByAlgorithm =
        connection.prepareStatement(
            "SELECT book_id, title, volume, edition, author_id, first_name, last_name\n"
                + "FROM (SELECT ref_book_id FROM algorithm_reference WHERE ref_algorithm_id = ?) AS algos\n"
                + "INNER JOIN book ON algos.ref_book_id = book_id INNER JOIN textbook ON book_id = txtbk_book_id \n"
                + "INNER JOIN author ON txtbk_author_id = author_id\n"
                + "ORDER BY book_id, author_id");
    deleteBook = connection.prepareStatement("DELETE FROM book WHERE book_id = ?");
    deleteReference =
        connection.prepareStatement(
            "DELETE FROM algorithm_reference WHERE algorithms.algorithm_reference.ref_algorithm_id = ? AND ref_book_id = ?");
    resetAuthors =
        connection.prepareStatement(
            "DELETE FROM algorithms.textbook WHERE algorithms.textbook.txtbk_book_id = ?");
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
    addAuthors(bookId, authors);
    return new Textbook(bookId, title, volume, edition, authors);
  }

  @Override
  public List<Textbook> getTextbooks() throws SQLException {
    // TODO add transaction
    logger.debug(() -> Util.format(allTextbooks));
    return textbooksFromSet(allTextbooks.executeQuery());
  }

  /*private List<Textbook> loadAlgorithms(List<Textbook> textbooks) throws SQLException {
    if (textbooks.size() > 0) {
      textbooks.sort(Comparator.comparingInt(Textbook::getId));
      StringBuilder builder = new StringBuilder();
      builder
          .append("SELECT \n")
          .append("ref_book_id,\n")
          .append("    algorithm_id,\n")
          .append("    algorithm,\n")
          .append("    complexity,\n")
          .append("    paradigm_id,\n")
          .append("    paradigm,\n")
          .append("    design_paradigm.description,\n")
          .append("    field_id,\n")
          .append("    field,\n")
          .append("    field_of_study.description")
          .append("    FROM\n")
          .append("    algorithm\n")
          .append("        INNER JOIN\n")
          .append("    design_paradigm ON algo_paradigm_id = paradigm_id\n")
          .append("        INNER JOIN\n")
          .append("    field_of_study ON algo_field_id = field_id\n")
          .append("        INNER JOIN\n")
          .append("    algorithm_reference ON ref_algorithm_id = algorithm_id\n")
          .append("WHERE\n")
          .append("    ref_book_id IN (");
      for (Textbook textbook : textbooks) {
        builder.append(textbook.getId()).append(",");
      }
      builder.setCharAt(builder.length() - 1, ')');
      builder.append("\nORDER BY ref_book_id , algorithm_id");
      String query = builder.toString();
      logger.debug(() -> Util.format(query));
      ResultSet set = connection.createStatement().executeQuery(query);
      if (set.next()) {
        for (int i = 0; !set.isAfterLast(); i++) {
          Textbook textbook = textbooks.get(i);
          int book_id = set.getInt(1);
          if (textbook.getId() != book_id) {
            continue;
          }
          do {
            textbook.addAlgorithm(
                new Algorithm(
                    set.getInt(2),
                    set.getString(3),
                    set.getString(4),
                    new DesignParadigm(set.getInt(5), set.getString(6), set.getString(7)),
                    new FieldOfStudy(set.getInt(8), set.getString(9), set.getString(10))));
          } while (set.next() && set.getInt(1) == book_id);
        }
      }
    }
    return textbooks;
  }*/

  @Override
  public List<Textbook> searchTextbook(
      String title, Integer volume, Integer edition, List<Author> authors) throws SQLException {
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
    if (authors.size() > 0) {
      builder.append(" AND ( 1 != 1");
      for (Author author : authors) {
        builder
            .append(" OR CONCAT(a1.first_name, a1.last_name) LIKE ")
            .append(Util.fixForLike(author.getFirstName() + " " + author.getLastName()));
      }
      builder.append(")");
    }
    builder.append(")");
    if (builder.length() == 0) {
      return getTextbooks();
    } else {
      builder.insert(
          0,
          "SELECT \n"
              + "    b2.book_id,\n"
              + "    b2.title,\n"
              + "    b2.volume,\n"
              + "    b2.edition,\n"
              + "    a2.author_id,\n"
              + "    a2.first_name,\n"
              + "    a2.last_name\n"
              + "FROM\n"
              + "    ((SELECT \n"
              + "        b1.book_id, b1.title, b1.edition, b1.volume\n"
              + "    FROM\n"
              + "        ((author AS a1\n"
              + "    INNER JOIN textbook AS t1 ON a1.author_id = t1.txtbk_author_id)\n"
              + "    INNER JOIN book AS b1 ON t1.txtbk_book_id = b1.book_id)"
              + " WHERE 1 = 1 ");
      builder
          .append(" AS b2\n")
          .append("    INNER JOIN textbook AS t2 ON b2.book_id = t2.txtbk_book_id)\n")
          .append("    INNER JOIN author AS a2 ON a2.author_id = t2.txtbk_author_id\n")
          .append("    order by b2.book_id, a2.author_id");
      Statement statement = connection.createStatement();
      String query = builder.toString();
      logger.debug(() -> Util.format(query));
      return textbooksFromSet(statement.executeQuery(query));
    }
  }

  @Override
  public <T> void updateBook(String column, T newValue, int id) throws SQLException {
    String query = "UPDATE book SET " + column + " = ";
    if (newValue instanceof Integer) {
      query += newValue;
    } else {
      query += Util.fixForString(newValue.toString());
    }
    query += " WHERE book_id = " + id;
    String query1 = query;
    logger.debug(() -> Util.format(query1));
    connection.createStatement().executeUpdate(query);
  }

  @Override
  public void deleteTextbookById(int id) throws SQLException {
    deleteBook.setInt(1, id);
    logger.debug(() -> Util.format(deleteBook));
    deleteBook.executeUpdate();
  }

  private void resetAuthors(int bookId) throws SQLException {
    resetAuthors.setInt(1, bookId);
    logger.debug(() -> Util.format(resetAuthors));
    resetAuthors.executeUpdate();
  }

  @Override
  public void addAuthors(int bookId, List<Author> authors) throws SQLException {
    if (authors.size() > 0) {
      StringBuilder builder =
          new StringBuilder("INSERT INTO textbook (txtbk_book_id, txtbk_author_id) VALUES ");
      for (Author author : authors) {
        builder.append("(").append(bookId).append(", ").append(author.getId()).append(")");
      }
      String query = builder.toString();
      logger.debug(() -> Util.format(query));
      connection.createStatement().executeUpdate(query);
    }
  }

  @Override
  public void setAuthors(int bookId, List<Author> authors) throws SQLException {
    resetAuthors(bookId);
    addAuthors(bookId, authors);
  }

  public void setAlgorithms(int bookId, List<Algorithm> algorithms) throws SQLException {
    if (algorithms.size() > 0) {
      StringBuilder builder =
          new StringBuilder(
              "INSERT INTO algorithm_reference (ref_book_id, ref_algorithm_id) VALUES ");
      for (Algorithm algorithm : algorithms) {
        builder.append("(").append(bookId).append(", ").append(algorithm.getId()).append(")");
      }
      String query = builder.toString();
      logger.debug(() -> Util.format(query));
      connection.createStatement().executeUpdate(query);
    }
  }

  public void addAlgorithm(Textbook textbook, Algorithm algorithm) throws SQLException {
    connectAlgorithm.setInt(1, textbook.getId());
    connectAlgorithm.setInt(2, algorithm.getId());
    logger.debug(() -> Util.format(connectAlgorithm));
    connectAlgorithm.executeUpdate();
  }

  private List<Textbook> textbooksFromSet(ResultSet set) throws SQLException {
    List<Textbook> textbooks = new ArrayList<>();
    if (set.next()) {
      for (; !set.isAfterLast(); ) {
        int volume = set.getInt(3);
        Textbook textbook;
        if (set.wasNull()) {
          textbook = new Textbook(set.getInt(1), set.getString(2), null, set.getInt(4));
        } else {
          textbook = new Textbook(set.getInt(1), set.getString(2), volume, set.getInt(4));
        }
        int id = set.getInt(1);
        do {
          textbook.addAuthor(new Author(set.getInt(5), set.getString(6), set.getString(7)));
        } while (set.next() && set.getInt(1) == id);
        textbooks.add(textbook);
      }
    }
    return textbooks;
  }

  @Override
  public boolean containsReference(int algorithmId, int bookId) throws SQLException {
    countReferences.setInt(1, algorithmId);
    countReferences.setInt(2, bookId);
    logger.debug(() -> Util.format(countReferences));
    ResultSet set = countReferences.executeQuery();
    set.next();
    return set.getInt(1) == 1;
  }

  @Override
  public void createReference(int algorithmId, int bookId) throws SQLException {
    createReference.setInt(1, algorithmId);
    createReference.setInt(2, bookId);
    logger.debug(() -> Util.format(createReference));
    createReference.executeUpdate();
  }

  @Override
  public void deleteReference(int algorithmId, int bookId) throws SQLException {
    deleteReference.setInt(1, algorithmId);
    deleteReference.setInt(2, bookId);
    logger.debug(() -> Util.format(deleteBook));
    deleteReference.executeUpdate();
  }

  @Override
  public List<Textbook> getTextbooksByAlgorithm(int algorithmId) throws SQLException {
    getTextbooksByAlgorithm.setInt(1, algorithmId);
    logger.debug(() -> Util.format(getTextbooksByAlgorithm));
    return textbooksFromSet(getTextbooksByAlgorithm.executeQuery());
  }
}
